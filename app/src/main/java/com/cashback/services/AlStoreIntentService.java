package com.cashback.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.cashback.MainActivity;
import com.cashback.R;
import com.cashback.data.Store;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;
import com.cashback.util.ExecuteServices;
import com.cashback.util.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AlStoreIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.cashback.services.action.FOO";
    private static final String ACTION_BAZ = "com.cashback.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.cashback.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.cashback.services.extra.PARAM2";

    public AlStoreIntentService() {
        super("AlStoreIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, AlStoreIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, AlStoreIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("xxxxttt "+isLoading);
        if(isLoading){
            return;
        }
        getCategories();
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {

                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
               // handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }
    public static ArrayList<Store> list = new ArrayList<>();
    boolean isLoading=false;
    void getCategories2() {        HttpRequest.excutePost(CommonFunctions.mUrl + "storeListBack","user_id=102");
    }
    /**
     * Method to fetch categories from server asynchronously
     */
    void getCategories() {
        list.clear();
        isLoading=true;


        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String userId=prefs.getString("USER_ID","102");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .build();
        new ExecuteServices().executePost2(CommonFunctions.mUrl + "storeListBack", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(String response) {
                isLoading=false;
                try {

                    JSONArray array=new JSONObject(response).getJSONArray("data");
                    int length=array.length();
                    for(int i=0;i<length;i++){
                        JSONObject obj=array.getJSONObject(i);
                        list.add(new Store(obj.getString("retailer_id"),obj.getString("title"),obj.getString("cashback")
                                ,obj.getString("retailer_status"),obj.getString("url"),obj.getString("image"),obj.has("description")?obj.getString("description"):"",obj.getString("is_favourite"),null));
                        System.out.println("running ...."+obj.getString("retailer_status"));
                    }

                    DatabaseHandler lDatabaseHandler;
                    lDatabaseHandler = DatabaseHandler.getInstance(getApplicationContext());
                    lDatabaseHandler.deleteAllStore();
                    Cursor cursor=lDatabaseHandler.getSQLiteData("all_stores");
                    if(cursor.getCount()<=0) {
                        for (int count = 0; count < list.size(); count++) {
                            Store lStore = list.get(count);
                            lDatabaseHandler.insertAllStores(lStore);
                            //Log.i("Loop Run",""+count);
                        }
                        long lCurrentTime=CommonFunctions.getCurrentTimeStamp();
                        lDatabaseHandler.updateLastUpdatedAllStoresTime(lCurrentTime);
                        cursor.close();
                    }
                    else{
                        //Log.i("Size is nonzero","sie is nonzero");
                    }
                    AlStoreIntentService.this.sendBroadcast(new Intent("com.cash.cashback.allstoress"));
                } catch (JSONException e) {
                }
            }

            @Override
            public void onServiceExecutedFailed(String message) {
                isLoading=false;
            }
        },requestBody);
    }


    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
}
