package com.cashback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cashback.adapter.CashbackCategoryAdapter;
import com.cashback.data.CashbackModel;
import com.cashback.data.CoupenListItems;
import com.cashback.data.Store;
import com.cashback.data.WebviewModel;
import com.cashback.fragment.AllDealsFragment;
import com.cashback.fragment.AllStoreFragment;
import com.cashback.fragment.CouponFragment;
import com.cashback.fragment.FeaturesFragment;
import com.cashback.fragment.HomeFragment;
import com.cashback.fragment.OfferFragment;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;
import com.cashback.util.ExecuteServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class StoreDetailPageScreen extends AppCompatActivity {
    private ViewPager viewPager;
    private SliderAdapter sliderAdapter;
    private ViewPager imageSlider;
    static  TextView mCashbackPercent,mStoreName,mShopNowBtn,mToastView,mStoreCashback;
    LinearLayout mBackFromStoreDetail;
    ListView mCashbackDataLayout;
    public static String sIsFav="0";
    AppCompatImageView mLikeFavourite,mBackArrow;
    static ImageView mCashbackIcon;
    static ImageView mInfoIcon;
    public static CoupenListItems sCouponDataListItem;
    Fragment allDealFragment,couponFragment,offerFragment;
    RelativeLayout mLayoutStoreDetail;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    String userId="",isFav="0";
    RequestBody requestBody=null;
    TextView mTopCashback;
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page_screen);
        mToastView=(TextView)findViewById(R.id.message);
        mTopCashback=(TextView)findViewById(R.id.storeCashback);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        imageSlider = (ViewPager) findViewById(R.id.myImage);
        mLikeFavourite = (AppCompatImageView) findViewById(R.id.likeFav);
        mBackArrow=(AppCompatImageView)findViewById(R.id.act_detailpage_backarrow);
        mCashbackIcon=(ImageView)findViewById(R.id.cashbackIcon);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mBackFromStoreDetail = (LinearLayout) findViewById(R.id.baclFromDetail);
        mInfoIcon = (AppCompatImageView) findViewById(R.id.infoIcon);
        mLayoutStoreDetail=(RelativeLayout)findViewById(R.id.act_detail_rl_storedetail);
        mStoreName = (TextView) findViewById(R.id.storeName);
        mCashbackPercent = (TextView) findViewById(R.id.cashTxt);
        mStoreCashback=(TextView)findViewById(R.id.storeCashback);
        mShopNowBtn = (TextView) findViewById(R.id.shopnow);
        mCashbackDataLayout = (ListView)findViewById(R.id.cashbackData);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_backward_arrow);
        ab.setDisplayHomeAsUpEnabled(false);
        //   ab.setDisplayShowTitleEnabled(true);
        ab.setTitle("Store Detail");
        prefs = PreferenceManager.getDefaultSharedPreferences(StoreDetailPageScreen.this); //initilize the shared preferences
        prefsEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;
        //AllStoreFragment.sStoreDataList=null;
        if(AllStoreFragment.sStoreDataList==null){
            showMessageDialog("Something went wrong.","#f2dede","#a94442");
            mShopNowBtn.setVisibility(View.GONE);
            mInfoIcon.setVisibility(View.GONE);
            mCashbackPercent.setVisibility(View.GONE);
            mStoreName.setVisibility(View.GONE);
            mLikeFavourite.setVisibility(View.GONE);
            mStoreCashback.setVisibility(View.GONE);
            mBackArrow.setVisibility(View.GONE);
            mLayoutStoreDetail.setVisibility(View.GONE);
            exitActivity=true;
        }
        else {
            getFavouriteStoreStatus();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            sliderAdapter = new SliderAdapter(this);
            imageSlider.setAdapter(sliderAdapter);
            if (viewPager != null) {
                setupViewPager(viewPager);
            }
            tabLayout.setupWithViewPager(viewPager);
            mBackFromStoreDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            mInfoIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonFunctions.showAlert(StoreDetailPageScreen.this, AllStoreFragment.sStoreDataList.desc, "Store Description");
                }
            });
            mCashbackIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent lIntentCashback=new Intent(StoreDetailPageScreen.this,CashbackActivity.class);
