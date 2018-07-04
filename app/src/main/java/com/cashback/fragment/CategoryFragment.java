package com.cashback.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cashback.CategoryWiseStoreActivity;
import com.cashback.MainActivity;
import com.cashback.R;
import com.cashback.data.Cat;
import com.cashback.util.CommonFunctions;
import com.cashback.util.DatabaseHandler;
import com.cashback.util.ExecuteServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by jack on 15/03/2017.
 */

public class CategoryFragment extends Fragment {
    //Recycler view to display the list of Categories items
    RecyclerView mRecyclerView;
    //Linear layout to display linear list in RecyclerView #mRecyclerView
    LinearLayoutManager mLayoutManager;
    //RecyclerView.Adapter class to inflate items in #mRecyclerView
    CategoryListAdapter adapter;
    //Root view for this fragment
    View root;
    //Arraylist contains list of categories fetch from Server @see {@link getCategories()}
    ArrayList<Cat> list = new ArrayList<>();

    public static Cat sCatItemData;

    private DatabaseHandler mDatabaseHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating view for current fragment
        return root = inflater.inflate(R.layout.fragment_category_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setup Category's RecyclerView
        mDatabaseHandler=DatabaseHandler.getInstance(getActivity());
        /*if(checkActivity()) {
            if (CommonFunctions.isInternetOn(getActivity())) {
                getCategories();
            } else {
                MainActivity.showMessageDialog("Oops!! No internet connection...!!!", "#fcf8e3", "#8a6d3b");
            }
        }*/

        if(checkActivity()) {
            list=mDatabaseHandler.getShopCategories();

            if(list.size()==0){
               getCategories();
            }
            else{
                setUpRecycler();
                root.findViewById(R.id.progressbar).setVisibility(View.GONE);
                root.findViewById(R.id.list).setVisibility(View.VISIBLE);
            }
        }
     //
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

        root.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
        root.findViewById(R.id.list).setVisibility(View.GONE);
        new ExecuteServices().execute(CommonFunctions.mUrl + "categoryList", new ExecuteServices.OnServiceExecute() {

            @Override
            public void onServiceExecutedResponse(String response) {
                try {

                    JSONArray array=new JSONObject(response).getJSONArray("data");
                        int length=array.length();
                        for(int i=0;i<length;i++){
                            JSONObject obj=array.getJSONObject(i);
                            list.add(new Cat(obj.getString("category_id"),obj.getString("name")));
                        }
                    if (!checkActivity())
                    {
                        return;
                    }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                root.findViewById(R.id.progressbar).setVisibility(View.GONE);
                                root.findViewById(R.id.list).setVisibility(View.VISIBLE);
                                setUpRecycler();
                                DatabaseHandler lDatabaseHandler=new DatabaseHandler(getActivity());
                                lDatabaseHandler.deleteShopCategory();

                                for(int lCount=0;lCount<list.size();lCount++){
                                    Cat lCat=list.get(lCount);
                                    lDatabaseHandler.insertShopCategory(lCat);
                                    Log.i("Category List",lCat.toString());
                                }

                                long lCurrentTime=CommonFunctions.getCurrentTimeStamp();
                                lDatabaseHandler.updateLastUpdatedShopCategoryTime(lCurrentTime);
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
                        root.findViewById(R.id.progressbar).setVisibility(View.GONE);
                        root.findViewById(R.id.list).setVisibility(View.VISIBLE);
                        MainActivity.showMessageDialog("Something went wrong.","#f2dede","#a94442");

                    }
                });
            }
        });
    }

    /**
     * Method to setup recycler view and it's component
     */
    public void setUpRecycler() {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new CategoryListAdapter();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * RecyclerView.Adapter class to inflate views for {@link #mRecyclerView} to display item list of Categories
     */
    class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
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
                holder.header_title.setText(list.get(position).name.substring(0,1));
            } else {
                holder.header_title.setVisibility(View.GONE);
                holder.header_divider.setVisibility(View.GONE);
            }
            holder.item_title.setText(list.get(position).name);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView item_title, header_title;

            View divider, header_divider;

            public ViewHolder(View itemView) {
                super(itemView);
                //initialize item views
                divider = itemView.findViewById(R.id.divider);
                header_divider = itemView.findViewById(R.id.header_divider);
                item_title = (TextView) itemView.findViewById(R.id.item_title);
                header_title = (TextView) itemView.findViewById(R.id.header_title);
                itemView.findViewById(R.id.item_title).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sCatItemData=list.get(getAdapterPosition());
                        startActivity(new Intent(getActivity(), CategoryWiseStoreActivity.class));
                    }
                });
            }
        }
    }
}
