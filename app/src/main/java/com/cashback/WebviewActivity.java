package com.cashback;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cashback.fragment.AllStoreFragment;
import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;
import com.cashback.util.GetInfoFromUrl;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by jack on 02-07-2015.
 */
public class WebviewActivity extends Activity {
    RelativeLayout mAnimLayout;
    RelativeLayout mBeforeWeb;
    TextView mStoreNameWeb, mStoreCashback, mHotDeals, mActivated, mPercentCashBack, mStoreName;
    WebView mBrowser;
    ProgressBar pBar, pBar2;
    RelativeLayout mBackFromBrowser;
    static TextView mToastView;
    public static ImageView mProgressLine;
    ImageView mMoveBackFromBrowser, mMoveForwardFromBrowser, mReload,mImg;
    private CustomTabActivityHelper mCustomTabActivityHelper;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    String userId="",userName="";
    private static Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.webview);
        activity=this;
        prefs = PreferenceManager.getDefaultSharedPreferences(WebviewActivity.this); //initilize the shared preferences
        prefsEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        userName=prefs.getString("USER_NAME","");
        mProgressLine = (ImageView) findViewById(R.id.progressLine);
        mStoreNameWeb = (TextView) findViewById(R.id.storeNameWeb);
        mStoreCashback = (TextView) findViewById(R.id.cashbackWeb);
        mToastView=(TextView)findViewById(R.id.message);
        mHotDeals = (TextView) findViewById(R.id.hotDeals);
        mBeforeWeb = (RelativeLayout) findViewById(R.id.mLayoutbeforeWeb);
        mAnimLayout = (RelativeLayout) findViewById(R.id.animLayout);
        mActivated = (TextView) findViewById(R.id.activated);
        mImg=(ImageView)findViewById(R.id.img);
        mCustomTabActivityHelper = new CustomTabActivityHelper();
        //MainActivity.sWebviewData=null;
        if(MainActivity.sWebviewData==null){
            showMessageDialog("Something went wrong.","#f2dede","#a94442");
            mBeforeWeb.setVisibility(View.GONE);
            mStoreNameWeb.setText("");
            mStoreCashback.setText("");
            mImg.setVisibility(View.GONE);
        }

        else {

            mStoreNameWeb.setText(MainActivity.sWebviewData.getRetailerName());
            if (!TextUtils.isEmpty(MainActivity.sWebviewData.getCashback())) {
                mStoreCashback.setText("" + MainActivity.sWebviewData.getCashback());
            }
            else
            {
                mStoreCashback.setVisibility(View.GONE);
            }
//            if (MainActivity.sWebviewData.getStatus().equalsIgnoreCase("inactive")) {
//                mStoreCashback.setText("Currently No Cashback");
//                mStoreCashback.setTextColor(Color.WHITE);
//            } else {
//                if (MainActivity.sWebviewData.getStatus().equalsIgnoreCase("")) {
//                    mStoreCashback.setText("BEST OFFER");
//                    mStoreCashback.setTextColor(Color.WHITE);
//                } else if (MainActivity.sWebviewData.getCashback() != null) {
//                    if (MainActivity.sWebviewData.getCashback().contains("%")) {
//                        mStoreCashback.setText("Upto " + MainActivity.sWebviewData.getCashback() + " Cash Back");
//                    } else {
//                        mStoreCashback.setText("Upto " + MainActivity.sWebviewData.getCashback() + " Cash Back");
//
//                    }
//                    mStoreCashback.setTextColor(Color.WHITE);
//                }
//            }

            mBackFromBrowser = (RelativeLayout) findViewById(R.id.backFromBrowser);
            mBackFromBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebviewActivity.this.finish();
                }
            });

            mBrowser = (WebView) findViewById(R.id.webview1);
            mBrowser.setVisibility(View.GONE);
            pBar = (ProgressBar) findViewById(R.id.progress);
            mBrowser.getSettings().setDomStorageEnabled(true);
            mBrowser.getSettings().setUseWideViewPort(true);
            mBrowser.getSettings().setJavaScriptEnabled(true);
            mBrowser.getSettings().setAllowContentAccess(true);
            mBrowser.getSettings().setAppCacheEnabled(true);
            // mBrowser.getSettings().set

            mMoveBackFromBrowser = (ImageView) findViewById(R.id.moveBack);
            mMoveBackFromBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBrowser.canGoBack()) {
                        mBrowser.goBack();
                    }
                    // CommonFunction.displayToast(WebviewActivity.this,"Work is in progress..!!");
                    System.out.println("Going back from pages in browser ");
                }
            });

            mReload = (ImageView) findViewById(R.id.refresh);
            mReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBrowser.reload();
                }
            });

            mMoveForwardFromBrowser = (ImageView) findViewById(R.id.moveForward);
            mMoveForwardFromBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBrowser.canGoForward()) {
                        mBrowser.goForward();
                    }
                    //CommonFunction.displayToast(WebviewActivity.this,"Work is in progress..!!");
                }
            });

            pBar2 = (ProgressBar) findViewById(R.id.progress2);
            mStoreName = (TextView) findViewById(R.id.storeName);
            mPercentCashBack = (TextView) findViewById(R.id.percent);
            mStoreName.setText("Congratulations " + userName + ", you are on the way to earn cashback.");
            if(!TextUtils.isEmpty(MainActivity.sWebviewData.getCashback())) {
                mPercentCashBack.setText("" + MainActivity.sWebviewData.getCashback().replace("Upto", "Upto \n"));
            }
