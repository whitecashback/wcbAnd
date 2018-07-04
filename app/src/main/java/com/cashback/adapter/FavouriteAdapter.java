package com.cashback.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.cashback.data.Store;
import com.cashback.data.WebviewModel;
import com.cashback.fragment.AllStoreFragment;
import com.cashback.fragment.FavouriteFragment;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;
import com.cashback.util.ExecuteServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jack on 15-03-2017.
 */
public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {

    private Activity activity;
    private LayoutInflater inflater;
    private Context context;
    static Activity act;
    ArrayList<Store> feedItems;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="";
    public FavouriteAdapter(Activity activity,ArrayList<Store> feedItems) {
        this.activity = activity;
        act=activity;
        this.feedItems=feedItems;
        prefs= PreferenceManager.getDefaultSharedPreferences(activity);
        prefEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
    }

    public FavouriteAdapter() {

    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        protected AppCompatImageView mStoreImage,mShareImage,mLikeBtn;
        protected TextView mStoreName, mStoreTxt, mPercentCashback, mAddToFav, mGoToStore, mText;
        protected CardView mCardView;
        public MyViewHolder(View view) {
            super(view);
            mCardView=(CardView)view.findViewById(R.id.favCard);
            mStoreImage = (AppCompatImageView) view.findViewById(R.id.storeImage);
            mShareImage=(AppCompatImageView) view.findViewById(R.id.shareIcon);
            mLikeBtn=(AppCompatImageView) view.findViewById(R.id.favIcon);
            mPercentCashback = (TextView) view.findViewById(R.id.cashbackPercent);

            mShareImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = " "+ FavouriteFragment.shareDataList.get(getAdapterPosition());
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    activity.startActivity(Intent.createChooser(sharingIntent, "Share app via"));
                }
            });

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AllStoreFragment.sStoreDataList=feedItems.get(getAdapterPosition());
                    MainActivity.sWebviewData=new WebviewModel();
                    MainActivity.sWebviewData.setStatus(feedItems.get(getAdapterPosition()).status);
                    MainActivity.sWebviewData.setStore_id(feedItems.get(getAdapterPosition()).store_id);
                    MainActivity.sWebviewData.setRetailerName(feedItems.get(getAdapterPosition()).name);
                    MainActivity.sWebviewData.setCashback(feedItems.get(getAdapterPosition()).cashback);
                    MainActivity.sWebviewData.setUrl(feedItems.get(getAdapterPosition()).url);
                    userId=prefs.getString("USER_ID","102");
                    if(userId.equalsIgnoreCase("102")){
                        MainActivity.sComingFrom="FavFragment";
                        activity.startActivity(new Intent(activity, MLoginActivity.class));

                    }
                    else {
                        activity.startActivity(new Intent(activity, WebviewActivity.class));
                    }
                }
            });


            mLikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userId=prefs.getString("USER_ID","102");
                    if(userId.equalsIgnoreCase("102")){
                        MainActivity.sComingFrom="FavUnselected";
                        activity.startActivity(new Intent(activity, MLoginActivity.class));

                    }
                    else
                    {
                        if (CommonFunctions.isInternetOn(activity)) {

                            Cursor cs= DatabaseHandler.getInstance(activity).checkSQLiteData(feedItems.get(getAdapterPosition()).store_id);
                            if(cs.getCount()>0)
                            {
                                DatabaseHandler.getInstance(activity).updateRecentFav(feedItems.get(getAdapterPosition()).store_id,"0");
                            }
                            RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("user_id", userId)
                                    .addFormDataPart("retailer_id", feedItems.get(getAdapterPosition()).store_id)
                                    .addFormDataPart("flag", "0")
                                    .build();
                            int pos=getAdapterPosition();
                            feedItems.remove(pos);
                            notifyItemRemoved(getAdapterPosition());
                              MainActivity.showMessageDialog("Store removed from your wish lists.","#f2dede","#a94442");
                            removeFromFavourite(CommonFunctions.mUrl+"addToFavourite",requestBody);
                        }
                        else
                        {
                            MainActivity.showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
                        }
                    }
                }
            });

        }
    }

    @Override
    public FavouriteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourite_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavouriteAdapter.MyViewHolder holder, int position) {
        Glide.with(activity).load("https://res.cloudinary.com/whitecashback/image/upload/logo/"+feedItems.get(position).image)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mStoreImage);

        if(feedItems.get(position).cashback.equalsIgnoreCase("inactive"))
        {
            holder.mPercentCashback.setText("No Cash Back");

        }
        else {
            if (feedItems.get(position).cashback.length() > 0) {
                if(feedItems.get(position).cashback.contains("%")) {
                    holder.mPercentCashback.setText(feedItems.get(position).cashback + " Cash Back");
                }
                else
                {
                    holder.mPercentCashback.setText("Upto "+feedItems.get(position).cashback + " Cash Back");
                }
            } else {
                holder.mPercentCashback.setText("Best Offer");

            }
        }
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void shareMethod() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Share text is dummy";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        activity.startActivity(Intent.createChooser(sharingIntent, "Share app via"));

    }

    private void removeFromFavourite(String url, RequestBody requestBody) {

        new ExecuteServices().executePost(url, new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                if(activity==null)
                {
                    return;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onServiceExecutedFailed(String msg) {

            }
        },requestBody);
    }
}
