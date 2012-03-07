package com.lizing.simple.singleactivityprovider.test;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import com.lizing.simple.singleactivityprovider.MyContentProvider;
import com.lizing.simple.singleactivityprovider.MySingleActivityProvider;
import com.lizing.simple.singleactivityprovider.SingleActivityProviderAppActivity;

public class TestCase2 extends ActivityProviderInstrumentationTestCase2<SingleActivityProviderAppActivity, MyContentProvider> {

	Activity activity;
	EditText text;
	Button addButton, delButton;
	
	public TestCase2() {
		super(SingleActivityProviderAppActivity.class,MyContentProvider.class, "com.lizing.simple.singleactivityprovider");
	}
	/*
	public void testPreConditions(){
		Activity a = getActivity();
		assertNotNull(a);
		text = (EditText) a.findViewById(com.lizing.simple.singleactivityprovider.R.id.editText1);
		assertNotNull(text);
		addButton = (Button) a.findViewById(com.lizing.simple.singleactivityprovider.R.id.btn_add);
		assertNotNull(addButton);
		delButton = (Button) a.findViewById(com.lizing.simple.singleactivityprovider.R.id.btn_del_all);
		assertNotNull(delButton);
		
	}
	*/
	/*
	public void testAdd(){
		Activity a = getActivity();
		ContentResolver resolver = a.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(MySingleActivityProvider.NoteColumns.NOTE, "New Test6");
		
		final Uri uri = resolver.insert(MySingleActivityProvider.NoteColumns.CONTENT_URI, values);
		assertNotNull(uri);
		
	}
	*/
	
	public void testAddUi(){
		Activity a = getActivity();
		//getInstrumentation().callActivityOnCreate(a, null);
		try {
			runTestOnUiThread(new Runnable() {
				
				//@Override
				public void run() {
					// TODO Auto-generated method stub
					text.requestFocus();
				}
			});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.sendKeys(KeyEvent.KEYCODE_T);
		this.sendKeys(KeyEvent.KEYCODE_E);
		this.sendKeys(KeyEvent.KEYCODE_S);
		this.sendKeys(KeyEvent.KEYCODE_T);
		this.sendKeys(KeyEvent.KEYCODE_L);
		this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
		
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		
	}
	

}
