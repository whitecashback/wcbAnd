package com.cashback.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cashback.MainActivity;
import com.cashback.R;
import com.cashback.util.CommonFunctions;

/**
 * Created by jack on 14-03-2017.
 */

public class InviteFragment extends Fragment
{
    CardView mCopyCardView,mShareCardView;
    View v;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    TextView mReferralLink;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.invite_layout,container,false);
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        mShareCardView=(CardView)v.findViewById(R.id.shareCard);
        mCopyCardView=(CardView)v.findViewById(R.id.copyCard);
        mReferralLink=(TextView)v.findViewById(R.id.referralLink);
        mReferralLink.setText(CommonFunctions.fromHtml("https://www.whitecashback.in/signUp?ref="+userId));
        ImageView image=(ImageView)v.findViewById(R.id.shareImg);
        Glide.with(getActivity()).load(CommonFunctions.referal_url)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);
        myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        mShareCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMethod();
            }
        });

        mCopyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyMethod();
            }
        });

        return v;
    }


    public void copyMethod() {
        String copyURL;
        copyURL = mReferralLink.getText().toString();
        myClip = ClipData.newPlainText("text", copyURL+"\n\nYour referral id is "+userId);
        myClipboard.setPrimaryClip(myClip);
        MainActivity.showMessageDialog("Your link has been copied","#dff0d8","#3c763d");
    }

    public void shareMethod() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hey, \n" +
                "I downloaded this WhiteCashBack App and I thought you would be interested " +
                "in downloading it too. Check it out! \n\n" +
                "http://tinyurl.com/oomzz8f"+"\nYour referral id is "+userId;
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share app via"));

    }
}
