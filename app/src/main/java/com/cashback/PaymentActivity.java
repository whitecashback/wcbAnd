package com.cashback;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cashback.data.PaymentModel;
import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jack on 23-03-2017.
 */
public class PaymentActivity extends Activity {
    TextView mAvailBalance, mPaymentMethod, mPaymentDetail,mEditAccount,mEditPaymentdetail,mMessageView;
    EditText mWithDrawAmount, sendText;
    Spinner mSpinner;
    ArrayAdapter adapter;
    ArrayList<String> mSpinItem;
    Button mWithdraw;
    String URL_FEED,mPMethod;
    TextView mBack;
    int paymentFlag=0;
    public static String sPaymentMethod="0",userId="",sPSelected="0";
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);
        prefs= PreferenceManager.getDefaultSharedPreferences(PaymentActivity.this);
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        mSpinItem = new ArrayList<>();
        for (int i = 0; i < MyCashBackScreen.sPaymentMethod.size(); i++) {
            mSpinItem.add(MyCashBackScreen.sPaymentMethod.get(i).getPaymentMethodTitle());
        }
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;
        //mEditAccount=(TextView)findViewById(R.id.editAccount);
        mMessageView=(TextView)findViewById(R.id.message);
        mAvailBalance = (TextView) findViewById(R.id.availBalance);
        mAvailBalance.setText(MyCashBackScreen.sAvailBalance);
        mPaymentDetail = (TextView) findViewById(R.id.paymentDetails);
        sendText = (EditText) findViewById(R.id.sendText);
        mWithDrawAmount = (EditText) findViewById(R.id.amount);
        mEditPaymentdetail=(TextView)findViewById(R.id.editPayment);
        mSpinner = (Spinner) findViewById(R.id.spinnerItem);
        adapter = new ArrayAdapter<String>(PaymentActivity.this, android.R.layout.simple_list_item_1, mSpinItem);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPMethod = MyCashBackScreen.sPaymentMethod.get(position).getPaymentMethosId();
                Log.e("payment detail", "" + mPMethod);
                if (mPMethod.equalsIgnoreCase("3")) {
                    for (int i = 0; i < CommonFunctions.sPaymentDetailList.size(); i++) {
                        if (CommonFunctions.sPaymentDetailList.get(i).get_PaymentId().equalsIgnoreCase("3")) {
                            mPaymentDetail.setText("Account Holder's Name : " + CommonFunctions.sPaymentDetailList.get(i).get_HolderName() + "\n" +
                                    "Bank Name : " + CommonFunctions.sPaymentDetailList.get(i).get_BankName() + "\n" +
                                    "Account Number : " + CommonFunctions.sPaymentDetailList.get(i).get_AccountNumber() + "\n" +
                                    "Account Type : " + CommonFunctions.sPaymentDetailList.get(i).get_AccountType() + "\n" +
                                    "IFSC : " + CommonFunctions.sPaymentDetailList.get(i).get_IFSCCODE() + "\n" +
                                    "City : " + CommonFunctions.sPaymentDetailList.get(i).get_City());
                            paymentFlag = 1;
                            Log.e("payment detail", "" + mPMethod);
                        } else {
                            mPaymentDetail.setText("");
                        }
                        if (paymentFlag == 1) {
                            paymentFlag = 0;
                            break;
                        }
                    }
                } else if (mPMethod.equalsIgnoreCase("1")) {
                    for (int i = 0; i < CommonFunctions.sPaymentDetailList.size(); i++) {
                        if (CommonFunctions.sPaymentDetailList.get(i).get_PaymentId().equalsIgnoreCase("1")) {
                            mPaymentDetail.setText("Paypal detail : " + CommonFunctions.sPaymentDetailList.get(i).get_PaypalAccount());
                            Log.e("payment detail", "" + mPMethod);
                            paymentFlag = 1;
                        } else {
                            mPaymentDetail.setText("");
                        }
                        if (paymentFlag == 1) {
                            paymentFlag = 0;
                            break;
                        }
                    }
                }
                // mPaymentDetail.setText(MyCashBackScreen.sPaymentMethod.get(position).getDetails().replace("<br />", ""));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mWithdraw = (Button) findViewById(R.id.withdraw);
        mWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount=mWithDrawAmount.getText().toString().trim();
                String info=mPaymentDetail.getText().toString().trim();
                if(amount.length()>0&&info.length()>0)
                {
                    if((Double.parseDouble(amount)>= Double.parseDouble(MyCashBackScreen.minBalance)))
                    {
                        if((Double.parseDouble(amount))<= Double.parseDouble(MyCashBackScreen.sAvailBalance.substring(3)))
                        {

                            URL_FEED=CommonFunctions.mUrl+"withdrawCash";
                            RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("user_id", userId)
                                    .addFormDataPart("payment_details", URLEncoder.encode(info))
                                    .addFormDataPart("payment_method", URLEncoder.encode(mPMethod))
                                    .addFormDataPart("amount", URLEncoder.encode(amount))
                                    .build();
                            findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                            findViewById(R.id.scrollView).setVisibility(View.GONE);
                            new ExecuteServices().executePost(URL_FEED, new ExecuteServices.OnServiceExecute() {
                                @Override
                                public void onServiceExecutedResponse(final String response) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                findViewById(R.id.progressbar).setVisibility(View.GONE);
                                                findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
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
                                            findViewById(R.id.progressbar).setVisibility(View.GONE);
                                            findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                                        }
                                    });


                                }
                            },requestBody);

                        }
                        else
                        {
                            showMessageDialog("Amount should be less than Available Balance.","#f2dede","#a94442");
                        }
                    }
                    else
                    {
                     showMessageDialog("Amount should be greater than Min Balance.","#f2dede","#a94442");
                    }
                }
                else
                {
                    if(info.trim().length()==0) {
                        showMessageDialog("Please add bank details.", "#f2dede", "#a94442");
                    }else if(amount.length()==0)
                    {
                        showMessageDialog("Please enter amount to withdraw.", "#f2dede", "#a94442");

                    }
                    else
                    {
                        showMessageDialog("Please fill all details.", "#f2dede", "#a94442");
                    }

                }
            }
        });

        ((LinearLayout)findViewById(R.id.backFromPayment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mEditPaymentdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPSelected=mPMethod;
                startActivity(new Intent(PaymentActivity.this, EditAccountDetail.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(CommonFunctions.isInternetOn(PaymentActivity.this)) {
            getPaymentDetail();
        }
        else
        {
            showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
        }
    }

    public void getPaymentDetail()
    {
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        findViewById(R.id.scrollView).setVisibility(View.GONE);
        String url=CommonFunctions.mUrl+"paymentDetails";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .build();
        new ExecuteServices().executePost(url, new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                System.out.println(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            findViewById(R.id.progressbar).setVisibility(View.GONE);
                            findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                            parsePaymentDetil(new JSONObject(response));
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
                     findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                     showMessageDialog("Something went wrong.","#f2dede","#a94442");
                 }
             });
            }
        },requestBody);
    }


    private void parsePaymentDetil(JSONObject response)
    {
        try {
            String status = response.getString("status");
            if (status.equalsIgnoreCase("1")) {
                JSONArray arr=response.getJSONArray("message");
                if(arr.length()==0)
                {
                    mEditPaymentdetail.setText("Add/Edit Account details");
                }
                else
                {
                    mEditPaymentdetail.setText("Edit payment detail");
                }
                CommonFunctions.sPaymentDetailList.clear();
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject obj=arr.getJSONObject(i);
                    PaymentModel data=new PaymentModel();
                    data.set_PaymentMethod(obj.getString("payment_method"));
                    data.set_PaymentId(obj.getString("payment_id"));
                    data.set_PaypalAccount(obj.getString("paypal_account"));
                    data.set_HolderName(obj.getString("holder_name"));
                    data.set_BankName(obj.getString("bank_name"));
                    data.set_City(obj.getString("city"));
                    data.set_AccountNumber(obj.getString("account_number"));
                    data.set_IFSCCODE(obj.getString("IFSC_code"));
                    data.set_AccountType(obj.getString("account_type"));
                    CommonFunctions.sPaymentDetailList.add(data);
                    if(!obj.getString("paypal_account").equalsIgnoreCase("")) {
                        mPaymentDetail.setText("Paypal detail : " + obj.getString("paypal_account"));
                    }
                }
                mSpinner.setSelection(0);
                adapter.notifyDataSetChanged();
            }
            else
            {
                String message=response.getString("message");
               if(message.equalsIgnoreCase("No payment found")){
                   showMessageDialog("Please add account detail.","#f2dede","#a94442");

               }
               else {
                   showMessageDialog("Something went wrong.","#f2dede","#a94442");
               }
            }
        } catch (Exception e) {

        }
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void parseJsonFeed(JSONObject response) {
        try {
            String status = response.getString("status");
            String message = response.getString("message");
            if (status.equalsIgnoreCase("1")) {
                String a=mWithDrawAmount.getText().toString().trim();
                Double requestAmt=Double.parseDouble(a);
                Double leftAmount=Double.parseDouble(MyCashBackScreen.sAvailBalance.substring(3))-requestAmt;
               MyCashBackScreen.sAvailBalance="Rs."+leftAmount;
                mWithDrawAmount.setText("");
                mAvailBalance.setText(MyCashBackScreen.sAvailBalance);
                showMessageDialog("Your request has been processed.","#dff0d8","#3c763d");
                //this.finish();
            }
            else
            {
                showMessageDialog("Something went wrong.","#f2dede","#a94442");
            }
        } catch (Exception e) {

        }
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
}