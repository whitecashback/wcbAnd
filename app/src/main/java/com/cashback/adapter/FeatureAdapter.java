package com.cashback.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cashback.MLoginActivity;
import com.cashback.MainActivity;
import com.cashback.R;
import com.cashback.StoreDetailPageScreen;
import com.cashback.WebviewActivity;
import com.cashback.data.HotCoupon;
import com.cashback.data.WebviewModel;
import com.cashback.fragment.AllStoreFragment;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.cashback.adapter.FavouriteAdapter.act;

/**
 * Created by jack on 22-04-2016.
 */
public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.MyViewHolder> {
    SimpleDateFormat sdf=new SimpleDateFormat("dd MMM, yyyy");
    SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Activity activity;
    private LayoutInflater inflater;
    private Context context;
    ArrayList<HotCoupon> feedItems;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="";
    private ClipboardManager myClipboard;
    private ClipData myClip;
    public FeatureAdapter(Activity activity, ArrayList<HotCoupon> feedItems) {
        this.activity = activity;
        act=activity;
        this.feedItems=feedItems;
        prefs= PreferenceManager.getDefaultSharedPreferences(activity);
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        myClipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public FeatureAdapter() {

    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mStoreImage,mShareImage;
        protected TextView mStoreName, mStoreTxt, mPercentCashback, mExpDate, mCouponCode,mCouponType;
        protected CardView mCardView;
        public MyViewHolder(View view) {
            super(view);
            mCardView=(CardView)view.findViewById(R.id.card_view);
            mStoreImage = (ImageView) view.findViewById(R.id.storeImage);
            mPercentCashback = (TextView) view.findViewById(R.id.cashbackPercent);
            mStoreTxt = (TextView) view.findViewById(R.id.storeTxt);
            mExpDate= (TextView) view.findViewById(R.id.expDate);
            mCouponCode= (TextView) view.findViewById(R.id.couponCode);
            mCouponType= (TextView) view.findViewById(R.id.couponType);
           /* mShareImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AllStoreFragment.sStoreDataList=feedItems.get(getAdapterPosition());
                    Cursor cs= DatabaseHandler.getInstance(activity).checkSQLiteData(AllStoreFragment.sStoreDataList.store_id);
                    if(cs.getCount()>0) {
                        DatabaseHandler.getInstance(activity).deleteStoreRow(AllStoreFragment.sStoreDataList.store_id);
                        DatabaseHandler.getInstance(activity).insertRecentStores(AllStoreFragment.sStoreDataList);
                    }
                    else
                    {
                        DatabaseHandler.getInstance(activity).insertRecentStores(AllStoreFragment.sStoreDataList);
                    }
                    activity.startActivity(new Intent(activity, StoreDetailPageScreen.class));
                }
            });
*/
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userId=prefs.getString("USER_ID","102");
                    MainActivity.sWebviewData=new WebviewModel();
                    MainActivity.sWebviewData.setStatus("active");
                    MainActivity.sWebviewData.setStore_id(feedItems.get(getAdapterPosition()).getRetailer_id());
                    MainActivity.sWebviewData.setRetailerName(feedItems.get(getAdapterPosition()).getRetailer_title());
                    MainActivity.sWebviewData.setCashback(feedItems.get(getAdapterPosition()).getCashback());
                    MainActivity.sWebviewData.setUrl(feedItems.get(getAdapterPosition()).getLink());
                    myClip = ClipData.newPlainText("text", feedItems.get(getAdapterPosition()).getCode());
                    myClipboard.setPrimaryClip(myClip);
                    MainActivity.showMessageDialog("Coupon code copied","#dff0d8","#3c763d");
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(userId.equalsIgnoreCase("102"))
                    {
                        MainActivity.sComingFrom="HotDeals";

                        activity.startActivity(new Intent(activity , MLoginActivity.class));
                    }
                    else {
                        activity.startActivity(new Intent(activity, WebviewActivity.class));
                    }


                }
            });
        }
    }

    @Override
    public FeatureAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_big, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeatureAdapter.MyViewHolder holder, int position) {
        Glide.with(activity).load(CommonFunctions.cloudPath+feedItems.get(position).getRetailer_image())

                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mStoreImage);
        if ((!TextUtils.isEmpty(feedItems.get(position).getCashback()))) {
            holder.mPercentCashback.setText(CommonFunctions.fromHtml("" + feedItems.get(position).getCashback()));
        }
//        if(feedItems.get(position).getCashback()!=null&&!feedItems.get(position).getCashback().isEmpty()) {
//            if (feedItems.get(position).getCashback().trim().length() > 0 && !feedItems.get(position).getCashback().contains("0%")) {
//                if (feedItems.get(position).getCashback().contains("%")) {
//                    holder.mPercentCashback.setText(CommonFunctions.fromHtml("Upto " + feedItems.get(position).getCashback() + " Cashback"));
//                } else {
//                    holder.mPercentCashback.setText(CommonFunctions.fromHtml("Upto " + feedItems.get(position).getCashback() + " Cashback"));
//                }
//            }
//        }
//        else
//        {
//            holder.mPercentCashback.setText("BEST OFFER");
//        }
        holder.mCouponType.setText("Coupon");
        holder.mStoreTxt.setText(CommonFunctions.fromHtml(feedItems.get(position).getTitle()));
        String dt= feedItems.get(position).getExpiry_date();
//        try {
//            dt = sdf.format(sdf2.parse(feedItems.get(position).getExpiry_date()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        holder.mExpDate.setText("Exp. Date: "+dt);
        holder.mCouponCode.setText(feedItems.get(position).getCode());
        holder.mCouponCode.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
