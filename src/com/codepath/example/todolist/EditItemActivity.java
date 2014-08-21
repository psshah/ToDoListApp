package com.codepath.example.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {
	EditText etEditItem;
	Item selectedItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		selectedItem = (Item) getIntent().getExtras().get("item");
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		etEditItem.setText(selectedItem.getValue());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    public void saveEditedItem(View v) {
    	Intent data = new Intent();
    	selectedItem.setValue(etEditItem.getText().toString());
    	data.putExtra("item", selectedItem);
    	setResult(RESULT_OK, data);
    	finish();
    }
}
