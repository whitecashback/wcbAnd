package com.cashback.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cashback.R;
import com.cashback.data.NotificationModel;
import java.util.ArrayList;

/**
 * Created by jack on 15-03-2017.
 */
public class NotificationAdapter extends BaseAdapter
{
    Activity activity;
    ViewHolderClass viewHolder;
    private LayoutInflater inflater;

    ArrayList<NotificationModel> listData;
    public NotificationAdapter(Activity activity, ArrayList<NotificationModel> listData)
    {
        this.activity=activity;
        this.listData=listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.notification_row, null);
            viewHolder=new ViewHolderClass();
            viewHolder.mBackImage=(ImageView)convertView.findViewById(R.id.storeImageCoupon);
            viewHolder.mNotificationTitle=(TextView)convertView.findViewById(R.id.titleNotifications);
            viewHolder.mDate=(TextView)convertView.findViewById(R.id.date);
            viewHolder.mNotifiactionDescription=(TextView)convertView.findViewById(R.id.descNotification);
            viewHolder.mCardNotify=(CardView)convertView.findViewById(R.id.cardNotify);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolderClass)convertView.getTag();
        }

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width/2-50);
        viewHolder.mBackImage.setLayoutParams(layoutParams);
        final ImageView imv= viewHolder.mBackImage;
        Glide.with(activity).load(listData.get(position).get_Image())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imv);
        viewHolder.mNotificationTitle.setText("" + listData.get(position).get_Title().trim());
        viewHolder.mNotifiactionDescription.setVisibility(View.GONE);
        viewHolder.mDate.setText(""+listData.get(position).get_Date());
        return convertView;
    }


    public static class ViewHolderClass
    {
        private TextView mNotificationTitle,mNotifiactionDescription,mDate;
        private ImageView mBackImage;
        private CardView mCardNotify;

    }

}
