package com.cashback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;

import org.json.JSONObject;

/**
 * Created by dev on 21/3/17.
 */

public class Conact_UsActivity extends Activity
{

    TextView mAddressText,mPhnText,mMailTxt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        LinearLayout call_layout = (LinearLayout) findViewById(R.id.call_layout);
        LinearLayout mail_layout = (LinearLayout) findViewById(R.id.mail_layout);
        mAddressText=(TextView)findViewById(R.id.address);
        mPhnText=(TextView)findViewById(R.id.phone);
        mMailTxt=(TextView)findViewById(R.id.email);

        mAddressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(Conact_UsActivity.this,ChatWidUsActivty.class));
            }
        });

        mAddressText.setText("");
        mPhnText.setText("");
        mMailTxt.setText("");  //contactus

         new ExecuteServices().execute(CommonFunctions.mUrl + "contactus", new ExecuteServices.OnServiceExecute() {
             @Override
             public void onServiceExecutedResponse(final String response) {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         parseJsonFeed(response);
                     }
                 });
             }

             @Override
             public void onServiceExecutedFailed(String message) {

             }
         });


        ((LinearLayout)findViewById(R.id.backFromContact)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void parseJsonFeed(String response) {
        try
        {
            JSONObject object=new JSONObject(response);
            String status=object.getString("status");
            if(status.equalsIgnoreCase("1"))
            {
                JSONObject obj=object.getJSONObject("data");
                mAddressText.setText(obj.getString("address"));
                mPhnText.setText(obj.getString("mobile"));
                mMailTxt.setText(obj.getString("email"));
            }
            else
            {
            }
        }
        catch (Exception e)
        {
        }
    }
}
