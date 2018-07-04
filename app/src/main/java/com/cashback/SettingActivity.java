package com.cashback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import com.cashback.data.Settings;
import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SettingActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    SettingListAdapter adapter;
    //Arraylist contains list of categories fetch from Server @see {@link getCategories()}
    ArrayList<Settings> list = new ArrayList<>();
    ProgressDialog pd;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    String userId="";
    TextView mToastView;
    private static Activity activity;
    private static Animation slideUpAnimation, slideDownAnimation;
    private static boolean exitActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        prefs = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this); //initilize the shared preferences
        prefsEditor=prefs.edit();
        userId=prefs.getString("USER_ID","102");
        mToastView=(TextView)findViewById(R.id.message);
        setUpRecycler();
        pd=new ProgressDialog(this);
        pd.setMessage("Wait...updating");
        pd.setCanceledOnTouchOutside(false);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Settings");
        findViewById(R.id.backFromSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        activity=this;
    }

    public  void showMessageDialog(String message,String color,String textColor)
    {
        if(mToastView.getVisibility()==View.VISIBLE)
        {
            mToastView.setVisibility(View.GONE);
        }
        mToastView.setBackgroundColor(Color.parseColor(color));
        mToastView.setText(message);
        mToastView.setTextColor(Color.parseColor(textColor));
        mToastView.setVisibility(View.VISIBLE);
        mToastView.startAnimation(slideDownAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mToastView.startAnimation(slideUpAnimation);
                mToastView.setVisibility(View.GONE);
                if(exitActivity) {
                    activity.finish();
                }
                exitActivity=false;
            }
        },3000);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void setUpRecycler() {
     /*   list.add(new Cat(-1, "Cashback Notification"));
        list.add(new Cat(-1, "Deal Notification"));
        list.add(new Cat(-1, "Whitecasback Notification"));
        list.add(new Cat(-1, "All Notification"));*/
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        adapter = new SettingListAdapter();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        if(CommonFunctions.isInternetOn(SettingActivity.this)) {
            getCategories();
        }
        else
        {
            showMessageDialog("No internet connection...!!!.","#fcf8e3","#8a6d3b");
        }
    }

    /**
     * Method to fetch categories from server asynchronously
     */
    void getCategories() {
        RequestBody params=new MultipartBody.Builder() .setType(MultipartBody.FORM).addFormDataPart("user_id",userId).build();
        new ExecuteServices().executePost(CommonFunctions.mUrl + "ntCategory", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(String response) {
                try {
                    if(new JSONObject(response).getInt("status")==1) {
                        JSONArray array = new JSONObject(response).getJSONArray("general");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            list.add(new Settings(obj.getInt("id"), obj.getString("title"),obj.getInt("status")));
                        }
                        list.add(new Settings(0,"All Notifications",1));
                             array = new JSONObject(response).getJSONArray("category");
                         length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            list.add(new Settings(obj.getInt("id"), obj.getString("title"),obj.getInt("status")));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }

                        });
                    }
                } catch (final JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMessageDialog("Something went wromg."+e.getLocalizedMessage(),"#f2dede","#a94442");
                        }
                    });

                }
            }

            @Override
            public void onServiceExecutedFailed(String message) {
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        showMessageDialog("Something went wromg.","#f2dede","#a94442");
    }
});
            }
        },params);
    }

    public void updateStatus(final SwitchCompat switchc, final int position){
     //  pd.show();
        RequestBody params=new MultipartBody.Builder() .setType(MultipartBody.FORM).addFormDataPart("user_id",userId)
                .addFormDataPart("category_id",""+list.get(position).id).addFormDataPart("status",""+(list.get(position).isChecked==1?0:1)).build();
        new ExecuteServices().executePost(CommonFunctions.mUrl + "notificationActivation", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                   //     pd.dismiss();
                        boolean success=false;
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getInt("status")==1){
                                success=true;
                                switchc.setChecked(!switchc.isChecked());

                                list.get(position).isChecked=switchc.isChecked()?1:0;
                                adapter.notifyItemChanged(position);
                                if(position==3)
                                {
                                    for(int i=4;i<list.size();i++){
                                        list.get(i).isChecked=list.get(3).isChecked;
                                        adapter.notifyItemChanged(i);
                                    }
                                }
                            }

                        }
                        catch (Exception e){

                        }
if(success)
{
    showMessageDialog("Updated.","#dff0d8","#3c763d");
}
else
{
    showMessageDialog("Some error occured.","#f2dede","#a94442");
}
                        //Toast.makeText(SettingActivity.this, success?"Updated":"Some error occurred",Toast.LENGTH_LONG).show();
                        }

                });

            }

            @Override
            public void onServiceExecutedFailed(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  pd.dismiss();
                        showMessageDialog("Something went wromg.","#f2dede","#a94442");
                       // Toast.makeText(SettingActivity.this,"Some error occurred",Toast.LENGTH_LONG).show();
                    }
                });
            }
        },params);
    }



    class SettingListAdapter extends RecyclerView.Adapter<SettingListAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(SettingActivity.this).inflate(R.layout.item_setting_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (position == 0 || position == 3) {
                //  holder.root.setVisibility(View.GONE);
                holder.group.setVisibility(View.VISIBLE);
                holder.group.setText(position == 0 ? "General Push Notification" : "Category wise Notification");
                holder.divider.setVisibility(View.VISIBLE);
            } else {
                //    holder.root.setVisibility(View.VISIBLE);
                holder.group.setVisibility(View.GONE);
                holder.divider.setVisibility(View.GONE);
            }
            holder.switchc.setChecked(list.get(position).isChecked==1);
            //if(!useAllNotification)
            holder.title.setText(list.get(position).name);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            SwitchCompat switchc;
            TextView group, title;
            View root, divider;

            public ViewHolder(View itemView) {
                super(itemView);
                root = itemView.findViewById(R.id.root);
                divider = itemView.findViewById(R.id.divider);
                switchc = (SwitchCompat) itemView.findViewById(R.id.switch_choice);
                group = (TextView) itemView.findViewById(R.id.group);
                title = (TextView) itemView.findViewById(R.id.title);
                root.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                updateStatus(switchc,getAdapterPosition());
                                            }
                                        }
                );
            }
        }
    }
}
