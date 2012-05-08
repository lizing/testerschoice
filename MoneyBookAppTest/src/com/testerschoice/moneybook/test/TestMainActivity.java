package com.testerschoice.moneybook.test;

import android.app.Activity;
import android.widget.Button;
import android.widget.ListView;

import com.testerschoice.moneybook.MainActivity;
import com.testerschoice.moneybook.MoneyBookProvider;
import com.testerschoice.moneybook.R;

public class TestMainActivity extends
		ActivityProviderInstrumentationTestCase2<MainActivity, MoneyBookProvider> {
	
	Activity testActivity;
	Button addButton, DelAllButton;
	ListView listView;
	
	public static final String AUTHORITY = "com.testerschoice.provider.MoneyBook";
	
	public TestMainActivity(){
		super(MainActivity.class, MoneyBookProvider.class, AUTHORITY);
	}
	
	public void setUp() throws Exception{
		super.setUp();
		testActivity = startActivity();
		addButton = (Button) testActivity.findViewById(R.id.add_button);
		DelAllButton = (Button) testActivity.findViewById(R.id.del_all_button);
		listView = (ListView) testActivity.findViewById(R.id.listView1);
	}
	
	public void testPreConditions(){
		assertNotNull(testActivity);
		assertNotNull(addButton);
		assertNotNull(DelAllButton);
		assertNotNull(listView);
	}
	
	public void testListViewCount(){
		final int expected = 2;
		final int actual = listView.getCount();
		assertEquals(expected, actual);
	}
	
	public void testListViewItemString(){
		final String expected = "2012년 3월 19일";
		final String actual = listView.getItemAtPosition(0).toString();
		assertEquals(expected, actual);
	}

}
