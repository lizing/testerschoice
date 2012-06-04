package com.testerschoice.moneybook;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.testerschoice.moneybook.MoneyBook.MoneyBookColumns;

public class ViewItemActivity extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //setContentView(R.layout.view_item);
	    
	    Intent i = getIntent();
	    
	    String year = i.getStringExtra("year");
	    String month = i.getStringExtra("month");
	    String day = i.getStringExtra("day");
	    
	    String[] projection = new String[]{
	    	MoneyBookColumns._ID,
	    	MoneyBookColumns.ITEM,
	    	MoneyBookColumns.ITEM_PRICE
	    };
	    
	    String selection = MoneyBookColumns.PURCHASE_DATE_YEAR + "=? and " + MoneyBookColumns.PURCHASE_DATE_MONTH + "=? and " + MoneyBookColumns.PURCHASE_DATE_DAY + "=?"; 
	    
	    Cursor c = getContentResolver().query(MoneyBookColumns.CONTENT_URI, projection, selection, new String[]{year, month, day}, null);
	    
	    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.itemlist, c, new String[]{MoneyBookColumns.ITEM, MoneyBookColumns.ITEM_PRICE}, new int[]{R.id.item_name, R.id.item_price});
	    setListAdapter(adapter);
	}

	// 아이템 선택되었을 시 불리는 메소드
	// 구현해야함
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		ListAdapter adapter = l.getAdapter();
		long selectedItemId;
		
		String[] lists = new String[]{"편집", "삭제"};
		
		new AlertDialog.Builder(this)
			.setTitle(R.string.choice)
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			})
			.setSingleChoiceItems(lists, -1, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch(which){
					case 0:
						break;
					case 1:
						break;
					}
				}
			})
			.show();
	}

}
