package com.cashback.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class SearchFragment extends Fragment {
    //Recycler view to display the list of Categories items
    RecyclerView mRecyclerView;
    //Linear layout to display linear list in RecyclerView #mRecyclerView
    LinearLayoutManager mLayoutManager;
    //RecyclerView.Adapter class to inflate items in #mRecyclerView
    SearchListAdapter adapter;
    //Root view for this fragment
    View root;
    //Arraylist contains list of categories fetch from Server @see {@link getCategories()}
    ArrayList<Store> list = new ArrayList<>();
    ArrayList<Store> history_list = new ArrayList<>();
    boolean historySearchOn=false;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String userId="";
    DatabaseHandler mDatabaseHandler;
    ArrayList<Store> mAllStoresList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating view for current fragment
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefEditor=prefs.edit();
        mDatabaseHandler=DatabaseHandler.getInstance(getActivity());
        userId=prefs.getString("USER_ID","102");
        return root = inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        root.findViewById(R.id.progressbar).setVisibility(View.GONE);
        root.findViewById(R.id.list).setVisibility(View.VISIBLE);
        //setup Category's RecyclerView
        setUpRecycler();
        root.findViewById(R.id.clearAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity()).setMessage("Do you want to clear all search history?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                root.findViewById(R.id.clearAll).setVisibility(View.GONE);
                                list.clear();
                                adapter.notifyDataSetChanged();
                                DatabaseHandler.getInstance(getActivity()).dropRecentStores();

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();

            }
        });

     //
    }
    public void filterList(String match){
        root.findViewById(R.id.clearAll).setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();
        int size=AlStoreIntentService.list.size();
        /*for(Store str:AlStoreIntentService.list){
            if(str.name.toLowerCase().startsWith(match.toLowerCase())){
                list.add(str);
                adapter.notifyDataSetChanged();
            }

        }
        if(list.size()==0){
            MainActivity.showMessageDialog("No store found","#dff0d8","#3c763d");

        }*/
        mAllStoresList=mDatabaseHandler.getAllStores();

        if(mAllStoresList.size()>0){
            for(Store str:mAllStoresList){
                if(str.name.toLowerCase().startsWith(match.toLowerCase())){
                    list.add(str);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        else{
            MainActivity.showMessageDialog("No store found","#dff0d8","#3c763d");
        }


    }
    public void clearList(){
        list.clear();
        if (adapter!=null)
        adapter.notifyDataSetChanged();
    }
    boolean checkActivity(){
        if(isAdded() && getActivity()!=null )
            return  true;
        else
            return  false;

    }
    public void getHistoryList(){
        historySearchOn=true;
        list.clear();
        history_list.clear();
        if(history_list.size()==0) {
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
             do{
                    Store item = new Store(cs.getString(0),cs.getString(1),cs.getString(5),
                            cs.getString(7),cs.getString(2),cs.getString(6),cs.getString(4),cs.getString(3),null);
                    System.out.println("recent running....");
                    history_list.add(item);
                    System.out.println(history_list.size());

                }while (cs.moveToPrevious());
               // setUpRecycler();
            }
        }
        list.addAll(history_list);
        adapter.notifyDataSetChanged();
            root.findViewById(R.id.clearAll).setVisibility(list.size()==0?View.GONE:View.VISIBLE);



    }

    /**
     * Method to fetch categories from server asynchronously
     */
    public void getCategories(String searchString) {
        historySearchOn=false;
        list.clear();
        adapter.notifyDataSetChanged();
        userId=prefs.getString("USER_ID","102");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .addFormDataPart("q", searchString)
                .build();
        root.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        root.findViewById(R.id.list).setVisibility(View.GONE);
        new ExecuteServices().executePost(CommonFunctions.mUrl + "search", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(String response) {
                try {
                    if (!checkActivity())
                    {
                        return;
                    }
                    String status=new JSONObject(response).getString("status");
                    if(status.equalsIgnoreCase("0"))
                    {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                root.findViewById(R.id.progressbar).setVisibility(View.GONE);
                                root.findViewById(R.id.list).setVisibility(View.VISIBLE);
                                MainActivity.showMessageDialog("No record found.", "#a94442", "#f2dede");

                                getHistoryList();
                            }});
                        return;
                    }
                    JSONArray array=new JSONObject(response).getJSONArray("data");
                        int length=array.length();
                        for(int i=0;i<length;i++){
                            JSONObject obj=array.getJSONObject(i);
                            list.add(new Store(obj.getString("retailer_id"),obj.getString("title"),obj.getString("cashback")
                                    ,obj.getString("status"),obj.getString("url"),obj.getString("image"),obj.has("description")?obj.getString("description"):"",obj.getString("is_favourite"),null));

                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                root.findViewById(R.id.progressbar).setVisibility(View.GONE);
                                root.findViewById(R.id.list).setVisibility(View.VISIBLE);
                              adapter.notifyDataSetChanged();
                            }
                        });
                } catch (final Exception e) { getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }});
                    }


            }

            @Override
            public void onServiceExecutedFailed(String message) {
                if (!checkActivity())
                {
                    return;
                }
            }
        },requestBody);
    }

    /**
     * Method to setup recycler view and it's component
     */
    public void setUpRecycler() {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new SearchListAdapter();
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * RecyclerView.Adapter class to inflate views for {@link #mRecyclerView} to display item list of Categories
     */
    class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
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
                holder.header_title.setVisibility(View.GONE);
                holder.header_divider.setVisibility(View.GONE);
                holder.header_title.setText((historySearchOn && position==0?"Recent Searches\n\n":"")+list.get(position).name.substring(0,1));
            } else {
                holder.header_title.setVisibility(View.GONE);
                holder.header_divider.setVisibility(View.GONE);
            }
            holder.item_title.setText(list.get(position).name);
            if(list.get(position).status.equalsIgnoreCase("inactive")) {
                holder.cashback.setText("Currently No Cashback");
                holder.cashback.setTextColor(Color.parseColor("#b2b2b2"));
                holder.cashback.setTextSize(14.0f);
            }
            else
            {
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
                cashback = (TextView) itemView.findViewById(R.id.cashback);
                cashback.setVisibility(View.VISIBLE);
                divider = itemView.findViewById(R.id.divider);
                header_divider = itemView.findViewById(R.id.header_divider);
                item_title = (TextView) itemView.findViewById(R.id.item_title);
                header_title = (TextView) itemView.findViewById(R.id.header_title);
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
                      //  history_list.add(list.get(getAdapterPosition()));
                        getActivity().startActivity(new Intent(getActivity(), StoreDetailPageScreen.class));
                    }
                });
            }
        }
    }
}
