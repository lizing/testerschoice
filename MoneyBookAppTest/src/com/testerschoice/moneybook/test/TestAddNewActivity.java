package com.testerschoice.moneybook.test;

import android.app.Activity;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.testerschoice.moneybook.AddNewActivity;
import com.testerschoice.moneybook.MoneyBookProvider;

public class TestAddNewActivity extends
		ActivityProviderInstrumentationTestCase2<AddNewActivity, MoneyBookProvider> {
	
	Activity testActivity;
	EditText itemName;
	EditText itemPrice;
	EditText itemYear;
	EditText itemMonth;
	EditText itemDay;
	Button submitButton;
	
	public static final String AUTHORITY = "com.testerschoice.provider.MoneyBook";
	
	public TestAddNewActivity(){
		super(AddNewActivity.class, MoneyBookProvider.class, AUTHORITY);
	}
	
	public void setUp() throws Exception{
		super.setUp();

		testActivity = startActivity();
		itemName = (EditText) testActivity.findViewById(com.testerschoice.moneybook.R.id.purchase_item_name);
		itemPrice = (EditText) testActivity.findViewById(com.testerschoice.moneybook.R.id.purchase_price);
		itemYear = (EditText)testActivity.findViewById(com.testerschoice.moneybook.R.id.year);
		itemMonth = (EditText)testActivity.findViewById(com.testerschoice.moneybook.R.id.month);
		itemDay = (EditText)testActivity.findViewById(com.testerschoice.moneybook.R.id.day);
		submitButton = (Button) testActivity.findViewById(com.testerschoice.moneybook.R.id.submit_button);
	}
	
	public void testPreConditions(){
		assertNotNull(testActivity);
		assertNotNull(itemName);
		assertNotNull(itemPrice);
		assertNotNull(itemYear);
		assertNotNull(itemMonth);
		assertNotNull(itemDay);
		assertNotNull(submitButton);
	}
	
	public void testAddNew(){
		itemName.setText("Peach");
		itemPrice.setText("2000");
		
		submitButton.performClick();
	}

}
