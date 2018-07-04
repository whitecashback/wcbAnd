package com.cashback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class CashbackActivity extends AppCompatActivity {

    private WebView mWebViewCashback;
    private String mStringCashbackRates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashback);

        mStringCashbackRates=getIntent().getStringExtra("cashbackrates");
        mWebViewCashback=(WebView)findViewById(R.id.act_cashback_wv_cashbackdetail);
        WebSettings mWebSettings=mWebViewCashback.getSettings();
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebViewCashback.loadDataWithBaseURL(null, mStringCashbackRates, "text/html", "utf-8", null);
    }
}
