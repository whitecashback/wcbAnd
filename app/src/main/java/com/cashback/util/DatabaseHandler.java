package com.cashback.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.cashback.data.Cat;
import com.cashback.data.HotCoupon;
import com.cashback.data.Store;
import com.cashback.data.StoreCashBackItems;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
	//database version
	private static final int DATABASE_VERSION=9;

	// database name
	private static final String DATABASE_NAME="WhiteCashbackIn.db";

	//table names
	private static final String TABLE_FEATUREDSTORES="FeaturedStores";
	private static final String TABLE_HOMECOUPONS="HomeCoupons";
	private static final String TABLE_EARNING="PaymentTable";
	private static final String TABLE_STORECASHBACK="StoreCashback";
	private static final String TABLE_ALL_STORE="AllStore";
	private static final String TABLE_RECENT_STORES="RecentStores";
    private static final String TABLE_LASTUPDATED_STATUS="LastUpdatedStatus";
    private static final String TABLE_SHOPBYCATEGORY="shopByCategory";

	//column name for FeaturedStores
	private static final String KEY_ID="r_id";
	private static final String KEY_TITLE="r_title";
	private static final String KEY_STORE_URL="r_store_url";
	private static final String KEY_FAVOURITE="r_fav";
	private static final String KEY_DESCRIPTION="r_desc";
	private static final String KEY_CASHBACK="r_cashback";
	private static final String KEY_IMAGE="r_image";
	private static final String KEY_STORE_STATUS="r_status";

	//column name for PaymentTable
	private static final String KEY_AC="avail_cash";
	private static final String KEY_PC="pending_cash";
	private static final String KEY_DC="decline_cash";
	private static final String KEY_COR="cash_out_req";
	private static final String KEY_COP="cash_out_process";
	private static final String KEY_LIFE="life_cashback";
	private static final String KEY_MINBALANCE="min_balance";

	//column name for StoreCahback
	private static final String KEY_TID="trans_id";
	private static final String KEY_RETAILER="retailer";
	private static final String KEY_RETAILERID="retailer_id";
	private static final String KEY_AMOUNT="amount";
	private static final String KEY_DATECREATED="date_created";
	private static final String KEY_PROCESSDATE="process_date";
	private static final String KEY_PAYMENTTYPE="payment_type";
	private static final String KEY_STATUS="status";
	private static final String KEY_REFER_USER="refer_user";
	private static final String KEY_REFER="refer";

	//column name for HomeCoupon
	private static final String KEY_COUPON_ID="coupon_id";
	private static final String KEY_COUPON_TITLE="title";
	private static final String KEY_COUPON_CODE="code";
	private static final String KEY_COUPON_LINK="link";
	private static final String KEY_COUPON_STARTDATE="start_date";
	private static final String KEY_COUPON_EXPIRYDATE="expiry_date";
	private static final String KEY_COUPON_RETAILERID="retailer_id";
	private static final String KEY_COUPON_RETAILERTITLE="retailer_title";
	private static final String KEY_COUPON_RETAILERIMAGE="retailer_image";
	private static final String KEY_COUPON_CASHBACK="cashback";


	//Column name for Last-Updated Table
	private static final String KEY_LASTUPDATEDTIME_ALLSTORES="allStores";
	private static final String KEY_LASTUPDATEDTIME_ALLDEALS="allDeals";
	private static final String KEY_LASTUPDATEDTIME_HOMECOUPONS="homeCoupons";
	private static final String KEY_LASTUPDATED_HOMECOUPONS_LIMITOVER="homeCouponslimitOver";
	private static final String KEY_LASTUPDATEDTIME_FEATURED="featured";
	private static final String KEY_LASTUPDATED_FEATURED_LIMITOVER="featuredLimitOver";
	private static final String KEY_LASTUPDATEDTIME_SHOPBYCATEGORY="shopByCategory";

	//Column name for Shop-By-Category Table
    private static final String KEY_SHOPBYCATEGORY_ID="category_id";
	private static final String KEY_SHOPBYCATEGORY_NAME="category_name";

	private static DatabaseHandler mInstance=null;
	private static Context mContext;

	public static synchronized DatabaseHandler getInstance(Context ctx)
	{
		if(mInstance==null)
		{
			mInstance=new DatabaseHandler(ctx.getApplicationContext());
			mContext=ctx;
		}
		return mInstance;
	}

	public DatabaseHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		//feature store table
		String CREATE_TABLE=" CREATE TABLE " + TABLE_FEATUREDSTORES + " ( " + KEY_ID + " Text, " + KEY_TITLE + " Text, " + KEY_STORE_URL + " Text, " + KEY_FAVOURITE + " Text, " + KEY_DESCRIPTION + " Text, " + KEY_CASHBACK + " Text, " + KEY_IMAGE + " Text, "+ KEY_STORE_STATUS + " Text)";
		//payment table
		String CREATE_PAYMENT_TABLE=" CREATE TABLE " + TABLE_EARNING + " ( " + KEY_AC + " Text, " + KEY_PC + " Text, " + KEY_DC + " Text, " + KEY_COR + " Text, " + KEY_COP + " Text, " + KEY_LIFE + " Text, " + KEY_MINBALANCE + " Text)";

		//StoreCashbackTable
		String CREATE_STORECASHBACK_TABLE=" CREATE TABLE " + TABLE_STORECASHBACK + " ( " + KEY_TID + " Text, " + KEY_RETAILER + " Text, " + KEY_RETAILERID + " Text, " + KEY_AMOUNT + " Text, " + KEY_DATECREATED + " Text, " + KEY_PROCESSDATE + " Text, " + KEY_PAYMENTTYPE + " Text, " + KEY_STATUS + " Text, "+ KEY_REFER +" Text, " + KEY_REFER_USER + " Text)";

		//All store table
		String CREATE_All_STORE_TABLE=" CREATE TABLE " + TABLE_ALL_STORE + " ( " + KEY_ID + " Text, " + KEY_TITLE + " Text, " + KEY_STORE_URL + " Text, " + KEY_FAVOURITE + " Text, " + KEY_DESCRIPTION + " Text, " + KEY_CASHBACK + " Text, " + KEY_IMAGE + " Text, " +KEY_STORE_STATUS +" Text)";

		//All store table
		String CREATE_RECENT_STORE_TABLE=" CREATE TABLE " + TABLE_RECENT_STORES + " ( " + KEY_ID + " Text, " + KEY_TITLE + " Text, " + KEY_STORE_URL + " Text, " + KEY_FAVOURITE + " Text, " + KEY_DESCRIPTION + " Text, " + KEY_CASHBACK + " Text, " + KEY_IMAGE + " Text, " + KEY_STORE_STATUS +" Text)";

		//Last Updated  Table
		String CREATE_LASTUPDATEDSTATUS_TABLE=" CREATE TABLE " + TABLE_LASTUPDATED_STATUS +" ( "+ KEY_LASTUPDATEDTIME_ALLSTORES +" Text," +KEY_LASTUPDATEDTIME_FEATURED+" Text,"+KEY_LASTUPDATEDTIME_HOMECOUPONS+" Text,"+KEY_LASTUPDATEDTIME_ALLDEALS+" Text, "+KEY_LASTUPDATED_FEATURED_LIMITOVER+" Text, "+KEY_LASTUPDATED_HOMECOUPONS_LIMITOVER+" Text, "+KEY_LASTUPDATEDTIME_SHOPBYCATEGORY+" Text )";

		//Home Coupons Table
		String CREATE_HOMECOUPONS_TABLE=" CREATE TABLE " +  TABLE_HOMECOUPONS + " ( "+KEY_COUPON_ID +" Text, "+KEY_COUPON_TITLE+" Text, "+KEY_COUPON_CODE +" Text, "+KEY_COUPON_LINK+" Text, "+KEY_COUPON_STARTDATE+" Text, "+KEY_COUPON_EXPIRYDATE+" Text, "+KEY_COUPON_RETAILERID+" Text, "+KEY_COUPON_RETAILERTITLE+ " Text, "+KEY_COUPON_RETAILERIMAGE+" Text, "+KEY_COUPON_CASHBACK+" Text )";

		//Shop Category Table
        String CREATE_SHOPBYCATEGORY_TABLE=" CREATE TABLE "+TABLE_SHOPBYCATEGORY+ " ( "+KEY_SHOPBYCATEGORY_ID+" Text, " +KEY_SHOPBYCATEGORY_NAME+" Text )";


		db.execSQL(CREATE_TABLE);
		db.execSQL(CREATE_PAYMENT_TABLE);
		db.execSQL(CREATE_STORECASHBACK_TABLE);
		db.execSQL(CREATE_All_STORE_TABLE);
		db.execSQL(CREATE_RECENT_STORE_TABLE);
	    db.execSQL(CREATE_LASTUPDATEDSTATUS_TABLE);
	    db.execSQL(CREATE_HOMECOUPONS_TABLE);
		db.execSQL(CREATE_SHOPBYCATEGORY_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// DROP OLD TABLE
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_FEATUREDSTORES);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_EARNING);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_STORECASHBACK);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_ALL_STORE);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_RECENT_STORES);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_LASTUPDATED_STATUS);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_HOMECOUPONS);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_SHOPBYCATEGORY);

		onCreate(db);
	}

	//delete Recent Stores Table
	public void dropRecentStores() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from "+TABLE_RECENT_STORES);
		//db.close();
	}

	//delete Featured stores Table
	public void deleteFeaturedStoresTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from "+ TABLE_FEATUREDSTORES);
		db.execSQL("VACUUM");
		//db.close();
	}

	//delete Payment Table
	public void deletePaymentTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from "+ TABLE_EARNING);
		db.execSQL("VACUUM");
		//db.close();
	}

	//Delete StoreCashback Table
	public void deleteStoreCashbackTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from "+ TABLE_STORECASHBACK);
		db.execSQL("VACUUM");
		//db.close();
	}

	//Delete AllStore Table
	public void deleteAllStore() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from "+ TABLE_ALL_STORE);
		db.execSQL("VACUUM");
		//db.close();
	}

	//Delete RecentStores Table
	public void deleteRecentSTore() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from "+ TABLE_RECENT_STORES);
		db.execSQL("VACUUM");
		Log.e("data is deleted", " Successfully");
		//db.close();
	}

	//---deletes a particular title---
	public boolean deleteStoreRow(String name) {
		SQLiteDatabase db=this.getReadableDatabase();
		return db.delete(TABLE_RECENT_STORES, KEY_ID + "=" + name, null) > 0;
	}

	//---deletes hot-coupons---//
	public void deleteHotCoupons() {
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL("Delete From "+TABLE_HOMECOUPONS);
	}

	//----deletes Shop Category----//
	public void deleteShopCategory(){
		SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("Delete From "+TABLE_SHOPBYCATEGORY);
	}

	/**
	 * Insert All Stores
	 */
	public void insertRecentStores(Store idModel)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues value=new ContentValues();
		value.put(KEY_ID, idModel.store_id);
		value.put(KEY_TITLE, idModel.name);
		value.put(KEY_STORE_URL, idModel.url);
		value.put(KEY_FAVOURITE, idModel.favourite);
		value.put(KEY_DESCRIPTION, idModel.desc);
		value.put(KEY_CASHBACK, idModel.cashback);
		value.put(KEY_IMAGE, idModel.image);
		value.put(KEY_STORE_STATUS, idModel.status);
		db.insert(TABLE_RECENT_STORES, null, value);
		//db.close();
		//Log.e("data is INSERTED", " Successfully");
	}


	/*
	* INsert Data into Earning table
	*/
	public void insertEarning(String myEarning)
	{
		String a[]=myEarning.split(":");
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues value=new ContentValues();
		value.put(KEY_AC,a[0]);
		value.put(KEY_PC,a[1]);
		value.put(KEY_DC,a[2]);
		value.put(KEY_COR,a[3]);
		value.put(KEY_COP,a[4]);
		value.put(KEY_LIFE,a[5]);
		value.put(KEY_MINBALANCE,a[6]);
		db.insert(TABLE_EARNING, null, value);
		//db.close();
		//Log.e("Earning Is INSERTED", " Successfully");
	}

	/*
	* INsert Data into Earning table
	*/
	public void insertStoreCashback(StoreCashBackItems items)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues value=new ContentValues();
		value.put(KEY_TID,items.getTransaction_Id());
		value.put(KEY_RETAILER,items.getRetailer());
		value.put(KEY_RETAILERID,items.getRetailer_Id());
		value.put(KEY_AMOUNT,items.getAmount());
		value.put(KEY_DATECREATED,items.getDateCreated());
		value.put(KEY_PROCESSDATE,items.getProcessDate());
		value.put(KEY_PAYMENTTYPE,items.getPayment_Type());
		value.put(KEY_STATUS,items.getStatus());
		value.put(KEY_REFER_USER,items.getReferUser());
		value.put(KEY_REFER,items.getReffer_To());
		db.insert(TABLE_STORECASHBACK, null, value);
		//db.close();
		Log.e("Strecashck Is INSERTED", " Successfully");
	}




	public Cursor getSQLiteData(String flag)
	{
		SQLiteDatabase db=this.getReadableDatabase();
		String selectQuerry=null;
		if(flag.equalsIgnoreCase("feature")){
		selectQuerry="SELECT * from "+TABLE_FEATUREDSTORES;
		}
		else if(flag.equalsIgnoreCase("earning"))
		{
			selectQuerry="SELECT * from "+TABLE_EARNING;
		}
		else if(flag.equalsIgnoreCase("staore_cashback"))
		{
			selectQuerry="SELECT * from "+TABLE_STORECASHBACK;
		}
		else if(flag.equalsIgnoreCase("all_stores"))
		{
			selectQuerry="SELECT * from "+TABLE_ALL_STORE;
		}
		else if(flag.equalsIgnoreCase("recent_stores"))
		{
			selectQuerry="SELECT * from "+TABLE_RECENT_STORES;
		}
		else if(flag.equalsIgnoreCase("home_coupons"))
		{
			selectQuerry="SELECT * from "+TABLE_HOMECOUPONS;
		}
		else if(flag.equalsIgnoreCase("shop_bycategory"))
		{
			selectQuerry="SELECT * from "+TABLE_SHOPBYCATEGORY;
		}

		Cursor cursor=db.rawQuery(selectQuerry, null);
		Log.d("result", "" + selectQuerry);
		Log.d("row count", "" + cursor.getCount());
		//db.close();
		return cursor;
	}

	public Cursor checkSQLiteData(String id)
	{
		SQLiteDatabase db=this.getReadableDatabase();
		String selectQuerry="SELECT * FROM "+TABLE_RECENT_STORES+" WHERE " + KEY_ID +"="+ id;
		Cursor cursor=db.rawQuery(selectQuerry, null);
		Log.d("result", "" + selectQuerry);
		Log.d("row count", "" + cursor.getCount());
		//db.close();
		return cursor;
	}

	public Cursor getSelectedIndexSQLiteData(String index)
	{
		SQLiteDatabase db=this.getReadableDatabase();
		String selectQuerry="SELECT * FROM "+TABLE_ALL_STORE+" WHERE " + KEY_TITLE+" LIKE '"+ index+"%'";
		Cursor cursor=db.rawQuery(selectQuerry, null);
		Log.d("result", "" + selectQuerry);
		Log.d("row count", "" + cursor.getCount());
		//db.close();
		return cursor;
	}

	public void updateRecentFav(String storeId,String flag)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues value=new ContentValues();
		value.put(KEY_ID, storeId);
		value.put(KEY_FAVOURITE, flag);
		db.update(TABLE_RECENT_STORES, value, KEY_ID + " = " + storeId,null);
		//db.close();
		Log.e("data is UPDATED", " Successfully");
	}

	//-----Update LastUpdated AllStores Time----//
	public void updateLastUpdatedAllStoresTime(long timestamp) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues lContentValues=new ContentValues();
		lContentValues.put(KEY_LASTUPDATEDTIME_ALLSTORES,timestamp);
		int count=db.update(TABLE_LASTUPDATED_STATUS,lContentValues,"",null);
	    if(count==0){
			lContentValues=new ContentValues();
			lContentValues.put(KEY_LASTUPDATEDTIME_ALLSTORES,timestamp);
			db.insert(TABLE_LASTUPDATED_STATUS,null,lContentValues);
		}
		//db.close();
	}


	//----Update LastUpdated HomeCoupons Time-----//
	public void updateLastUpdatedHomeCouponsTime(long timeStamp)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues lContentValues=new ContentValues();
		lContentValues.put(KEY_LASTUPDATEDTIME_HOMECOUPONS,timeStamp);
		int count=db.update(TABLE_LASTUPDATED_STATUS,lContentValues,"",null);
		if(count==0){
			lContentValues=new ContentValues();
			lContentValues.put(KEY_LASTUPDATEDTIME_HOMECOUPONS,timeStamp);
			db.insert(TABLE_LASTUPDATED_STATUS,null,lContentValues);
		}
		//db.close();
	}


	//----Update LastUpdated ShopByCategory Time-----//
	public void updateLastUpdatedShopCategoryTime(long timeStamp)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues lContentValues=new ContentValues();
		lContentValues.put(KEY_LASTUPDATEDTIME_SHOPBYCATEGORY,timeStamp);
		int count=db.update(TABLE_LASTUPDATED_STATUS,lContentValues,"",null);
		if(count==0){
			lContentValues=new ContentValues();
			lContentValues.put(KEY_LASTUPDATEDTIME_SHOPBYCATEGORY,timeStamp);
			db.insert(TABLE_LASTUPDATED_STATUS,null,lContentValues);
		}
		//db.close();
	}

	//----Update LastUpdated Features Time-----//
	public void updateLastUpdatedFeaturesTime(long timeStamp)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues lContentValues=new ContentValues();
		lContentValues.put(KEY_LASTUPDATEDTIME_FEATURED,timeStamp);
		int count=db.update(TABLE_LASTUPDATED_STATUS,lContentValues,"",null);
		if(count==0){
			lContentValues=new ContentValues();
			lContentValues.put(KEY_LASTUPDATEDTIME_FEATURED,timeStamp);
			db.insert(TABLE_LASTUPDATED_STATUS,null,lContentValues);
		}
		//db.close();
	}


	//-----Update LastUpdated HomeCoupons limitOver-----//
	public void updateLastUpdatedHomeCopounsLastOver(String status) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues lContentValues=new ContentValues();
		lContentValues.put(KEY_LASTUPDATED_HOMECOUPONS_LIMITOVER,status);
		int count=db.update(TABLE_LASTUPDATED_STATUS,lContentValues,"",null);
		if(count==0){
			lContentValues=new ContentValues();
			lContentValues.put(KEY_LASTUPDATED_HOMECOUPONS_LIMITOVER,status);
			db.insert(TABLE_LASTUPDATED_STATUS,null,lContentValues);
		}
		//db.close();
	}

	//-----Update LastUpdated Features limitOver------//
	public void updateLastUpdatedFeaturedLastOver(String status){
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues lContentValues=new ContentValues();
		lContentValues.put(KEY_LASTUPDATED_FEATURED_LIMITOVER,status);
		int count=db.update(TABLE_LASTUPDATED_STATUS,lContentValues,"",null);
		if(count==0){
			lContentValues=new ContentValues();
			lContentValues.put(KEY_LASTUPDATED_FEATURED_LIMITOVER,status);
			db.insert(TABLE_LASTUPDATED_STATUS,null,lContentValues);
		}
	}

	public String getLastUpdatedTime(String value){
		String lastUpdateTime=null;

		SQLiteDatabase db=this.getWritableDatabase();
		String lQuery="select*from "+TABLE_LASTUPDATED_STATUS;
		Cursor lCursor=db.rawQuery(lQuery,null);

		if(lCursor.getCount()>0) {

			if(lCursor.moveToFirst()) {

				do {
					if (value.equalsIgnoreCase("all_stores")) {
						lastUpdateTime=lCursor.getString(0);
					}
					else if (value.equalsIgnoreCase("features")) {
						lastUpdateTime=lCursor.getString(1);
					}
					else if (value.equalsIgnoreCase("home_coupons")) {
                         lastUpdateTime=lCursor.getString(2);
					}
					else if (value.equalsIgnoreCase("all_deals")) {
						lastUpdateTime=lCursor.getString(3);
					}
					else if (value.equalsIgnoreCase("shop_bycategory")) {
						lastUpdateTime=lCursor.getString(6);
					}
				}
				while(lCursor.moveToNext());
			}
		}
		return lastUpdateTime;
	}

	public String getLimitOverStatus(String value){
		String limitOverStatus=null;

		SQLiteDatabase db=this.getWritableDatabase();
		String lQuery="Select*from "+TABLE_LASTUPDATED_STATUS;
		Cursor lCursor=db.rawQuery(lQuery,null,null);

		if(lCursor.getCount()>0){

			if(lCursor.moveToFirst()){
				do{
					if(value.equalsIgnoreCase("home_coupons")){
						limitOverStatus=lCursor.getString(5);
					}

					else if (value.equalsIgnoreCase("features")) {
						limitOverStatus=lCursor.getString(4);
					}
				}
				while (lCursor.moveToNext());
			}
		}
		return limitOverStatus;
	}


	/**
	 * Insert All Stores
	 */
	public void insertAllStores(Store idModel)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues value=new ContentValues();
		value.put(KEY_ID, idModel.store_id);
		value.put(KEY_TITLE, idModel.name);
		value.put(KEY_STORE_URL, idModel.url);
		value.put(KEY_FAVOURITE, idModel.favourite);
		value.put(KEY_DESCRIPTION, idModel.desc);
		value.put(KEY_CASHBACK, idModel.cashback);
		value.put(KEY_IMAGE, idModel.image);
		value.put(KEY_STORE_STATUS, idModel.status);
		db.insert(TABLE_ALL_STORE, null, value);
		//db.close();
		//Log.e("data is INSERTED", " Successfully");
	}


	public ArrayList<Store> getAllStores(){
		ArrayList<Store> storeList=new ArrayList<Store>();
		Cursor cursor=this.getSQLiteData("all_stores");
		if(cursor.getCount()>0){
			if(cursor.moveToFirst()){
				do{
					Store lStore=new Store(
							cursor.getString(0),
							cursor.getString(1),
							cursor.getString(5),
							cursor.getString(7),
							cursor.getString(2),
							cursor.getString(6),
							cursor.getString(4),
							cursor.getString(3),
							cursor.getString(3)
					);
					storeList.add(lStore);
				}
				while (cursor.moveToNext());
			}
		}
		else{

		}
		cursor.close();
		return storeList;
	}


	/**
	 * Insert FeaturedStores new lable
	 */
	public void insertFeaturedStores(Store idModel)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues value=new ContentValues();
		value.put(KEY_ID, idModel.store_id);
		value.put(KEY_TITLE, idModel.name);
		value.put(KEY_STORE_URL, idModel.url);
		value.put(KEY_FAVOURITE, idModel.favourite);
		value.put(KEY_DESCRIPTION, idModel.desc);
		value.put(KEY_CASHBACK, idModel.cashback);
		value.put(KEY_IMAGE, idModel.image);
		value.put(KEY_STORE_STATUS, idModel.status);
		long id=db.insert(TABLE_FEATUREDSTORES, null, value);
		//dbclose();
		//Log.e("data is INSERTED", " Successfully");
	}


	public ArrayList<Store> getFeaturedStores(){
		ArrayList<Store> featuredStoresList=new ArrayList<Store>();
		Cursor cursor=this.getSQLiteData("feature");
		if(cursor.getCount()>0){

			if(cursor.moveToFirst()){
				do{
					Store lStore=new Store(
							cursor.getString(0),
							cursor.getString(1),
							cursor.getString(5),
							cursor.getString(7),
							cursor.getString(2),
							cursor.getString(6),
							cursor.getString(4),
							cursor.getString(3),
							cursor.getString(3)
					);
					featuredStoresList.add(lStore);
				}
				while (cursor.moveToNext());
			}
		}
		else{
		}
		cursor.close();
		return featuredStoresList;
	}

	public void insertHomeCoupons(HotCoupon hotCoupon){
		SQLiteDatabase db=this.getWritableDatabase();

		ContentValues lContentValues=new ContentValues();
		lContentValues.put(KEY_COUPON_ID,hotCoupon.getCoupon_id());
		lContentValues.put(KEY_COUPON_TITLE,hotCoupon.getTitle());
		lContentValues.put(KEY_COUPON_CASHBACK,hotCoupon.getCashback());
		lContentValues.put(KEY_COUPON_CODE,hotCoupon.getCode());
		lContentValues.put(KEY_COUPON_STARTDATE,hotCoupon.getStart_date());
		lContentValues.put(KEY_COUPON_EXPIRYDATE,hotCoupon.getExpiry_date());
		lContentValues.put(KEY_COUPON_LINK,hotCoupon.getLink());
		lContentValues.put(KEY_COUPON_RETAILERID,hotCoupon.getRetailer_id());
		lContentValues.put(KEY_COUPON_RETAILERTITLE,hotCoupon.getRetailer_title());
		lContentValues.put(KEY_COUPON_RETAILERIMAGE,hotCoupon.getRetailer_image());

		long id=db.insert(TABLE_HOMECOUPONS,null,lContentValues);
		//db.close();
	}

	public ArrayList<HotCoupon> getAllHomeCoupons(){
		ArrayList<HotCoupon> hotCouponsList=new ArrayList<HotCoupon>();

		Cursor cursor=this.getSQLiteData("home_coupons");

		if(cursor.getCount()>0){

			if(cursor.moveToFirst()){
				do {
		            HotCoupon lHotCoupon=new HotCoupon();
					lHotCoupon.setCoupon_id(cursor.getString(0));
					lHotCoupon.setTitle(cursor.getString(1));
					lHotCoupon.setCode(cursor.getString(2));
					lHotCoupon.setLink(cursor.getString(3));
					lHotCoupon.setStart_date(cursor.getString(4));
					lHotCoupon.setExpiry_date(cursor.getString(5));
					lHotCoupon.setRetailer_id(cursor.getString(6));
					lHotCoupon.setRetailer_title(cursor.getString(7));
					lHotCoupon.setRetailer_image(cursor.getString(8));

					hotCouponsList.add(lHotCoupon);
				}
				while (cursor.moveToNext());
			}
		}
		else{
		}
		return hotCouponsList;
	}


	//insert ShopCategory data
	public void insertShopCategory(Cat category){
		SQLiteDatabase db=this.getWritableDatabase();

		ContentValues lContentValues=new ContentValues();
		lContentValues.put(KEY_SHOPBYCATEGORY_ID,category.id);
		lContentValues.put(KEY_SHOPBYCATEGORY_NAME,category.name);

		db.insert(TABLE_SHOPBYCATEGORY,null,lContentValues);
	}


	//get all shop categories
	public ArrayList<Cat>  getShopCategories(){

		ArrayList<Cat> shopCategoryList=new ArrayList<>();

		Cursor lCursor=this.getSQLiteData("shop_bycategory");

		if((lCursor!=null)&&(lCursor.getCount()>0)){

			if(lCursor.moveToFirst()){
				do {
					Cat lCat=new Cat(lCursor.getString(0),lCursor.getString(1));
					shopCategoryList.add(lCat);
				}
				while (lCursor.moveToNext());
			}
		}
		return shopCategoryList;
	}



}
