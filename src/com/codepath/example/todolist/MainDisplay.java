package com.codepath.example.todolist;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainDisplay extends Activity {

	ArrayList<String> items;
	ListView lvItems;
	ArrayAdapter<String> itemsAdapter;
	EditText etNewItem;
	private final int REQUEST_CODE = 20;
	TextToSpeech tts;
	String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display);
        
        // instantiate list of items
        readItems();
        
        // instantiate an adapter with item list
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        
        // assign the adapter to listview, to display it's contents
        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
        
        intializeTextToSpeech();
        setupListViewListener();
    }


    private void intializeTextToSpeech() {
		// TODO Auto-generated method stub
    	tts = new TextToSpeech(MainDisplay.this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
				if(status == TextToSpeech.SUCCESS) {
					int result = tts.setLanguage(Locale.US);
					if(result==TextToSpeech.LANG_MISSING_DATA || 
							result==TextToSpeech.LANG_NOT_SUPPORTED) {
						//error
					}
				}
				else {
					//error	
				}
			}
		});

	}

    @Override
    protected void onStop() {
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onStop();
    }
    
    @Override
    protected void onStart() {
        if(tts == null){
        	intializeTextToSpeech();
        }
        super.onStart();
    }


    @Override
    protected void onResume() {
	    intializeTextToSpeech();
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_display, menu);
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
    
    public void addToDoItem(View v) {
    	etNewItem = (EditText) findViewById(R.id.etNewItem);
    	//items.add(etNewItem.getText().toString());
    	itemsAdapter.add(etNewItem.getText().toString());
    	etNewItem.setText("");
		saveItems();
		//mySaveItems();
    }


    public void readText(View v) {
    	convertTextToSpeech();
    }
    
    public void convertTextToSpeech() {
    	for(int i=0; i<items.size(); i++)
    	{
    		text = items.get(i).toString();
	        if(text==null || "".equals(text))
	        {
	            text = "Content not available";
	            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
	        }else {
	            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
	        }
    	}
    }

    
    public void setupListViewListener() {
    	lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
    	    @Override
    		 public boolean onItemLongClick(AdapterView<?> parent, View v,
    				 	int position, long rowId) {
    			 items.remove(position);
    			 itemsAdapter.notifyDataSetChanged();
    			 saveItems();
    			 //mySaveItems();
    			 return true;
    		 }
		});
    	
    	lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, 
    				int position, long id) {
    			// bring up edit activity with items[position]
    			Intent i = new Intent(MainDisplay.this, EditItemActivity.class);
    			Item selectedItem = new Item(position, items.get(position));
    			i.putExtra("item", selectedItem);
    			startActivityForResult(i, REQUEST_CODE);
    		}
		});
    }

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
    		Item updatedItem = (Item) data.getExtras().get("item");
    		items.set(updatedItem.getPosition(), updatedItem.getValue());
    		itemsAdapter.notifyDataSetChanged();
    		saveItems();
    	}
    }
    
    /* FILE FUNCTIONALITY */
    public void readItems() {
    	File fileDir = getFilesDir();
    	File todoFile = new File(fileDir, "todolist.txt");
    	try {
			items = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			items = new ArrayList<String>();
			e.printStackTrace();
		}
    }
    
    
    public void saveItems() {
    	File fileDir = getFilesDir();
    	File todoFile = new File(fileDir, "todolist.txt");
    	try {
			FileUtils.writeLines(todoFile, items);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    /* Another implementation of save items */
    public void mySaveItems() {
    	File fileDir = getFilesDir();
    	File todoFile = new File(fileDir, "todolist.txt");
        BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(todoFile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    	for(int i=0; i<items.size(); i++)
    	{
    		try {
				output.write(items.get(i).toString());
				output.write("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
