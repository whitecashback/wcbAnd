package com.cash.cashback;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.cashback.MainActivity;
import com.cashback.R;
import com.cashback.util.CommonFunctions;
import com.google.android.gcm.GCMBaseIntentService;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class GCMIntentService extends GCMBaseIntentService {

	static String loggedremember;
	private static final String TAG = "GCMIntentService";
	SharedPreferences pref;
	SharedPreferences.Editor prefEditor;
	private AppController aController = null;
	public static String regid;
	private static NotificationManager mNotificationManager;
	Context mContext;
	String img,message,nfS;
	private final static int NORMAL = 0x00;
	private final static int BIG_PICTURE_STYLE = 0x02;
	public GCMIntentService() {
		// Call extended class Constructor GCMBaseIntentService
		super(Config.GOOGLE_SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {

		//Get Global Controller Class object (see application tag in AndroidManifest.xml)
		if(aController == null)
			aController = (AppController) getApplicationContext();

		//	Log.i(TAG, "Device registered: regId = " + registrationId);
		//  After_SplashActivity.sRegId=registrationId;
		pref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefEditor=pref.edit();
		prefEditor.putString("FCM_TOKEN",registrationId).commit();
		System.out.println("Tokensssss"+registrationId);
		//aController.displayMessageOnScreen(context, "Your device registred with GCM");
		aController.register(context,  registrationId);
	}

	/**
	 * Method called on device unregistred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		if(aController == null)
			aController = (AppController) getApplicationContext();
		//	Log.i(TAG, "Device unregistered");
		//aController.displayMessageOnScreen(context, getString(R.string.gcm_unregistered));
		aController.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message from GCM server
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {

		if(aController == null)
			aController = (AppController) getApplicationContext();
		Bundle extras = intent.getExtras();
		mContext=context;
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		img=extras.getString("url");
		message=extras.getString("msg");

		System.out.println("dddd ddm "+extras.toString());
		nfS=extras.getString("retailer");
		Log.d("image url 3", "" +  " "+extras.toString());
		if(img!=null&& (!img.equals(""))) {
			new CreateNotification(BIG_PICTURE_STYLE).execute();
		}
		else
		{
			new CreateNotification(NORMAL).execute();
		}
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {

		if(aController == null)
			aController = (AppController) getApplicationContext();
		//	Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		//aController.displayMessageOnScreen(context, message);
		// notifies user
		//generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		if(aController == null)
			aController = (AppController) getApplicationContext();
		//Log.i(TAG, "Received error: " + errorId);
		//	aController.displayMessageOnScreen(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		if(aController == null)
			aController = (AppController) getApplicationContext();
		// log message
		//	Log.i(TAG, "Received recoverable error: " + errorId);
		//	aController.displayMessageOnScreen(context, getString(R.string.gcm_recoverable_error,errorId));
		return super.onRecoverableError(context, errorId);
	}


	/**
	 * Notification AsyncTask to create and return the
	 * requested notification.
	 *
	 * @see CreateNotification#CreateNotification(int)
	 */
	public class CreateNotification extends AsyncTask<Void, Void, Void> {

		int style = NORMAL;


		public CreateNotification(int style) {
			this.style = style;
		}
		@Override
		protected Void doInBackground(Void... params) {
			Notification noti = new Notification();
			switch (style)
			{
				case NORMAL:
					noti = setNormalNotification();
					break;
				case BIG_PICTURE_STYLE:
					noti = setBigPictureStyleNotification();
					break;
			}
			noti.defaults |= Notification.DEFAULT_LIGHTS;
			noti.defaults |= Notification.DEFAULT_VIBRATE;
			noti.defaults |= Notification.DEFAULT_SOUND;
			noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
			mNotificationManager.notify(0, noti);
			return null;
		}
	}


	/**
	 * Normal Notification
	 *
	 * @return Notification
	 * @see CreateNotification
	 */
	private Notification setNormalNotification() {
		Bitmap remote_picture = null;

		try {
			remote_picture = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.launchericon);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Setup an explicit intent for an ResultActivity to receive.
		Intent resultIntent = new Intent(this, MainActivity.class);
resultIntent.putExtra("nfS",nfS);
		// TaskStackBuilder ensures that the back button follows the recommended convention for the back key.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		// Adds the back stack for the Intent (but not the Intent itself).
		stackBuilder.addParentStack(MainActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack.
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		return new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.dialognew)
				.setAutoCancel(true)
				.setLargeIcon(remote_picture)
				.setContentIntent(resultPendingIntent)
				.setContentTitle("White Cashback")
						.setTicker("via White Cashback")
				//.setContentText(""+message)
				.setStyle(new NotificationCompat.BigTextStyle()
						.bigText("" + message))
				.build();
	}

	/**
	 * Big Picture Style Notification
	 *
	 * @return Notification
	 * @see CreateNotification
	 */
	private Notification setBigPictureStyleNotification() {
		Bitmap remote_picture = null;
		// Create the style object with BigPictureStyle subclass.
		NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
		notiStyle.setBigContentTitle("White Cashback");
		notiStyle.setSummaryText(""+message);
		try {
			remote_picture = BitmapFactory.decodeStream((InputStream) new URL(img).getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		//AppController.sComeFrom = "noti";
		// Add the big picture to the style.
		notiStyle.bigPicture(remote_picture).setBigContentTitle("White Cashback");
		// Creates an explicit intent for an ResultActivity to receive.
		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.putExtra("nfS",nfS);
		// This ensures that the back button follows the recommended convention for the back key.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself).
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack.
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.launchericon);
		System.out.println("message "+message);
		return new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.dialognew)
				.setAutoCancel(true)
				.setLargeIcon(icon)//.setLargeIcon(remote_picture)
				.setContentIntent(resultPendingIntent)
				.setContentTitle("White Cashback")
						.setTicker("via White Cashback")
				.setContentText("" + CommonFunctions.fromHtml(message))
				.setStyle(new NotificationCompat.BigTextStyle()
						.bigText("" + CommonFunctions.fromHtml(message)))
//				.setStyle(new NotificationCompat.BigTextStyle()
//   			.bigText("" + message))
				.setStyle(notiStyle).build();
	}
}
