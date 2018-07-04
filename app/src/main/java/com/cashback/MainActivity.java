package com.cashback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.AppUpdateActivity;
import com.cashback.adapter.NavDrawerListAdapter;
import com.cashback.data.NavDrawerItem;
import com.cashback.data.Store;
import com.cashback.data.WebviewModel;
import com.cashback.fragment.AllStoreFragment;
import com.cashback.fragment.CategoryFragment;
import com.cashback.fragment.FavouriteFragment;
import com.cashback.fragment.HomeFragment;
import com.cashback.fragment.InviteFragment;
import com.cashback.fragment.MyAccountFragment;
import com.cashback.fragment.RecentStoresFragment;
import com.cashback.fragment.SearchFragment;
import com.cashback.fragment.WhitecashbackFragment;
import com.cashback.services.AlStoreIntentService;
import com.cashback.services.AllCategoryIntentService;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;
import com.cashback.util.ExecuteServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private Menu menu;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String titleString="Cashback";
    public static String sSelectedFragment="",sComingFrom="";
    public static Handler sHandleRequestForFragment;
    public final static String APP_PNAME = "com.cash.cashback";
    LinearLayout mSearchLayout,mBackFromSearchLayout,mDrawerIcon,mToolBarCustom;
    RelativeLayout mMainToolBarLayout;
    ImageView mNOtificationICon,mSearchIcon,mToolLogo,mNavigationIcon;
    TextView mToolTitle;
    static TextView mTextViewRetry;
    static TextView mToastView;
    Boolean mSlideState=false;
    EditText mEditSearchText;
    FrameLayout mFrameContainer;
    SearchFragment mSearchFragment;
    private String userId="";
    public static WebviewModel sWebviewData;
    boolean doubleBackToExitPressedOnce = false;
    Fragment fragment = null;
    public static int registerFlag=0,loginFlag=0;
    AppCompatImageView mGoogleSearchIcon;
    private static Activity sContext;
    private static boolean sDialogShown;
    private static boolean sReturnFromUpdate=false;
    private static boolean sResetDrawer=false;
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;
    private static Long mLastUpdatedTimeAllStore;
    private static long mLastUpdatedTimeShopCategory;
    private static boolean mIsSecondDay;
    private static long mCurrentTime;
    private DatabaseHandler mDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFrameContainer=(FrameLayout)findViewById(R.id.searchFrame);
        mMainToolBarLayout=(RelativeLayout)findViewById(R.id.mainToolbar);
        mSearchLayout=(LinearLayout)findViewById(R.id.searchLayout);
        mBackFromSearchLayout=(LinearLayout)findViewById(R.id.backFromSearch);
        mSearchIcon=(ImageView)findViewById(R.id.search);
        mNOtificationICon=(ImageView)findViewById(R.id.notification);
        mDrawerIcon=(LinearLayout)findViewById(R.id.drawerIcon);
        mToolBarCustom=(LinearLayout)findViewById(R.id.toolbarCustom);
        mToolLogo=(ImageView)findViewById(R.id.toolLogo);
        mNavigationIcon=(ImageView)findViewById(R.id.navigationIcon);
        mToolTitle=(TextView)findViewById(R.id.titleTool);
        mToastView=(TextView)findViewById(R.id.toast);
        mEditSearchText=(EditText)findViewById(R.id.searchHere);
        mGoogleSearchIcon=(AppCompatImageView)findViewById(R.id.googleSearchIcon);
        mTextViewRetry=(TextView)findViewById(R.id.retry);
        mBackFromSearchLayout.setOnClickListener(this);
        mSearchIcon.setOnClickListener(this);
        mNOtificationICon.setOnClickListener(this);
        mDrawerIcon.setOnClickListener(this);
        mNavigationIcon.setOnClickListener(this);
        mGoogleSearchIcon.setOnClickListener(this);
        mSearchFragment= (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.searchFragment);

        prefs= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        mTitle = mDrawerTitle = getTitle();
        mDatabaseHandler=DatabaseHandler.getInstance(this);
        sContext=MainActivity.this;
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        checkForAllStoreUpdate();
        checkForShopCategoryUpdate();

        if(getIntent().getExtras()!=null){
        System.out.println("dddd dxxd "+getIntent().getExtras().getString("nfS"));
        if(!TextUtils.isEmpty(getIntent().getExtras().getString("nfS"))){
            try {

                JSONObject obj=new JSONObject(getIntent().getExtras().getString("nfS"));
                System.out.println("dddd dxxd ssss ");
                AllStoreFragment.sStoreDataList=  new Store(obj.getString("retailer_id"),obj.getString("title"),obj.getString("cashback")
                    ,"active",obj.getString("url"),obj.getString("image"),obj.getString("description"),"1",null);

            /**
                Cursor cs= DatabaseHandler.getInstance(this).checkSQLiteData(AllStoreFragment.sStoreDataList.store_id);

            if(cs.getCount()>0) {
                DatabaseHandler.getInstance(this).deleteStoreRow(AllStoreFragment.sStoreDataList.store_id);
                DatabaseHandler.getInstance(this).insertRecentStores(AllStoreFragment.sStoreDataList);
            }
            else
            {
                DatabaseHandler.getInstance(this).insertRecentStores(AllStoreFragment.sStoreDataList);
            }
             **/
            RecentStoresFragment.reload=true;
                System.out.println("dddd dxxd ss");
            Intent intent = new Intent(this, StoreDetailPageScreen.class);
            startActivity(intent);
            } catch (Exception e) {
                System.out.println("dddd dxxd "+e.getLocalizedMessage());
                e.printStackTrace();
            }
        }}
        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        // adding nav drawer items to array
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], R.drawable.fstore));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], R.drawable.all_store));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], R.drawable.category));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], R.drawable.fav_icon));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], R.drawable.cashback));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], R.drawable.invite));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], R.drawable.thumbs_up));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], R.drawable.about));

        // Recycle the typed array
        navMenuIcons.recycle();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        mDrawerList.setAdapter(adapter);
        // enabling action bar app icon and behaving it as toggle button
        //        ActionBar related work
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(false);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;
        //toolbar.setNavigationIcon(R.drawable.fstore);
      /*  mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) */

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        )
        {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                mSlideState=false;
                if (sSelectedFragment.equalsIgnoreCase("MyAccount")||sSelectedFragment.equalsIgnoreCase("Invite")
                        ||sSelectedFragment.equalsIgnoreCase("Whitecashback")||sSelectedFragment.equalsIgnoreCase("RateUs"))
                {
                    mNOtificationICon.setVisibility(View.GONE);
                    mSearchIcon.setVisibility(View.GONE);
                }
                else
                {
                    mNOtificationICon.setVisibility(View.VISIBLE);
                    mSearchIcon.setVisibility(View.VISIBLE);
                }
                mToolTitle.setVisibility(View.VISIBLE);
                mToolTitle.setText(titleString);
                mToolLogo.setVisibility(View.GONE);

                // calling onPrepareOptionsMenu() to show action bar icons
                // invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                // invalidateOptionsMenu();
                mSlideState=true;
                mNOtificationICon.setVisibility(View.GONE);
                mSearchIcon.setVisibility(View.GONE);
                mToolTitle.setVisibility(View.VISIBLE);
                mToolTitle.setText("Cashback");
                mToolLogo.setVisibility(View.GONE);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);


        if (savedInstanceState == null) {
            // on first time display view for first nav item
          //displayView(0);
        }


        findViewById(R.id.logoIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetHome();
                mDrawerList.setItemChecked(0, true);
                mDrawerList.setSelection(0);
                /*ragment fragment = null;
                fragment = new HomeFragment();
                if (fragment != null) {
                    mToolTitle.setText("Featured stores");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).commit();
                    // update selected item and title, then close the drawer
                    mDrawerList.setItemChecked(0, true);
                    mDrawerList.setSelection(0);
                    setTitle(navMenuTitles[0]);
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }*/
            }
        });


         mEditSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             mSearchFragment.filterList(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                VectorDrawableCompat vc= VectorDrawableCompat.create(getResources(),s.toString().length() > 0?R.drawable.cross:R.drawable.magnifier_tool,null);
                if (s.toString().length() > 0) {
                    mEditSearchText.setCompoundDrawablesWithIntrinsicBounds(null, null, vc, null);
                } else {
                    mEditSearchText.setCompoundDrawablesWithIntrinsicBounds(vc,null,null,null);
                }
            }
        });

        mEditSearchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        if (event.getRawX() >= (mEditSearchText.getRight() - mEditSearchText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            mEditSearchText.setText("");
                            if(!(getSupportFragmentManager().findFragmentById(R.id.frame_container) instanceof AllStoreFragment))
                                mSearchFragment.getHistoryList();
                            return true;
                        }
                    } catch (Exception e) {
                    }
                }
                return false;
            }
        });


        //EditorActionListener on Search EdtText to catch event when search button click on keyboad
        mEditSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mSearchFragment.filterList(mEditSearchText.getText().toString());
                    //  mSearchFragment.getCategories(mEditSearchText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void checkForAllStoreUpdate(){
        if(mDatabaseHandler.getLastUpdatedTime("all_stores")!=null){
            mLastUpdatedTimeAllStore=Long.valueOf(mDatabaseHandler.getLastUpdatedTime("all_stores"));
            mCurrentTime=CommonFunctions.getCurrentTimeStamp();
            mIsSecondDay=CommonFunctions.isSecondDay(mCurrentTime,mLastUpdatedTimeAllStore);
            if(mIsSecondDay){
                startService(new Intent(this, AlStoreIntentService.class));
            }
        }
        else{
            startService(new Intent(this, AlStoreIntentService.class));
        }
    }

    private void checkForShopCategoryUpdate(){

        if(mDatabaseHandler.getLastUpdatedTime("shop_bycategory")!=null){
            mLastUpdatedTimeShopCategory=Long.valueOf(mDatabaseHandler.getLastUpdatedTime("shop_bycategory"));
            mCurrentTime=CommonFunctions.getCurrentTimeStamp();
            mIsSecondDay=CommonFunctions.isSecondDay(mCurrentTime,mLastUpdatedTimeShopCategory);
            if(mIsSecondDay){
                startService(new Intent(this,AllCategoryIntentService.class));
            }
        }
        else{
            startService(new Intent(this, AllCategoryIntentService.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sResetDrawer){
            resetHome();
            sResetDrawer=false;
        }

        mToastView.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mToastView.setVisibility(View.GONE);
            }
        },5000);

        if(MyAccountFragment.logoutFlag==1)
        {
            MyAccountFragment.logoutFlag=0;
            showMessageDialog("logged out successfully.","#f2dede","#a94442");
        }

        if(MainActivity.registerFlag==1)
        {
            MainActivity.registerFlag=0;
            showMessageDialog("Congratulations !! You are registered.","#dff0d8","#3c763d");
        }

        if(MainActivity.loginFlag==1)
        {
            MainActivity.loginFlag=0;
            showMessageDialog("Welcome to WhiteCashback.","#dff0d8","#3c763d");
        }

        String videoFlag=prefs.getString("VIDEO_FLAG","0");
        System.out.println("video flag "+videoFlag);
        if(videoFlag.equalsIgnoreCase("0") && userId.equalsIgnoreCase("102"))
        {
            prefEditor.putString("VIDEO_FLAG","1").commit();
           // startActivity(new Intent(MainActivity.this,VideoViewActivity.class));
        }
        if(sReturnFromUpdate==false) {
            checkUpdate();
        }

        //Toast.makeText(activity, "OnResume is called in MainActivity", Toast.LENGTH_SHORT).show();


    }


    private void checkUpdate() {
        new ExecuteServices().execute(CommonFunctions.mUrl + "appversion", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(response);
                            JSONObject object=new JSONObject(response);
                            String status=object.getString("status");
                            if(status.equalsIgnoreCase("1")) {
                                PackageManager manager = getPackageManager();
                                PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
                                int vNum = info.versionCode;
                                Float num=Float.parseFloat(object.getString("varsionName"));
                                System.out.println(""+num);
                                if (vNum<num) {
                                    Intent intent1 = new Intent(MainActivity.this, AppUpdateActivity.class);
                                    //intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent1);
                                    //MainActivity.this.finish();
                                    sReturnFromUpdate=true;
                                }
                            }
                            else
                            {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onServiceExecutedFailed(String msg) {

            }
        });

    }


    public static void showMessageDialog(String message)
    {
        if(mToastView.getVisibility()==View.VISIBLE)
        {
            mToastView.setVisibility(View.GONE);
        }
        mToastView.setText(message);
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
            }
        },4000);
    }


    /**
     *
     * @param message
     * @param color
     * @param textColor
     */
    public static void showMessageDialog(String message,String color,String textColor)
    {

        if(mToastView.getVisibility()==View.VISIBLE)
        {
            mToastView.setVisibility(View.GONE);
        }
        mToastView.setBackgroundColor(Color.parseColor(color));
        mToastView.setText(message);
        mToastView.setVisibility(View.VISIBLE);
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
            }
        },4000);
    }

    @Override
    public void onClick(View v)
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId())
        {
            case R.id.navigationIcon:
                if(mSlideState)
                {
                    mDrawerLayout.closeDrawer(mDrawerList);

                }
                else
                {

                    mDrawerLayout.openDrawer(mDrawerList);
                }
                break;
            case R.id.search:
                mEditSearchText.setText("");
                mEditSearchText.requestFocus();
                imm.showSoftInput(mEditSearchText, 0);

                mMainToolBarLayout.setVisibility(View.GONE);
                mSearchLayout.setVisibility(View.VISIBLE);
                //  if(!(getSupportFragmentManager().findFragmentById(R.id.frame_container) instanceof AllStoreFragment))
                mSearchFragment.getHistoryList();
                mFrameContainer.setVisibility(View.VISIBLE);

                break;
            case R.id.notification:
                startActivity(new Intent(MainActivity.this,NotificationActivity.class));
                break;

            case R.id.backFromSearch:
                mEditSearchText.setText("");
                mMainToolBarLayout.setVisibility(View.VISIBLE);
                mSearchLayout.setVisibility(View.GONE);
                mFrameContainer.setVisibility(View.GONE);

                imm.hideSoftInputFromWindow(mBackFromSearchLayout.getWindowToken(), 0);
                break;

            case R.id.googleSearchIcon:
                promptSpeechInput();
                break;
        }

    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //  menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    void resetHome(){
        titleString="Featured stores";
        mToolTitle.setText("Featured stores");
        sSelectedFragment="stores";

        mNOtificationICon.setVisibility(View.VISIBLE);
        mSearchIcon.setVisibility(View.VISIBLE);
        mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        removeFragment();
    }


   /* //Method commented to fix issue of Crash after Login 10-05-2017 by Kishor
   void removeFragment(){
        mDrawerList.setItemChecked(0, true);
        mDrawerList.setSelection(0);

        if(fragment==null)
        fragment=    getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment != null && !(fragment instanceof HomeFragment)) {


        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        fragment=null;
        super.onBackPressed();

        }
    }*/



    void removeFragment(){
        mDrawerList.setItemChecked(0, true);
        mDrawerList.setSelection(0);
        if(fragment==null)
            fragment=    getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment != null && !(fragment instanceof HomeFragment)) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            fragment=null;
        }
    }


    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments

        userId=prefs.getString("USER_ID","102");
        switch (position) {
            case 0:
                //resetHome();
                removeFragment();
               fragment = new HomeFragment();
               break;

            case 1:
                removeFragment();
                titleString="All stores";
                mToolTitle.setText("All stores");
                sSelectedFragment="All_Stores";
                mNOtificationICon.setVisibility(View.VISIBLE);
                mSearchIcon.setVisibility(View.VISIBLE);
                fragment = new AllStoreFragment();
                break;

            case 2:
                removeFragment();
                titleString="Shop by category";
                mToolTitle.setText("Shop by category");
                sSelectedFragment="category";
                mNOtificationICon.setVisibility(View.VISIBLE);
                mSearchIcon.setVisibility(View.VISIBLE);
                fragment = new CategoryFragment();

                break;


            case 3:
                sSelectedFragment="MyFav";
                sComingFrom="MyFav";
                mDrawerLayout.closeDrawer(mDrawerList);
                if(userId.equalsIgnoreCase("102")){
                    sResetDrawer=true;
                    startActivityForResult(new Intent(MainActivity.this,MLoginActivity.class),1000);
                }
                else
                {
                    titleString="Wish List";
                    removeFragment();
                    mToolTitle.setText("Wish List");
                    mNOtificationICon.setVisibility(View.GONE);
                    mSearchIcon.setVisibility(View.GONE);
                    fragment = new FavouriteFragment();
                }

                break;


            case 4:
                sSelectedFragment="MyAccount";
                sComingFrom="MyAccount";
                mDrawerLayout.closeDrawer(mDrawerList);
                if(userId.equalsIgnoreCase("102")){
                    sResetDrawer=true;
                    startActivityForResult(new Intent(MainActivity.this,MLoginActivity.class),1000);
                }
                else
                {     titleString="My Account";
                    removeFragment();
                    mToolTitle.setText("My Account");
                    mNOtificationICon.setVisibility(View.GONE);
                    mSearchIcon.setVisibility(View.GONE);
                    fragment = new MyAccountFragment();
                }

                break;

            case 5:
                sSelectedFragment="Invite";
                sComingFrom="Invite";
                mDrawerLayout.closeDrawer(mDrawerList);
                if(userId.equalsIgnoreCase("102"))
                {
                    sResetDrawer=true;
                    startActivityForResult(new Intent(MainActivity.this,MLoginActivity.class),1000);
                }
                else {
                    removeFragment();
                    titleString="Invite your friend";
                    mToolTitle.setText("Invite your friend");
                    mNOtificationICon.setVisibility(View.GONE);
                    mSearchIcon.setVisibility(View.GONE);
                    fragment = new InviteFragment();
                }
                break;

            case 6:
                sSelectedFragment="RateUs";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                mDrawerLayout.closeDrawer(mDrawerList);
                // fragment=null;
                break;

            case 7:
                removeFragment();
                titleString="White Cashback";
                sSelectedFragment="Whitecashback";
                mToolTitle.setText("WhiteCashback");
                mNOtificationICon.setVisibility(View.GONE);
                mSearchIcon.setVisibility(View.GONE);
                fragment=new WhitecashbackFragment();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).addToBackStack("ss").commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode==RESULT_OK && requestCode==1000)
