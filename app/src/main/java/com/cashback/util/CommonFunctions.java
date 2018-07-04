package com.cashback.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import com.cash.cashback.AppController;
import com.cashback.R;
import com.cashback.data.PaymentModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jack on 16/03/2017.
 */

public class CommonFunctions {
   // public final static String host="http://103.205.244.12/whitecashback/";
    //public static String hostName="https://www.whitecashback.in/";
    //public final static String mUrl="http://103.205.244.12/whitecashback/api/webservices_pdo.php/";
    //public static String cloudUrl="https://res.cloudinary.com/whitecashback";
    //public static String cloudPath=cloudUrl+"/image/upload/logo/";
    //public static String referal_url=host+"public/images/referral/whitecashback_refferal.jpg";
    //public static String banner_image=hostName;
    //public static String sliderPath=hostName+"images/slider/b";
    public final static String host="https://www.whitecashback.in/";
    public static String hostName=host;
    public final static String mUrl=host+"api/webservices_ios.php/";
    public static String banner_image="https://www.whitecashback.in/";//host;
    public static String cloudPath="https://res.cloudinary.com/whitecashback/image/upload/logo/";
    public static String sliderPath=hostName+"images/slider/b";
    public static String referal_url=host+"public/images/referral/whitecashback_refferal.jpg";
    public static String notificationPath=host+"img/";
    public static String howItWorkPath=host+"public/images/howitworks/";
    public static String sShopUrl="";
    private static boolean sIsInternetOn=false;
    public static ArrayList<PaymentModel> sPaymentDetailList=new ArrayList<>();

   public static final boolean isInternetOn(Context context) {
        ConnectivityManager connec = (ConnectivityManager) AppController.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public static void showAlert(Context mContext, String message, String title) {
        // Create custom dialog object
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.show_store_detail);
        dialog.setCancelable(true);
        dialog.show();
        TextView mTextViewDetail = (TextView)dialog.findViewById(R.id.storeDetail);
        mTextViewDetail.setText(CommonFunctions.fromHtml(message));
        TextView mTitle=(TextView)dialog.findViewById(R.id.storeTitle);
        mTitle.setText(title);
        //mTitle.setText(title);
        TextView mOk = (TextView) dialog.findViewById(R.id.ok);
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    @NonNull
    public static Spanned fromHtml(@NonNull String html) {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N){
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            //noinspection deprecation
            return Html.fromHtml(html);
        }
    }


    public static String getFormattedDate(String format,long timestamp){

        String lStringFromat=null;

        SimpleDateFormat simpleDateFormat= new SimpleDateFormat(format, Locale.getDefault());
        lStringFromat=simpleDateFormat.format(new Date(timestamp));

        return lStringFromat;
    }

    public static long getCurrentTimeStamp(){
        long currentTimeStamp=0;
        currentTimeStamp=Calendar.getInstance().getTimeInMillis();
        return currentTimeStamp;
    }

    public static boolean isSecondDay(long currentTimeStamp,long pastTimeStamp){
        boolean isSecondDay=false;
        Date lCurrentDate=null,lPastDate=null;
        String lParsedDate;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        cal.setTimeInMillis(currentTimeStamp);
        lParsedDate = DateFormat.format("dd-MM-yyyy", cal).toString();
        try {
            lCurrentDate = (Date)formatter.parse(lParsedDate);
            lCurrentDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.setTimeInMillis(pastTimeStamp);
        lParsedDate=DateFormat.format("dd-MM-yyyy",cal).toString();
        try {
            lPastDate=(Date)formatter.parse(lParsedDate);
            lPastDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(lCurrentDate.toString().equalsIgnoreCase(lPastDate.toString())){
            isSecondDay=false;
        }
        else{
            isSecondDay=true;
        }
        return isSecondDay;
    }


}
