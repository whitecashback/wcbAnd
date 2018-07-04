package com.cashback.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cashback.MainActivity;
import com.cashback.R;
import com.cashback.StoreDetailPageScreen;
import com.cashback.data.Store;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;
import com.cashback.util.ExecuteServices;
import com.facebook.internal.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;


/**
 * Created by jack on 28-04-2016.
 */
public class FeaturesFragment extends Fragment {
    @SuppressLint("ValidFragment")
    FeaturesFragment(){

    }

    HomeFragment fragment;
    private String URL_FEED;
    private RecyclerView couponList;
    private FeatureFragmentAdapter featureAdapter;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    ArrayList<Store> feedItems=new ArrayList<>();
    boolean limitOver=false;
    String userId="";
    View rootView;
    boolean lonOnActivity=false;
    boolean isdataLoading=false;
    private long mLastUpdatedTime;
    private long mCurrentTime;
    private boolean mIsLimitOver;
    private DatabaseHandler mDatabaseHandler;
    private boolean mIsSecondDay;


    @SuppressLint("ValidFragment")
    public FeaturesFragment(HomeFragment fragment)
    {
        this.fragment=fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feature, container, false);
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        setRetainInstance(true);
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        mDatabaseHandler=DatabaseHandler.getInstance(getActivity());
        couponList = (RecyclerView) rootView.findViewById(R.id.couponList);
        featureAdapter = new FeatureFragmentAdapter(getActivity(),feedItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        couponList.setLayoutManager(mLayoutManager);
        couponList.setAdapter(featureAdapter);
        return rootView;
    }

    boolean checkActivity(){
        if(isAdded() && getActivity()!=null )
            return  true;
        else
            return  false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        couponList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if(!limitOver && !isdataLoading && ((LinearLayoutManager)couponList.getLayoutManager()).findLastVisibleItemPosition()>=feedItems.size()/2){
//                    getHotDeals();
//                }
//            }
//        });
//
//        if(lonOnActivity) {
//            lonOnActivity=false;
//            loadData();
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CommonFunctions.isInternetOn(getActivity())) {
               feedItems.clear();
                getHotDeals();

        }
//        if(checkActivity()) {
//            loadData();
//        }
//        else
//            lonOnActivity=true;
    }

