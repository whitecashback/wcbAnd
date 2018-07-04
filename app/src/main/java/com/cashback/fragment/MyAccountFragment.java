package com.cashback.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cashback.ChangePasswordActivity;
import com.cashback.EditAccountDetail;
import com.cashback.MainActivity;
import com.cashback.MissingCashbackActivity;
import com.cashback.MyCashBackScreen;
import com.cashback.PaymentActivity;
import com.cashback.ProfileActivity;
import com.cashback.R;

/**
 * Created by jack on 14-03-2017.
 */

public class MyAccountFragment extends Fragment
{

View v;
    TextView addAcount,myEarning,missingCashback,myProfile,logout,changePassword;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    String userId="",loginFrom="0";
    public static int logoutFlag=0;
    boolean checkActivity(){
        if(isAdded() && getActivity()!=null )
            return  true;
        else
            return  false;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.my_account_xml,container,false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefsEditor = prefs.edit();
        userId=prefs.getString("USER_ID","102");
        loginFrom=prefs.getString("LOGIN_FROM","0");
        addAcount=(TextView)v.findViewById(R.id.addAccount);
        myEarning=(TextView)v.findViewById(R.id.aboutUs);
        missingCashback=(TextView)v.findViewById(R.id.itWorks);
        myProfile=(TextView)v.findViewById(R.id.faq);
        logout=(TextView)v.findViewById(R.id.help);
        changePassword=(TextView)v.findViewById(R.id.changePassword);
         if(loginFrom.equalsIgnoreCase("G")||loginFrom.equalsIgnoreCase("F"))
         {
             changePassword.setVisibility(View.GONE);
         }
         else
         {
             changePassword.setVisibility(View.VISIBLE);

         }


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));

            }
        });


        addAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentActivity.sPSelected="3";
                startActivity(new Intent(getActivity(), EditAccountDetail.class));
            }
        });

        myEarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), MyCashBackScreen.class));
            }
        });

        missingCashback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MissingCashbackActivity.class));

            }
        });

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsEditor.putString("LOGIN_FLAG", "NO");
                prefsEditor.putString("USER_ID", "102");
                prefsEditor.putString("NAME", "");
                prefsEditor.putString("REG_ID_FLAG", "");
                prefsEditor.commit();
                logoutFlag=1;
                Intent intent1 = new Intent(getActivity(), MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                getActivity().finish();
            }
        });


        return v;
    }


}
