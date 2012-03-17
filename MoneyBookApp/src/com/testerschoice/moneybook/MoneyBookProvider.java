package com.testerschoice.moneybook;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.testerschoice.moneybook.MoneyBook.MoneyBookColumns;

public class MoneyBookProvider extends ContentProvider {

	private static final String TAG = "MoneyBookProvider";
	
	private static final String DATABASE_NAME = "moneybook.db";
    private static final int DATABASE_VERSION = 1;
    private static final String MONEYBOOK_TABLE_NAME = "items";
    
    private class DataBaseHelper extends SQLiteOpenHelper{

		public DataBaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + MONEYBOOK_TABLE_NAME + " (" 
			+ MoneyBookColumns._ID + "INTEGER PRIMARY KEY,"
			+ MoneyBookColumns.ITEM + " TEXT,"
			+ MoneyBookColumns.ITEM_PRICE + " INTEGER,"
			+ MoneyBookColumns.PURCHASE_DATE_YEAR + " INTEGER,"
			+ MoneyBookColumns.PURCHASE_DATE_MONTH + " INTEGER,"
			+ MoneyBookColumns.PURCHASE_DATE_DAY + " INTEGER"
			+ ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
		}
    }
    
    private DataBaseHelper mOpenHelper;
    
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		
		count = db.delete(MONEYBOOK_TABLE_NAME, where, whereArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		
		ContentValues values;
		if(initialValues == null){
			values = new ContentValues();
		} else {
			values = new ContentValues(initialValues);
		}
		
		if(values.containsKey(MoneyBookColumns.ITEM) == false){
			values.put(MoneyBookColumns.ITEM, "Untitled");
		}
		
		if(values.containsKey(MoneyBookColumns.ITEM_PRICE) == false){
			values.put(MoneyBookColumns.ITEM_PRICE, 0);
		}
		
		if(values.containsKey(MoneyBookColumns.PURCHASE_DATE_YEAR) == false){
			values.put(MoneyBookColumns.PURCHASE_DATE_YEAR, 0000);
		}
		
		if(values.containsKey(MoneyBookColumns.PURCHASE_DATE_MONTH) == false){
			values.put(MoneyBookColumns.PURCHASE_DATE_MONTH, 00);
		}
		
		if(values.containsKey(MoneyBookColumns.PURCHASE_DATE_DAY) == false){
			values.put(MoneyBookColumns.PURCHASE_DATE_DAY, 00);
		}
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(MONEYBOOK_TABLE_NAME, MoneyBookColumns.ITEM, values);
		if(rowId > 0){
			Uri moneyBookUri = ContentUris.withAppendedId(MoneyBookColumns.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(moneyBookUri, null);
			return moneyBookUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DataBaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(MONEYBOOK_TABLE_NAME);
		
		String orderBy;
		if(TextUtils.isEmpty(sortOrder)){
			orderBy = MoneyBookColumns.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}
		
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		
		c.setNotificationUri(getContext().getContentResolver(), uri);
		
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		count = db.update(MONEYBOOK_TABLE_NAME, values, where, whereArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
