package com.cashback.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cashback.MainActivity;
import com.cashback.R;
import com.cashback.StoreDetailPageScreen;
import com.cashback.animations.DepthPageTransformer;
import com.cashback.animations.ZoomOutPageTransformer;
import com.cashback.data.Store;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;
import com.cashback.util.ExecuteServices;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class HomeFragment extends Fragment {

    public ArrayList<Store> sSliderList;
    private ViewPager viewPager;
    private String URL_FEED;
    private SliderAdapter sliderAdapter;
    private ViewPager imageSlider;
    int currentPage = 0;
    //int[] imageResource = {R.drawable.banner, R.drawable.banner, R.drawable.banner, R.drawable.banner, R.drawable.banner};
    HomeCouponFragment couponFragment;
    RecentStoresFragment recentStoresFragment;

    public HomeFragment() {

    }

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="";
    public static String sBannerImage="1";

    boolean checkActivity(){
        if(isAdded() && getActivity()!=null )
            return  true;
        else
            return  false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setRetainInstance(true);
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        sSliderList = new ArrayList<>();
        sliderAdapter = new SliderAdapter(getActivity(),sSliderList);
        imageSlider = (ViewPager) rootView.findViewById(R.id.myImage);
        imageSlider.setPageTransformer(true,new DepthPageTransformer());
        imageSlider.setAdapter(sliderAdapter);
        imageSlider.setCurrentItem(0);
        // if (viewPager != null) {

        imageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               currentPage=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

      //  getHotDeals(requestBody);
        setupViewPager(viewPager);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 5) {
                    currentPage = 0;
                }
                imageSlider.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
        //}
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position ==0)
                {
                    ((FeaturesFragment)adapter.getItem(position)).loadData();

                //    couponFragment.requestData();
                }
                else  if(position ==1){
                    couponFragment.loadData();
                }

                else if(position==2){
                    recentStoresFragment.loadData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .build();
        if(CommonFunctions.isInternetOn(getActivity())) {
            getHotDeals(requestBody);
        }
        else
        {
            MainActivity.showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");

        }
    }

    public void updateSliderPager(ArrayList<Store> sSliderItem) {
        sliderAdapter.notifyDataSetChanged();
    }
    Adapter adapter;

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getChildFragmentManager());
        couponFragment = new HomeCouponFragment();
        recentStoresFragment=new RecentStoresFragment();
        adapter.addFragment(new FeaturesFragment(this), "Featured");
        adapter.addFragment(couponFragment, "Coupons");
        adapter.addFragment(recentStoresFragment, "Recent Stores ");
        viewPager.setAdapter(adapter);
    }

    //@@@@@@@@@@@@@@@@@@@@@ Defining the pager adapter for home screen @@@@@@@@@@@@@@@@@
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    public class SliderAdapter extends PagerAdapter{

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<Store> sSliderList;

        public SliderAdapter(Context context,ArrayList<Store> sSliderList) {
            mContext = context;
            this.sSliderList=sSliderList;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return sSliderList.size() > 5 ? 5 : sSliderList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            //imageView.setImageResource(imageResource[position]);
            String lSlidePath=CommonFunctions.sliderPath+ (position + 1) + ".png";
            Glide.with(mContext).load(lSlidePath)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            ImageView storeImg = (ImageView) itemView.findViewById(R.id.storeImage);
            Glide.with(mContext).load(CommonFunctions.cloudPath + sSliderList.get(position).image)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(storeImg);

            TextView cashbackText = (TextView) itemView.findViewById(R.id.itemCashback);
            cashbackText.setText(""+sSliderList.get(position).cashback);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AllStoreFragment.sStoreDataList=sSliderList.get(position);
                    HomeFragment.sBannerImage=""+(position+1);
                    Cursor cs= DatabaseHandler.getInstance(getActivity()).checkSQLiteData(AllStoreFragment.sStoreDataList.store_id);
                    if(cs.getCount()>0) {
                        DatabaseHandler.getInstance(getActivity()).deleteStoreRow(AllStoreFragment.sStoreDataList.store_id);
                        DatabaseHandler.getInstance(getActivity()).insertRecentStores(AllStoreFragment.sStoreDataList);
                    }
                    else
                    {
                        DatabaseHandler.getInstance(getActivity()).insertRecentStores(AllStoreFragment.sStoreDataList);
                    }
                    RecentStoresFragment.reload=true;
                    getActivity().startActivity(new Intent(getActivity(), StoreDetailPageScreen.class));

                }
            });
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }


    private void getHotDeals(RequestBody requestBody) {

        new ExecuteServices().executePost(CommonFunctions.mUrl + "userFeaturedStore", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                if (!checkActivity()){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            CommonFunctions.cloudPath = object.getString("logoUrl");
                            System.out.println("00000 " + CommonFunctions.cloudPath);
                            CommonFunctions.sliderPath=object.getString("sliderUrl");
                            System.out.println("00000 " + CommonFunctions.sliderPath);
                            if (status.equalsIgnoreCase("1")) {
                                if (object.has("data")) {
                                    JSONArray cArr = object.getJSONArray("data");
                                    sSliderList.clear();
                                    for (int i = 0; i < cArr.length(); i++) {
                                        JSONObject obj = cArr.getJSONObject(i);
                                        sSliderList.add(new Store(obj.getString("retailer_id"), obj.getString("title"), obj.getString("cashback")
                                                , obj.getString("retailer_status"), obj.getString("url"), obj.getString("image"), obj.has("description") ? obj.getString("description") : "", obj.getString("is_favourite"),null));


                                    }
                                }
                            } else {
                                MainActivity.showMessageDialog("No coupon available.", "#f2dede", "#a94442");

                                //TODO--Here also update data from Database
                            }
                            sliderAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onServiceExecutedFailed(String message) {
                if (!checkActivity()) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }, requestBody);

    }

    public void loadData(ArrayList<Store> stores){
        if(sSliderList.size()>0)return;
        sSliderList.clear();
        sSliderList.addAll(stores);
        sliderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewPager!=null){
            //Toast.makeText(getActivity(), "Selected Item of Home Fragment:"+viewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
            if(viewPager.getCurrentItem()==2){
                viewPager.setCurrentItem(0);
            }
          }
        }
    }