//            if((MainActivity.sWebviewData.getCashback()!=null)&&(MainActivity.sWebviewData.getCashback().isEmpty())) {
//                if (MainActivity.sWebviewData.getCashback().equalsIgnoreCase("")) {
//                    mPercentCashBack.setText("UP TO\n0%\nCASH BACK");
//                } else if (MainActivity.sWebviewData.getCashback().equalsIgnoreCase("Currently No Cashback")) {
//                    mPercentCashBack.setText("UP TO\n0%\nCASH BACK");
//                } else {
//                    if (MainActivity.sWebviewData.getCashback().contains("%")) {
//                        mPercentCashBack.setText("UP TO\n" + MainActivity.sWebviewData.getCashback() + "\nCASH BACK");
//
//                    } else {
//                        mPercentCashBack.setText("UP TO\n" + MainActivity.sWebviewData.getCashback() + "\nCASH BACK");
//
//                    }
//                }
//            }
//            else{
//                mPercentCashBack.setText("Best Offer");
//            }

         /*   mBeforeWeb = (RelativeLayout) findViewById(R.id.mLayoutbeforeWeb);
              mAnimLayout = (RelativeLayout) findViewById(R.id.animLayout);
              mActivated = (TextView) findViewById(R.id.activated);*/
            //mActivated.setVisibility(View.GONE);
            final Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            mAnimLayout.startAnimation(anim);
            mActivated.startAnimation(anim);

            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //  mActivated.setVisibility(View.VISIBLE);
                    mBeforeWeb.setVisibility(View.VISIBLE);
                    // mBrowser.setVisibility(View.VISIBLE);
                    try {
                        Thread.sleep(3 * 1000);
                        //callBrowser();
                        userId = prefs.getString("USER_ID", "102");
                        String storeUrl = MainActivity.sWebviewData.getUrl().replace("{USERID}", userId);
                        Log.e("Store url", storeUrl + " Store url " + userId);
                        Uri uri = Uri.parse(storeUrl);
                        openCustomChromeTab(uri, WebviewActivity.this);
                        //WebviewActivity.this.finish();
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            numberOfClick("0");
        }

    }

    void callBrowser() {
        Intent i;
        GetInfoFromUrl get = new GetInfoFromUrl();
        mBrowser.setVisibility(View.VISIBLE);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mCustomTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCustomTabActivityHelper.unbindCustomTabsService(this);
    }

    private void numberOfClick(String s) {
        userId=prefs.getString("USER_ID","102");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .addFormDataPart("retailer_id", MainActivity.sWebviewData.getStore_id())
                .build();
        new ExecuteServices().executePost(CommonFunctions.mUrl + "click", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(String response) {
                System.out.println("click count");
            }

            @Override
            public void onServiceExecutedFailed(String message) {

            }
        },requestBody);
    }


    /**
     * Handles opening the url in a custom chrome tab
     * @param uri
     */
    public void openCustomChromeTab(Uri uri,Activity activity) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        // set toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        // set start and exit animations
        intentBuilder.setStartAnimations(activity, R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(activity, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        // call helper to open custom tab
        CustomTabActivityHelper.openCustomTab(activity, customTabsIntent, uri, new CustomTabActivityHelper.CustomTabFallback() {
            @Override
            public void openUri(Activity activity, Uri uri) {
                // fall back, call open open webview
                //openWebView(uri);
                mBeforeWeb.setVisibility(View.GONE);
                mBrowser.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * Creates a pending intent to send a broadcast to the {@link ChromeTabActionBroadcastReceiver}
     * @param actionSource
     * @return
     */
    private PendingIntent createPendingIntent(int actionSource, Activity activity) {
        Intent actionIntent = new Intent(activity, ChromeTabActionBroadcastReceiver.class);
        actionIntent.putExtra(ChromeTabActionBroadcastReceiver.KEY_ACTION_SOURCE, actionSource);
        return PendingIntent.getBroadcast(activity, actionSource, actionIntent, 0);
    }

    public  static  void showMessageDialog(String message,String color,String textColor)
    {
        if(mToastView.getVisibility()==View.VISIBLE)
        {
            mToastView.setVisibility(View.GONE);
        }
        mToastView.setBackgroundColor(Color.parseColor(color));
        mToastView.setText(message);
        mToastView.setTextColor(Color.parseColor(textColor));
        mToastView.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mToastView.setVisibility(View.GONE);
                activity.finish();
            }
        },3000);
    }

}
