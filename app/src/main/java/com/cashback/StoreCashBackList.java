package com.cashback;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cashback.data.StoreCashBackItems;
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

/**
 * Created by jack on 09-07-2015.
 */
public class StoreCashBackList extends Activity {
    private static final String TAG = "StoreCashBackList";
    ListView mStoreList;
    StoreListCashBack adapter;
    ImageView mBack;
    String URL_FEED,userId="";
    private int preLast;
    public static String page_ids="-1";
    ArrayList<StoreCashBackItems> feedItemsForCashback,feedItems,feedItemsForReferral;
    DatabaseHandler ds;
    int rowCount=0;
    Cursor cs=null;
  //  Spinner paymentTypeSpinner;
    public static int selectedPayment;
    TextView mMessageView;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_cash_back_list);
        prefs= PreferenceManager.getDefaultSharedPreferences(StoreCashBackList.this);
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        ds=DatabaseHandler.getInstance(StoreCashBackList.this);
        mStoreList = (ListView) findViewById(R.id.storeList);
        mMessageView=(TextView)findViewById(R.id.message);
        feedItemsForCashback = new ArrayList<>();
        feedItemsForReferral=new ArrayList<>();
        feedItems=new ArrayList<>();
        adapter = new StoreListCashBack(StoreCashBackList.this, feedItemsForCashback);
        mStoreList.setAdapter(adapter);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;
        ((LinearLayout)findViewById(R.id.backFromReport)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreCashBackList.this.finish();
            }
        });

      //  paymentTypeSpinner=(Spinner)findViewById(R.id.paymentTypeList);

