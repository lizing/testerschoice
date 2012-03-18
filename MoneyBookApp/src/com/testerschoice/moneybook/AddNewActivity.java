package com.testerschoice.moneybook;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.testerschoice.moneybook.MoneyBook.MoneyBookColumns;

public class AddNewActivity extends Activity {

	Button mSubmitButton;
	Button mCancelButton;
	EditText mItem;
	EditText mPrice;
	DatePicker mDate;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.add_new);
	    
	    mSubmitButton = (Button) findViewById(R.id.submit_button);
	    mCancelButton = (Button) findViewById(R.id.cancel_button);
	    mItem = (EditText) findViewById(R.id.purchase_item_name);
	    mPrice = (EditText) findViewById(R.id.purchase_price);
	    mDate = (DatePicker) findViewById(R.id.purchase_date);
	    
	    mSubmitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String item = mItem.getText().toString();
				String itemPrice = mPrice.getText().toString();
				int day = mDate.getDayOfMonth();
				int month = mDate.getMonth();
				int year = mDate.getYear();
				
				if(item.isEmpty()){
					return;
				}
				
				if(itemPrice.isEmpty()){
					return;
				}
				
				int price = Integer.parseInt(mPrice.getText().toString());
				
				ContentValues values = new ContentValues();
				values.put(MoneyBookColumns.ITEM, item);
				values.put(MoneyBookColumns.ITEM_PRICE, price);
				values.put(MoneyBookColumns.PURCHASE_DATE_YEAR, year);
				values.put(MoneyBookColumns.PURCHASE_DATE_MONTH, month);
				values.put(MoneyBookColumns.PURCHASE_DATE_DAY, day);
				
				Uri newUri = getContentResolver().insert(MoneyBookColumns.CONTENT_URI, values);
				
				if(newUri != null){
					Toast.makeText(AddNewActivity.this, "입력 완료", Toast.LENGTH_SHORT);
				} else {
					Toast.makeText(AddNewActivity.this, "입력 실패, 입력 정보를 확인해 주세요", Toast.LENGTH_SHORT);
					mItem.setText("");
					mPrice.setText("");
				}
				
			}
		});
	    
	    mCancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
