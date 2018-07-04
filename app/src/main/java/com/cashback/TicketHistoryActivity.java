package com.cashback;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.cashback.data.TicketModel;
import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jack on 16-09-2016.
 */
public class TicketHistoryActivity extends Activity
{
    ListView mTicketList;
    ArrayList<TicketModel> feedItems;
    TicketListAdapter mAdapter;
    String userId="";
    TextView mMessageView;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referral_earning_layout);
        prefs= PreferenceManager.getDefaultSharedPreferences(TicketHistoryActivity.this);
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        mTicketList=(ListView)findViewById(R.id.ticketList);
        feedItems=new ArrayList<>();
        mAdapter=new TicketListAdapter(TicketHistoryActivity.this,feedItems);
        mTicketList.setAdapter(mAdapter);
        String URL_FEED = CommonFunctions.mUrl + "getTickets";
        if (CommonFunctions.isInternetOn(TicketHistoryActivity.this)) {
            webServicePart(URL_FEED);
            Log.e("url of click", URL_FEED);
        } else {
           // CommonFunction.displayToast(TicketHistoryActivity.this, "No internet...");
            showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
        }

        findViewById(R.id.backFromTicket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;
    }

    void webServicePart(String URL_FEED) {
       // CommonFunction.viewProgressVisible(TicketHistoryActivity.this, "Please wait...");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .build();
        new ExecuteServices().executePost(URL_FEED, new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      try {
                          parseJSONData(new JSONObject(response));
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


    void parseJSONData(JSONObject response)
    {
        try {
            String status = response.getString("status");
            if(status.equalsIgnoreCase("1"))
            {
                JSONArray feedArray = response.getJSONArray("data");
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);
                    TicketModel items = new TicketModel();
                    items.set_Date(feedObj.getString("date"));
                    items.set_ProductName(feedObj.getString("product_name"));
                    items.set_ProductId(feedObj.getString("order_id"));
                    items.set_TicketId(feedObj.getString("ticket_no"));
                    items.set_Status(feedObj.getString("status"));
                    feedItems.add(items);
                }
            }
            else
            {
                showMessageDialog("No data found.","#f2dede","#a94442");
            }
        }
        catch (Exception e)
        {

        }
        mAdapter.notifyDataSetChanged();
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
        mMessageView.setText(message);
        mMessageView.setTextColor(Color.parseColor(textColor));
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
}



class TicketListAdapter extends BaseAdapter
{
    private Activity activity;
    private LayoutInflater inflater;
    private List<TicketModel> feedItems;
    public TicketListAdapter(Activity act,ArrayList<TicketModel> feedItems)
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
            viewHolder.mLinLayout=(LinearLayout)convertView.findViewById(R.id.linRow);
            viewHolder.mProductName=(TextView)convertView.findViewById(R.id.productName);
            viewHolder.mDate=(TextView)convertView.findViewById(R.id.date);
            viewHolder.mProductId=(TextView)convertView.findViewById(R.id.productId1);
            viewHolder.mTackerId = (TextView) convertView.findViewById(R.id.ticketId);
            viewHolder.mStatus = (TextView) convertView.findViewById(R.id.status);
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
        TicketModel item = feedItems.get(position);
        viewHolder.mProductName.setText(""+item.get_ProductName());
        viewHolder.mProductId.setText(""+item.get_ProductId());
        String a[]=item.get_Date().split(" ");
        viewHolder.mDate.setText(""+item.get_Date());
        viewHolder.mTackerId.setText(""+item.get_TicketId());
        viewHolder.mStatus.setText(""+item.get_Status());
        if(item.get_Status().equalsIgnoreCase("pending"))
        {
            viewHolder.mStatus.setTextColor(Color.parseColor("#ef7625"));
        }
        else
        {
            viewHolder.mStatus.setTextColor(Color.parseColor("#000000"));
        }
        return convertView;
    }

    static class ViewHolderItems
    {

        protected TextView mDate,mProductId,mProductName,mStatus,mTackerId;
        LinearLayout mLinLayout;
    }
}