//        {
//            android.support.v4.app.Fragment fragment = null;
//            if(MainActivity.sSelectedFragment.equalsIgnoreCase("MyFav"))
//            {
//                System.out.println("in my Favourite fragment");
//
//                mToolTitle.setText("My Favourite");
//
//                fragment = new MyAccountFragment();
//            }
//            else if(MainActivity.sSelectedFragment.equalsIgnoreCase("MyAccount"))
//            {
//                System.out.println("in my account fragment");
//
//               mToolTitle.setText("My Account");
//
//                fragment = new MyAccountFragment();
//            }
//            else if(MainActivity.sSelectedFragment.equalsIgnoreCase("Invite"))
//            {
//                System.out.println("in invite");
//
//                mToolTitle.setText("Invite your Friend");
//
//                fragment = new InviteFragment();
//            }
//            mNOtificationICon.setVisibility(View.GONE);
//            mSearchIcon.setVisibility(View.GONE);
//            if (fragment != null) {
//                System.out.println("in fragment");
//
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.frame_container, fragment).commit();
//                // update selected item and title, then close the drawer
//                mDrawerLayout.closeDrawer(mDrawerList);
//            } else {
//                // error in creating fragment
//                Log.e("MainActivity", "Error in creating fragment");
//            }
//        }
//    }


    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        }
