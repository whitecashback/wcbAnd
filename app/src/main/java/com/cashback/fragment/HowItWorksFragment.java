package com.cashback.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.cash.cashback.adapters.HowItWorksViewPagerAdapter;
import com.cashback.R;

import java.util.ArrayList;

public class HowItWorksFragment extends Fragment
{
    ImageView imgview1, imgview2, imgview3;
    View viewLine1, viewLine2;
    TextView txt1, txt2, txt3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_how_it_works, container, false);
        ArrayList<Integer> vp_img = new ArrayList<>();
        vp_img.add(R.drawable.h1);
        vp_img.add(R.drawable.h2);
        vp_img.add(R.drawable.h3);
        txt1 = (TextView) v.findViewById(R.id.txt1);
        txt2 = (TextView) v.findViewById(R.id.txt2);
        txt3 = (TextView) v.findViewById(R.id.txt3);
        imgview1 = (ImageView) v.findViewById(R.id.imgview1);
        imgview2 = (ImageView) v.findViewById(R.id.imgview2);
        imgview3 = (ImageView) v.findViewById(R.id.imgview3);
        viewLine1 = v.findViewById(R.id.viewLine1);
        viewLine2 = v.findViewById(R.id.viewLine2);
        ViewPager vp_how_it_works = (ViewPager) v.findViewById(R.id.vp_how_it_works);
        HowItWorksViewPagerAdapter adapter = new HowItWorksViewPagerAdapter(vp_img, getActivity());
        vp_how_it_works.setAdapter(adapter);

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
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        return v;
    }

}
