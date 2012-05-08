package com.lizing.simple.singleactivityprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lizing.simple.singleactivityprovider.MySingleActivityProvider.NoteColumns;

public class SingleActivityProviderAppActivity extends Activity {
	
	private EditText text; 
	private Button addButton;
	private Button delAllButton;
	private Button moveButton;
	private RadioButton radioButton;
	private CheckBox checkbox;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        text = (EditText) findViewById(R.id.editText1);
        addButton = (Button) findViewById(R.id.btn_add);
        delAllButton = (Button) findViewById(R.id.btn_del_all);
        moveButton = (Button) findViewById(R.id.btn_move);
        
        addButton.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String note = text.getText().toString();
				
				ContentValues values = new ContentValues();
				values.put(NoteColumns.NOTE, note);
				
				Uri newUri = null; 
				newUri = getContentResolver().insert(NoteColumns.CONTENT_URI, values);
				
				if(newUri != null){
					Toast.makeText(SingleActivityProviderAppActivity.this, "Success!", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(SingleActivityProviderAppActivity.this, "Fail!", Toast.LENGTH_LONG).show();
					finish();
				}
			}
		});
        
        delAllButton.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int count = getContentResolver().delete(NoteColumns.CONTENT_URI, null, null);
				CharSequence text = count + " Deleted";
				Toast.makeText(SingleActivityProviderAppActivity.this, text, Toast.LENGTH_LONG).show();
			}
		});
        
        moveButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SingleActivityProviderAppActivity.this, SecondActivity.class);
				startActivity(i);
			}
		});
    }
}