package com.cashback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jack on 24-03-2017.
 */
public class EditAccountDetail extends Activity
{
    Spinner mPaymentType,mAccountType;
    TextView mBackImage,mMessageView;
    EditText mAccountNameWrapper,mBankNameWrapper,mAccountNumberWrapper,mIFSCWrapper,mCityWrapper;
    EditText mPayPalDetail;
    TextView mLabel,mAddAccount,titleName;
    ArrayAdapter adapter,accountTypeAdapter;
    String pmethod,accType;
    ArrayList<String> paymentTYpe,accountType;
    int pType=0;
    LinearLayout mWireLayout;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="",wireMessage="0",paypalMessage="0";
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_bank_account_2);
        prefs= PreferenceManager.getDefaultSharedPreferences(EditAccountDetail.this);
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        mPaymentType=(Spinner)findViewById(R.id.spinnerItem);
        mAccountType=(Spinner)findViewById(R.id.accountType);
        mAccountNameWrapper=(EditText)findViewById(R.id.accountNameWrapper);
        mBankNameWrapper=(EditText)findViewById(R.id.bankNameWrapper);
        mAccountNumberWrapper=(EditText)findViewById(R.id.accountNumberWrapper);
        mIFSCWrapper=(EditText)findViewById(R.id.ifscWrapper);
        mCityWrapper=(EditText)findViewById(R.id.cityWrapper);
        mPayPalDetail=(EditText)findViewById(R.id.paypalDetail);
        mLabel=(TextView)findViewById(R.id.paypalLabel);
        mAddAccount=(TextView)findViewById(R.id.addAccount);
        mWireLayout=(LinearLayout)findViewById(R.id.wireDeatailLayout);
        titleName=(TextView) findViewById(R.id.back);
        mMessageView=(TextView)findViewById(R.id.message);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;

        ((LinearLayout)findViewById(R.id.backFromAddDetail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Log.e("selected method",PaymentActivity.sPSelected);

        mWireLayout.setVisibility(View.GONE);
        mPayPalDetail.setVisibility(View.GONE);
        paymentTYpe=new ArrayList<>();
        paymentTYpe.add("Select your payment method");
        paymentTYpe.add("Wire Transfer");
        paymentTYpe.add("Paypal");

        accountType=new ArrayList<>();
                accountType.add("Saving");
        accountType.add("Current");
        adapter = new ArrayAdapter<String>(EditAccountDetail.this, android.R.layout.simple_list_item_1, paymentTYpe);
        mPaymentType.setAdapter(adapter);
        int a=Integer.parseInt(PaymentActivity.sPSelected);
        if(a==3) {
            mPaymentType.setSelection(1);
        }
        else if(a==1)
        {
            mPaymentType.setSelection(2);
        }
        else
        {
            mPaymentType.setSelection(1);

        }


        accountTypeAdapter = new ArrayAdapter<String>(EditAccountDetail.this, android.R.layout.simple_list_item_1, accountType);
        mAccountType.setAdapter(accountTypeAdapter);
        mAccountType.setSelection(0);


        mPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pmethod = paymentTYpe.get(position);

                if (pmethod.equalsIgnoreCase("wire transfer")) {
                    mWireLayout.setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.tag_title)).setText("Enter Bank Account Details:");
                    titleName.setText("Bank Details");
                    mPayPalDetail.setVisibility(View.GONE);
                    mLabel.setVisibility(View.GONE);
                    mAddAccount.setVisibility(View.VISIBLE);
                    //Log.e("pMethod", pmethod);
                } else if (pmethod.equalsIgnoreCase("paypal")) {
                    mWireLayout.setVisibility(View.GONE);
                    mPayPalDetail.setVisibility(View.VISIBLE);
                    titleName.setText("Paypal ID");
                    ((TextView)findViewById(R.id.tag_title)).setText("Enter Paypal Account Details:");
                    mLabel.setVisibility(View.VISIBLE);
                    mAddAccount.setVisibility(View.VISIBLE);
                    //Log.e("pMethod", pmethod);
                } else {
                    // Log.e("pMethod",pmethod);
                    ((TextView)findViewById(R.id.tag_title)).setText("Enter Bank Account Details:");
                    mAddAccount.setVisibility(View.GONE);
                    mLabel.setVisibility(View.GONE);
                    mWireLayout.setVisibility(View.GONE);
                    mPayPalDetail.setVisibility(View.GONE);
                }
                Log.e("pMethod", pmethod);

                for(int i = 0; i< CommonFunctions.sPaymentDetailList.size(); i++)
                {
                    String a[]=pmethod.split(" ");
                    String b[]=CommonFunctions.sPaymentDetailList.get(i).get_PaymentMethod().split("_");
                    if(b[0].equalsIgnoreCase(a[0]))
                    {
                        mAddAccount.setText("Update");
                        break;
                    }
                    else
                    {
                        mAddAccount.setText("Submit");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                accType=accountType.get(position);
                System.out.println(accType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!CommonFunctions.isInternetOn(EditAccountDetail.this))
                {
                    showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
                    //TODO--Here also update from database
                    return;
                }

                accountUpdateDialog();

            }
        });

        titleName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String URL_FEED=CommonFunctions.mUrl+"paymentDetails";
        Log.e("URL_FEED", URL_FEED);
        if(CommonFunctions.isInternetOn(EditAccountDetail.this)) {
            getPaymentDetail(URL_FEED);
        }
        else
        {
            showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");

            //TODO--Here also update from database
        }
    }

    /**
     *
     */
    void submitDetail()
    {
        String name=mAccountNameWrapper.getText().toString().trim();
        String bname=mBankNameWrapper.getText().toString().trim();
        String number=mAccountNumberWrapper.getText().toString().trim();
        String ifsc=mIFSCWrapper.getText().toString().trim();
        String city=mCityWrapper.getText().toString().trim();



        if(name.length()==0)
        {
            mAccountNameWrapper.setError("Please, enter account holder name");
        }
        else if(bname.length()==0)
        {
            mBankNameWrapper.setError("Please, enter bank name");
        }
        else if(number.length()==0)
        {
            mAccountNumberWrapper.setError("Please, enter account number");
        }
        else if(ifsc.length()==0)
        {
            mIFSCWrapper.setError("Please, enter IFSC code");
        }
        else if(city.length()==0)
        {
            mCityWrapper.setError("Please, enter your city");
        }
        else
        {
            String url=CommonFunctions.mUrl+"savePaymentDetail";
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_id", userId)
                    .addFormDataPart("payment_id", "3")
                    .addFormDataPart("account_name", URLEncoder.encode(name))
                    .addFormDataPart("bank_name", URLEncoder.encode(bname))
                    .addFormDataPart("account_no", number)
                    .addFormDataPart("city", URLEncoder.encode(city))
                    .addFormDataPart("ifsc_code", ifsc)
                    .addFormDataPart("account_type",accType)
                    .build();
            webservice(url,requestBody);
        }
    }

    void paypalDetail()
    {
        if(mPayPalDetail.getText().toString().trim().length()>0)
        {
            String url=CommonFunctions.mUrl+"savePaymentDetail";
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_id", userId)
                    .addFormDataPart("payment_id", "1")
                    .addFormDataPart("paypal_detail", mPayPalDetail.getText().toString())
                    .build();
            webservice(url,requestBody);
        }
        else
        {
            //  CommonFunction.displayToast(EditAccountDetail.this,"Please enter paypal details.");
            showMessageDialog("Please enter paypal details.","#fcf8e3","#8a6d3b");
        }
    }

    /**
     *
     * @param url
     * @param requestBody
     */
    void webservice(String url,RequestBody requestBody)
    {
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        Log.e("Payment edit url", url);

        new ExecuteServices().executePost(url, new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            findViewById(R.id.progressbar).setVisibility(View.GONE);
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
                    }
                });

            }
        },requestBody);

    }


    void parsePaymentDetil(JSONObject response)
    {
        try {
            String status =response.getString("status");
            if(status.equalsIgnoreCase("1"))
            {
                //profileUpdateDilaog(EditAccountDetail.this);
                if(wireMessage.equalsIgnoreCase("1")||paypalMessage.equalsIgnoreCase("1")) {
                    showMessageDialog("Account information updated.", "#dff0d8", "#3c763d");
                }
                else
                {
                    wireMessage="1";
                    paypalMessage="1";
                    showMessageDialog("Account information added.", "#dff0d8", "#3c763d");
                }
            }
            else
            {
                showMessageDialog("Unable to update the account detail.","#f2dede","#a94442");
            }
        }
        catch (Exception e)
        {

        }
    }


    public void accountUpdateDialog()
    {
        final Dialog dialog = new Dialog(EditAccountDetail.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.account_update_dialog);
        dialog.setCancelable(false);
        dialog.show();
        TextView acc=(TextView)dialog.findViewById(R.id.account);
        acc.setText("Are you sure you want to update account inforamtion?");
        TextView mOkBtn=(TextView)dialog.findViewById(R.id.update);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pmethod.equalsIgnoreCase("Wire Transfer")) {
                    pType = 3;
                    submitDetail();
                } else if (pmethod.equalsIgnoreCase("paypal")) {
                    pType = 1;
                    if(Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(),mPayPalDetail.getText().toString().trim())) {
                        paypalDetail();
                    }
                    else
                    {

                        showMessageDialog("Enter correct Paypal Id","#f2dede","#a94442");
                    }
                } else {
                    showMessageDialog("Select your payment method.","#f2dede","#a94442");

                }

                dialog.cancel();
            }
        });

        TextView mNoBtn=(TextView)dialog.findViewById(R.id.noThanks);
        mNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonFunctions.sPaymentDetailList.size()>0) {
                    for (int i = 0; i < CommonFunctions.sPaymentDetailList.size(); i++) {
                        if (CommonFunctions.sPaymentDetailList.get(i).get_PaymentId().equalsIgnoreCase("3")) {
                            //  mPaymentType.setSelection(1);
                            mAccountNameWrapper.setText(CommonFunctions.sPaymentDetailList.get(i).get_HolderName());
                            mBankNameWrapper.setText(CommonFunctions.sPaymentDetailList.get(i).get_BankName());
                            mAccountNumberWrapper.setText(CommonFunctions.sPaymentDetailList.get(i).get_AccountNumber());
                            mIFSCWrapper.setText(CommonFunctions.sPaymentDetailList.get(i).get_IFSCCODE());
                            mCityWrapper.setText(CommonFunctions.sPaymentDetailList.get(i).get_City());
                        } else if (CommonFunctions.sPaymentDetailList.get(i).get_PaymentId().equalsIgnoreCase("1")) {
                            // mPaymentType.setSelection(2);
                            mPayPalDetail.setText(CommonFunctions.sPaymentDetailList.get(i).get_PaypalAccount());
                        }
                    }
                }
                dialog.cancel();
            }
        });
    }


    public static void profileUpdateDilaog(final Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.profile_update_dialog);
        dialog.setCancelable(false);
        dialog.show();
        TextView acc=(TextView)dialog.findViewById(R.id.account);
        acc.setText("Account information added successfully.");
        TextView mOkBtn=(TextView)dialog.findViewById(R.id.update);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    /**
     *
     * @param url
     */
    public void getPaymentDetail(String url)
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
                            parsePaymentDetil2(new JSONObject(response));
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



    private void parsePaymentDetil2(JSONObject response)
    {
        try {
            System.out.println(response.toString());
            String status = response.getString("status");
            if (status.equalsIgnoreCase("1")) {
                JSONArray arr=response.getJSONArray("message");
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
                    CommonFunctions.sPaymentDetailList.add(data);
                }
                if(CommonFunctions.sPaymentDetailList.size()>0) {
                    for (int i = 0; i < CommonFunctions.sPaymentDetailList.size(); i++) {
                        if (CommonFunctions.sPaymentDetailList.get(i).get_PaymentId().equalsIgnoreCase("3")) {
                            //  mPaymentType.setSelection(1);
                            wireMessage="1";
                            mAccountNameWrapper.setText(CommonFunctions.sPaymentDetailList.get(i).get_HolderName());
                            mBankNameWrapper.setText(CommonFunctions.sPaymentDetailList.get(i).get_BankName());
                            mAccountNumberWrapper.setText(CommonFunctions.sPaymentDetailList.get(i).get_AccountNumber());
                            mIFSCWrapper.setText(CommonFunctions.sPaymentDetailList.get(i).get_IFSCCODE());
                            mCityWrapper.setText(CommonFunctions.sPaymentDetailList.get(i).get_City());
                        } else if (CommonFunctions.sPaymentDetailList.get(i).get_PaymentId().equalsIgnoreCase("1")) {
                            // mPaymentType.setSelection(2);
                            paypalMessage="1";
                            mPayPalDetail.setText(CommonFunctions.sPaymentDetailList.get(i).get_PaypalAccount());
                        }
                        System.out.println("In payment "+wireMessage+" "+paypalMessage);
                    }
                }

            }
            else
            {
                showMessageDialog("No account details found.","#f2dede","#a94442");

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
                Thread.sleep(0);
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
