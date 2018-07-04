package com.cashback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.cash.cashback.AppController;
import com.cash.cashback.Config;
import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    TabLayout tablay;
    ViewPager mPager;
    LoginPagerAdapter mLoginPagerAdapter;
    //fb google setup
    LoginButton loginButtonFB;
    GoogleApiClient mGoogleApiClient;
    CallbackManager callbackManager;
    ProgressDialog pd;
    final static int RC_SIGN_IN = 101;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="";
    static TextView mMessageView;
    AppController aController;
    AsyncTask<Void, Void, Void> mRegisterTask;
    public static String regid="";
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        pd.setMessage("Wait...");
        pd.setCanceledOnTouchOutside(false);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_mlogin);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(R.string.app_name);
        loginButtonFB = (LoginButton) findViewById(R.id.login_fb);
        tablay = (TabLayout) findViewById(R.id.tab_lay);
        tablay.setSelectedTabIndicatorColor(Color.WHITE);
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mMessageView=(TextView)findViewById(R.id.message);
        mLoginPagerAdapter = new LoginPagerAdapter(getSupportFragmentManager(), 2, this);
        mPager.setAdapter(mLoginPagerAdapter);
        tablay.setupWithViewPager(mPager);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        findViewById(R.id.backFromLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        prefs= PreferenceManager.getDefaultSharedPreferences(MLoginActivity.this);
        prefEditor=prefs.edit();
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;

        //init fb google logins
        setFbLoginManager();
        initGoogleSignIn();

        //Registering for GCM

        GetRegistrationId();
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest permissions was properly set
        GCMRegistrar.checkManifest(this);
        aController=(AppController)getApplicationContext();
        //  lblMessage = (TextView) findViewById(R.id.lblMessage);
        System.out.println("Registration id Tokensssss l "+GCMRegistrar.getRegistrationId(getApplicationContext()));
        // Register custom Broadcast receiver to show messages on activity
        registerReceiver(mHandleMessageReceiver, new IntentFilter(Config.DISPLAY_MESSAGE_ACTION));
        Log.d("Registration id ", "" + GCMRegistrar.getRegistrationId(getApplicationContext()));
        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);

        // Check if regid already presents
        if (regId.equals("")) {

            // Register with GCM
            GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
            Log.d("Registration idssssss", ""+GCMRegistrar.getRegistrationId(getApplicationContext()));
            regid=GCMRegistrar.getRegistrationId(getApplicationContext());
        } else {

            // Device is already registered on GCM Server
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                Log.d("Registration ids", ""+GCMRegistrar.getRegistrationId(getApplicationContext()));
                // Skips registration.
                //  Toast.makeText(getApplicationContext(), "Already registered with GCM Server", Toast.LENGTH_LONG).show();
                regid=GCMRegistrar.getRegistrationId(getApplicationContext());
            }
            else
            {

                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        aController.register(context,  regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }
                };

                // execute AsyncTask
                mRegisterTask.execute(null, null, null);
            }
        }


    }


    public void GetRegistrationId()
    {
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        String regId = GCMRegistrar.getRegistrationId(MLoginActivity.this);
        if(regid!="") {
            regid = regId;
        }
        //  Log.d("the reg id is", "regId - "+regid);
        if (regId.equals("")) {
            GCMRegistrar.register(MLoginActivity.this, Config.GOOGLE_SENDER_ID); //register device for gcm

        } else {
            //  Log.d("GCM", "Already registered");
        }
    }
    // Create a broadcast receiver to get message and show on screen
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            try
            {
                aController.acquireWakeLock(getApplicationContext());

                //   Toast.makeText(getApplicationContext(), "Got Message: " + newMessage, Toast.LENGTH_LONG).show();
                // Releasing wake lock
                aController.releaseWakeLock();
            }
            catch(Exception e)
            {

            }
        }
    };


    @Override
    protected void onDestroy() {
        // Cancel AsyncTask
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            // Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);
            //Clear internal resources.
            GCMRegistrar.onDestroy(this);

        } catch (Exception e) {
            // Log.e("UnRegister Receiver Error",""+e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void onFBClicked(Fragment fragment, int type) {
        LoginManager.getInstance().logOut();
        loginButtonFB.performClick();
    }

    public void onGClicked(Fragment fragment, int type) {
        signInG();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void signInG() {
        pd.show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    ProfileTracker mProfileTracker;

    void setFbLoginManager() {
        loginButtonFB.setReadPermissions("email");
        // If using in a fragment
        //loginButtonFB.setFragment(this);
        callbackManager = CallbackManager.Factory.create();
        loginButtonFB.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        pd.show();
                        GraphRequest gRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                //   Toast.makeText(MLoginActivity.this, "" + object.toString(), Toast.LENGTH_SHORT).show();
                                try {
                                    String fcmToken=prefs.getString("FCM_TOKEN","0");
                                    final String name_ = object.getString("first_name") + " " + object.getString("last_name");
                                    System.out.println("first name " + name_);
                                    RequestBody requestBody = new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("facebook_id", URLEncoder.encode(object.getString("id"), "UTF-8"))
                                            .addFormDataPart("fname", name_)
                                            .addFormDataPart("reg_id", URLEncoder.encode(""+fcmToken, "UTF-8"))
                                            .addFormDataPart("device_type", URLEncoder.encode("1", "UTF-8"))
                                            .build();

                                    new ExecuteServices().executePost(CommonFunctions.mUrl+"facebookLogin", new ExecuteServices.OnServiceExecute() {
                                        @Override
                                        public void onServiceExecutedResponse(final String response) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.e("Response from fb login",response);
                                                    try {
                                                        JSONObject obj = new JSONObject(response);
                                                        String status=obj.getString("status");
                                                        if(status.equalsIgnoreCase("1"))
                                                        {
                                                            String userId=obj.getString("user_id");
                                                            userId=userId;
                                                            System.out.println("fb 1");
                                                            prefEditor.putString("USER_ID",userId).commit();
                                                            prefEditor.putString("USER_NAME",obj.getString("name")).commit();
                                                            prefEditor.putString("FB_LOGIN","true").commit();
                                                            prefEditor.putString("LOGIN_FROM","F").commit();
                                                            System.out.println("fb 2");
                                                            moveToActivity();
                                                            System.out.println("fb 3");

                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(MLoginActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    catch (Exception e)
                                                    {
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onServiceExecutedFailed(String message) {

                                        }
                                    },requestBody);

                                } catch (JSONException e) {
                                    pd.dismiss();
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    pd.dismiss();
                                    e.printStackTrace();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                        gRequest.setParameters(parameters);
                        gRequest.executeAsync();

                        }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MLoginActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(MLoginActivity.this, "exception", Toast.LENGTH_SHORT).show();
                        // App code
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        pd.dismiss();
        final GoogleSignInAccount acct = result.getSignInAccount();
        System.out.println("we are in "+result.isSuccess()+" "+result.getStatus().toString());
        if (result.isSuccess()) {
            try {
                String fcmToken=prefs.getString("FCM_TOKEN","0");
                final String name_ = acct.getDisplayName();
                System.out.println("Data " + name_);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("google_id", URLEncoder.encode(acct.getId(), "UTF-8"))
                        .addFormDataPart("fname", name_)
                        .addFormDataPart("email", acct.getEmail())
                        .addFormDataPart("device_type", URLEncoder.encode("1", "UTF-8"))
                        .addFormDataPart("reg_id", URLEncoder.encode(""+fcmToken, "UTF-8"))
                        .build();

                new ExecuteServices().executePost(CommonFunctions.mUrl + "googleLogin", new ExecuteServices.OnServiceExecute() {
                    @Override
                    public void onServiceExecutedResponse(final String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("Response from G login",response);
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    String status=obj.getString("status");
                                    if(status.equalsIgnoreCase("1"))
                                    {
                                        String id=obj.getString("user_id");
                                        userId=id;
                                        prefEditor.putString("USER_ID",userId).commit();
                                        prefEditor.putString("USER_NAME",obj.getString("name")).commit();
                                        prefEditor.putString("G_LOGIN","true").commit();
                                        prefEditor.putString("LOGIN_FROM","G").commit();

                                        moveToActivity();
                                    }
                                    else
                                    {
                                        Toast.makeText(MLoginActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                catch (Exception e)
                                {

                                }
                            }
                        });
                    }

                    @Override
                    public void onServiceExecutedFailed(String message) {

                    }
                },requestBody);

            }
            catch (UnsupportedEncodingException e) {
            }
        } else {
        }
    }

    void signOutG() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }


    void initGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()

                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(MLoginActivity.this)
                .enableAutoManage(MLoginActivity.this /* FragmentActivity */, this /* OnConnectionFailedListener */)

                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }



    void moveToActivity()
    {
        if(MainActivity.sComingFrom.equalsIgnoreCase("MyAccount")||MainActivity.sComingFrom.equalsIgnoreCase("MyFav")||
                MainActivity.sComingFrom.equalsIgnoreCase("invite")||MainActivity.sComingFrom.equalsIgnoreCase("LikeFav"))
        {

            setResult(Activity.RESULT_OK);
            finish();

        }
        else if(MainActivity.sComingFrom.equalsIgnoreCase("HotDeals")||MainActivity.sComingFrom.equalsIgnoreCase("FavFragment")
                ||MainActivity.sComingFrom.equalsIgnoreCase("StoreCoupon"))
        {
            startActivity(new Intent(MLoginActivity.this,WebviewActivity.class));
            finish();
        }

        else if(MainActivity.sComingFrom.equalsIgnoreCase("FavUnselected"))
        {
            finish();
        }

    }



    public static void showMessageDialog(String message,String color,String textColor)
    {
        if(mMessageView.getVisibility()== View.VISIBLE)
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

            }
        },3000);
    }
}
