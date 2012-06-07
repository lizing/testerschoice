package com.testerschoice.moneybook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
	EditText mYear;
	EditText mMonth;
	EditText mDay;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.add_new);
	    
	    mSubmitButton = (Button) findViewById(R.id.submit_button);
	    mCancelButton = (Button) findViewById(R.id.cancel_button);
	    mItem = (EditText) findViewById(R.id.purchase_item_name);
	    mPrice = (EditText) findViewById(R.id.purchase_price);
	    mYear = (EditText) findViewById(R.id.year);
	    mMonth = (EditText) findViewById(R.id.month);
	    mDay = (EditText) findViewById(R.id.day);
	    
	    mSubmitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String item = mItem.getText().toString();
				String itemPrice = mPrice.getText().toString();
				
				
				if(item.isEmpty()){
					// 알림 창 코드 추가
					String message = getResources().getString(R.string.input_name);
					displayDialog(message);
					return;
				}
				
				if(itemPrice.isEmpty()){
					//알림 창 코드 추가
					String message = getResources().getString(R.string.input_price);
					displayDialog(message);
					return;
				}
				
				if(mDay.getText().toString().isEmpty() || mMonth.getText().toString().isEmpty() || mYear.getText().toString().isEmpty()){
					String message = getResources().getString(R.string.input_day);
					displayDialog(message);
					return;
				}
				
				int day = Integer.parseInt(mDay.getText().toString());
				int month = Integer.parseInt(mMonth.getText().toString());
				int year = Integer.parseInt(mYear.getText().toString());
				
				if(day <= 0 || day > 31){
					String message = getResources().getString(R.string.input_day);
					displayDialog(message);
					return;
				}
				
				if(month <= 0 || month > 12){
					String message = getResources().getString(R.string.input_month);
					displayDialog(message);
					return;
				}
				
				if(year < 2000 || year > 9999){
					String message = getResources().getString(R.string.input_day);
					displayDialog(message);
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
					Toast.makeText(AddNewActivity.this, "입력 완료", Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(AddNewActivity.this, "입력 실패, 입력 정보를 확인해 주세요", Toast.LENGTH_SHORT).show();
					//mItem.setText("");
					//mPrice.setText("");
				}
				
			}
		});
	    
	    mCancelButton.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private void displayDialog(String message){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.alert);
		alert.setMessage(message);
		alert.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		alert.show();
	}

}
