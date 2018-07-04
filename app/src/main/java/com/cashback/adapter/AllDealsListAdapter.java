package com.cashback.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cashback.MLoginActivity;
import com.cashback.MainActivity;
import com.cashback.R;
import com.cashback.StoreDetailPageScreen;
import com.cashback.WebviewActivity;
import com.cashback.data.CoupenListItems;
import com.cashback.data.WebviewModel;
import com.cashback.fragment.AllDealsFragment;
import com.cashback.fragment.AllStoreFragment;
import com.cashback.util.CommonFunctions;
import java.util.ArrayList;
import static com.cashback.adapter.FavouriteAdapter.act;


public class AllDealsListAdapter extends RecyclerView.Adapter<AllDealsListAdapter.View_Holder>
{
    Context context;
    ArrayList<CoupenListItems> feedItems;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    public AllDealsListAdapter(Context context, ArrayList<CoupenListItems> feedItems)
    {
        this.context = context;
        this.feedItems=feedItems;
        prefs = PreferenceManager.getDefaultSharedPreferences(context); //initilize the shared preferences
        prefsEditor=prefs.edit();
        myClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public class View_Holder extends RecyclerView.ViewHolder
    {
        TextView mStoreDesc,mExpDate,mCashback,couponCode,mCouponType;
        ImageView mImage;
        LinearLayout mCouponCodeLayout;
        CardView mCouponCard;
        public View_Holder(View itemView)
        {
            super(itemView);
            mCouponType=(TextView)itemView.findViewById(R.id.couponType);
            mStoreDesc=(TextView)itemView.findViewById(R.id.storeTxt);
            mExpDate=(TextView)itemView.findViewById(R.id.expDate);
            mCashback=(TextView)itemView.findViewById(R.id.cashbackPercent);
            couponCode=(TextView)itemView.findViewById(R.id.couponCode);
            mImage=(ImageView)itemView.findViewById(R.id.storeImage);
          //  mCouponCodeLayout=(LinearLayout)itemView.findViewById(R.id.couponCodeLayout);
            mCouponCard=(CardView)itemView.findViewById(R.id.card_view);
            mCouponCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MainActivity.sComingFrom="StoreCoupon";
                    MainActivity.sWebviewData=new WebviewModel();
                    //StoreDetailPageScreen.sCouponDataListItem=feedItems.get(getAdapterPosition());
                    MainActivity.sWebviewData.setStatus(AllStoreFragment.sStoreDataList.status);
                    MainActivity.sWebviewData.setStore_id(AllStoreFragment.sStoreDataList.store_id);
                    MainActivity.sWebviewData.setRetailerName(AllStoreFragment.sStoreDataList.name);
                    MainActivity.sWebviewData.setCashback(AllStoreFragment.sStoreDataList.cashback);
                    MainActivity.sWebviewData.setUrl(feedItems.get(getAdapterPosition()).get_CoupenLink());
                    String userId=prefs.getString("USER_ID","102");
                    if(feedItems.get(getAdapterPosition()).get_Coupon_Code().trim().length()>0) {
                        myClip = ClipData.newPlainText("text", feedItems.get(getAdapterPosition()).get_Coupon_Code());
                        myClipboard.setPrimaryClip(myClip);
                        MainActivity.showMessageDialog("Coupon code copied","#dff0d8","#3c763d");
                        Toast.makeText(context.getApplicationContext(),"Coupon code copied",Toast.LENGTH_SHORT).show();
                    }
                    if(userId.equalsIgnoreCase("102"))
                    {
                        context.startActivity(new Intent(context.getApplicationContext(), MLoginActivity.class));

                    }
                    else
                    {
                        context.startActivity(new Intent(context.getApplicationContext(), WebviewActivity.class));

                    }
                }
            });

        }
    }
    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_big, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }
    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        holder.mStoreDesc.setText(CommonFunctions.fromHtml(feedItems.get(position).get_CoupenTitle()));
        holder.mExpDate.setText(CommonFunctions.fromHtml(("Exp. Date: "+feedItems.get(position).getCoupon_End_Date())));
        holder.couponCode.setText(CommonFunctions.fromHtml(feedItems.get(position).get_Coupon_Code()));
        holder.mCouponType.setText(CommonFunctions.fromHtml(feedItems.get(position).get_Coupen_Type()));
        holder.mCashback.setText(""+CommonFunctions.fromHtml(AllDealsFragment.cashbck));
//        if(AllDealsFragment.cashbck.contains("%")) {
//            holder.mCashback.setText("Upto "+CommonFunctions.fromHtml(AllDealsFragment.cashbck) + " Cash Back");
//        }
//        else
//        {
//            holder.mCashback.setText("Upto "+CommonFunctions.fromHtml(AllDealsFragment.cashbck) + " Cash Back");
//
//        }
            Glide.with(context).load(CommonFunctions.cloudPath+ AllDealsFragment.sStoreImage)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mImage);

        if (feedItems.get(position).get_Coupen_Type().equalsIgnoreCase("coupon")&&feedItems.get(position).get_Coupon_Code().trim().length()>0)
        {
            holder.couponCode.setVisibility(View.VISIBLE);
        }
        else
        {
           holder.couponCode.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
