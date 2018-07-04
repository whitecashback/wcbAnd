package com.cashback;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jack on 09-07-2015.
 */
public class StatementActivity extends Activity {

    private static final String TAG = "StoreCashBackList";
    ListView mStoreList;
    StatementAdapter adapter;
    ImageView mBack;
    String URL_FEED;
    private int preLast;
    public static String page_ids="-1";
    ArrayList<StoreCashBackItems> feedItemsForCashback,feedItems,feedItemsForReferral;
    DatabaseHandler ds;
    int rowCount=0;
    Cursor cs=null;
    Spinner paymentTypeSpinner;
    public static int selectedPayment;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    TextView mMessageView,mRefferedTo;
    String userId="";
    boolean isStart=true;
    LinearLayout mLebelCashback,mLebelRefer;
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statement);
        prefs= PreferenceManager.getDefaultSharedPreferences(StatementActivity.this);
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        ds=DatabaseHandler.getInstance(StatementActivity.this);
        mStoreList = (ListView) findViewById(R.id.storeList);
        feedItemsForCashback = new ArrayList<>();
        feedItemsForReferral=new ArrayList<>();
        feedItems=new ArrayList<>();
        adapter = new StatementAdapter(StatementActivity.this, feedItemsForCashback);
        mStoreList.setAdapter(adapter);
        mLebelCashback=(LinearLayout)findViewById(R.id.lebel);
        mLebelRefer=(LinearLayout)findViewById(R.id.lebelRefer);
        mMessageView=(TextView)findViewById(R.id.message);
        findViewById(R.id.backFromReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatementActivity.this.finish();
            }
        });
        TextView mTitle=(TextView)findViewById(R.id.title);
        TextView mDate=(TextView)findViewById(R.id.date);
        TextView storeName=(TextView)findViewById(R.id.sn);
        TextView mPType=(TextView)findViewById(R.id.pt);
        TextView mCB=(TextView)findViewById(R.id.cb);
        TextView mStatus=(TextView)findViewById(R.id.status);
        mRefferedTo=(TextView)findViewById(R.id.referedTo);
        paymentTypeSpinner=(Spinner)findViewById(R.id.paymentTypeList);

        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPayment = position;
                Log.e("position for referal", "" + position);
                if(isStart){
                    isStart=false;
                    return;

                }
                if (position == 0) {

                  //  if(feedItemsForCashback.size()>0) {
                    mRefferedTo.setVisibility(View.GONE);
                    selectedPayment = 0;
                    mLebelCashback.setVisibility(View.VISIBLE);
                    mLebelRefer.setVisibility(View.GONE);
                    adapter = new StatementAdapter(StatementActivity.this, feedItemsForCashback);
                    mStoreList.setAdapter(adapter);
                    //}
                    //else
                    //{
                       // showMessageDialog("No store cashback.","#f2dede","#a94442");
                    //}
                } else if (position == 1) {
                    //if(feedItemsForReferral.size()>0) {
                        selectedPayment = 1;
                    mLebelCashback.setVisibility(View.GONE);
                    mLebelRefer.setVisibility(View.VISIBLE);

                        adapter = new StatementAdapter(StatementActivity.this, feedItemsForReferral);
                        mStoreList.setAdapter(adapter);
                    //}
                    //else
                    //{
                     //   adapter = new StatementAdapter(StatementActivity.this, feedItemsForReferral);
                      //  mStoreList.setAdapter(adapter);
                     //   showMessageDialog("No referral cashback.","#f2dede","#a94442");
                    //}
                }
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        if(CommonFunctions.isInternetOn(StatementActivity.this))
        {
            DatabaseHandler.getInstance(this).deleteStoreCashbackTable();
            webServicePart();
        }
        else
        {
            showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
            //CommonFunctions.displayToast(StatementActivity.this, "No internet...");
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
                    items.setReferUser(cs.getString(8));
                    items.setReffer_To(cs.getString(9));
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
            else {
                showMessageDialog("No record found.","#f2dede","#a94442");
            }
        }
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;
    }


    public void showMessageDialog(String message,String color,String textColor)
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
        mMessageView.setTextColor(Color.parseColor(textColor));
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
       // CommonFunction.viewProgressVisible(StatementActivity.this, "Fetching from server...");
        mStoreList.setVisibility(View.GONE);
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
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
                            //CommonFunction.viewProgressGone();
                            mStoreList.setVisibility(View.VISIBLE);
                            findViewById(R.id.progressbar).setVisibility(View.GONE);
                            parseJsonFeed(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            @Override
            public void onServiceExecutedFailed(String msg) {
               // CommonFunction.viewProgressGone();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mStoreList.setVisibility(View.VISIBLE);
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        showMessageDialog("Something went wrong.","#f2dede","#a94442");
                    }
                });
            }
        }, requestBody);
    }


    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void parseJsonFeed(JSONObject response) {
        try {

            String status = response.getString("status");
            String message = response.getString("message");
            System.out.println(response.toString());
//
            if (status.equalsIgnoreCase("1")) {
                JSONArray feedArray = response.getJSONArray("data");
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);
                    StoreCashBackItems items=new StoreCashBackItems();
                    System.out.println(" running  "+i);
                    items.setTransaction_Id(feedObj.getString("transaction_id"));
                    items.setRetailer(feedObj.getString("retailer"));
                    items.setRetailer_Id(feedObj.getString("retailer_id"));
                    items.setAmount(feedObj.getString("amount"));
                    items.setDateCreated(feedObj.getString("date_created"));
                    items.setProcessDate(feedObj.getString("process_date"));
                    items.setPayment_Type(feedObj.getString("payment_type"));
                    items.setStatus(feedObj.getString("status"));
                    if(feedObj.has("refer")) {
                        items.setReffer_To(feedObj.getString("refer"));
                    }
                    else
                    {
                        items.setReffer_To("0");

                    }
                    if(feedObj.has("refer_user")) {
                        items.setReferUser(feedObj.getString("refer_user"));
                    }
                    else
                    {
                        items.setReferUser("");

                    }
                    if(items.getReffer_To().equalsIgnoreCase("1"))
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
                showMessageDialog("No data found.","#f2dede","#a94442");
                if (!page_ids.equalsIgnoreCase("-1")) {
                   // CommonFunction.displayToast(StatementActivity.this, message);
                }

            }
            //CommonFunction.viewProgressGone();
        } catch (JSONException e) {
            e.printStackTrace();
            showMessageDialog("Oops! Something went wrong.","#f2dede","#a94442");
        }
    }
}

