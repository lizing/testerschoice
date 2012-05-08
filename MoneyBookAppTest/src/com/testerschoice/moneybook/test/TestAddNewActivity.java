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
	DatePicker itemPurchaseDate;
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
		itemPurchaseDate = (DatePicker) testActivity.findViewById(com.testerschoice.moneybook.R.id.purchase_date);
		submitButton = (Button) testActivity.findViewById(com.testerschoice.moneybook.R.id.submit_button);
	}
	
	public void testPreConditions(){
		assertNotNull(testActivity);
		assertNotNull(itemName);
		assertNotNull(itemPrice);
		assertNotNull(itemPurchaseDate);
		assertNotNull(submitButton);
	}
	
	public void testAddNew(){
		itemName.setText("Peach");
		itemPrice.setText("2000");
		
		submitButton.performClick();
	}

}
