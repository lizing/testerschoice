package com.lizing.simple.singleactivityprovider.test;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import com.lizing.simple.singleactivityprovider.MyContentProvider;
import com.lizing.simple.singleactivityprovider.SingleActivityProviderAppActivity;

public class TestCase3 extends
		ActivityMockProviderInstrumentationTestCase<SingleActivityProviderAppActivity, MyContentProvider> {

	Activity mTestActivity;
	EditText mText;
	Button mAddButton;
	Button mDelAllButton;
	
	public TestCase3(){
		super(SingleActivityProviderAppActivity.class, MyContentProvider.class, "com.lizing.simple.singleactivityprovider");
	}
	
	public void setUp() throws Exception{
		super.setUp();
		mTestActivity = startActivity();
		assertEquals(mTestActivity, getActivity());
		mText = (EditText) mTestActivity.findViewById(com.lizing.simple.singleactivityprovider.R.id.editText1);
		assertNotNull(mText);
		mAddButton = (Button) mTestActivity.findViewById(com.lizing.simple.singleactivityprovider.R.id.btn_add);
		assertNotNull(mAddButton);
		mDelAllButton = (Button) mTestActivity.findViewById(com.lizing.simple.singleactivityprovider.R.id.btn_del_all);
		assertNotNull(mDelAllButton);
	}
	
	public void testAdd(){
		mText.setText("Mock Provider Test");
		//mAddButton.performClick();
	}
	
	public void testDeleteAll(){
		//mDelAllButton.performClick();
	}
	
}
