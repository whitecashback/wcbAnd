package com.cashback.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cashback.data.Cat;
import com.cashback.data.Store;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;
import com.cashback.util.ExecuteServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dev on 25/5/17.
 */

public class AllCategoryIntentService extends IntentService {


    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.cashback.services.action.FOO";
    private static final String ACTION_BAZ = "com.cashback.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.cashback.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.cashback.services.extra.PARAM2";

    public static ArrayList<Cat> categoryList = new ArrayList<>();
    boolean isLoading=false;



    public AllCategoryIntentService() {
        super("AllCategoryIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(isLoading){
            return;
        }
        getCategories();
        if(intent!=null){
            final String action=intent.getAction();

            if(ACTION_FOO.equalsIgnoreCase(action)){
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                // handleActionFoo(param1, param2);
            }
            else if(ACTION_BAZ.equalsIgnoreCase(action)){
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    //Customize Helper Method
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

    public void getCategories(){
        categoryList.clear();
        isLoading=true;
        new ExecuteServices().execute(CommonFunctions.mUrl + "categoryList", new ExecuteServices.OnServiceExecute() {

            @Override
            public void onServiceExecutedResponse(String response) {
                isLoading=false;
                try {
                    JSONArray lJsonArray=new JSONObject(response).getJSONArray("data");
                    int length=lJsonArray.length();
                    for(int i=0;i<length;i++){
                        JSONObject obj=lJsonArray.getJSONObject(i);
                        categoryList.add(new Cat(obj.getString("category_id"),obj.getString("name")));
                    }

                    DatabaseHandler lDatabaseHandler=new DatabaseHandler(getApplicationContext());
                    lDatabaseHandler.deleteShopCategory();

                    for(int lCount=0;lCount<categoryList.size();lCount++){
                        Cat lCat=categoryList.get(lCount);
                        lDatabaseHandler.insertShopCategory(lCat);
                        Log.i("Category List",lCat.toString());
                    }

                    long lCurrentTime=CommonFunctions.getCurrentTimeStamp();
                    lDatabaseHandler.updateLastUpdatedShopCategoryTime(lCurrentTime);
                }
                catch(Exception e){

                }
            }

            @Override
            public void onServiceExecutedFailed(String message) {
               isLoading=false;
            }
        });
    }

}
