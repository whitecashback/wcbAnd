package com.cashback.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cashback.data.Cat;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;
import com.cashback.util.ExecuteServices;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by dev on 29/5/17.
 */

public class GetCategoryPathIntentService extends IntentService {

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.cashback.services.action.FOO";
    private static final String ACTION_BAZ = "com.cashback.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.cashback.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.cashback.services.extra.PARAM2";

    private static boolean isLoading=false;

    public GetCategoryPathIntentService() {
        super("GetCategoryPathIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(isLoading){
            return;
        }
        getCategoryPath();
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

    public void getCategoryPath(){
        isLoading=true;

        new ExecuteServices().execute(CommonFunctions.mUrl+"", new ExecuteServices.OnServiceExecute(){

            @Override
            public void onServiceExecutedResponse(String response) {
                isLoading=false;
                try {
                    JSONArray lJsonArray=new JSONObject(response).getJSONArray("data");
                    int length=lJsonArray.length();
                    for(int i=0;i<length;i++){
                        JSONObject obj=lJsonArray.getJSONObject(i);
                        String lCategoryPath=obj.getString("categoryPath");

                        //TODO---Change Server URl and path response and set value in Common Functions
                    }
                }
                catch(Exception e){

                }
            }

            @Override
            public void onServiceExecutedFailed(String message) {

            }
        });

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public static void startActionFoo(Context context, String param1, String param2){
        Intent intent = new Intent(context, AlStoreIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionBaz(Context context, String param1, String param2){
        Intent intent = new Intent(context, AlStoreIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    private void handleActionBaz(String param1, String param2){
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionFoo(String param1, String param2){
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
