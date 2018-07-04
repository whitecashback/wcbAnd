package com.cashback;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cashback.data.Store;
import com.cashback.fragment.AllStoreFragment;
import com.cashback.fragment.CategoryFragment;
import com.cashback.fragment.RecentStoresFragment;
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
 * Created by dev on 20/3/17.
 */

public class CategoryWiseStoreActivity extends Activity
{
    //Recycler view to display the list of Categories items
    RecyclerView mRecyclerView;
    //Linear layout to display linear list in RecyclerView #mRecyclerView
    LinearLayoutManager mLayoutManager;
    //RecyclerView.Adapter class to inflate items in #mRecyclerView
    CategoryWiseStoreActivity.StoreListAdapter adapter;
    //Root view for this fragment
    //Arraylist contains list of categories fetch from Server @see {@link getCategories()}
    ArrayList<Store> list = new ArrayList<>();
    Activity act;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    String userId="";
    private static Animation mSlideUpAnimation,mSlideDownAnimation;
    private static Activity activity;
    private static TextView mToastView;
    private static boolean exitActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_wise_activity);
        prefs = PreferenceManager.getDefaultSharedPreferences(CategoryWiseStoreActivity.this);
        prefsEditor = prefs.edit();
        userId=prefs.getString("USER_ID","102");
        act=CategoryWiseStoreActivity.this;
        mToastView=(TextView)findViewById(R.id.toast);
        mSlideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        mSlideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;
        ((LinearLayout)findViewById(R.id.backFromCategory)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((TextView)findViewById(R.id.catTitle)).setText(CategoryFragment.sCatItemData.name);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CommonFunctions.isInternetOn(getApplicationContext())) {
            getCategories();
        }
        else{
            CategoryWiseStoreActivity.showMessageDialog("No internet connection...!!!", "#fcf8e3", "#8a6d3b");
            findViewById(R.id.progressbar).setVisibility(View.GONE);
            findViewById(R.id.list).setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * @param message
     * @param color
     * @param textColor
     */
    public static void showMessageDialog(String message,String color,String textColor)
    {

        if(mToastView.getVisibility()==View.VISIBLE)
        {
            mToastView.setVisibility(View.GONE);
        }
        mToastView.setBackgroundColor(Color.parseColor(color));
        mToastView.setText(message);
        mToastView.setVisibility(View.VISIBLE);
        mToastView.setTextColor(Color.parseColor(textColor));
        mToastView.setVisibility(View.VISIBLE);
        mToastView.startAnimation(mSlideDownAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mToastView.startAnimation(mSlideUpAnimation);
                mToastView.setVisibility(View.GONE);
                if(exitActivity) {
                    activity.finish();
                }
                exitActivity=false;
            }
        },4000);
    }

    /**
     * Method to fetch categories from server asynchronously
     */
    void getCategories() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id",userId)
                .addFormDataPart("category_id", CategoryFragment.sCatItemData.id)
                .build();
        findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        findViewById(R.id.list).setVisibility(View.GONE);
        new ExecuteServices().executePost(CommonFunctions.mUrl + "storeListBack", new ExecuteServices.OnServiceExecute() {

            @Override
            public void onServiceExecutedResponse(String response) {
                try {
                    JSONArray array=new JSONObject(response).getJSONArray("data");
                    int length=array.length();
                    for(int i=0;i<length;i++){
                        JSONObject obj=array.getJSONObject(i);
                        list.add(new Store(obj.getString("retailer_id"),obj.getString("title"),obj.getString("cashback")
                                ,obj.getString("retailer_status"),obj.getString("url"),obj.getString("image"),obj.has("description")?obj.getString("description"):"",obj.getString("is_favourite"),null));
                        System.out.println("running ....");
                    }
                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.progressbar).setVisibility(View.GONE);
                            findViewById(R.id.list).setVisibility(View.VISIBLE);
                            setUpRecycler();
                        }
                    });
                } catch (JSONException e) {

                }
            }

            @Override
            public void onServiceExecutedFailed(String message) {

            }
        },requestBody);
    }

    /**
     * Method to setup recycler view and it's component
     */
    public void setUpRecycler() {
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        adapter = new CategoryWiseStoreActivity.StoreListAdapter();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }



    /**
     * RecyclerView.Adapter class to inflate views for {@link #mRecyclerView} to display item list of Categories
     */
    class StoreListAdapter extends RecyclerView.Adapter<CategoryWiseStoreActivity.StoreListAdapter.ViewHolder> {
        @Override
        public CategoryWiseStoreActivity.StoreListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflate view layout for Adapter's items
            View v = LayoutInflater.from(CategoryWiseStoreActivity.this).inflate(R.layout.category_item, parent, false);
            return new CategoryWiseStoreActivity.StoreListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CategoryWiseStoreActivity.StoreListAdapter.ViewHolder holder, int position) {
            //binding values to item views
            //   holder.item_title.setText(nav_items[position]);
            //   holder.icon.setImageResource(nav_drawables[position]);
            if (position==0 || !list.get(position).name.toLowerCase().startsWith(list.get(position-1).name.toLowerCase().substring(0,1))) {
                holder.header_title.setVisibility(View.VISIBLE);
                holder.header_divider.setVisibility(View.VISIBLE);
                holder.header_title.setText(list.get(position).name.substring(0,1));
            } else {
                holder.header_title.setVisibility(View.GONE);
                holder.header_divider.setVisibility(View.GONE);
            }
            holder.item_title.setText(list.get(position).name);

            if(list.get(position).status.equalsIgnoreCase("inactive")){
                holder.cashback.setText("Currently No Cashback");
                holder.cashback.setTextColor(Color.parseColor("#b2b2b2"));
                holder.cashback.setTextSize(14.0f);
            }else {
                if (list.get(position).cashback.length() > 0) {
                    holder.cashback.setText(list.get(position).cashback);
                    holder.cashback.setTextColor(Color.parseColor("#F26522"));
                    holder.cashback.setTextSize(16.0f);

                } else {
                    holder.cashback.setText("Best Offer");
                    holder.cashback.setTextColor(Color.parseColor("#00796b"));
                    holder.cashback.setTextSize(14.0f);
                }
            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView item_title, header_title,cashback;
            View divider, header_divider;

            public ViewHolder(View itemView) {
                super(itemView);
                //initialize item views
                divider = itemView.findViewById(R.id.divider);
                cashback = (TextView) itemView.findViewById(R.id.cashback);
                header_divider = itemView.findViewById(R.id.header_divider);
                item_title = (TextView) itemView.findViewById(R.id.item_title);
                header_title = (TextView) itemView.findViewById(R.id.header_title);
                cashback.setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.item_title).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AllStoreFragment.sStoreDataList=list.get(getAdapterPosition());
                        System.out.println("Retailer name "+AllStoreFragment.sStoreDataList.name+" status "+AllStoreFragment.sStoreDataList.status);
                        Cursor cs= DatabaseHandler.getInstance(CategoryWiseStoreActivity.this).checkSQLiteData(AllStoreFragment.sStoreDataList.store_id);
                        if(cs.getCount()>0) {
                            DatabaseHandler.getInstance(CategoryWiseStoreActivity.this).deleteStoreRow(AllStoreFragment.sStoreDataList.store_id);
                            DatabaseHandler.getInstance(CategoryWiseStoreActivity.this).insertRecentStores(AllStoreFragment.sStoreDataList);
                        }
                        else
                        {
                            DatabaseHandler.getInstance(CategoryWiseStoreActivity.this).insertRecentStores(AllStoreFragment.sStoreDataList);
                        }
                        RecentStoresFragment.reload=true;
                        startActivity(new Intent(CategoryWiseStoreActivity.this, StoreDetailPageScreen.class));
                    }
                });
            }
        }
    }
}
