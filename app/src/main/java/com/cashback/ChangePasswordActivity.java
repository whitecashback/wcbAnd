package com.cashback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jack on 03-05-2016.
 */
public class ChangePasswordActivity extends Activity {
    TextInputLayout mNewPassWrapper, mConfrimNewPassWrapper,mOldPasswordWrapper;
    TextView mChangePasswordBtn,mToastView;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    String flag="",mOldPassoword="",userId="";
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        prefs = PreferenceManager.getDefaultSharedPreferences(ChangePasswordActivity.this);
        prefsEditor = prefs.edit();
        userId=prefs.getString("USER_ID","102");
        mNewPassWrapper = (TextInputLayout) findViewById(R.id.newPassWrapper);
        mConfrimNewPassWrapper = (TextInputLayout) findViewById(R.id.confirmNewPassWrapper);
        mOldPasswordWrapper=(TextInputLayout)findViewById(R.id.oldPassWrapper);
        mToastView=(TextView)findViewById(R.id.message);
        ((LinearLayout)findViewById(R.id.backFromChangePassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mChangePasswordBtn = (TextView) findViewById(R.id.changePass);
        mChangePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass=mOldPasswordWrapper.getEditText().getText().toString().trim();
                String newPass = mNewPassWrapper.getEditText().getText().toString().trim();
                String cPass = mConfrimNewPassWrapper.getEditText().getText().toString().trim();
                if(oldPass.length()==0)
                {
                    mOldPasswordWrapper.setError("Please enter old password.");
                }
                else if(newPass.length()<0)
                {
                    mNewPassWrapper.setError("Please enter new password of length sic character.");
                }
                else if(cPass.length()==0&&!newPass.equalsIgnoreCase(cPass))
                {
                    mConfrimNewPassWrapper.setError("Confirm password not matched with new password");
                }
                else
                {

                    findViewById(R.id.formView).setVisibility(View.GONE);
                    findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("user_id", userId)
                            .addFormDataPart("password",newPass)
                            .addFormDataPart("old_password", oldPass)
                            .build();
                    if(CommonFunctions.isInternetOn(ChangePasswordActivity.this)) {
                        changePass(requestBody);
                    }
                    else
                    {
                        showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
                    }
                }

            }
        });
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;
    }

    private void changePass(RequestBody requestBody) {

        new ExecuteServices().executePost(CommonFunctions.mUrl + "changeUserPassword", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                System.out.println(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            findViewById(R.id.formView).setVisibility(View.VISIBLE);
                            findViewById(R.id.progressbar).setVisibility(View.GONE);
                            parseJsonUpdateFeed(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onServiceExecutedFailed(String msg) {
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     findViewById(R.id.formView).setVisibility(View.VISIBLE);
                     findViewById(R.id.progressbar).setVisibility(View.GONE);
                     showMessageDialog("Something went wrong.","#f2dede","#a94442");
                 }
             });
            }
        }, requestBody);

    }

    private void parseJsonUpdateFeed(JSONObject response) {
        try {
            String status = response.getString("status");
            String message = response.getString("message");
            if (status.equalsIgnoreCase("1")) {
                prefsEditor.putString("LOGIN_FLAG", "NO");
                prefsEditor.putString("USER_ID", "102");
                prefsEditor.putString("NAME", "");
                prefsEditor.putString("REG_ID_FLAG", "");
                prefsEditor.commit();

                mChangePasswordBtn.setVisibility(View.GONE);
                showMessageDialog2("Password changed successfully","#dff0d8","#3c763d");


            } else {
             showMessageDialog("Something went wrong.","#f2dede","#a94442");
            }
        } catch (Exception e) {

        }
    }

    public  void showMessageDialog(String message,String color,String textColor)
    {
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
                mToastView.setVisibility(View.GONE);
            }
        },4000);
    }


    public  void showMessageDialog2(String message,String color,String textColor)
    {
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
                Intent intent1 = new Intent(ChangePasswordActivity.this, MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();
            }
        },4000);
    }
}
