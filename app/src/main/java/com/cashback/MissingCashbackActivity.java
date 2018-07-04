package com.cashback;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by dev on 21/3/17.
 */

public class MissingCashbackActivity extends Activity
{

    View v;
    String URL_FEED,userId="";
    Spinner mRetailerSpinner;
    ArrayAdapter adapter;
    ArrayList<String> mRetailerName,mRetailerId;
    TextView mSubmitTicket,mTicketHistory;
    static  TextView mDateOfTranscation;
    Calendar myCalendar = Calendar.getInstance();
    private DatePicker datePicker;
    private int year, month, day;
    EditText mProductName,mOrderAmount,mOrderId;
    TextView mMessageView;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing_cashback);
        prefs= PreferenceManager.getDefaultSharedPreferences(MissingCashbackActivity.this);
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        mRetailerSpinner=(Spinner)findViewById(R.id.retailerList);
        mDateOfTranscation=(TextView)findViewById(R.id.dateOfTranscation);
        //  mCashbackEmail=(EditText)v.findViewById(R.id.cashbackEmail);
        mSubmitTicket=(TextView)findViewById(R.id.submitTicket);
        mTicketHistory=(TextView)findViewById(R.id.ticketHistory);
        mProductName=(EditText)findViewById(R.id.productName);
        mOrderId=(EditText)findViewById(R.id.orderId);
        mOrderAmount=(EditText)findViewById(R.id.orderAmount);
        mMessageView=(TextView)findViewById(R.id.message);
        mDateOfTranscation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;

        mRetailerName=new ArrayList();
        mRetailerId=new ArrayList();
        mRetailerName.add("Name of online stores:");
        mRetailerName.add("No stores found");
        adapter = new ArrayAdapter<String>(MissingCashbackActivity.this, android.R.layout.simple_list_item_1, mRetailerName);
        mRetailerSpinner.setAdapter(adapter);
        mRetailerSpinner.setSelection(0);

        URL_FEED = CommonFunctions.mUrl + "getClicksRetailers";
        if (CommonFunctions.isInternetOn(this)) {
            networkCallForGetRetailer(URL_FEED);
            Log.e("url of click", URL_FEED);
        } else {
            //CommonFunction.displayToast(getActivity(), "No internet...");
            showMessageDialog("No internet connection...","#fcf8e3","#8a6d3b");
        }

        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);
        day = myCalendar.get(Calendar.DAY_OF_MONTH);
        mDateOfTranscation.setText(year+"/"+(month+1)+"/"+day);

        mSubmitTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = mDateOfTranscation.getText().toString().trim();
                String pName = mProductName.getText().toString().trim();
                String orderId = mOrderId.getText().toString().trim();
                String orderAmt = mOrderAmount.getText().toString().trim();
                int id = mRetailerSpinner.getSelectedItemPosition();
                String retailerId = mRetailerId.get(id);
                if(retailerId.equalsIgnoreCase("-1"))
                {
                    showMessageDialog("Please select the store.","#f2dede","#a94442");
                    return;
                }

                if (pName.length() > 0) {
                    if (orderId.length() > 0) {
                        if (orderAmt.length() > 0) {
                            URL_FEED = CommonFunctions.mUrl + "saveMissingCashback";
                            System.out.println(URL_FEED+" "+userId);
                            RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("user_id", userId)
                                    .addFormDataPart("retailer_id", retailerId)
                                    .addFormDataPart("product_name", pName)
                                    .addFormDataPart("date", date)
                                    .addFormDataPart("order", orderId)
                                    .addFormDataPart("amount", orderAmt)
                                    .build();
                            saveMissingCashback(URL_FEED,requestBody );
                        } else {
                            showMessageDialog("Please enter Order Amount.","#f2dede","#a94442");

                        }
                    } else {
                        showMessageDialog("Please enter Order Id.","#f2dede","#a94442");
                    }
                } else {
                    showMessageDialog("Please enter Product Name.","#f2dede","#a94442");

                }
            }
        });

        mTicketHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MissingCashbackActivity.this,TicketHistoryActivity.class));
            }
        });

        ((LinearLayout)findViewById(R.id.backFromMissingCashback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            Date today = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add( Calendar.MONTH, -1 ) ;// Subtract 1 months
            long minDate = c.getTime().getTime(); // Twice!
            DatePickerDialog picker=new DatePickerDialog(activity, this, yy, mm, dd);
            if(Build.VERSION.SDK_INT>19) {
                picker.getDatePicker().setMaxDate(calendar.getTime().getTime());
                picker.getDatePicker().setMinDate(minDate);
            }
            return picker;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            mDateOfTranscation.setText(year+"/"+month+"/"+day);
        }

    }


    void networkCallForGetRetailer(String url)
    {
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        findViewById(R.id.scrollView).setVisibility(View.GONE);
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
                            findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                            parseGetRetailer(new JSONObject(response));
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


    void parseGetRetailer(JSONObject response) {
        try {
            String status = response.getString("status");
            if (status.equalsIgnoreCase("1")) {
                mRetailerName = new ArrayList();
                mRetailerId = new ArrayList();
                mRetailerName.clear();
                mRetailerId.clear();
                mRetailerName.add("Name of online stores:");
                mRetailerId.add("-1");
                JSONArray arr = response.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    mRetailerName.add(obj.getString("retailer"));
                    mRetailerId.add(obj.getString("retailer_id"));
                }
                adapter = new ArrayAdapter<String>(MissingCashbackActivity.this, android.R.layout.simple_list_item_1, mRetailerName);
                mRetailerSpinner.setAdapter(adapter);
                mRetailerSpinner.setSelection(0);
            } else {

                mRetailerName = new ArrayList();
                mRetailerId = new ArrayList();
                mRetailerName.add("Name of online stores:");
                mRetailerName.add("No stores found");
                mRetailerId.add("-1");
                mRetailerId.add("-1");
                adapter = new ArrayAdapter<String>(MissingCashbackActivity.this, android.R.layout.simple_list_item_1, mRetailerName);
                mRetailerSpinner.setAdapter(adapter);
                mRetailerSpinner.setSelection(0);
            }
        } catch (Exception e) {

        }
    }

    void saveMissingCashback(String url,RequestBody requestBody)
    {
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        findViewById(R.id.scrollView).setVisibility(View.GONE);
        new ExecuteServices().executePost(url, new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            findViewById(R.id.progressbar).setVisibility(View.GONE);
                            findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                            parseSaveMissingCashbackTicket(new JSONObject(response));
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

    void parseSaveMissingCashbackTicket(JSONObject response)
    {
        try {
            String status = response.getString("status");
            if(status.equalsIgnoreCase("1"))
            {
                String ticket=response.getString("ticketNo");
                showMessageDialog("Thank you, your ticket submitted successfully.","#dff0d8","#3c763d");
                mProductName.setText("");
                mDateOfTranscation.setText("");
                mOrderAmount.setText("");
                mOrderId.setText("");
                mRetailerSpinner.setSelection(0);
              //  CommonFunctions.showAlert(MissingCashbackActivity.this,"Your Ticket ID is "+ticket+"\n\nThank you","0");
            }
            else
            {
                String ticket=response.getString("ticketNo");
                String message=response.getString("message");
                showMessageDialog(""+message,"#f2dede","#a94442");
            //    CommonFunctions.showAlert(MissingCashbackActivity.this,message+"\nYour Ticket ID is "+ticket+"\n\nThank you","0");

            }
        }
        catch (Exception e)
        {
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
        },3000);
    }
}
