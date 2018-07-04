
package com.cash.cashback.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.cashback.R;

import java.util.ArrayList;

public class HowItWorksViewPagerAdapter extends PagerAdapter
{
    ArrayList<Integer> img;
    Context context;
    LayoutInflater inflater = null;

    public HowItWorksViewPagerAdapter(ArrayList<Integer> img, Context context)
    {
        this.img = img;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return img.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View itemView = inflater.inflate(R.layout.how_it_works_image_view, container, false);
        ImageView bacImage=(ImageView)itemView.findViewById(R.id.iv_vpHeader);
        bacImage.setImageResource(img.get(position));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((LinearLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view==object;
    }
}
