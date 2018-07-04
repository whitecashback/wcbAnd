package com.cashback.util;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by HP on 5/25/2016.
 */
public class HttpRequest {
    public static String excutePost(String targetURL, String urlParameters) {

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("USER_ID", "102");
        String query = builder.build().getEncodedQuery();
        /*



            new Thread() {

                @Override
                public void run() {
                    Looper.prepare();
                    new Handler().post(new Runnable() {

                        @Override
                        public void run() {
                            HttpRequest.test2();//excutePost("http://chatfiesta.herobo.com/tst/getChapters.php","");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        prgDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this,StudentActivity.class));
                    }
                });
                        } });



                    Looper.loop();
                }}.start();
         */


        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
          /*  connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");*/
/*
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));*/
            connection.setRequestProperty("Content-Language", "en-US");

            // connection.setUseCaches (false);
            //          connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = connection.getResponseCode();
            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            System.out.println("ttt " + response.toString());
            return response.toString();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("ttt mal " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("ttt IO " + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
    public static  void test2(){
    OkHttpClient client = new OkHttpClient();

    RequestBody formBody = new FormBody.Builder()
            .add("message", "Your message")
            .build();
    Request request = new Request.Builder()
            .url("http://tu-lmtsm.com/logjson?emailid=mail@gmail.com&password=123")
          //  .post(formBody)
            .build();

    try

    {
        Response response = client.newCall(request).execute();


        String jsonData = response.body().string();
        JSONObject Jobject = new JSONObject(jsonData);
        System.out.println("ttt "+Jobject.toString());
        JSONArray Jarray = Jobject.getJSONArray("employees");

        for (int i = 0; i < Jarray.length(); i++) {
            JSONObject object     = Jarray.getJSONObject(i);
        }
        // Do something with the response.
    }

    catch(
    IOException e
    )

    {
        System.out.println("ttt ***"+e.getLocalizedMessage());
        e.printStackTrace();
    } catch (JSONException e) {
        e.printStackTrace();
    }

    }
  public static  void test(){

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            System.out.println("ttt sttart");
            url = new URL("http://tu-lmtsm.com/logjson?emailid=mail@gmail.com&password=123");//http://chatfiesta.herobo.com/tst/getLessons.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
           // urlConnection.setChunkedStreamingMode(0);
          //  urlConnection.connect();
            /*OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out);*/
          //DataOutputStream wr = new DataOutputStream(
            //        urlConnection.getOutputStream ());
         //  wr.writeBytes ("emailid="+URLEncoder.encode("mail@gmail.com", "UTF-8")+"&password="+URLEncoder.encode("123", "UTF-8"));
      //      wr.flush ();
        //    wr.close ();
            int responseCode = urlConnection.getResponseCode();
            System.out.println("ttt "+responseCode+" ");//urlConnection.getResponseMessage() );
          //  InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStream is = urlConnection.getInputStream();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                    System.out.println(line+"\n");
                }
                is.close();
                System.out.println("ttt "+sb.toString());
            } catch (IOException e) {
                System.out.println("ttt io1 "+e.getLocalizedMessage());
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }



          /*  BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();*/



        } catch (MalformedURLException e) {
            System.out.println("ttt mal "+e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ttt IO "+e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        System.out.println("ttt end");
    }
}