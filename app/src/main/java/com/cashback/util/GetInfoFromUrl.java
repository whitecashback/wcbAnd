package com.cashback.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ClipDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cashback.WebviewActivity;


/* this class is defined for showing web url on web view using custom web view client */
public class GetInfoFromUrl {
    ClipDrawable drawable;
    static Context mContext;
    static WebView mBrowser;
    private static final String TAG = "WebviewActivity";
    private ConnectionTimeoutHandler timeoutHandler = null;
    private static int PAGE_LOAD_PROGRESS = 0;
    public static final String KEY_REQUESTED_URL = "requested_url";
    public static final String CALLBACK_URL = "success";
    public static ProgressBar pBar;
    public static void initComponents() {
        WebSettings webSettings = mBrowser.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    public void addListener() {
        mBrowser.setWebViewClient(new MyWebViewClient());
        mBrowser.setWebChromeClient(new MyWebChromeClient());
    }

    //Custom web view client
    public class MyWebViewClient extends WebViewClient {
        @SuppressWarnings("static-access")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
          //  Log.i(TAG, "Loading url : " + url);
            if (url.contains(CALLBACK_URL)) {
             //   Log.i(TAG, "Callback url matched... Handling the result");
                Intent intent = new Intent();
                Uri uri = Uri.parse(url);
                intent.setData(uri);
                ((Activity)mContext).setResult(((Activity)mContext).RESULT_OK, intent);
                ((Activity)mContext).finish();
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            timeoutHandler = new ConnectionTimeoutHandler(mContext, view);
            timeoutHandler.execute();
            super.onPageStarted(view, url, favicon);
        }

        @SuppressWarnings("static-access")
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pBar.setVisibility(View.GONE);
            if (timeoutHandler != null)
                timeoutHandler.cancel(true);
            // Do all your result processing here
            if (url.contains(CALLBACK_URL)) {
               // Log.i(TAG, "Callback url matched... Handle result");
                Intent mIntent = new Intent();
                Uri uri = Uri.parse(url);
                mIntent.setData(uri);
                ((Activity)mContext).setResult(((Activity)mContext).RESULT_OK, mIntent);
                ((Activity)mContext).finish();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {

            //Log.i(TAG, "GOT Page error : code : " + errorCode + " Desc : " + description);
            showError(mContext, errorCode);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    //Custom web chrome client
    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            PAGE_LOAD_PROGRESS = newProgress;
            //Log.i(TAG, "Page progress [" + PAGE_LOAD_PROGRESS + "%]");
            super.onProgressChanged(view, newProgress);
        }
    }

    private void showError(final Context mContexts, int errorCode) {
        //Prepare message
        String message = null;
        String title = null;
        if (errorCode == WebViewClient.ERROR_AUTHENTICATION) {
            message = "User authentication failed on server";
            title = "Auth Error";
        } else if (errorCode == WebViewClient.ERROR_TIMEOUT) {
            message = "The server is taking too much time to communicate. Try again later.";
            title = "Connection Timeout";
        } else if (errorCode == WebViewClient.ERROR_TOO_MANY_REQUESTS) {
            message = "Too many requests during this load";
            title = "Too Many Requests";
        } else if (errorCode == WebViewClient.ERROR_UNKNOWN) {
            message = "Generic error";
            title = "Unknown Error";
        } else if (errorCode == WebViewClient.ERROR_BAD_URL) {
            message = "Check entered URL..";
            title = "Malformed URL";
        } else if (errorCode == WebViewClient.ERROR_CONNECT) {
            message = "Failed to connect to the server";
            title = "Connection";
        } else if (errorCode == WebViewClient.ERROR_FAILED_SSL_HANDSHAKE) {
            message = "Failed to perform SSL handshake";
            title = "SSL Handshake Failed";
        } else if (errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
            message = "Server or proxy hostname lookup failed";
            title = "Host Lookup Error";
        } else if (errorCode == WebViewClient.ERROR_PROXY_AUTHENTICATION)
        {
            message = "User authentication failed on proxy";
            title = "Proxy Auth Error";
        } else if (errorCode == WebViewClient.ERROR_REDIRECT_LOOP) {
            message = "Too many redirects";
            title = "Redirect Loop Error";
        } else if (errorCode == WebViewClient.ERROR_UNSUPPORTED_AUTH_SCHEME) {
            message = "Unsupported authentication scheme (not basic or digest)";
            title = "Auth Scheme Error";
        } else if (errorCode == WebViewClient.ERROR_UNSUPPORTED_SCHEME) {
            message = "Unsupported URI scheme";
            title = "URI Scheme Error";
        } else if (errorCode == WebViewClient.ERROR_FILE) {
            message = "Generic file error";
            title = "File";
        } else if (errorCode == WebViewClient.ERROR_FILE_NOT_FOUND) {
            message = "File not found";
            title = "File";
        } else if (errorCode == WebViewClient.ERROR_IO) {
            message = "The server failed to communicate. Try again later.";
            title = "IO Error";
        }

        if (message != null)
        {
            new AlertDialog.Builder(mContext)
                    .setMessage(message)
                    .setTitle(title)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @SuppressWarnings("static-access")
                                public void onClick(DialogInterface dialog, int id) {
                                    ((Activity)mContexts).setResult(((Activity)mContexts).RESULT_CANCELED);
                                    //((Activity)mContext).finish();
                                }
                            }).show();
        }
    }

