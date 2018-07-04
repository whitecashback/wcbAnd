package com.cashback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cashback.fragment.HowItWorksFragment;
import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dev on 11/4/17.
 */

public class VideoViewActivity extends AppCompatActivity
{
    TextView skipButtn;
    VideoView videoView;
    ProgressBar pBar;
    public String videoUrl="";//http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";//"http://techslides.com/demos/sample-videos/small.mp4";
    LinearLayout mPLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view_new);
        getSupportActionBar().setTitle("How it works");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mitem=menu.add(0,0,0,"Skip");
        MenuItemCompat.setShowAsAction(mitem,MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==0){
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view_xml);
        pBar=(ProgressBar)findViewById(R.id.pBar);
        videoView=(VideoView)findViewById(R.id.videoView);
        mPLayout=(LinearLayout)findViewById(R.id.Playout);
        if(CommonFunctions.isInternetOn(VideoViewActivity.this)) {
            pBar.setVisibility(View.VISIBLE);
            mPLayout.setVisibility(View.VISIBLE);
            System.out.println("my on response ");
            networkCall();
        }
        else
        {
            Toast.makeText(VideoViewActivity.this,"No internet connection.",Toast.LENGTH_SHORT).show();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pBar.setVisibility(View.GONE);
            }
        },8000);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                            pBar.setVisibility(View.VISIBLE);
                        mPLayout.setVisibility(View.VISIBLE);

                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                            pBar.setVisibility(View.GONE);
                        mPLayout.setVisibility(View.GONE);

                        return false;
                    }
                });
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                pBar.setVisibility(View.GONE);
                mPLayout.setVisibility(View.GONE);

                return false;
            }
        });


        skipButtn=(TextView)findViewById(R.id.skip);
        skipButtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }*/

    private void networkCall() {

        new ExecuteServices().execute(CommonFunctions.mUrl+"getVideoUrl", new ExecuteServices.OnServiceExecute() {
            @Override
            public void onServiceExecutedResponse(final String response) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("my on response "+response);
                        try {
                            JSONObject object=new JSONObject(response);
                            String status=object.getString("status");
                            if(status.equalsIgnoreCase("1"))
                            {
                                videoUrl=object.getString("url");
                                if(videoUrl.length()>0)
                                {
                                    MediaController mediaController = new MediaController(VideoViewActivity.this);
                                    mediaController.setAnchorView(videoView);
                                    videoView.setMediaController(mediaController);
                                    videoView.setVideoURI(Uri.parse(videoUrl));
                                    videoView.start();
                                }
                                else
                                {
                                    alertDialog();
                                   // Toast.makeText(VideoViewActivity.this,"No video available at this moment",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                alertDialog();
                               // Toast.makeText(VideoViewActivity.this,"No video available at this moment",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(VideoViewActivity.this,"Some error occur while retrieving video from server.",Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }

            @Override
            public void onServiceExecutedFailed(String message) {

            }
        });
    }


    void alertDialog()
    {
        new AlertDialog.Builder(VideoViewActivity.this)
                .setTitle("Video")
                .setMessage("Currently how it works video not available.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        VideoViewActivity.this.finish();
                    }
                })

               .show();

    }

}
