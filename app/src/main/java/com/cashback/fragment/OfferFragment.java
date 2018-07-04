package com.cashback.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cashback.R;
import com.cashback.StoreDetailPageScreen;
import com.cashback.adapter.AllDealsListAdapter;
import com.cashback.data.CoupenListItems;

import java.util.ArrayList;


/**
 * Created by jack on 01-07-2015.
 */
public class OfferFragment extends Fragment {

    RecyclerView recycler_allDealsList;
    ArrayList<CoupenListItems> feedItems;
    AllDealsListAdapter adapter;
    public static ArrayList<CoupenListItems> sCouponList,sOfferList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_all_deals, container, false);
        recycler_allDealsList = (RecyclerView) v.findViewById(R.id.recycler_allDealsList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_allDealsList.setLayoutManager(mLayoutManager);
        feedItems=new ArrayList<CoupenListItems>();
        adapter = new AllDealsListAdapter(getActivity(),AllDealsFragment.sOfferList);
        recycler_allDealsList.setAdapter(adapter);

        return v;
    }


   public void menthodNotify()
   {
       adapter.notifyDataSetChanged();
       if(AllDealsFragment.sOfferList.size()==0)
       {
         //  StoreDetailPageScreen.showMessageDialog("Oops!! No deal found.","#f2dede","#a94442");
       }
   }
}

