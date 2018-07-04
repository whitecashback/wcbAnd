package com.cashback.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import com.cash.cashback.AppController;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jack on 18/10/2016.
 */

public class ExecuteServices {
    OnServiceExecute mOnServiceExecute;

    public ExecuteServices(){

    }
  public  interface OnServiceExecute{
      void onServiceExecutedResponse(String response);
      void onServiceExecutedFailed(String message);
    }

    public boolean isConnected(@NonNull Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
      //  if(!isConnected)
       //     Toast.makeText(AppContext.getContext(), "No network connection available", Toast.LENGTH_SHORT).show();
        return isConnected;
    }

    public void execute(@NonNull String url, @NonNull final OnServiceExecute mOnServiceExecute)  {
        if(!isConnected(AppController.getAppContext())){
            mOnServiceExecute.onServiceExecutedFailed("No network connection available");
            return;

        }
        System.out.println("xxxurl "+url);
        this.mOnServiceExecute=mOnServiceExecute;
        Request request = new Request.Builder()
                .url(url)
                .build();

        AppController.httpClient.newCall(request)
                .enqueue(new Callback() {
                             @Override
                             public void onFailure(Call call, IOException e) {
                                 if(mOnServiceExecute!=null)
                                 mOnServiceExecute.onServiceExecutedFailed("Error");
                             }

                             @Override
                             public void onResponse(Call call, @NonNull Response response)  {
                                 String res = null;
                                 try {
                                     res = response.body().string();
                                     if(mOnServiceExecute!=null)
                                     mOnServiceExecute.onServiceExecutedResponse(res);
                                 } catch (IOException e) {
                                     if(mOnServiceExecute!=null)
                                     mOnServiceExecute.onServiceExecutedFailed("Error");
                                     e.printStackTrace();
                                 }

                             }
                         }
                );}
    public void executePost(@NonNull String url, @NonNull final OnServiceExecute mOnServiceExecute, RequestBody params)  {
        if(!isConnected(AppController.getAppContext())){
            mOnServiceExecute.onServiceExecutedFailed("Check Network connection");
            return;

        }
      /*  RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)

                .addFormDataPart("name", name_)
                .addFormDataPart("email", email_)
                .addFormDataPart("id", id)
                .addFormDataPart("contact", contact__)
                .addFormDataPart("dob", birthdate)
                .addFormDataPart("blood_group", "" + bloodgroup_)
                .addFormDataPart("last_donation", "" + lastDateOfDonation)
                .addFormDataPart("donation_count", noOfDonation)
                .build();*/
        this.mOnServiceExecute=mOnServiceExecute;
        Request request = new Request.Builder()
                .url(url)
                .post(params)
                .build();
        /*OkHttpClient httpClient=new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build();
*/
        AppController.httpClient.newCall(request)
                .enqueue(new Callback() {
                             @Override
                             public void onFailure(Call call, IOException e) {
                                 if(mOnServiceExecute!=null)
                                     mOnServiceExecute.onServiceExecutedFailed("Error");
                             }

                             @Override
                             public void onResponse(Call call, @NonNull Response response)  {
                                 String res = null;
                                 try {
                                     res = response.body().string();
                                     if(mOnServiceExecute!=null)
                                         mOnServiceExecute.onServiceExecutedResponse(res);
                                 } catch (IOException e) {
                                     if(mOnServiceExecute!=null)
                                         mOnServiceExecute.onServiceExecutedFailed("Error");
                                     e.printStackTrace();
                                 }

                             }
                         }
                );}
    public void executePost2(@NonNull String url, @NonNull final OnServiceExecute mOnServiceExecute, RequestBody params)  {
        if(!isConnected(AppController.getAppContext())){
            mOnServiceExecute.onServiceExecutedFailed("Check Network connection");
            return;

        }
      /*  RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)

                .addFormDataPart("name", name_)
                .addFormDataPart("email", email_)
                .addFormDataPart("id", id)
                .addFormDataPart("contact", contact__)
                .addFormDataPart("dob", birthdate)
                .addFormDataPart("blood_group", "" + bloodgroup_)
                .addFormDataPart("last_donation", "" + lastDateOfDonation)
                .addFormDataPart("donation_count", noOfDonation)
                .build();*/
        this.mOnServiceExecute=mOnServiceExecute;
        Request request = new Request.Builder()
                .url(url)
                .post(params)
                .build();
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build();
        httpClient.newCall(request)
                .enqueue(new Callback() {
                             @Override
                             public void onFailure(Call call, IOException e) {
                                 if(mOnServiceExecute!=null)
                                 mOnServiceExecute.onServiceExecutedFailed("Error");
                             }

                             @Override
                             public void onResponse(Call call, @NonNull Response response)  {
                                 String res = null;
                                 try {
                                     res = response.body().string();
                                     if(mOnServiceExecute!=null)
                                     mOnServiceExecute.onServiceExecutedResponse(res);
                                 } catch (IOException e) {
                                     if(mOnServiceExecute!=null)
                                     mOnServiceExecute.onServiceExecutedFailed("Error");
                                     e.printStackTrace();
                                 }

                             }
                         }
                );}

    public void uploadImage(@NonNull String url, @NonNull final OnServiceExecute mOnServiceExecute, RequestBody params)  {
        if(!isConnected(AppController.getAppContext())){
            mOnServiceExecute.onServiceExecutedFailed("No network connection");
            return;
        }

        this.mOnServiceExecute=mOnServiceExecute;
        Request request = new Request.Builder()
                .url(url)
                .post(params)
                .build();

        AppController.httpClient.newCall(request)
                .enqueue(new Callback() {
                             @Override
                             public void onFailure(Call call, IOException e) {

                                 mOnServiceExecute.onServiceExecutedFailed("Error");
                             }

                             @Override
                             public void onResponse(Call call, @NonNull Response response)  {
                                 String res = null;
                                 try {
                                     res = response.body().string();
                                     mOnServiceExecute.onServiceExecutedResponse(res);
                                 } catch (IOException e) {
                                     mOnServiceExecute.onServiceExecutedFailed("Error");
                                     e.printStackTrace();
                                 }

                             }
                         }
                );}

    @NonNull
    static SimpleDateFormat smp=new SimpleDateFormat("MMMM dd, hh:mm a");
  /*  public static class MsgList implements Comparator<FriendListObj> {


        @Override
        public int compare(FriendListObj lhs, FriendListObj rhs) {

            try {
                Calendar cl=Calendar.getInstance();
                //  Date dt2=cl.getTime();
                cl.add(Calendar.DATE,4);
                Date dt=smp.parse(lhs.last_login);
                Date dt2=smp.parse(rhs.last_login);
                System.out.println("dtttt "+dt2+" "+dt2);
                return dt2.compareTo(dt);//dt>dt2?-1:0;
            } catch (Exception e) {
                System.out.println("dttttxxx "+e.getLocalizedMessage());
            }
            return -1;

        }*/
    //}
/*
    void getFavList(){
        ProgressDialog pd;
        pd=new ProgressDialog(this);
        pd.setMessage("Wait");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new ExecuteServices().execute(CommonFields.webserver_url, new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj=new JSONObject(response);
                            if(obj.getInt("status")==1){

                            }
                            else{
                                Toast.makeText(GetFavList.this,obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(GetFavList.this,"Error in response", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onServiceExecutedFailed(final String message) {
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GetFavList.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }*/
}
