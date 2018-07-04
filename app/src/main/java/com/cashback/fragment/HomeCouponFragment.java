package com.cashback.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cashback.MainActivity;
import com.cashback.R;
import com.cashback.adapter.FeatureAdapter;
import com.cashback.data.HotCoupon;
import com.cashback.data.Store;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;
import com.cashback.util.ExecuteServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by jack on 28-04-2016.
 */
public class HomeCouponFragment extends Fragment {

    private String URL_FEED;
    private RecyclerView couponList;
    private FeatureAdapter featureAdapter;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    ArrayList<HotCoupon> feedItems;
    boolean limitOver=false;
    String userId = "";
    View rootView;
    boolean nowStart=false;
    boolean isdataLoading=false;
    private DatabaseHandler mDatabaseHandler;
    private long mLastUpdatedTime;
    private long mCurrentTime;
    private boolean mIsSecondDay=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feature, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefEditor = prefs.edit();
        setRetainInstance(true);
        mDatabaseHandler=DatabaseHandler.getInstance(getActivity());
        userId = prefs.getString("USER_ID", "102");
        couponList = (RecyclerView) rootView.findViewById(R.id.couponList);
        feedItems = new ArrayList<>();
        featureAdapter = new FeatureAdapter(getActivity(), feedItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        couponList.setLayoutManager(mLayoutManager);
        couponList.setAdapter(featureAdapter);

//        couponList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
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
//                    //Toast.makeText(getActivity(), "onScrolled Called in Coupon", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CommonFunctions.isInternetOn(getActivity())) {
                 feedItems.clear();
                getHotDeals();

        } else {
            MainActivity.showMessageDialog("No internet connection...!!!", "#fcf8e3", "#8a6d3b");
            rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
            rootView.findViewById(R.id.couponList).setVisibility(View.GONE);
        }
    }



    boolean checkActivity() {
        if (isAdded() && getActivity() != null)
            return true;
        else
            return false;
    }


    public void loadData(){
        nowStart=true;
        if (checkActivity()) {

            mCurrentTime = CommonFunctions.getCurrentTimeStamp();

            if(mDatabaseHandler.getLastUpdatedTime("home_coupons")!=null){
                mLastUpdatedTime = Long.valueOf(mDatabaseHandler.getLastUpdatedTime("home_coupons"));

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
                        feedItems.addAll(mDatabaseHandler.getAllHomeCoupons());
                        featureAdapter.notifyDataSetChanged();

                    }
                } else {
                    //TODO--Day is not second day get values from database and update this section.
                    feedItems.clear();
                    feedItems.addAll(mDatabaseHandler.getAllHomeCoupons());
                    featureAdapter.notifyDataSetChanged();

                    rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                    rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);
                    String lLimitOver = mDatabaseHandler.getLimitOverStatus("home_coupons");

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
        isdataLoading=true;
      //  System.out.println("loadinddddd "+""+(feedItems.size()/10+1));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
        //        .addFormDataPart("page", ""+(feedItems.size()/10+1))
                .build();
        if(feedItems.size()==0) {
            rootView.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.couponList).setVisibility(View.GONE);
        }

        new ExecuteServices().executePost(CommonFunctions.mUrl+ "coupons", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {

                if (!checkActivity()) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                        rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);
                        try {
                            JSONObject object = new JSONObject(response);
                            System.out.println("loadinddddd "+response);
//                            if(object.getString("page").equalsIgnoreCase("-1")){
//                                limitOver=true;
//                                mDatabaseHandler.updateLastUpdatedHomeCopounsLastOver("Completed");
//                               // Toast.makeText(getActivity(), "Now Limit is Over", Toast.LENGTH_LONG).show();
//                            }
//                            else{
//                                mDatabaseHandler.updateLastUpdatedHomeCopounsLastOver("Incomplete");
//                                //Toast.makeText(getActivity(), "Now Limit is Incomplete", Toast.LENGTH_SHORT).show();
//                            }
                            String status = object.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                if (object.has("coupons")) {
                                    JSONArray cArr = object.getJSONArray("coupons");
                                    ArrayList<HotCoupon> tempCouponsList=new ArrayList<HotCoupon>();
                                    System.out.println("loadinddddd cAr"+cArr.length());
                                    for (int i = 0; i < cArr.length(); i++) {
                                        JSONObject ob = cArr.getJSONObject(i);
                                        HotCoupon data = new HotCoupon();
                                        data.setCoupon_id(ob.getString("coupon_id"));
                                        data.setTitle(ob.getString("title"));
                                        data.setCode(ob.getString("code"));
                                        data.setLink(ob.getString("link"));
                                        data.setStart_date(ob.getString("start_date"));
                                        data.setExpiry_date(ob.getString("expiry_date"));
                                        data.setRetailer_id(ob.getString("retailer_id"));
                                        data.setRetailer_title(ob.getString("retailer_title"));
                                        data.setRetailer_image(ob.getString("retailer_image"));
                                        data.setCashback(ob.getString("cashback"));
                                        feedItems.add(data);
                                      //  tempCouponsList.add(data);
                                     //   featureAdapter.notifyItemInserted(feedItems.size()-1);
                                    }
                                  featureAdapter.notifyDataSetChanged();
//                                    mDatabaseHandler.updateLastUpdatedHomeCouponsTime(mCurrentTime);
//
//                                    if(mIsSecondDay){
//                                        mDatabaseHandler.deleteHotCoupons();
//                                        for (int count = 0; count < tempCouponsList.size(); count++) {
//                                            mDatabaseHandler.insertHomeCoupons(tempCouponsList.get(count));
//                                        }
//                                        mIsSecondDay=false;
//                                    }
//                                    else {
//                                        for (int count = 0; count < tempCouponsList.size(); count++) {
//                                            mDatabaseHandler.insertHomeCoupons(tempCouponsList.get(count));
//                                        }
//                                    }
                                }
                            } else {
                                MainActivity.showMessageDialog("No coupon available.", "#f2dede", "#a94442");
                            }
                            isdataLoading=false;
                        } catch (JSONException e) {
                            isdataLoading=false;
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onServiceExecutedFailed(String message) {
                isdataLoading=false;
                if (!checkActivity()) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                        rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);
                        MainActivity.showMessageDialog("Something went wrong.", "#f2dede", "#a94442");
                    }
                });
            }
        }, requestBody);
    }


}
