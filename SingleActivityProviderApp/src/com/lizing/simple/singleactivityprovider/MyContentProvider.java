package com.lizing.simple.singleactivityprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.lizing.simple.singleactivityprovider.MySingleActivityProvider.NoteColumns;

public class MyContentProvider extends ContentProvider {

	private static final String TAG = "MyContentProvider";
	private static final String DATABASE_NAME = "simpledb.db";
	private static final int DATABASE_VERSION = 1;
    private static final String NOTES_TABLE_NAME = "notes";
    
    private DatabaseHelper mOpenHelper;
    
    private static class DatabaseHelper extends SQLiteOpenHelper{
    	DatabaseHelper(Context context){
    		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + NOTES_TABLE_NAME + " ("
					+ NoteColumns._ID + " INTEGER PRIMARY KEY,"
					+ NoteColumns.NOTE + " TEXT"
					+ ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
		}
    }
    
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        
        count = db.delete(NOTES_TABLE_NAME, where, whereArgs);
		
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		/*
		if(!uri.equals(NoteColumns.CONTENT_URI)){
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		*/
		ContentValues values;
		if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
		
		if (values.containsKey(NoteColumns.NOTE) == false) {
            values.put(NoteColumns.NOTE, "");
        }
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(NOTES_TABLE_NAME, NoteColumns.NOTE, values);
		if(rowId > 0){
			Uri noteUri = ContentUris.withAppendedId(NoteColumns.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
