package com.cashback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cash.cashback.adapters.HowItWorksViewPagerAdapter;
import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dev on 21/3/17.
 */

public class How_ItWorksActivity extends Activity
{


    ImageView imgview1, imgview2, imgview3;
    View viewLine1, viewLine2;
    TextView txt1, txt2, txt3,mMessageView,mTextImage;
    ArrayList<String> sImagesList,sDataList;
    HowItWorksViewPagerAdapter adapter;
    AppCompatImageView mVideoView;
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_it_works);
        sImagesList=new ArrayList<>();
        sDataList=new ArrayList<>();

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        imgview1 = (ImageView) findViewById(R.id.imgview1);
        imgview2 = (ImageView) findViewById(R.id.imgview2);
        imgview3 = (ImageView) findViewById(R.id.imgview3);
        viewLine1 = findViewById(R.id.viewLine1);
        viewLine2 = findViewById(R.id.viewLine2);
        mTextImage=(TextView)findViewById(R.id.textImage);
        mVideoView=(AppCompatImageView)findViewById(R.id.videoSegment);
        ArrayList<Integer> vp_img = new ArrayList<>();
        vp_img.add(R.drawable.h1);
        vp_img.add(R.drawable.h2);
        vp_img.add(R.drawable.h3);
        ViewPager vp_how_it_works = (ViewPager) findViewById(R.id.vp_how_it_works);
        adapter = new HowItWorksViewPagerAdapter(vp_img, How_ItWorksActivity.this);
        vp_how_it_works.setAdapter(adapter);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;

      /*  if(CommonFunctions.isInternetOn(How_ItWorksActivity.this))
        {
            getHowItWorks();
        }
        else
        {
            showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
        }*/

        vp_how_it_works.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                if (position==0)
                {
                    imgview1.setBackgroundResource(R.drawable.selected);
                    imgview2.setBackgroundResource(R.drawable.non_selected);
                    imgview3.setBackgroundResource(R.drawable.non_selected);
                    viewLine1.setBackgroundColor(Color.parseColor("#a9eacc"));
                    viewLine2.setBackgroundColor(Color.parseColor("#a9eacc"));
                    txt1.setTextColor(Color.parseColor("#000000"));
                    txt2.setTextColor(Color.parseColor("#cccccc"));
                    txt3.setTextColor(Color.parseColor("#cccccc"));
                }
                else if (position==1)
                {
                    imgview1.setBackgroundResource(R.drawable.selected);
                    imgview2.setBackgroundResource(R.drawable.selected);
                    imgview3.setBackgroundResource(R.drawable.non_selected);
                    viewLine1.setBackgroundColor(Color.parseColor("#0ea45e"));
                    viewLine2.setBackgroundColor(Color.parseColor("#a9eacc"));
                    txt1.setTextColor(Color.parseColor("#000000"));
                    txt2.setTextColor(Color.parseColor("#000000"));
                    txt3.setTextColor(Color.parseColor("#cccccc"));
                }
                else
                {
                    imgview1.setBackgroundResource(R.drawable.selected);
                    imgview2.setBackgroundResource(R.drawable.selected);
                    imgview3.setBackgroundResource(R.drawable.selected);
                    viewLine1.setBackgroundColor(Color.parseColor("#0ea45e"));
                    viewLine2.setBackgroundColor(Color.parseColor("#0ea45e"));
                    txt1.setTextColor(Color.parseColor("#000000"));
                    txt2.setTextColor(Color.parseColor("#000000"));
                    txt3.setTextColor(Color.parseColor("#000000"));
                }
              /*  mTextImage.setText(""+sDataList.get(position));
                if (position==0)
                {
                    imgview1.setBackgroundResource(R.drawable.selected);
                    imgview2.setBackgroundResource(R.drawable.non_selected);
                    imgview3.setBackgroundResource(R.drawable.non_selected);
                    viewLine1.setBackgroundColor(Color.parseColor("#a9eacc"));
                    viewLine2.setBackgroundColor(Color.parseColor("#a9eacc"));
                    txt1.setTextColor(Color.parseColor("#000000"));
                    txt2.setTextColor(Color.parseColor("#cccccc"));
                    txt3.setTextColor(Color.parseColor("#cccccc"));
                }
                else if (position==1)
                {
                    imgview1.setBackgroundResource(R.drawable.selected);
                    imgview2.setBackgroundResource(R.drawable.selected);
                    imgview3.setBackgroundResource(R.drawable.non_selected);
                    viewLine1.setBackgroundColor(Color.parseColor("#0ea45e"));
                    viewLine2.setBackgroundColor(Color.parseColor("#a9eacc"));
                    txt1.setTextColor(Color.parseColor("#000000"));
                    txt2.setTextColor(Color.parseColor("#000000"));
                    txt3.setTextColor(Color.parseColor("#cccccc"));
                }
                else
                {
                    imgview1.setBackgroundResource(R.drawable.selected);
                    imgview2.setBackgroundResource(R.drawable.selected);
                    imgview3.setBackgroundResource(R.drawable.selected);
                    viewLine1.setBackgroundColor(Color.parseColor("#0ea45e"));
                    viewLine2.setBackgroundColor(Color.parseColor("#0ea45e"));
                    txt1.setTextColor(Color.parseColor("#000000"));
                    txt2.setTextColor(Color.parseColor("#000000"));
                    txt3.setTextColor(Color.parseColor("#000000"));
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        ((LinearLayout)findViewById(R.id.backFromHowItWorks)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(How_ItWorksActivity.this,VideoViewActivity.class));
            }
        });

    }

    private void getHowItWorks() {

        new ExecuteServices().execute(CommonFunctions.mUrl+"getHowItImages", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(response);
                            JSONObject obj=new JSONObject(response);
                            JSONArray arr=obj.getJSONArray("images");
                            JSONArray dta=obj.getJSONArray("text");
                            sImagesList.clear();
                            sDataList.clear();
                            for(int i=0;i<arr.length();i++)
                            {
                                sImagesList.add(arr.getString(i));
                                sDataList.add(dta.getString(i));
                                System.out.println(arr.getString(i));
                            }
                            mTextImage.setText(sDataList.get(0));
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }

            @Override
            public void onServiceExecutedFailed(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMessageDialog("Oops!! something went wrong.","#fcf8e3","#8a6d3b");
                    }
                });
            }
        });

    }


    public void showMessageDialog(String message,String color,String textColor)
    {
        if(mMessageView.getVisibility()==View.VISIBLE)
        {
            mMessageView.setVisibility(View.GONE);
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mMessageView.setBackgroundColor(Color.parseColor(color));
        mMessageView.setText(message);
        mMessageView.setTextColor(Color.parseColor(textColor));
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.startAnimation(slideDownAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageView.startAnimation(slideUpAnimation);
                mMessageView.setVisibility(View.GONE);
                if(exitActivity) {
                    activity.finish();
                }
                exitActivity=false;
            }
        },4000);
    }
}
