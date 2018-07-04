package com.cashback.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cashback.MainActivity;
import com.cashback.R;
import com.cashback.adapter.FavouriteAdapter;
import com.cashback.adapter.FeatureAdapter;
import com.cashback.data.Store;
import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Handler;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jack on 15-03-2017.
 */

public class FavouriteFragment extends Fragment {
    private String URL_FEED,userId="";
    private RecyclerView favList;
    private FavouriteAdapter featureAdapter;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    ArrayList<Store> feedItems;
    public static ArrayList<String> shareDataList;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_feature, container, false);
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        favList = (RecyclerView) rootView.findViewById(R.id.couponList);
        feedItems=new ArrayList<>();
        shareDataList=new ArrayList<>();
        if(checkActivity()) {
            featureAdapter = new FavouriteAdapter(getActivity(), feedItems);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity()) {
                @Override
                public boolean supportsPredictiveItemAnimations() {
                    return true;
                }
            };
            favList.setLayoutManager(mLayoutManager);
            favList.setAdapter(featureAdapter);
            requestData();
        }
        return rootView;
    }
    boolean checkActivity(){
        if(isAdded() && getActivity()!=null )
            return  true;
        else
            return  false;

    }
    public void requestData()
    {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .build();
        if(CommonFunctions.isInternetOn(getActivity())) {
            getFavourite(requestBody);
        }


        else
        {
            MainActivity.showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
            rootView.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.couponList).setVisibility(View.GONE);
        }
    }

    private void getFavourite(RequestBody requestBody) {

        rootView.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.couponList).setVisibility(View.GONE);
        new ExecuteServices().executePost(CommonFunctions.mUrl + "favoriteStores", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(String response) {
                try {

                    System.out.println(response);
                    String status=new JSONObject(response).getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray array = new JSONObject(response).getJSONArray("data");
                        int length = array.length();
                        feedItems.clear();
                        shareDataList.clear();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            feedItems.add(new Store(obj.getString("retailer_id"), obj.getString("title"), obj.getString("cashback")
                                    , obj.getString("retailer_status"), obj.getString("url"), obj.getString("image"), obj.has("description")?obj.getString("description"):"", "1",null));
                            shareDataList.add(obj.getString("share_text"));
                            System.out.println("running ....");
                        }
                        if (!checkActivity())
                        {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                                rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);
                                featureAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    else
                    {
                        if (!checkActivity())
                        {
                            return;

                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                                rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);
                                MainActivity.showMessageDialog("There is no wish list.","#f2dede","#a94442");
                            }
                        });
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void onServiceExecutedFailed(String message) {
                if (!checkActivity())
                {
                    return;

                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rootView.findViewById(R.id.progressbar).setVisibility(View.GONE);
                        rootView.findViewById(R.id.couponList).setVisibility(View.VISIBLE);
                        MainActivity.showMessageDialog("Something went wrong.","#f2dede","#a94442");
                    }
                });
            }
        },requestBody);


    }


}