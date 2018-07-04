package com.cashback;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cashback.adapter.NotificationAdapter;
import com.cashback.data.NotificationModel;
import com.cashback.data.Store;
import com.cashback.fragment.AllStoreFragment;
import com.cashback.fragment.RecentStoresFragment;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;
import com.cashback.util.ExecuteServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jack on 15-03-2017.
 */

public class NotificationActivity extends Activity {

    LinearLayout mBackFromNotification;
    ListView mNotificationList;
    TextView mToastView;
    NotificationAdapter mAdapter;
    View v;
    ArrayList<NotificationModel> feedItems;
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_xml);
        mBackFromNotification=(LinearLayout)findViewById(R.id.backFromNotification);
        mBackFromNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mNotificationList=(ListView) findViewById(R.id.notificationList);
        mToastView=(TextView)findViewById(R.id.toast);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;
       // mMessageDialog.setText("There is no new notification...!!!");
        feedItems = new ArrayList<NotificationModel>();
        mAdapter=new NotificationAdapter(this,feedItems);
        mNotificationList.setAdapter(mAdapter);
        mNotificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                AllStoreFragment.sStoreDataList=  new Store(feedItems.get(i).get_RetailerId(),feedItems.get(i).get_RetailerName(),feedItems.get(i).get_Cashback()
                        ,"active",feedItems.get(i).get_RetailerUrl(),feedItems.get(i).get_RetailerImage(),feedItems.get(i).get_RetailerDesc(),"1",null);

                Cursor cs= DatabaseHandler.getInstance(NotificationActivity.this).checkSQLiteData(AllStoreFragment.sStoreDataList.store_id);

                if(cs.getCount()>0) {
                    DatabaseHandler.getInstance(NotificationActivity.this).deleteStoreRow(AllStoreFragment.sStoreDataList.store_id);
                    DatabaseHandler.getInstance(NotificationActivity.this).insertRecentStores(AllStoreFragment.sStoreDataList);
                }
                else
                {
                    DatabaseHandler.getInstance(NotificationActivity.this).insertRecentStores(AllStoreFragment.sStoreDataList);
                }
                RecentStoresFragment.reload=true;

                Intent intent = new Intent(NotificationActivity.this, StoreDetailPageScreen.class);
                startActivity(intent);
            }


        });
     final    String url= CommonFunctions.mUrl + "notification";
        Log.e("user data ", " " + url);

        if(CommonFunctions.isInternetOn(NotificationActivity.this))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    webServicePart(url);
                }
            },0);
        }
        else
        {
            showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
        }




    }

    public void showMessageDialog(String message,String color,String textColor)
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
        },5000);
    }

    void webServicePart(String URL_FEED) {
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        mNotificationList.setVisibility(View.GONE);
        System.out.println("nnnnnn"+URL_FEED);
        new ExecuteServices().execute(URL_FEED, new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e("ResponseNOtification",response);
                            findViewById(R.id.progressbar).setVisibility(View.GONE);
                            mNotificationList.setVisibility(View.VISIBLE);
                            parseJsonFeed(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onServiceExecutedFailed(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        mNotificationList.setVisibility(View.VISIBLE);
                        showMessageDialog("Something went wrong.","#f2dede","#a94442");
                    }
                });


            }
        });
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            String status = response.getString("status");
            String message = response.getString("message");
            if (status.equalsIgnoreCase("1")) {
                findViewById(R.id.NoNotiFound).setVisibility(View.GONE);
                mNotificationList.setVisibility(View.VISIBLE);
                JSONArray feedArray = response.getJSONArray("data");
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);
                    NotificationModel data=new NotificationModel();
                    data.set_NotificationId(feedObj.getString("id"));
                    data.set_Title(feedObj.getString("title"));
                    data.set_Description(feedObj.getString("title"));
                    data.set_Date(feedObj.getString("date"));
                    data.set_Image(feedObj.getString("image"));
                    JSONObject obj=feedObj.getJSONObject("retailer");
                    if(!obj.has("retailer_id"))continue;
                    data.set_RetailerId(obj.getString("retailer_id"));
                    data.set_RetailerName(obj.getString("title"));
                    data.set_Cashback(obj.getString("cashback"));
                    data.set_RetailerDesc(obj.getString("description"));
                    data.set_RetailerImage(obj.getString("image"));
                    data.set_RetailerUrl(obj.getString("url"));
                    feedItems.add(data);
                }
                mAdapter.notifyDataSetChanged();
            } else {

                findViewById(R.id.NoNotiFound).setVisibility(View.VISIBLE);
                mNotificationList.setVisibility(View.GONE);
                showMessageDialog("No notification found.","#f2dede","#a94442");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