//        if (doubleBackToExitPressedOnce) {
//            //  Homefragment.appUpdatestatus=0;
//            super.onBackPressed();
//            return;
//        }
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);


        if(mFrameContainer.getVisibility()==View.VISIBLE){
            mFrameContainer.setVisibility(View.GONE);
            mMainToolBarLayout.setVisibility(View.VISIBLE);
            mSearchLayout.setVisibility(View.GONE);
            mFrameContainer.setVisibility(View.GONE);
            return;
        }

        if (fragment != null && !(fragment instanceof HomeFragment)) {
            resetHome();
            return;
        }

        if(mDrawerLayout.isDrawerOpen(mDrawerList)){
            mDrawerLayout.closeDrawer(mDrawerList);
            return;
        }



        new AlertDialog.Builder(MainActivity.this)
                .setTitle("WhiteCashback")
                .setMessage("Are you sure want to exit the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.cancel();
                    }
                })
                .setIcon(R.drawable.launchericon)
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mEditSearchText.setText(result.get(0));
                    mEditSearchText.setSelection(result.get(0).length());
                    mSearchFragment.filterList(result.get(0));
                    //   mSearchFragment.getCategories(result.get(0));
                }
                break;
            }
            case 1000:
                if (resultCode==RESULT_OK && requestCode==1000)
                {
                    android.support.v4.app.Fragment fragment = null;
                    if(MainActivity.sSelectedFragment.equalsIgnoreCase("MyFav"))
                    {
                        System.out.println("in my Favourite fragment");

                        mToolTitle.setText("Wish List");

                        fragment = new FavouriteFragment();
                    }
                    else if(MainActivity.sSelectedFragment.equalsIgnoreCase("MyAccount"))
                    {
                        System.out.println("in my account fragment");

                        mToolTitle.setText("My Account");

                        fragment = new MyAccountFragment();
                    }
                    else if(MainActivity.sSelectedFragment.equalsIgnoreCase("Invite"))
                    {
                        System.out.println("in invite");

                        mToolTitle.setText("Invite your Friend");

                        fragment = new InviteFragment();
                    }
                    mNOtificationICon.setVisibility(View.GONE);
                    mSearchIcon.setVisibility(View.GONE);
                    if (fragment != null) {
                        System.out.println("in fragment");

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, fragment).commit();
                        // update selected item and title, then close the drawer
                        mDrawerLayout.closeDrawer(mDrawerList);
                    } else {
                        // error in creating fragment
                        Log.e("MainActivity", "Error in creating fragment");
                    }
                }

        }

    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }}


}
