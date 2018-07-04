package com.cashback;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cashback.data.PaymentMethosItems;
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
 * Created by jack on 23-03-2017.
 */
public class MyCashBackScreen extends Activity implements View.OnClickListener
{
    LinearLayout mPandingcashBackLayout,mWhiteCashBackLayout;
    TextView mAvailCash,mPadingCashBack,mDeclinedCash,mCashOutReq,mCashOutProcessed,mLifeTimeCash
            ,mStoreCashBack,mPayment,mPaymentTxt,mMessageView;
    private Callbacks mCallbacks;
    String URL_FEED,sMyEarning="";
    ProgressBar p1,p2,p3,p4,p5,p6;
    public static ArrayList<PaymentMethosItems> sPaymentMethod;
    public static String minBalance,sAvailBalance;
    public Handler mHandle;
    int rowCount=0;
    Cursor cs=null;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="";
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;

    public interface Callbacks {
        public void onItemSelected(String id);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashback_activity);
        prefs= PreferenceManager.getDefaultSharedPreferences(MyCashBackScreen.this);
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        mPadingCashBack=(TextView)findViewById(R.id.pandingCash);
        mAvailCash=(TextView)findViewById(R.id.availableCash);
        mDeclinedCash=(TextView)findViewById(R.id.declinedCash);
        mCashOutReq=(TextView)findViewById(R.id.cashOutReq);
        mCashOutProcessed=(TextView)findViewById(R.id.cashOutProcessed);
        mLifeTimeCash=(TextView)findViewById(R.id.lifeTimeCash);
        mPaymentTxt=(TextView)findViewById(R.id.mPaymentTxt);
        mStoreCashBack=(TextView)findViewById(R.id.mStoreCashBack);
        mPayment=(TextView)findViewById(R.id.mPayment);
        mMessageView=(TextView)findViewById(R.id.message);

