package com.cashback.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cashback.R;
import com.cashback.StoreDetailPageScreen;
import com.cashback.adapter.AllDealsListAdapter;
import com.cashback.data.CashbackModel;
import com.cashback.data.CoupenListItems;
import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AllDealsFragment extends Fragment
{
    RecyclerView recycler_allDealsList;
    ArrayList<CoupenListItems> feedItems;
    AllDealsListAdapter adapter;
    public static ArrayList<CoupenListItems> sCouponList,sOfferList;
    String isFavourite="0";
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="";
    public static String stats,retailId,title,url,image,cashbck,isFav="0";
    public static String sStoreImage="",desc="",cashbackRates;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_all_deals, container, false);
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        recycler_allDealsList = (RecyclerView) v.findViewById(R.id.recycler_allDealsList);
        sCouponList=new ArrayList<>();
        sOfferList=new ArrayList<>();

        if(checkActivity()) {

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recycler_allDealsList.setLayoutManager(mLayoutManager);
            feedItems = new ArrayList<CoupenListItems>();
            adapter = new AllDealsListAdapter(getActivity(), feedItems);
            recycler_allDealsList.setAdapter(adapter);

            if (CommonFunctions.isInternetOn(getActivity())) {
                v.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                v.findViewById(R.id.recycler_allDealsList).setVisibility(View.GONE);
                networkCall();
            }
            else {
                StoreDetailPageScreen.showMessageDialog("Oops!! No internet connection..", "#fcf8e3", "#8a6d3b");
                //TODO--Here also update data from database
            }
        }
        return v;
    }

      boolean checkActivity(){
         if(isAdded() && getActivity()!=null )
            return  true;
         else
            return  false;
      }

    private void networkCall() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id",userId)
                .addFormDataPart("retailer_id", AllStoreFragment.sStoreDataList.store_id)
                .build();

        new ExecuteServices().executePost(CommonFunctions.mUrl + "storeDetails", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                if(!checkActivity())
                {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            v.findViewById(R.id.progressbar).setVisibility(View.GONE);
                            v.findViewById(R.id.recycler_allDealsList).setVisibility(View.VISIBLE);
                            Log.i("All Deals..",response.toString());
                            int maxLogSize = 1000;
                            for(int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start = i * maxLogSize;
                                int end = (i+1) * maxLogSize;
                                end = end > response.toString().length() ? response.toString().length() : end;
                                Log.v("All Deals", response.toString().substring(start, end));
                            }
                            parseJsonFeed(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onServiceExecutedFailed(String message) {
                if(checkActivity()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            v.findViewById(R.id.progressbar).setVisibility(View.GONE);
                            v.findViewById(R.id.recycler_allDealsList).setVisibility(View.VISIBLE);
                        }
                    });
                }

            }
        },requestBody);
    }


    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {

            String status=response.getString("status");
            String message=response.getString("message");
            if(response.has("retailer"))
            {
                JSONObject ob=response.getJSONObject("retailer");
                stats=ob.getString("status");
                retailId=ob.getString("retailer_id");
                title=ob.getString("title");
                url=ob.getString("url");
                sStoreImage=image=ob.getString("image");
                cashbck=ob.getString("cashback");
                desc=ob.getString("description");
                cashbackRates=ob.getString("cashback_rates");
                if(desc!=null){
                    AllStoreFragment.sStoreDataList.desc=desc;
                    StoreDetailPageScreen.displayInfoIcon();
                }

                StoreDetailPageScreen.sHandleData.sendEmptyMessage(0);
            }

            if (response.has("cashback_rate"))
            {
                //    AllStoreFragment.sStoreDataList.cashbackrates=cashbackRates;
                    JSONArray feedArray = response.getJSONArray("cashback_rate");
                    ArrayList<CashbackModel> cashBackTableList = new ArrayList<>();
                    for (int i = 0; i < feedArray.length(); i++) {
                        JSONObject feedObj = (JSONObject) feedArray.get(i);
                        CashbackModel data = new CashbackModel();
                        data.setCatName(feedObj.getString("cat_title"));
                        data.setCatValue(feedObj.getString("rate"));
                        cashBackTableList.add(data);
                    }
                    ((StoreDetailPageScreen)getActivity()).setCategoryAdtapter(cashBackTableList);

            }

            if(status.equalsIgnoreCase("1")) {
                JSONArray feedArray = response.getJSONArray("coupon");

                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);

                    CoupenListItems item = new CoupenListItems();
                    item.set_Coupen_Desc(feedObj.getString("description"));
                    item.set_CoupenTitle(feedObj.getString("title"));
                    item.set_CoupenLink(feedObj.getString("link"));
                    item.set_Coupon_Code(feedObj.getString("code"));
                    item.setCoupon_End_Date(feedObj.getString("end_date"));
                    item.set_Coupen_Type(feedObj.getString("coupon_type"));
                    item.set_Coupon_Like(feedObj.getString("likes"));
                    item.set_Coupon_Exclusive(feedObj.getString("exclusive"));

                    if(item.get_Coupen_Type().equalsIgnoreCase("coupon"))
                    {
                        sCouponList.add(item);
                        System.out.println("coupon");
                    }
                    else
                    {
                        sOfferList.add(item);
                        System.out.println("no coupon");

                    }
                    feedItems.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
            else {
           // StoreDetailPageScreen.showMessageDialog("Oops!! No coupon/deal found.","#f2dede","#a94442");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}