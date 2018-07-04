package com.cashback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by dev on 4/4/17.
 */

public class ChatWidUsActivty extends Activity
{
String data="<!DOCTYPE html>\n" +
        "<html>\n" +
        "<head>\n" +
        "<meta charset=\"UTF-8\">\n" +
        "<meta name=\"viewport\" content=\"width=device-width; user-scalable=0;\" />\n" +
        "<title>My HTML</title>\n" +
        "</head>\n" +
        "<body>\n" +
        "<h1>MyHTML</h1>\n" +
        "<p id=\"mytext\">Hello!</p>\n" +
        "<input type=\"button\" value=\"Say hello\" onClick=\"showAndroidToast('Hello Android!')\" />\n" +
        "<input type=\"button\" value=\"Open Dialog\" onClick=\"openAndroidDialog()\" />\n" +
        "<script type=\"text/javascript\">window.$zopim||(function(d,s){var z=$zopim=function{z._.push},$=z.s=d.createElement(s),e=d.getElementsByTagName(s)[0];z.set=function{z.set._.push};z._=[];z.set._=[];$.async=!0;$.setAttribute(\"charset\",\"utf-8\");$.src=\"//v2.zopim.com/?4479pzBAL6nkCAu3LNuVklLq4ucGTtn9\";z.t=+new Date;$.type=\"text/javascript\";e.parentNode.insertBefore($,e)})(document,\"script\");</script>\n" +
        "\n" +
        "</body>\n" +
        "</html>";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_activity);

        findViewById(R.id.backFromFaq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((TextView)findViewById(R.id.term)).setText("Chat with us");

        WebView myWebView=(WebView)findViewById(R.id.faq_Webview);
        final MyJavaScriptInterface myJavaScriptInterface
                = new MyJavaScriptInterface(this);
      //  myWebView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadData(data, "text/html; charset=UTF-8", null);
    }

    public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        public void showToast(String toast){
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        public void openAndroidDialog(){
            AlertDialog.Builder myDialog
                    = new AlertDialog.Builder(ChatWidUsActivty.this);
            myDialog.setTitle("DANGER!");
            myDialog.setMessage("You can do what you want!");
            myDialog.setPositiveButton("ON", null);
            myDialog.show();
        }

    }
}