    public class ConnectionTimeoutHandler extends AsyncTask<Void, Void, String> {

        private static final String PAGE_LOADED = "PAGE_LOADED";
        private static final String CONNECTION_TIMEOUT = "CONNECTION_TIMEOUT";
        private static final long CONNECTION_TIMEOUT_UNIT = 6000000L; //1 minute
        private Context mContext = null;
        private WebView webView;
        private Time startTime = new Time();
        private Time currentTime = new Time();
        private Boolean loaded = false;
        public ConnectionTimeoutHandler(Context mContext, WebView webView) {
            this.mContext = mContext;
            this.webView = webView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.startTime.setToNow();
            GetInfoFromUrl.PAGE_LOAD_PROGRESS = 0;
        }

        @Override
        protected void onPostExecute(String result) {
            if (CONNECTION_TIMEOUT.equalsIgnoreCase(result)) {
                showError(this.mContext, WebViewClient.ERROR_TIMEOUT);
                this.webView.stopLoading();
            } else if (PAGE_LOADED.equalsIgnoreCase(result)) {
                //Toast.makeText(this.mContext, "Page load success", Toast.LENGTH_LONG).show();            
            } else {
                //Handle unknown events here
            }
        }
        @Override
        protected String doInBackground(Void... params) {

            while (! loaded) {
                currentTime.setToNow();
                if (GetInfoFromUrl.PAGE_LOAD_PROGRESS != 100
                        && (currentTime.toMillis(true) - startTime.toMillis(true)) > CONNECTION_TIMEOUT_UNIT) {
                    return CONNECTION_TIMEOUT;
                } else if (GetInfoFromUrl.PAGE_LOAD_PROGRESS == 100) {
                    loaded = true;
                }
            }
            return PAGE_LOADED;
        }
    }

    public  void callWebview(final Activity activity, String requestedURL, int requestCode,WebView browser,ProgressBar pBar,final String txt) {
        mContext=activity;
        mBrowser=browser;
        initComponents();
        addListener();
        this.pBar=pBar;

        if (requestedURL != null) {
            pBar.setVisibility(View.GONE);
            mBrowser.setWebChromeClient(new WebChromeClient(){

                public void onProgressChanged(WebView view, int progress) {
                    ((Activity) mContext).setTitle("Loading...");
                    ((Activity) mContext).setProgress(progress * 100);
                            drawable = (ClipDrawable) WebviewActivity.mProgressLine.getDrawable();
                    drawable.setLevel(100 * progress);
                    if(progress == 100) {
                                WebviewActivity.mProgressLine.setVisibility(View.GONE);
                            ((Activity) activity).setTitle("My title");
                    }
                }
            });
            mBrowser.loadUrl(requestedURL);
        } else {
          //  Log.e(TAG, "Can not process ... URL missing.");
        }
    }
}
