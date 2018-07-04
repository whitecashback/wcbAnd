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
public class CouponFragment extends Fragment {

    RecyclerView recycler_allDealsList;
    ArrayList<CoupenListItems> feedItems;
    AllDealsListAdapter adapter;
    View v;
    boolean checkActivity(){
        if(isAdded() && getActivity()!=null )
            return  true;
        else
            return  false;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_all_deals, container, false);
        recycler_allDealsList = (RecyclerView) v.findViewById(R.id.recycler_allDealsList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_allDealsList.setLayoutManager(mLayoutManager);
        if(checkActivity())
        {
            feedItems=new ArrayList<CoupenListItems>();
            adapter = new AllDealsListAdapter(getActivity(),AllDealsFragment.sCouponList);
            recycler_allDealsList.setAdapter(adapter);
            recycler_allDealsList.setVisibility(View.VISIBLE);
            System.out.println("CouponFragment....");
        }

        return v;
    }


    public void methodNotify()
    {
        System.out.println(AllDealsFragment.sCouponList.size());
        adapter.notifyDataSetChanged();
        if(AllDealsFragment.sCouponList.size()==0)
        {
           // StoreDetailPageScreen.showMessageDialog("Oops!! No coupon found.","#f2dede","#a94442");
        }
    }

}