//                    lIntentCashback.putExtra("cashbackrates",AllStoreFragment.sStoreDataList.cashbackrates);
//                    startActivity(lIntentCashback);
                    //((TextView)findViewById(R.id.cashback_rates)).setText(CommonFunctions.fromHtml(AllStoreFragment.sStoreDataList.cashbackrates));
                }
            });
            isFav = AllStoreFragment.sStoreDataList.favourite;
            Log.e("Fav Value ", isFav);
            /*if (isFav.equalsIgnoreCase("1")) {
                mLikeFavourite.setImageResource(R.drawable.favorite_black_24dp);
            } else {
                mLikeFavourite.setImageResource(R.drawable.favorite_border_black_24dp);
            }*/

            mLikeFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prefs = PreferenceManager.getDefaultSharedPreferences(StoreDetailPageScreen.this); //initilize the shared preferences
                    prefsEditor=prefs.edit();
                    userId=prefs.getString("USER_ID","102");
                    if (!CommonFunctions.isInternetOn(StoreDetailPageScreen.this)) {
                        showMessageDialog("No internet connection...!!!.", "#fcf8e3", "#8a6d3b");
                        return;
                    }

                    Log.e("Fav Value ", isFav);
                    if (userId.equalsIgnoreCase("102")) {
                        MainActivity.sComingFrom = "LikeFav";
                        startActivityForResult(new Intent(StoreDetailPageScreen.this, MLoginActivity.class), 1000);
                    } else {
                        if (isFav.equalsIgnoreCase("1")) {
                            mLikeFavourite.setImageResource(R.drawable.favorite_border_black_24dp);
                            isFav = "0";
                            requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("user_id", userId)
                                    .addFormDataPart("retailer_id", AllStoreFragment.sStoreDataList.store_id)
                                    .addFormDataPart("flag", "0")
                                    .build();
                            Cursor cs = DatabaseHandler.getInstance(StoreDetailPageScreen.this).checkSQLiteData(AllStoreFragment.sStoreDataList.store_id);
                            if (cs.getCount() > 0) {
                                DatabaseHandler.getInstance(StoreDetailPageScreen.this).updateRecentFav(AllStoreFragment.sStoreDataList.store_id, "0");
                            }

                        } else {
                            mLikeFavourite.setImageResource(R.drawable.favorite_black_24dp);
                            isFav = "1";
                            requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("user_id", userId)
                                    .addFormDataPart("retailer_id", AllStoreFragment.sStoreDataList.store_id)
                                    .addFormDataPart("flag", "1")
                                    .build();
                            Cursor cs = DatabaseHandler.getInstance(StoreDetailPageScreen.this).checkSQLiteData(AllStoreFragment.sStoreDataList.store_id);
                            if (cs.getCount() > 0) {
                                DatabaseHandler.getInstance(StoreDetailPageScreen.this).updateRecentFav(AllStoreFragment.sStoreDataList.store_id, "1");
                            }
                        }
                        if (CommonFunctions.isInternetOn(StoreDetailPageScreen.this)) {
                            addToFavourite(CommonFunctions.mUrl + "addToFavourite", requestBody);
                        } else {
                            //No internet
                        }
                    }
                }
            });
            mStoreName.setText(AllStoreFragment.sStoreDataList.name);
            if (!TextUtils.isEmpty(AllStoreFragment.sStoreDataList.cashback)) {
                mCashbackPercent.setText(""+AllStoreFragment.sStoreDataList.cashback);
                mTopCashback.setText(""+AllStoreFragment.sStoreDataList.cashback );
            }

