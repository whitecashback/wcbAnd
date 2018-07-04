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

import com.cashback.AboutUsActivity;
import com.cashback.Conact_UsActivity;
import com.cashback.FAQ_Activity;
import com.cashback.How_ItWorksActivity;
import com.cashback.MainActivity;
import com.cashback.PrivacyPolicy;
import com.cashback.R;
import com.cashback.SettingActivity;
import com.cashback.TermsAndConditionActivity;
import com.cashback.VideoViewActivity;

/**
 * Created by jack on 14-03-2017.
 */

public class WhitecashbackFragment extends Fragment  {

    View v;
    TextView mAboutUs,mHwItWorks,mFAQ,mHelp,mSetting,mTerms,mPrivacy;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="";
    boolean checkActivity(){
        if(isAdded() && getActivity()!=null )
            return  true;
        else
            return  false;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       v=inflater.inflate(R.layout.white_cashback_xml,container,false);
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        mTerms=(TextView)v.findViewById(R.id.termsCondiotion);
        mPrivacy=(TextView)v.findViewById(R.id.privacy);
       mAboutUs=(TextView)v.findViewById(R.id.aboutUs);
       mHwItWorks=(TextView)v.findViewById(R.id.itWorks);
       mFAQ=(TextView)v.findViewById(R.id.faq);
       mHelp=(TextView)v.findViewById(R.id.help);
       mSetting=(TextView)v.findViewById(R.id.settings);
        if(userId.equalsIgnoreCase("102"))
        {
           mSetting.setVisibility(View.GONE);
        }
        else
        {
            mSetting.setVisibility(View.VISIBLE);

        }


        mTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TermsAndConditionActivity.class));
            }
        });


        mPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PrivacyPolicy.class));
            }
        });


        mAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });

        mHwItWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VideoViewActivity.class));

            }
        });

        mFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FAQ_Activity.class));

            }
        });

        mHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Conact_UsActivity.class));

            }
        });

        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingActivity.class));

            }
        });

        return v;
    }
}
