package com.testerschoice.moneybook;

import java.util.ArrayList;
import java.util.StringTokenizer;

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
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import android.widget.Toast;

import com.testerschoice.moneybook.MoneyBook.MoneyBookColumns;

public class MainActivity extends Activity {

	Button mAddButton, mDelAllButton;
	ListView mListView;
	ArrayAdapter<String> adapter;
	ArrayList<String> items;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    mAddButton = (Button) findViewById(R.id.add_button);
	    mDelAllButton = (Button) findViewById(R.id.del_all_button);
	    mListView = (ListView) findViewById(R.id.listView1);
	    items = new ArrayList<String>();
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
	    displayList();
	    
	    mAddButton.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, AddNewActivity.class);
				startActivity(intent);
			}
		});
	    
	    mDelAllButton.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getContentResolver().delete(MoneyBookColumns.CONTENT_URI, null, null);
				Toast.makeText(MainActivity.this, "내용을 모두 삭제하였습니다.", Toast.LENGTH_SHORT).show();
				items.clear();
				adapter.notifyDataSetChanged();
				displayList();
			}
		});
	    
	    mListView.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				// 아직 구현되지 않음
				// 리스트뷰를 선택했을 때 해당 아이템의 정보를 볼 수 있는 새 액티비티를 띄우는 메쏘드
				String[] data = new String[3];
				int i = 0;
				
				if(items.get(0).equals(getResources().getString(R.string.no_item))){
					return;
				}
				
				String s = items.get(position);
				StringTokenizer token = new StringTokenizer(s);
				while(token.hasMoreTokens()){
					data[i] = token.nextToken("-");
					i++;
				}
				
				Intent intent = new Intent(MainActivity.this, ViewItemActivity.class);
				intent.putExtra("year", data[0]);
				intent.putExtra("month", data[1]);
				intent.putExtra("day", data[2]);
				
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		displayList();
	}

	public void setItemLists(){
		Cursor c = getContentResolver().query(MoneyBookColumns.CONTENT_URI, null, null, null, null);
		
		if(c == null){
			return;
		}
		
		if(!c.moveToFirst()){
			return;
		}
		
		int indexOfYear = c.getColumnIndexOrThrow(MoneyBookColumns.PURCHASE_DATE_YEAR);
		int indexOfMonth = c.getColumnIndexOrThrow(MoneyBookColumns.PURCHASE_DATE_MONTH);
		int indexOfDay = c.getColumnIndexOrThrow(MoneyBookColumns.PURCHASE_DATE_DAY);
		
		items.clear();
		
		do{
			int year = c.getInt(indexOfYear);
			int month = c.getInt(indexOfMonth);
			int day = c.getInt(indexOfDay);
			
			String text = year + "-" + month + "-" + day;
			
			if(items.contains(text))
				continue;
			
			items.add(text);
		}while(c.moveToNext());
	}
	
	public void displayList(){
		setItemLists();
		
	    if(items.size() == 0){
	    	items.add(getResources().getString(R.string.no_item));
	    }
	    
	    mListView.setAdapter(adapter);
	}
}