//            //mCashbackPercent.setText(AllStoreFragment.sStoreDataList.cashback+ "Cash Back");
//            if (AllStoreFragment.sStoreDataList.status.equalsIgnoreCase("inactive")) {
//                mCashbackPercent.setText("Currently No Cashback");
//                mTopCashback.setText("Currently No Cashback");
//            } else {
//                if (AllStoreFragment.sStoreDataList.cashback.equalsIgnoreCase("")) {
//                    mCashbackPercent.setText("Best Offer");
//                    mTopCashback.setText("Best Offer");
//                } else {
//                    mCashbackPercent.setText("Upto " + AllStoreFragment.sStoreDataList.cashback + " Cash Back");
//                    mTopCashback.setText("Upto " + AllStoreFragment.sStoreDataList.cashback + " Cash Back");
//                }
//
//            }
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    if (position == 1) {
                        ((CouponFragment) couponFragment).methodNotify();
                    } else if (position == 2) {
                        ((OfferFragment) offerFragment).menthodNotify();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            mShopNowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userId = prefs.getString("USER_ID", "102");
                    MainActivity.sComingFrom = "StoreCoupon";
                    MainActivity.sWebviewData = new WebviewModel();
                    //StoreDetailPageScreen.sCouponDataListItem=feedItems.get(getAdapterPosition());
                    MainActivity.sWebviewData.setStatus(AllStoreFragment.sStoreDataList.status);
                    MainActivity.sWebviewData.setStore_id(AllStoreFragment.sStoreDataList.store_id);
                    MainActivity.sWebviewData.setRetailerName(AllStoreFragment.sStoreDataList.name);
                    MainActivity.sWebviewData.setCashback(AllStoreFragment.sStoreDataList.cashback);
                    MainActivity.sWebviewData.setUrl(AllStoreFragment.sStoreDataList.url);
                    //MainActivity.sComingFrom="LikeFav";
                    if (userId.equalsIgnoreCase("102")) {
                        startActivity(new Intent(StoreDetailPageScreen.this, MLoginActivity.class));
                    } else {
                        startActivity(new Intent(StoreDetailPageScreen.this, WebviewActivity.class));
                    }
                }
            });

            if((AllStoreFragment.sStoreDataList.desc==null)||(AllStoreFragment.sStoreDataList.desc.isEmpty())){
                mInfoIcon.setVisibility(View.INVISIBLE);
            }

            if(AllStoreFragment.sStoreDataList.cashbackrates==null){
                mCashbackIcon.setVisibility(View.INVISIBLE);
            }
        }

    }

    public static void displayCashbackIcon(){
        mCashbackIcon.setVisibility(View.VISIBLE);
    }

    public static void displayInfoIcon(){mInfoIcon.setVisibility(View.VISIBLE);}

    public static Handler sHandleData=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //    mCashbackPercent.setText(AllDealsFragment.cashbck+" Cash Back");
            //    mStoreName.setText(AllDealsFragment.title);
            //      VectorDrawableCompat vc= VectorDrawableCompat.create(this.getResources(),s.toString().length() > 0?R.drawable.cross:R.drawable.magnifier_tool,null);

            return false;
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==1000) {

        }
    }


    private void addToFavourite(String url, RequestBody requestBody) {
        new ExecuteServices().executePost(url, new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            parseFavJsonData(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onServiceExecutedFailed(String msg) {

            }
        },requestBody);
    }


    void parseFavJsonData(JSONObject response)
    {
        try {
            String status = response.getString("status");
            String message = response.getString("message");
            if (status.equalsIgnoreCase("1")) {
//                if (AllStoreFragment.sStoreDataList.favourite.equalsIgnoreCase("1")) {
//                  //  mLikeFavourite.setImageResource(R.drawable.favorite_border_black_24dp);
//                    isFav="0";
//                } else {
//
//                  //  mLikeFavourite.setImageResource(R.drawable.favorite_black_24dp);
//                    isFav="1";
//                }
                // CommonFunction.displayToast(StoreItemDetailActivity.this, message);
                if(isFav.equalsIgnoreCase("1")){
                    showMessageDialog("Store added to your wish list.", "#dff0d8", "#3c763d");
                }
                else if(isFav.equalsIgnoreCase("0")){
                    showMessageDialog("Store removed from your wish list.", "#f2dede", "#a94442");
                }
            } else {
                // CommonFunction.displayToast(StoreItemDetailActivity.this, message);
            }
        }
        catch (Exception e){}
    }



    private void setupViewPager(ViewPager viewPager) {
        allDealFragment=new AllDealsFragment();
        couponFragment=new CouponFragment();
        offerFragment=new OfferFragment();

        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(allDealFragment, "All Deals");
        adapter.addFragment(couponFragment, "Coupons");
        adapter.addFragment(offerFragment, "Deals");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

    }
    MenuItem favIcon;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_page_screen, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        public Adapter(FragmentManager fm) {
            super(fm);
        }
        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
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

    public class SliderAdapter extends PagerAdapter {
        Context mContext;
        LayoutInflater mLayoutInflater;
        public SliderAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            HomeFragment.sBannerImage="1";
            String lSliderPath=CommonFunctions.sliderPath+HomeFragment.sBannerImage+".png";
            Glide.with(mContext).load(lSliderPath)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imageView)
            ;
            HomeFragment.sBannerImage="0";

            ImageView storeImg=(ImageView)itemView.findViewById(R.id.storeImage);
            Glide.with(mContext).load(CommonFunctions.cloudPath+AllStoreFragment.sStoreDataList.image)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(storeImg);

            TextView cashbackText = (TextView) itemView.findViewById(R.id.itemCashback);
            cashbackText.setVisibility(View.GONE);
            container.addView(itemView);
            return itemView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    public void setCategoryAdtapter(ArrayList<CashbackModel> cashBackTableList)
    {
        if (cashBackTableList != null && cashBackTableList.size()>0) {
            ((TextView)findViewById(R.id.storeNameTxtCashback)).setText(AllStoreFragment.sStoreDataList.name+" offers rates");
            CashbackCategoryAdapter adapter = new CashbackCategoryAdapter(StoreDetailPageScreen.this, cashBackTableList);
            mCashbackDataLayout.setAdapter(adapter);
            setListViewHeightBasedOnChildren(mCashbackDataLayout);

        }
        else {
            mCashbackDataLayout.setVisibility(View.GONE);
            ((TextView)findViewById(R.id.storeNameTxtCashback)).setVisibility(View.GONE);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight()+5;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() + 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public  static  void showMessageDialog(String message,String color,String textColor)
    {
        if(mToastView.getVisibility()==View.VISIBLE)
        {
            mToastView.setVisibility(View.GONE);
        }
        mToastView.setBackgroundColor(Color.parseColor(color));
        mToastView.setText(message);
        mToastView.setTextColor(Color.parseColor(textColor));
        mToastView.setVisibility(View.VISIBLE);
        mToastView.startAnimation(slideDownAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mToastView.startAnimation(slideUpAnimation);
                mToastView.setVisibility(View.GONE);
                if(exitActivity) {
                    activity.finish();
                }
                exitActivity=false;
            }
        },3000);
    }


    public void getFavouriteStoreStatus(){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .build();
        if(CommonFunctions.isInternetOn(this)) {
            if(!userId.equalsIgnoreCase("102")) {
                getFavourite(requestBody);
            }
        }
        else{
            mLikeFavourite.setVisibility(View.GONE);
        }
    }

    private void getFavourite(RequestBody requestBody) {


        new ExecuteServices().executePost(CommonFunctions.mUrl + "favoriteStores", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(String response) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    System.out.println(response);
                    String status=new JSONObject(response).getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray array = new JSONObject(response).getJSONArray("data");
                        int length = array.length();

                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);

                            if(obj.getString("retailer_id").equalsIgnoreCase(AllStoreFragment.sStoreDataList.store_id)){
                                isFav="1";
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFav.equalsIgnoreCase("1")) {
                                    mLikeFavourite.setImageResource(R.drawable.favorite_black_24dp);
                                } else {
                                    mLikeFavourite.setImageResource(R.drawable.favorite_border_black_24dp);
                                }
                            }
                        });


                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.showMessageDialog("There is no wish list.","#f2dede","#a94442");
                            }
                        });
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void onServiceExecutedFailed(String message) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                        //rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);
                        MainActivity.showMessageDialog("Something went wrong.","#f2dede","#a94442");
                    }
                });
            }
        },requestBody);


    }




}