    public void loadData(){

        if(checkActivity()) {
            //Toast.makeText(getActivity(), "Last Status"+mDatabaseHandler.getLimitOverStatus("features"), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), "Last Updated Time"+mDatabaseHandler.getLastUpdatedTime("features"), Toast.LENGTH_SHORT).show();
            mCurrentTime = CommonFunctions.getCurrentTimeStamp();
            if(mDatabaseHandler.getLastUpdatedTime("features")!=null) {

                mLastUpdatedTime = Long.valueOf(mDatabaseHandler.getLastUpdatedTime("features"));

                mIsSecondDay = CommonFunctions.isSecondDay(mCurrentTime, mLastUpdatedTime);
                //Toast.makeText(getActivity(), "Second Day:"+mIsSecondDay, Toast.LENGTH_LONG).show();
                    if (mIsSecondDay) {
                        //Get
                        if (CommonFunctions.isInternetOn(getActivity())) {
                            if (feedItems.size() == 0 && !isdataLoading) {
                                feedItems.clear();
                                getHotDeals();
                            } else {
                                rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                                rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);
                            }
                        } else {
                            MainActivity.showMessageDialog("No internet connection...!!!", "#fcf8e3", "#8a6d3b");
                            rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                            rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);

                            feedItems.clear();
                            feedItems.addAll(mDatabaseHandler.getFeaturedStores());
                            featureAdapter.notifyDataSetChanged();

                            ArrayList<Store> tempStoreList=new ArrayList<Store>();
                            for(int count=0;count<10;count++){
                                tempStoreList.add(feedItems.get(count));
                            }
                            fragment.loadData(tempStoreList);
                        }
                    } else {
                        //TODO--Day is not second day get values from database and update this section.
                        feedItems.clear();
                        feedItems.addAll(mDatabaseHandler.getFeaturedStores());
                        featureAdapter.notifyDataSetChanged();

                        rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                        rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);
                        String lLimitOver = mDatabaseHandler.getLimitOverStatus("features");

                        ArrayList<Store> tempStoreList=new ArrayList<Store>();
                        if(feedItems.size()>10) {
                            for (int count = 0; count < 10; count++) {
                                tempStoreList.add(feedItems.get(count));
                            }
                        }
                        fragment.loadData(tempStoreList);

                        //Toast.makeText(getActivity(), "Limit Over:"+lLimitOver, Toast.LENGTH_LONG).show();
                        if (lLimitOver.equals("Completed")) {
                            limitOver = true;
                        } else {
                            limitOver = false;
                            getHotDeals();
                        }
                    }
                }
            else{
                if (CommonFunctions.isInternetOn(getActivity())) {
                    if (feedItems.size() == 0 && !isdataLoading) {
                        feedItems.clear();
                        getHotDeals();
                    } else {
                        rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                        rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);
                    }
                } else {
                    MainActivity.showMessageDialog("No internet connection...!!!", "#fcf8e3", "#8a6d3b");
                    rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                    rootView.findViewById(R.id.couponList).setVisibility(View.GONE);

                }
            }
        }
    }

    private void getHotDeals() {
        System.out.println("cccccccxxx " +(feedItems.size()/10+1)+" "+limitOver);
        isdataLoading=true;
        if(feedItems.size()==0) {
            rootView.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.couponList).setVisibility(View.GONE);
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
               // .addFormDataPart("page", ""+(feedItems.size()/10+1))
                .build();

        new ExecuteServices().executePost(CommonFunctions.mUrl + "userFeaturedStore", new ExecuteServices.OnServiceExecute() {

            @Override
            public void onServiceExecutedResponse(final String response) {
                if(!checkActivity())
                {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                        rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);
                        try {
                            System.out.println("ccccccc responsesss "+response);
                            JSONObject object=new JSONObject(response);
//                            if(object.getString("page").equalsIgnoreCase("-1")){
//                                limitOver=true;
//                                mDatabaseHandler.updateLastUpdatedFeaturedLastOver("Completed");
//                                //Toast.makeText(getActivity(), "Now Limit is Over", Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                            {
//                                mDatabaseHandler.updateLastUpdatedFeaturedLastOver("Incomplete");
//                                //Toast.makeText(getActivity(), "Now Incomplete is Called", Toast.LENGTH_SHORT).show();
//                            }
                            String status=object.getString("status");
                            if(object.has("logoUrl"))
                            CommonFunctions.cloudPath=object.getString("logoUrl");
                            if(object.has("sliderUrl"))
                            CommonFunctions.sliderPath=object.getString("sliderUrl");
                          //  System.out.println("ccccccc pp "+object.getString("page"));

                            if(status.equalsIgnoreCase("1"))
                            {
                                if(object.has("data"))
                                {
                                    JSONArray cArr=object.getJSONArray("data");

                                    ArrayList<Store> lNewStoresList=new ArrayList<Store>();

                                    for(int i=0;i<cArr.length();i++)
                                    {
                                        JSONObject obj=cArr.getJSONObject(i);
//                                        HotCoupon data=new HotCoupon();
//                                        data.setCoupon_id(ob.getString("coupon_id"));
//                                        data.setTitle(ob.getString("title"));
//                                        data.setCode(ob.getString("code"));
//                                        data.setLink(ob.getString("link"));
//                                        data.setExpiry_date(ob.getString("expiry_date"));
//                                        data.setRetailer_id(ob.getString("retailer_id"));
//                                        data.setRetailer_title(ob.getString("retailer_title"));
//                                        data.setRetailer_image(ob.getString("retailer_image"));
//                                        data.setCashback(ob.getString("cashback"));
//                                        feedItems.add(data);
                                        feedItems.add(new Store(obj.getString("retailer_id"),obj.getString("title"),obj.getString("cashback")
                                                ,obj.getString("retailer_status"),obj.getString("url"),obj.getString("image"),obj.has("description")?obj.getString("description"):"",obj.getString("is_favourite"),null));

                                        lNewStoresList.add(new Store(obj.getString("retailer_id"),obj.getString("title"),obj.getString("cashback")
                                                ,obj.getString("retailer_status"),obj.getString("url"),obj.getString("image"),obj.has("description")?obj.getString("description"):"",obj.getString("is_favourite"),null));
                                       // featureAdapter.notifyItemInserted(feedItems.size()-1);
                                        featureAdapter.notifyDataSetChanged();
                                    }

                                    //TODO here you also need to truncate database tables and Insert New Data if it is first day otherwise insert new data only
//                                    mDatabaseHandler.updateLastUpdatedFeaturesTime(mCurrentTime);
//                                    if(mIsSecondDay){
//                                        mDatabaseHandler.deleteFeaturedStoresTable();
//
//                                        for (int count = 0; count < lNewStoresList.size(); count++) {
//                                            mDatabaseHandler.insertFeaturedStores(lNewStoresList.get(count));
//                                        }
//                                        mIsSecondDay=false;
//                                    }
//                                    else {
//                                        for (int count = 0; count < lNewStoresList.size(); count++) {
//                                            mDatabaseHandler.insertFeaturedStores(lNewStoresList.get(count));
//                                        }
//                                    }
//                                    if(feedItems.size()==10){
//                                        fragment.loadData(feedItems);
//                                        getHotDeals();
//                                    }
                                }
                            }
                            else
                            {
                                MainActivity.showMessageDialog("No coupon available.","#f2dede","#a94442");
                            }

                           // isdataLoading=false;
                        } catch (JSONException e) {
                          //  isdataLoading=false;
                            e.printStackTrace();
                        }

                    }
                });

            }

            @Override
            public void onServiceExecutedFailed(String message) {
                isdataLoading=false;
                if(!checkActivity())
                {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                        rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);
                        MainActivity.showMessageDialog("Something went wrong","#f2dede","#a94442");
                    }
                });
            }
        },requestBody);

    }

    /**
     * Created by jack on 03-04-2016.
     */
    class FeatureFragmentAdapter extends RecyclerView.Adapter<FeatureFragmentAdapter.MyViewHolder> {
        SimpleDateFormat sdf=new SimpleDateFormat("dd MMM, yyyy");
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        private Activity activity;
        private LayoutInflater inflater;
        private Context context;
        ArrayList<Store> feedItems;
        SharedPreferences prefs;
        SharedPreferences.Editor prefEditor;
        String userId="";
        private ClipboardManager myClipboard;
        private ClipData myClip;

        public FeatureFragmentAdapter(Activity activity, ArrayList<Store> feedItems) {
            this.activity = activity;
            this.feedItems=feedItems;
            prefs= PreferenceManager.getDefaultSharedPreferences(activity);
            prefEditor=prefs.edit();
            userId=prefs.getString("USER_ID","102");
            myClipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        }

        public FeatureFragmentAdapter() {

        }

        public  class MyViewHolder extends RecyclerView.ViewHolder {
            protected ImageView mStoreImage;
            protected  AppCompatImageView mBasket;
            protected TextView mStoreName, mStoreTxt, mPercentCashback, mExpDate, mCouponCode,mCouponType;
            protected CardView mCardView;

            public MyViewHolder(View view) {
                super(view);
                mBasket=(AppCompatImageView) view.findViewById(R.id.basket);
                mCardView=(CardView)view.findViewById(R.id.card_view);
                mStoreImage = (ImageView) view.findViewById(R.id.storeImage);
                mPercentCashback = (TextView) view.findViewById(R.id.cashbackPercent);
                mStoreTxt = (TextView) view.findViewById(R.id.storeTxt);
                mExpDate= (TextView) view.findViewById(R.id.expDate);
                mCouponCode= (TextView) view.findViewById(R.id.couponCode);
                mCouponType= (TextView) view.findViewById(R.id.couponType);

                mCardView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        userId=prefs.getString("USER_ID","102");
//                        MainActivity.sWebviewData=new WebviewModel();
//                        MainActivity.sWebviewData.setStatus(feedItems.get(getAdapterPosition()).status);
//                        MainActivity.sWebviewData.setStore_id(feedItems.get(getAdapterPosition()).store_id);
//                        MainActivity.sWebviewData.setRetailerName(feedItems.get(getAdapterPosition()).name);
//                        MainActivity.sWebviewData.setCashback(feedItems.get(getAdapterPosition()).cashback);
//                        MainActivity.sWebviewData.setUrl(feedItems.get(getAdapterPosition()).url);
//                       // myClip = ClipData.newPlainText("text", feedItems.get(getAdapterPosition()).getCode());
//                      //  myClipboard.setPrimaryClip(myClip);
//                       // MainActivity.showMessageDialog("Coupon code copied","#dff0d8","#3c763d");
//                        if(userId.equalsIgnoreCase("102"))
//                        {
//                            MainActivity.sComingFrom="HotDeals";
//                            activity.startActivity(new Intent(activity , MLoginActivity.class));
//                        }
//                        else {
//                            activity.startActivity(new Intent(activity, WebviewActivity.class));
//                        }

                        AllStoreFragment.sStoreDataList=feedItems.get(getAdapterPosition());
                        HomeFragment.sBannerImage=""+(getAdapterPosition()+1);
                        System.out.println("Retailer name "+AllStoreFragment.sStoreDataList.name+" status "+AllStoreFragment.sStoreDataList.status);
                        Cursor cs= DatabaseHandler.getInstance(getActivity()).checkSQLiteData(AllStoreFragment.sStoreDataList.store_id);
                        if(cs.getCount()>0){
                            DatabaseHandler.getInstance(getActivity()).deleteStoreRow(AllStoreFragment.sStoreDataList.store_id);
                            DatabaseHandler.getInstance(getActivity()).insertRecentStores(AllStoreFragment.sStoreDataList);
                        }
                        else {
                            DatabaseHandler.getInstance(getActivity()).insertRecentStores(AllStoreFragment.sStoreDataList);
                        }

                        getActivity().startActivity(new Intent(getActivity(), StoreDetailPageScreen.class));
                    }
                });
            }
        }

        @Override
        public FeatureFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.feature_row, parent, false);
            return new FeatureFragmentAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FeatureFragmentAdapter.MyViewHolder holder, int position) {
            Glide.with(activity).load(CommonFunctions.cloudPath+feedItems.get(position).image)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mStoreImage);

            if(!TextUtils.isEmpty(feedItems.get(position).cashback)){
                holder.mPercentCashback.setText(CommonFunctions.fromHtml("" + feedItems.get(position).cashback));

//                if (feedItems.get(position).cashback.contains("%")) {
//                    holder.mPercentCashback.setText(CommonFunctions.fromHtml("" + feedItems.get(position).cashback));
//                } else {
//                    holder.mPercentCashback.setText(CommonFunctions.fromHtml("Upto " + feedItems.get(position).cashback + " Cashback"));
//                }
            }


            holder.mCouponType.setVisibility(View.GONE);
            holder.mBasket.setVisibility(View.GONE);
            holder.mStoreTxt.setSingleLine(false);
            holder.mStoreTxt.setVisibility(View.GONE);
            holder.mStoreTxt.setText(CommonFunctions.fromHtml(feedItems.get(position).desc));
            holder.mStoreTxt.setMaxLines(2);
            holder.mExpDate.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return feedItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

}
