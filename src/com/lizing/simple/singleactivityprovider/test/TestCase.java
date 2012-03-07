package com.lizing.simple.singleactivityprovider.test;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import com.lizing.simple.singleactivityprovider.MyContentProvider;
import com.lizing.simple.singleactivityprovider.SingleActivityProviderAppActivity;

public class TestCase extends ActivityProviderInstrumentationTestCase2<SingleActivityProviderAppActivity, MyContentProvider> {

	Activity activity;
	EditText text;
	Button addButton;
	
	public TestCase(){
		super(SingleActivityProviderAppActivity.class, MyContentProvider.class, "com.lizing.simple.singleactivityprovider");
	}
	
	public void setUp() throws Exception{
		super.setUp();
		
		activity = getActivity();
		text = (EditText) activity.findViewById(com.lizing.simple.singleactivityprovider.R.id.editText1);
		addButton = (Button) activity.findViewById(com.lizing.simple.singleactivityprovider.R.id.btn_add);
	}
	
	public void testPreConditions(){
		assertNotNull(activity);
		assertNotNull(text);
		assertNotNull(addButton);
	}
	
	// Hi! Test1이란 값을 입력 후 추가 버튼 클릭
	// test.simpledb.db 에 추가 되어야 
	public void testAddToDb(){
		text.setText("Hi! Test1");
		addButton.performClick();
	}
}
