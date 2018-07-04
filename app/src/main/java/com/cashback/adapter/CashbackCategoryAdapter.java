package com.cashback.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashback.R;
import com.cashback.data.CashbackModel;
import com.cashback.data.NotificationModel;

import java.util.ArrayList;

/**
 * Created by priya on 1/25/2018.
 */

public class CashbackCategoryAdapter extends BaseAdapter {

    Activity activity;
    ViewHolderClass viewHolder;
    private LayoutInflater inflater;

    ArrayList<CashbackModel> listData;


    public CashbackCategoryAdapter(Activity activity, ArrayList<CashbackModel> listData) {
        this.activity = activity;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cashback_list, null);
            viewHolder=new ViewHolderClass();
            viewHolder.mCatName=(TextView)convertView.findViewById(R.id.catName);
            viewHolder.mCatValue=(TextView)convertView.findViewById(R.id.catValue);
            viewHolder.mLinearLayout = (LinearLayout)convertView.findViewById(R.id.linear);
             convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolderClass)convertView.getTag();
        }

        viewHolder.mCatName.setText(listData.get(i).getCatName());
        viewHolder.mCatValue.setText(listData.get(i).getCatValue());
        if (i%2==0)
        {
            viewHolder.mLinearLayout.setBackgroundColor(Color.parseColor("#eeeeee"));

        }
        else {
            viewHolder.mLinearLayout.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }

    public static class ViewHolderClass
    {
        private TextView mCatName,mCatValue;
        private LinearLayout  mLinearLayout;

    }
}
