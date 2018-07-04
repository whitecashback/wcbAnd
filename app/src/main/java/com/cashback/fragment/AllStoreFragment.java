package com.cashback.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cashback.MainActivity;
import com.cashback.R;
import com.cashback.StoreDetailPageScreen;
import com.cashback.data.Store;
import com.cashback.services.AlStoreIntentService;
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
 * Created by jack on 15/03/2017.
 */

public class AllStoreFragment extends Fragment {
    //Recycler view to display the list of Categories items
    RecyclerView mRecyclerView;
    //Linear layout to display linear list in RecyclerView #mRecyclerView
    LinearLayoutManager mLayoutManager;
    //RecyclerView.Adapter class to inflate items in #mRecyclerView
    StoreListAdapter adapter;
    //Root view for this fragment
    View root;
    //Arraylist contains list of categories fetch from Server @see {@link getCategories()}
    ArrayList<Store> list = new ArrayList<>();
    public static Store sStoreDataList;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="";
    DataReceiver mDataReceiver;
    DatabaseHandler mDatabaseHandler;
    private long mCurrentTime;
    private long mLastUpdatedAllStoreTime;
    private boolean mIsSecondDay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating view for current fragment
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefEditor=prefs.edit();
        mDatabaseHandler=DatabaseHandler.getInstance(getActivity());
        userId=prefs.getString("USER_ID","102");
        mCurrentTime=CommonFunctions.getCurrentTimeStamp();
        return root = inflater.inflate(R.layout.fragment_category_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setup Category's RecyclerView
        if(checkActivity()) {
            setUpRecycler();
            mDataReceiver=new DataReceiver();
            IntentFilter filter = new IntentFilter("com.cash.cashback.allstoress");
            getActivity().registerReceiver(mDataReceiver, filter);
            if(mDatabaseHandler.getLastUpdatedTime("all_stores")!=null) {
                list.addAll(mDatabaseHandler.getAllStores());
                setUpRecycler();
                if (list.size() == 0) {
                    root.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                    root.findViewById(R.id.list).setVisibility(View.GONE);
                    AlStoreIntentService.startActionFoo(getActivity(), "", "");
                }

                if (CommonFunctions.isInternetOn(getActivity())) {
                    // getCategories();
                } else {
                    MainActivity.showMessageDialog("No internet connection...!!!", "#fcf8e3", "#8a6d3b");
                    Cursor cs = mDatabaseHandler.getSQLiteData("all_stores");
                    root.findViewById(R.id.progressbar).setVisibility(View.GONE);

                    if (cs.getCount() > 0) {
                        root.findViewById(R.id.list).setVisibility(View.VISIBLE);
                        list = mDatabaseHandler.getAllStores();
                        adapter.notifyDataSetChanged();
                        ;
                    } else {
                        //No data is store on database do nothing
                    }
                }
            }
            else{
                root.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                root.findViewById(R.id.list).setVisibility(View.GONE);
                AlStoreIntentService.startActionFoo(getActivity(), "", "");
            }
        }
     //
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    if(checkActivity() && mDataReceiver!= null ){
        getActivity().unregisterReceiver(mDataReceiver);
    }
    }

    boolean checkActivity(){
        if(isAdded() && getActivity()!=null )
            return  true;
        else
            return  false;

    }

    /**
     * Method to fetch categories from server asynchronously
     */
    void getCategories() {

        userId=prefs.getString("USER_ID","102");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .build();
        root.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        root.findViewById(R.id.list).setVisibility(View.GONE);
        new ExecuteServices().executePost(CommonFunctions.mUrl + "storeListBack", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(String response) {
                try {
                    if(!checkActivity())
                    {
                        return;
                    }
                    JSONArray array=new JSONObject(response).getJSONArray("data");
                        int length=array.length();
                        for(int i=0;i<length;i++){
                            JSONObject obj=array.getJSONObject(i);
                            list.add(new Store(obj.getString("retailer_id"),obj.getString("title"),obj.getString("cashback")
                            ,obj.getString("retailer_status"),obj.getString("url"),obj.getString("image"),null,obj.getString("is_favourite"),null));
                            System.out.println("running ...."+obj.getString("retailer_status"));
                        }
                    if(checkActivity()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                root.findViewById(R.id.progressbar).setVisibility(View.GONE);
                                root.findViewById(R.id.list).setVisibility(View.VISIBLE);
                                setUpRecycler();
                            }
                        });
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void onServiceExecutedFailed(String message) {
                if (!checkActivity())
                {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.showMessageDialog("Something went wrong.","#f2dede","#a94442");
                        root.findViewById(R.id.progressbar).setVisibility(View.GONE);
                        root.findViewById(R.id.list).setVisibility(View.VISIBLE);


                        //TODO---Update this section from database
                    }
                });
            }
        },requestBody);
    }

    /**
     * Method to setup recycler view and it's component
     */
    public void setUpRecycler() {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new StoreListAdapter();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * RecyclerView.Adapter class to inflate views for {@link #mRecyclerView} to display item list of Categories
     */
    class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflate view layout for Adapter's items
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.category_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //binding values to item views
            //   holder.item_title.setText(nav_items[position]);
            //   holder.icon.setImageResource(nav_drawables[position]);
            if (position==0 || !list.get(position).name.toLowerCase().startsWith(list.get(position-1).name.toLowerCase().substring(0,1))) {
                holder.header_title.setVisibility(View.VISIBLE);
                holder.header_divider.setVisibility(View.VISIBLE);
                holder.header_title.setText(list.get(position).name.substring(0,1).toUpperCase());
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
                        Cursor cs= DatabaseHandler.getInstance(getActivity()).checkSQLiteData(AllStoreFragment.sStoreDataList.store_id);
                        if(cs.getCount()>0) {
                            DatabaseHandler.getInstance(getActivity()).deleteStoreRow(AllStoreFragment.sStoreDataList.store_id);
                            DatabaseHandler.getInstance(getActivity()).insertRecentStores(AllStoreFragment.sStoreDataList);
                        }
                        else
                        {
                            DatabaseHandler.getInstance(getActivity()).insertRecentStores(AllStoreFragment.sStoreDataList);
                        }
                        RecentStoresFragment.reload=true;
                        getActivity().startActivity(new Intent(getActivity(), StoreDetailPageScreen.class));
                    }
                });
            }
        }
    }
    int i=0;

    class DataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {


            root.findViewById(R.id.progressbar).setVisibility(View.GONE);
            root.findViewById(R.id.list).setVisibility(View.VISIBLE);
            if(list.size()==0) {
                list.addAll(AlStoreIntentService.list);
                adapter.notifyDataSetChanged();
            }
        }}

}
