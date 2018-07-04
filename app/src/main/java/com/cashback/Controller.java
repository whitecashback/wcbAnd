package com.cashback;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;


import com.cash.cashback.Config;
import com.google.android.gcm.GCMRegistrar;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@SuppressLint("Wakelock")
public class Controller extends Application {
	
	private  final int MAX_ATTEMPTS = 5;
    private  final int BACKOFF_MILLI_SECONDS = 2000;
    private  final Random random = new Random();
   @Override
    public void onCreate() {
        super.onCreate();
    }

    // Register this account with the server.
    @SuppressWarnings("unused")
	public void register(final Context context, final String regId) {
    	 
      //  Log.i(Config.TAG, "registering device (regId = " + regId + ")");
        
  //      String serverUrl = Config.YOUR_SERVER_URL;
        
        Map<String, String> params = new HashMap<String, String>();
      params.put("regId", regId);
       
        
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        
        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
        	
          //  Log.d(Config.TAG, "Attempt #" + i + " to register");
            
            //Send Broadcast to Show message on screen
			// Post registration values to web server
    //           post(serverUrl, params);
			
			GCMRegistrar.setRegisteredOnServer(context, true);
			
			//Send Broadcast to Show message on screen
			String message = context.getString(R.string.server_registered);
		//	displayMessageOnScreen(context, message);
			
			return;
        }
        
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        
        //Send Broadcast to Show message on screen
       // displayMessageOnScreen(context, message);
    }

     // Unregister this account/device pair within the server.
     void unregister(final Context context, final String regId) {
    	 
     //   Log.i(Config.TAG, "unregistering device (regId = " + regId + ")");
        
   //     String serverUrl = Config.YOUR_SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        
        //      post(serverUrl, params);
                    GCMRegistrar.setRegisteredOnServer(context, false);
		        String message = context.getString(R.string.server_unregistered);
		    //    displayMessageOnScreen(context, message);
    }

    // Checking for all possible internet providers
    public boolean isConnectingToInternet(){
    	
        ConnectivityManager connectivity =
        	                 (ConnectivityManager) getSystemService(
        	                  Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
	
   // Notifies UI to display a message.
   void displayMessageOnScreen(Context context, String message) {
    	 
        Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(Config.EXTRA_MESSAGE, message);
        
        // Send Broadcast to Broadcast receiver with message
        context.sendBroadcast(intent);
        
    }
    
    
   //Function to display simple Alert Dialog
   @SuppressWarnings("deprecation")
public void showAlertDialog(Context context, String title, String message,
                            Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Set Dialog Title
		alertDialog.setTitle(title);

		// Set Dialog Message
		alertDialog.setMessage(message);

		if(status != null)
			// Set alert dialog icon
			alertDialog.setIcon((status) ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);

		// Set OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});

		// Show Alert Message
		alertDialog.show();
	}
    
    private PowerManager.WakeLock wakeLock;
    
    @SuppressWarnings("deprecation")
	public  void acquireWakeLock(Context context) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "WakeLock");
        
        wakeLock.acquire();
    }

    public  void releaseWakeLock() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
   
}
