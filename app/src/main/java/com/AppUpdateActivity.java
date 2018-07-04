package com;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cashback.EditAccountDetail;
import com.cashback.R;
import com.cashback.util.CommonFunctions;

import java.util.regex.Pattern;


/**
 * Created by jack on 28-02-2017.
 */

public class AppUpdateActivity extends Activity
{
    public final static String APP_PNAME = "com.cash.cashback";
    private Dialog dialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_app);
        dialog = new Dialog(AppUpdateActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.account_update_dialog);
        dialog.setCancelable(false);
        dialog.show();
        TextView acc=(TextView)dialog.findViewById(R.id.account);
        acc.setText("Please update the latest version to get new features");
        TextView mOkBtn=(TextView)dialog.findViewById(R.id.update);
        mOkBtn.setText("UPDATE");
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                finish();
                dialog.cancel();
            }
        });

        TextView mNoBtn=(TextView)dialog.findViewById(R.id.noThanks);
        mNoBtn.setText("NO THANKS");
        mNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(dialog.isShowing()) {
            dialog.cancel();
            finish();
        }
    }
}