        mPadingCashBack.setText("");
        mAvailCash.setText("");
        mDeclinedCash.setText("");
        mCashOutReq.setText("");
        mCashOutProcessed.setText("");
        mLifeTimeCash.setText("");
        p1=(ProgressBar)findViewById(R.id.p1);
        p2=(ProgressBar)findViewById(R.id.p2);
        p3=(ProgressBar)findViewById(R.id.p3);
        p4=(ProgressBar)findViewById(R.id.p4);
        p5=(ProgressBar)findViewById(R.id.p5);
        p6=(ProgressBar)findViewById(R.id.p6);
        p6.setVisibility(View.VISIBLE);
        p1.setVisibility(View.VISIBLE);
        p2.setVisibility(View.VISIBLE);
        p3.setVisibility(View.VISIBLE);
        p4.setVisibility(View.VISIBLE);
        p5.setVisibility(View.VISIBLE);
        mPadingCashBack.setVisibility(View.GONE);
        mAvailCash.setVisibility(View.GONE);
        mDeclinedCash.setVisibility(View.GONE);
        mCashOutReq.setVisibility(View.GONE);
        mCashOutProcessed.setVisibility(View.GONE);
        mLifeTimeCash.setVisibility(View.GONE);
        mStoreCashBack.setOnClickListener(this);
        mPayment.setOnClickListener(this);
        mHandle=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (CommonFunctions.isInternetOn(MyCashBackScreen.this)) {
                    webService();
                } else {
                    showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
                }
                return false;
            }
        });


        ((LinearLayout)findViewById(R.id.backFromEarning)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView mReferralTxtView=(TextView)findViewById(R.id.mReferral);
        mReferralTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    startActivity(new Intent(getActivity(),TicketHistoryFragment.class));
            }
        });

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;
    }

    void webService()
    {
        String url=CommonFunctions.mUrl+"mywallet";
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
                        showMessageDialog("Something went wrong.","#f2dede","#a94442");
                    }
                });

            }
        },requestBody);
    }
    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            String status = response.getString("status");
            String message = response.getString("message");
            if (status.equalsIgnoreCase("1")) {
                sAvailBalance=response.getString("UserBalance");
                String UserBalance = response.getString("UserBalance");
                String PendingBalance = response.getString("PendingBalance");
                String DeclinedBalance = response.getString("DeclinedBalance");
                String CashOutRequested = response.getString("CashOutRequested");
                String CashOutProcessed = response.getString("CashOutProcessed");
                String LifetimeCashback = response.getString("LifetimeCashback");
                minBalance=response.getString("Min_Amount");
                mPaymentTxt.setText("The minimum amount for request payout is Rs "+minBalance);
                sPaymentMethod=new ArrayList<>();
                JSONArray feedArray = response.getJSONArray("payment_method");
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);
                    PaymentMethosItems items=new PaymentMethosItems();
                    items.setPaymentMethosId(feedObj.getString("pmethod_id"));
                    items.setPaymentMethodTitle((feedObj.getString("pmethod_title")));
                    items.setDetails((feedObj.getString("pmethod_details")));
                    sPaymentMethod.add(items);
                }
                //Hiding the progress bar
                p1.setVisibility(View.GONE);
                p2.setVisibility(View.GONE);
                p3.setVisibility(View.GONE);
                p4.setVisibility(View.GONE);
                p5.setVisibility(View.GONE);
                p6.setVisibility(View.GONE);
                //set visibility of textview
                mPadingCashBack.setVisibility(View.VISIBLE);
                mAvailCash.setVisibility(View.VISIBLE);
                mDeclinedCash.setVisibility(View.VISIBLE);
                mCashOutReq.setVisibility(View.VISIBLE);
                mCashOutProcessed.setVisibility(View.VISIBLE);
                mLifeTimeCash.setVisibility(View.VISIBLE);
                mPadingCashBack.setText("" + PendingBalance);
                mAvailCash.setText(""+UserBalance);
                mDeclinedCash.setText(""+DeclinedBalance);
                mCashOutReq.setText(""+CashOutRequested);
                mCashOutProcessed.setText(""+CashOutProcessed);
                mLifeTimeCash.setText(""+LifetimeCashback);
                sMyEarning=PendingBalance+":"+UserBalance+":"+DeclinedBalance+":"+CashOutRequested+":"+CashOutProcessed+":"+LifetimeCashback+":"+minBalance;
                DatabaseHandler.getInstance(MyCashBackScreen.this).insertEarning(sMyEarning);
            }
            else {
                showMessageDialog("Something went wrong.","#f2dede","#a94442");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (CommonFunctions.isInternetOn(MyCashBackScreen.this)) {
            //Call to a web service of My Wallet
            DatabaseHandler.getInstance(MyCashBackScreen.this).deletePaymentTable();
            webService();
        } else {
            showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");

            cs=DatabaseHandler.getInstance(MyCashBackScreen.this).getSQLiteData("earning");
            cs.moveToFirst();
            if(cs!=null&& !cs.isClosed())
            {
                rowCount=cs.getCount();
                Log.d("row count", "" + rowCount);
            }
            if(rowCount!=0) {
                //Hiding the progress bar
                p1.setVisibility(View.GONE);
                p2.setVisibility(View.GONE);
                p3.setVisibility(View.GONE);
                p4.setVisibility(View.GONE);
                p5.setVisibility(View.GONE);
                p6.setVisibility(View.GONE);
                //hiding of textview
                mPadingCashBack.setVisibility(View.VISIBLE);
                mAvailCash.setVisibility(View.VISIBLE);
                mDeclinedCash.setVisibility(View.VISIBLE);
                mCashOutReq.setVisibility(View.VISIBLE);
                mCashOutProcessed.setVisibility(View.VISIBLE);
                mLifeTimeCash.setVisibility(View.VISIBLE);
                mPadingCashBack.setText("" + cs.getString(0));
                mAvailCash.setText("" + cs.getString(1));
                mDeclinedCash.setText("" + cs.getString(2));
                mCashOutReq.setText("" + cs.getString(3));
                mCashOutProcessed.setText("" + cs.getString(4));
                mLifeTimeCash.setText("" + cs.getString(5));
                minBalance = cs.getString(6);
                mPaymentTxt.setText("The minimum amount for request payout is Rs. " + minBalance);
                sAvailBalance=cs.getString(1);
                sPaymentMethod=new ArrayList<>();
            }
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.mStoreCashBack:
                intent=new Intent(MyCashBackScreen.this,StatementActivity.class);
                startActivity(intent);
                break;
            case R.id.mPayment:
                Log.d("test value ", "" + Double.parseDouble(sAvailBalance.substring(3)) + " " + Double.parseDouble(minBalance));
              //  minBalance="5";
                if(Double.parseDouble(sAvailBalance.substring(3))>= Double.parseDouble(minBalance)) {
                    intent=new Intent(MyCashBackScreen.this,PaymentActivity.class);//Open the payment activity when
                    //user cross the minimum balance limit
                    startActivity(intent);
                }
                else
                {
                    showMessageDialog("The minimum amount for request payout is Rs. "+minBalance,"#f2dede","#a94442");
                }
                break;
        }
    }

//    public void showDialog(String message) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage(message);
//        alertDialog.setIcon(R.drawable.internet);
//        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
//                getActivity().startActivity(intent);
//            }
//        });
//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                getActivity().finish();
//            }
//        });
//        alertDialog.show();
//    }

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
        mMessageView.setTextColor(Color.parseColor(textColor));
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
}