//        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedPayment = position;
//                Log.e("position for referal", "" + position);
//                if (position == 0) {
//                    adapter = new StoreListCashBack(StoreCashBackList.this, feedItemsForCashback);
//                    mStoreList.setAdapter(adapter);
//                } else if (position == 1) {
//                    adapter = new StoreListCashBack(StoreCashBackList.this, feedItemsForReferral);
//                    mStoreList.setAdapter(adapter);
//                }
//                //adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


        if(CommonFunctions.isInternetOn(StoreCashBackList.this))
        {
            DatabaseHandler.getInstance(this).deleteStoreCashbackTable();
            webServicePart();
        }
        else
        {
            showMessageDialog("No internet connection...!!!","#fcf8e3");
            cs=DatabaseHandler.getInstance(this).getSQLiteData("staore_cashback");
            cs.moveToFirst();
            if(cs!=null&& !cs.isClosed())
            {
                rowCount=cs.getCount();
                Log.d("row count", "" + rowCount);
            }
            if(rowCount!=0)
            {
                // Log.e("Featured Stores Offline data", cs.getString(0) + "\n"+cs.getString(1));
                for(int i=0;i<rowCount;i++)
                {
                    StoreCashBackItems items=new StoreCashBackItems();
                    items.setTransaction_Id(cs.getString(0));
                    items.setRetailer(cs.getString(1));
                    items.setRetailer_Id(cs.getString(2));
                    items.setAmount(cs.getString(3));
                    items.setDateCreated(cs.getString(4));
                    items.setProcessDate(cs.getString(5));
                    items.setPayment_Type(cs.getString(6));
                    items.setStatus(cs.getString(7));
                    feedItems.add(items);
                    if(cs.getString(6).equalsIgnoreCase("referral_cashback"))
                    {
                        feedItemsForReferral.add(items);
                    }
                    else
                    {
                        feedItemsForCashback.add(items);
                    }
                    //Log.e("Featured", cs.getString(0) + "\n"+cs.getString(1));
                    cs.moveToNext();
                }
                adapter.notifyDataSetChanged();
            }
            else
            {
                showMessageDialog("No data found.","#f2dede");
            }
        }

        findViewById(R.id.moreOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(StoreCashBackList.this, findViewById(R.id.moreOption));
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.pop_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                       // Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                       switch (item.getItemId())
                       {
                           case R.id.cashback:
                               if(feedItemsForCashback.size()>0) {
                                   selectedPayment = 0;
                                   adapter = new StoreListCashBack(StoreCashBackList.this, feedItemsForCashback);
                                   mStoreList.setAdapter(adapter);
                               }
                               else
                               {
                                   showMessageDialog("No cashback.","#f2dede");
                               }
                               break;

                           case R.id.referal:
                               if(feedItemsForReferral.size()>0) {
                                   selectedPayment = 1;
                                   adapter = new StoreListCashBack(StoreCashBackList.this, feedItemsForReferral);
                                   mStoreList.setAdapter(adapter);
                               }
                               else
                               {
                                   showMessageDialog("No referral cashback.","#f2dede");
                               }
                               break;
                       }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

    }
    public void showMessageDialog(String message,String color)
    {
        if(mMessageView.getVisibility()==View.VISIBLE)
        {
            mMessageView.setVisibility(View.GONE);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mMessageView.setBackgroundColor(Color.parseColor(color));
        mMessageView.setText(message);
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.startAnimation(slideDownAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageView.startAnimation(slideUpAnimation);
                mMessageView.setVisibility(View.GONE);
                if(exitActivity) {
                    activity.finish();
                }
                exitActivity=false;
            }
        },4000);
    }

    void webServicePart() {
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        mStoreList.setVisibility(View.GONE);
        String url=CommonFunctions.mUrl+"cashbackDetails";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .build();

        new ExecuteServices().executePost(url, new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            findViewById(R.id.progressbar).setVisibility(View.GONE);
                            mStoreList.setVisibility(View.VISIBLE);
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
                        mStoreList.setVisibility(View.VISIBLE);
                    }
                });

            }
        },requestBody);
    }


    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void parseJsonFeed(JSONObject response) {
        try {

            String status = response.getString("status");
            String message = response.getString("message");
//
            if (status.equalsIgnoreCase("1")) {
                JSONArray feedArray = response.getJSONArray("data");
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);
                    StoreCashBackItems items=new StoreCashBackItems();
                    items.setTransaction_Id(feedObj.getString("transaction_id"));
                    items.setRetailer(feedObj.getString("retailer"));
                    items.setRetailer_Id(feedObj.getString("retailer_id"));
                    items.setAmount(feedObj.getString("amount"));
                    items.setDateCreated(feedObj.getString("date_created"));
                    items.setProcessDate(feedObj.getString("process_date"));
                    items.setPayment_Type(feedObj.getString("payment_type"));
                    items.setStatus(feedObj.getString("status"));
                    if(feedObj.getString("payment_type").equalsIgnoreCase("referral_cashback"))
                    {
                        feedItemsForReferral.add(items);
                    }
                    else
                    {
                        feedItemsForCashback.add(items);
                    }

                  DatabaseHandler.getInstance(this).insertStoreCashback(items);
                }
                // notify data changes to list adapater
                adapter.notifyDataSetChanged();
            } else {
//                if (!page_ids.equalsIgnoreCase("-1")) {
//                    CommonFunction.displayToast(StoreCashBackList.this, message);
//                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

class StoreListCashBack extends BaseAdapter
{
    private Activity activity;
    private LayoutInflater inflater;
    private List<StoreCashBackItems> feedItems;
    public StoreListCashBack(Activity act,ArrayList<StoreCashBackItems> feedItems)
    {
        activity=act;
        this.feedItems=feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItems viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ticket_history_row, null);
            viewHolder=new ViewHolderItems();
            viewHolder.mRetailer=(TextView)convertView.findViewById(R.id.productName);
            //   viewHolder.mPercentCashback=(TextView)convertView.findViewById(R.id.percentCashback);
            viewHolder.mDate=(TextView)convertView.findViewById(R.id.date);
            viewHolder.mPaymentType=(TextView)convertView.findViewById(R.id.ticketId);
            viewHolder.mPercentCashback = (TextView) convertView.findViewById(R.id.orderId);
            viewHolder.mStatus = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder=(ViewHolderItems)convertView.getTag();
        }



        StoreCashBackItems item = feedItems.get(position);

        if(StoreCashBackList.selectedPayment==1&&item.getPayment_Type().equalsIgnoreCase("referral_cashback")) {
            viewHolder.mRetailer.setText("Store Name : " + item.getRetailer());
            viewHolder.mPercentCashback.setText("Cashback : " + item.getAmount());
            viewHolder.mDate.setText("Date : " + item.getDateCreated());
            viewHolder.mPaymentType.setText("Payment Type : " + item.getPayment_Type());
            viewHolder.mStatus.setText("" + item.getStatus());
        }
        else if(StoreCashBackList.selectedPayment==0)
        {
            viewHolder.mRetailer.setText("Store Name : " + item.getRetailer());
            viewHolder.mPercentCashback.setText("Cashback : " + item.getAmount());
            viewHolder.mDate.setText("Date : " + item.getDateCreated());
            viewHolder.mPaymentType.setText("Payment Type : " + item.getPayment_Type());
            viewHolder.mStatus.setText("" + item.getStatus());
        }
        if(item.getStatus().equalsIgnoreCase("pending"))
        {
            viewHolder.mStatus.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.mStatus.setBackgroundResource(R.drawable.pending_orange);
        }
        else if(item.getStatus().equalsIgnoreCase("confirmed"))
        {
            viewHolder.mStatus.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.mStatus.setBackgroundResource(R.drawable.btn_stand_focus);

        }
        else if(item.getStatus().equalsIgnoreCase("declined"))
        {
            viewHolder.mStatus.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.mStatus.setBackgroundResource(R.drawable.red_btn);
        }

        return convertView;
    }


    static class ViewHolderItems
    {

        protected TextView mDate,mPaymentType,mPercentCashback,mStatus,mRetailer;
        LinearLayout mLinLayout;
    }
}