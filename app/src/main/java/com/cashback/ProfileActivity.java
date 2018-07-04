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
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jack on 08-03-2017.
 */

public class ProfileActivity extends Activity {

    EditText mFullName,mAddress1,mAddress2,mCity,mZip,mState,mPhone,mEmail;
    TextView mUpdateBtn,mMessageView;
    String chkString1="",chkString2="";
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="";
    LinearLayout mBackFromProfile;
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_profile_activity);
        prefs= PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        mFullName=(EditText)findViewById(R.id.fullName);
        mEmail=(EditText)findViewById(R.id.email);
        mAddress1=(EditText)findViewById(R.id.address1);
        //mAddress2=(EditText)findViewById(R.id.address2);
        mCity=(EditText)findViewById(R.id.city);
        mZip=(EditText)findViewById(R.id.zip);
        mState=(EditText)findViewById(R.id.state);
        mPhone=(EditText)findViewById(R.id.phone);
        mUpdateBtn=(TextView)findViewById(R.id.updateBtn);
        mMessageView=(TextView)findViewById(R.id.message);
        networkCall();
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname=mFullName.getText().toString().trim();
                String email=mEmail.getText().toString().trim();
                String address1=mAddress1.getText().toString().trim();
                String address2="";//mAddress2.getText().toString().trim();
                String city=mCity.getText().toString().trim();
                String zip=mZip.getText().toString().trim();
                String state=mState.getText().toString().trim();
                String phone=mPhone.getText().toString().trim();
                chkString2=fullname+" "+email+" "+address1+" "+city+" "+state+" "+zip+" "+phone;


                if(chkString1.equalsIgnoreCase(chkString2))
                {
                    System.out.println("In chk string");
                    showMessageDialog("Changes allow you to save profile data.","#fcf8e3","#8a6d3b");
                    return;
                }

                if(fullname.length()>0&&address1.length()>0)
                {
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("user_id", userId)
                            .addFormDataPart("name",fullname)
                            .addFormDataPart("email",email.toLowerCase())
                            .addFormDataPart("address1",address1)
                            .addFormDataPart("address2", address2)
                            .addFormDataPart("city",city)
                            .addFormDataPart("state",state)
                            .addFormDataPart("zip",zip)
                            .addFormDataPart("phone",phone)
                            .build();
                    networkCall(requestBody);
                }
                else
                {
                    mFullName.setHintTextColor(Color.RED);
                    mEmail.setHintTextColor(Color.RED);
                    mAddress1.setHintTextColor(Color.RED);

                    showMessageDialog("Fill all mendatory fields.","#f2dede","#a94442");
                }
            }
        });


        ((LinearLayout)findViewById(R.id.backFromProfile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void networkCall()
    {
        if(!CommonFunctions.isInternetOn(ProfileActivity.this))
        {
            showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
            return;
        }
        userId=prefs.getString("USER_ID","102");
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        findViewById(R.id.scrollView).setVisibility(View.GONE);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .build();
        new ExecuteServices().executePost(CommonFunctions.mUrl+"userDetails", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(userId+" "+response);
                            parseJsonFeed(new JSONObject(response));
                            findViewById(R.id.progressbar).setVisibility(View.GONE);
                            findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
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



    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void parseJsonFeed(JSONObject response) {
        try {
            String status = response.getString("status");
            String message = response.getString("message");
            if (status.equalsIgnoreCase("1")) {
                JSONArray feedArray = response.getJSONArray("data");
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);
                    String fullname = feedObj.getString("name");
                    String add1 = feedObj.getString("address1");
                    String add2 = feedObj.getString("address2");
                    String city = feedObj.getString("city");
                    String state = feedObj.getString("state");
                    String zip = feedObj.getString("zip");
                    String phone = feedObj.getString("phone");
                    String email=feedObj.getString("email");
                    mFullName.setText(fullname);
                    mEmail.setText(""+email.toLowerCase());
                    mAddress1.setText(add1);

                    mCity.setText(city);
                    mState.setText(state);
                    mZip.setText(zip);
                    mPhone.setText(phone);
                    chkString1=fullname.trim()+" "+email.trim()+" "+add1.trim()+" "+city.trim()+" "+state.trim()+" "+zip.trim()+" "+phone.trim();
                }
            } else {
               // showMessageDialog("Something went wrong.","#f2dede");

            }
        } catch (JSONException e) {
            e.printStackTrace();
            showMessageDialog("Something went wrong.","#f2dede","#a94442");
        }
    }

    void networkCall(RequestBody requestBody)
    {
        if(!CommonFunctions.isInternetOn(ProfileActivity.this))
        {
            showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
            return;
        }
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        findViewById(R.id.scrollView).setVisibility(View.GONE);
        new ExecuteServices().executePost(CommonFunctions.mUrl + "updateuserdetails", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            parseJsonUpdateFeed(new JSONObject(response));
                            findViewById(R.id.progressbar).setVisibility(View.GONE);
                            findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
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


    private void parseJsonUpdateFeed(JSONObject response) {
        try {
            // CommonFunction.viewProgressGone();
            String status = response.getString("status");
            String message = response.getString("message");
            if (status.equalsIgnoreCase("1")) {
                chkString1=chkString2;
               // profileUpdateDilaog(ProfileActivity.this);
                showMessageDialog("Information updated successfully.","#dff0d8","#3c763d");
            } else {
                showMessageDialog("Something went wrong.","#f2dede","#a94442");
            }
        } catch (Exception e) {

        }
    }

    public static void profileUpdateDilaog(final Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.profile_update_dialog);
        dialog.setCancelable(false);
        dialog.show();
        TextView mOkBtn=(TextView)dialog.findViewById(R.id.update);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


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
        },3000);
    }

}
