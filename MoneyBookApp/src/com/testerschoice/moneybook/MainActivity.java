package com.testerschoice.moneybook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.testerschoice.moneybook.MoneyBook.MoneyBookColumns;

public class MainActivity extends Activity {

	Button addButton, mDelAllButton;
	ListView mListView;
	ArrayAdapter<String> adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    addButton = (Button) findViewById(R.id.add_button);
	    mDelAllButton = (Button) findViewById(R.id.del_all_button);
	    mListView = (ListView) findViewById(R.id.listView1);
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
	    
	    displayList();
	    
	    addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, AddNewActivity.class);
				startActivity(intent);
			}
		});
	    
	    mDelAllButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getContentResolver().delete(MoneyBookColumns.CONTENT_URI, null, null);
				Toast.makeText(MainActivity.this, "내용을 모두 삭제하였습니다.", Toast.LENGTH_SHORT);
				displayList();
			}
		});
	    
	    mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				// 아직 구현되지 않음
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		displayList();
	}

	public String[] getItems(){
		String[] items;
		Cursor c = getContentResolver().query(MoneyBookColumns.CONTENT_URI, null, null, null, null);
		
		if(c == null){
			return null;
		}
		
		if(!c.moveToFirst()){
			return null;
		}
		
		int counter = 0;
		int size = c.getCount();
		items = new String[size];
		
		int indexOfYear = c.getColumnIndexOrThrow(MoneyBookColumns.PURCHASE_DATE_YEAR);
		int indexOfMonth = c.getColumnIndexOrThrow(MoneyBookColumns.PURCHASE_DATE_MONTH);
		int indexOfDay = c.getColumnIndexOrThrow(MoneyBookColumns.PURCHASE_DATE_DAY);
		
		do{
			int year = c.getInt(indexOfYear);
			int month = c.getInt(indexOfMonth);
			int day = c.getInt(indexOfDay);
			
			items[counter] = year + "년 " + month + "월 " + day + "일";
			
			counter++;
		}while(c.moveToNext());
		
		return items;
	}
	
	public void displayList(){
		String[] items;
	    items = getItems();
	
	    if(items == null){
	    	items = new String[1];
	    	items[0] = "추가된 내용이 없습니다.";
	    }
	    
	    adapter.clear();
	    
	    for(int i = 0; i < items.length; i++){
	    	adapter.add(items[i]);
	    }
	    
	    mListView.setAdapter(adapter);
	}
}