class StatementAdapter extends BaseAdapter
{
    private Activity activity;
    private LayoutInflater inflater;
    private List<StoreCashBackItems> feedItems;
    public StatementAdapter(Activity act,ArrayList<StoreCashBackItems> feedItems)
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
            convertView = inflater.inflate(R.layout.statement_row, null);
            viewHolder=new ViewHolderItems();
            viewHolder.mRetailer=(TextView)convertView.findViewById(R.id.retailer);
            //   viewHolder.mPercentCashback=(TextView)convertView.findViewById(R.id.percentCashback);
            viewHolder.mDate=(TextView)convertView.findViewById(R.id.date);
            viewHolder.mPaymentType=(TextView)convertView.findViewById(R.id.paymentType);
            viewHolder.mPercentCashback = (TextView) convertView.findViewById(R.id.cashback);
            viewHolder.mStatus = (TextView) convertView.findViewById(R.id.status);
            viewHolder.mLinLayout=(LinearLayout)convertView.findViewById(R.id.linRow);
            viewHolder.mRefferTo=(TextView)convertView.findViewById(R.id.referTo);
            viewHolder.mRetailer1=(TextView)convertView.findViewById(R.id.retailer1);
            //   viewHolder.mPercentCashback=(TextView)convertView.findViewById(R.id.percentCashback);
            viewHolder.mDate1=(TextView)convertView.findViewById(R.id.date1);
            viewHolder.mPaymentType1=(TextView)convertView.findViewById(R.id.paymentType1);
            viewHolder.mPercentCashback1 = (TextView) convertView.findViewById(R.id.cashback1);
            viewHolder.mStatus1 = (TextView) convertView.findViewById(R.id.status1);
            viewHolder.mReferToLayout=(LinearLayout)convertView.findViewById(R.id.referToLayout);
            viewHolder.mCashbackLayout=(LinearLayout)convertView.findViewById(R.id.cashbackLayout) ;
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder=(ViewHolderItems)convertView.getTag();
        }


        if(position%2==0)
        {
            viewHolder.mLinLayout.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
        else
        {
            viewHolder.mLinLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        StoreCashBackItems item = feedItems.get(position);

        if(StatementActivity.selectedPayment==1&&item.getReffer_To().equalsIgnoreCase("1")) {
            viewHolder.mReferToLayout.setVisibility(View.VISIBLE);
            viewHolder.mCashbackLayout.setVisibility(View.GONE);
            viewHolder.mRetailer1.setText("" + item.getRetailer());
            viewHolder.mPercentCashback1.setText("" + item.getAmount());
            viewHolder.mDate1.setText("" + item.getDateCreated());
            viewHolder.mPaymentType1.setText("" + item.getPayment_Type());
            viewHolder.mStatus1.setText("" + item.getStatus());
            viewHolder.mRefferTo.setText(""+item.getReferUser());

        }
        else if(StatementActivity.selectedPayment==0)
        {
            viewHolder.mReferToLayout.setVisibility(View.GONE);
            viewHolder.mCashbackLayout.setVisibility(View.VISIBLE);
            viewHolder.mRetailer.setText("" + item.getRetailer());
            viewHolder.mPercentCashback.setText("" + item.getAmount());
            viewHolder.mDate.setText("" + item.getDateCreated());
            viewHolder.mPaymentType.setText("" + item.getPayment_Type());
            viewHolder.mStatus.setText("" + item.getStatus());

        }
        if(item.getStatus().equalsIgnoreCase("pending"))
        {
            viewHolder.mStatus.setTextColor(Color.parseColor("#ef7625"));
            viewHolder.mStatus1.setTextColor(Color.parseColor("#ef7625"));
        }
        else if(item.getStatus().equalsIgnoreCase("confirmed"))
        {
            viewHolder.mStatus1.setTextColor(Color.parseColor("#16943e"));
            viewHolder.mStatus.setTextColor(Color.parseColor("#16943e"));
        }
        else if(item.getStatus().equalsIgnoreCase("declined"))
        {
            viewHolder.mStatus1.setTextColor(Color.parseColor("#e10a1f"));
            viewHolder.mStatus.setTextColor(Color.parseColor("#e10a1f"));
        }

        return convertView;
    }


    static class ViewHolderItems
    {

        protected TextView mDate,mPaymentType,mPercentCashback,mStatus,mRetailer,mRefferTo,
                mDate1,mPaymentType1,mPercentCashback1,mStatus1,mRetailer1;
        LinearLayout mLinLayout,mCashbackLayout,mReferToLayout;
    }
}