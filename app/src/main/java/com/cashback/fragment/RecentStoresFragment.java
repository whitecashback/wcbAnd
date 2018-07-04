package com.cashback.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cashback.MLoginActivity;
import com.cashback.MainActivity;
import com.cashback.MyCashBackScreen;
import com.cashback.R;
import com.cashback.StoreDetailPageScreen;
import com.cashback.data.Cat;
import com.cashback.data.Store;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;
import com.cashback.util.ExecuteServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by jack on 22-03-2017.
 */
public class RecentStoresFragment extends Fragment
{

    private static final String TAG = "RecentStoreFragment";
    View v;
    public static boolean reload=false;
    RecyclerView recentList;
    StoreListAdapter adapter;
    ArrayList<Store> list;
    LinearLayoutManager mLayoutManager;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.recent_store,container,false);
        recentList = (RecyclerView) v.findViewById(R.id.list);
        list=new ArrayList<>();
        setRetainInstance(true);
        v.findViewById(R.id.progressbar).setVisibility(View.GONE);
       /* v.findViewById(R.id.top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.size()>0)
                {
                    list.clear();
                    DatabaseHandler.getInstance(getActivity()).deleteRecentSTore();
                    adapter.notifyDataSetChanged();
                    v.findViewById(R.id.top).setVisibility(View.GONE);
                    MainActivity.showMessageDialog("Recent history cleared.","#f2dede","#a94442");
                }
            }
        });*/

        return v;
    }


    boolean checkActivity(){
        if(isAdded() && getActivity()!=null )
            return  true;
        else
            return  false;
    }
   /* void loadData(){
    list.clear();
    int rowCount=0;
    Cursor cs=null;
    cs= DatabaseHandler.getInstance(getActivity()).getSQLiteData("recent_stores");
    // cs.moveToFirst();
    cs.moveToLast();
    if(cs!=null&& !cs.isClosed())
    {
        rowCount=cs.getCount();
        Log.d("row count", "" + rowCount);
    }
    if(rowCount!=0)
    {
        // Log.e("Featured Stores Offline data", cs.getString(0) + "\n"+cs.getString(1));
        //CommonFunction.sAllStoresList.clear();
        for(int i=0;i<rowCount;i++)
        {
            Store item = new Store(cs.getString(0),cs.getString(1),cs.getString(5),
                    cs.getString(7),cs.getString(2),cs.getString(6),cs.getString(4),cs.getString(3));
            list.add(item);
            System.out.println(list.size());
            cs.moveToPrevious();
        }
        setUpRecycler();
        v.findViewById(R.id.top).setVisibility(View.VISIBLE);
        recentList.setVisibility(View.VISIBLE);
    }
    else
    {
        v.findViewById(R.id.top).setVisibility(View.GONE);
        recentList.setVisibility(View.GONE);
        v.findViewById(R.id.message).setVisibility(View.VISIBLE);
        //MainActivity.showMessageDialog("There is no recently visited stores.","#f2dede","#a94442");
    }
}*/

    public void loadData(){
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefEditor = prefs.edit();
        userId = prefs.getString("USER_ID", "102");
        if (userId.equals("102")) {
            MainActivity.sComingFrom = "RecentStores";
            Intent lIntentLogin = new Intent(getActivity(), MLoginActivity.class);
            startActivity(lIntentLogin);
        } else {
            getRecentStrores();
        }
    }

    /**
     * Method to setup recycler view and it's component
     */
    public void setUpRecycler() {
        recentList = (RecyclerView) v.findViewById(R.id.list);
        adapter=new StoreListAdapter();
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
        recentList.setLayoutManager(mLayoutManager);
        recentList.setAdapter(adapter);
    }


    void getRecentStrores(){
        v.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        v.findViewById(R.id.list).setVisibility(View.GONE);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .build();
        new ExecuteServices().executePost(CommonFunctions.mUrl + "getRecentStore", new ExecuteServices.OnServiceExecute() {

            @Override
            public void onServiceExecutedResponse(String response) {
                try {

                    JSONArray array=new JSONObject(response).getJSONArray("data");
                    Log.i("Recent Stores",array.toString());
                    int length=array.length();
                    list.clear();
                    for(int i=0;i<length;i++){
                        JSONObject obj=array.getJSONObject(i);
                        list.add(new Store(obj.getString("retailer_id"),obj.getString("title"),obj.getString("cashback"),
                                obj.getString("retailer_status"),obj.getString("url"),obj.getString("image"),obj.getString("description"),obj.getString("is_favourite"),null));

                    }
                    if (!checkActivity())
                    {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUpRecycler();
                            v.findViewById(R.id.progressbar).setVisibility(View.GONE);
                            recentList.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (JSONException e) {

                }
            }

            @Override
            public void onServiceExecutedFailed(String message) {
                if(!checkActivity())
                {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.findViewById(R.id.progressbar).setVisibility(View.GONE);
                        v.findViewById(R.id.list).setVisibility(View.VISIBLE);
                        MainActivity.showMessageDialog("Something went wrong.","#f2dede","#a94442");

                    }
                });
            }
        },requestBody);
    }


    /**
     * RecyclerView.Adapter class to inflate views for {@link #recentList} to display item list of Categories
     */
    class StoreListAdapter extends RecyclerView.Adapter<RecentStoresFragment.StoreListAdapter.ViewHolder> {
        String isFav="0",userId="";
        SharedPreferences prefs;
        SharedPreferences.Editor prefsEditor;

        @Override
        public RecentStoresFragment.StoreListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflate view layout for Adapter's items
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.recent_share_row, parent, false);
            return new RecentStoresFragment.StoreListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecentStoresFragment.StoreListAdapter.ViewHolder holder, int position) {
            //binding values to item views

            if(list.get(position).favourite.equalsIgnoreCase("1"))
            {
                holder.mFavIcon.setImageResource(R.drawable.favorite_black_24dp);
            }
            else
            {
                holder.mFavIcon.setImageResource(R.drawable.favorite_border_black_24dp);
            }

if (!TextUtils.isEmpty(list.get(position).cashback)) {
    holder.cashback.setText("" + list.get(position).cashback);
}
//            if(list.get(position).status.equalsIgnoreCase("inactive")){
//                holder.cashback.setText("Currently No cashback");
//                //  holder.cashback.setTextColor(Color.parseColor("#b2b2b2"));
//                //  holder.cashback.setTextSize(14.0f);
//            }
//
//            else {
//                if (list.get(position).cashback.length() > 0) {
//                    if(list.get(position).cashback.contains("%")) {
//                        holder.cashback.setText("Upto " + list.get(position).cashback+" Cashback");
//                    }
//                    else
//                    {
//                        holder.cashback.setText("Upto " + list.get(position).cashback+" Cashback");
//                    }
//                    //  holder.cashback.setTextColor(Color.parseColor("#F26522"));
//                    //    holder.cashback.setTextSize(16.0f);
//
//                } else {
//                    holder.cashback.setText("Best Offer");
//                    // holder.cashback.setTextColor(Color.parseColor("#00796b"));
//                    holder.cashback.setTextSize(14.0f);
//
//                }
//            }

            Glide.with(getActivity()).load("https://res.cloudinary.com/whitecashback/image/upload/logo/"+list.get(position).image)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mStoreImage);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView item_title, header_title,cashback;

            AppCompatImageView mFavIcon,mStoreImage,mDeleteIcon;
            public ViewHolder(View itemView) {
                super(itemView);
                //initialize item views
                mFavIcon=(AppCompatImageView)itemView.findViewById(R.id.favIcon);
                mStoreImage=(AppCompatImageView)itemView.findViewById(R.id.storeImage);
                mDeleteIcon=(AppCompatImageView)itemView.findViewById(R.id.deleteIcon);

                mDeleteIcon.setVisibility(View.GONE);
                cashback = (TextView) itemView.findViewById(R.id.cashbackPercent);
                itemView.findViewById(R.id.favCard).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AllStoreFragment.sStoreDataList=list.get(getAdapterPosition());
                        getActivity().startActivity(new Intent(getActivity(), StoreDetailPageScreen.class));
                    }
                });

                prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()); //initilize the shared preferences
                prefsEditor=prefs.edit();
                userId=prefs.getString("USER_ID","102");
                mFavIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!CommonFunctions.isInternetOn(getActivity()))
                        {
                            MainActivity.showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
                            return;
                        }


                        userId = prefs.getString("USER_ID", "102");
                        if (userId.equalsIgnoreCase("102")) {
                            MainActivity.sComingFrom="FavUnselected";
                            getActivity().startActivity(new Intent(getActivity(), MLoginActivity.class));

                        } else {
                            isFav = list.get(getAdapterPosition()).favourite;
                            RequestBody requestBody;
                            if (isFav.equalsIgnoreCase("1")) {
                                list.get(getAdapterPosition()).favourite = "0";
                                System.out.println(list.get(getAdapterPosition()).favourite);
                                requestBody = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("user_id", userId)
                                        .addFormDataPart("retailer_id", list.get(getAdapterPosition()).store_id)
                                        .addFormDataPart("flag", "0")
                                        .build();
                                DatabaseHandler.getInstance(getActivity()).updateRecentFav(list.get(getAdapterPosition()).store_id,"0");
                                mFavIcon.setImageResource(R.drawable.favorite_border_black_24dp);
                                MainActivity.showMessageDialog("Store removed from wish list.","#f2dede","#a94442");
                            } else {
                                list.get(getAdapterPosition()).favourite = "1";
                                System.out.println(list.get(getAdapterPosition()).favourite);
                                requestBody = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("user_id", userId)
                                        .addFormDataPart("retailer_id", list.get(getAdapterPosition()).store_id)
                                        .addFormDataPart("flag", "1")
                                        .build();
                                DatabaseHandler.getInstance(getActivity()).updateRecentFav(list.get(getAdapterPosition()).store_id,"1");
                                mFavIcon.setImageResource(R.drawable.favorite_black_24dp);
                                MainActivity.showMessageDialog("Store added to your wish list.","#dff0d8","#3c763d");
                            }

                            addToFavourite(CommonFunctions.mUrl + "addToFavourite", requestBody);
                        }
                    }
                });

                mDeleteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor cs= DatabaseHandler.getInstance(getActivity()).checkSQLiteData(list.get(getAdapterPosition()).store_id);
                        if(cs.getCount()>0) {
                            DatabaseHandler.getInstance(getActivity()).deleteStoreRow(list.get(getAdapterPosition()).store_id);
                            int pos=getAdapterPosition();
                            list.remove(pos);
                            notifyItemRemoved(getAdapterPosition());
                            MainActivity.showMessageDialog("Store removed from your recent history.","#f2dede","#a94442");

                        }
                    }
                });
            }


            private void addToFavourite(String url, RequestBody requestBody) {
                new ExecuteServices().executePost(url, new ExecuteServices.OnServiceExecute() {
                    @Override
                    public void onServiceExecutedResponse(final String response) {
                        if(!checkActivity()){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
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
    }
}
