package com.testerschoice.moneybook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.testerschoice.moneybook.MoneyBook.MoneyBookColumns;

public class MainActivity extends Activity {

	Button addButton;
	ListView mListView;
	
	private static final int INDEX_COLUMN_YEAR = 2;
	private static final int INDEX_COLUMN_MONTH = 3;
	private static final int INDEX_COLUMN_DAY = 4;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    addButton = (Button) findViewById(R.id.add_button);
	    mListView = (ListView) findViewById(R.id.listView1);
	    
	    String[] items;
	    items = getItems();
	    
	    if(items == null){
	    	items = new String[1];
	    	items[0] = "가계부에 추가된 내용이 없습니다.";
	    }
	   
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
	    
	    for(int i = 0; i < items.length; i++){
	    	adapter.add(items[i]);
	    }
	    
	    mListView.setAdapter(adapter);
	    
	    addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, AddNewActivity.class);
				startActivity(intent);
			}
		});
	    
	}
	
	public String[] getItems(){
		String[] items;
		Cursor c = getContentResolver().query(MoneyBookColumns.CONTENT_URI, null, null, null, null);
		
		if(!c.moveToFirst()){
			return null;
		}
		
		int counter = 0;
		int size = c.getCount();
		items = new String[size];
		
		while(c.moveToNext()){
			int year = c.getInt(INDEX_COLUMN_YEAR);
			int month = c.getInt(INDEX_COLUMN_MONTH);
			int day = c.getInt(INDEX_COLUMN_DAY);
			
			items[counter] = year + "년 " + month + "월 " + day + "일";
			
			counter++;
		}
		
		return items;
	}
}
