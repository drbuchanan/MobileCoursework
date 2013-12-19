package com.example.newproject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.text.Editable;

public class DiaryActivity extends Activity implements OnClickListener,
OnKeyListener{

	ArrayList<String>venues;
	ArrayAdapter<String>entries;
	
	ListView entryList;
	EditText editText;
	EditText input;
	Button button;
	

	//Context context;
	//public static final String PREFS_ENTRY = "Entry_preferences";
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
			//.getSharedPreferences(PREFS_ENTRY,
		//						MODE_PRIVATE);
	//SharedPreferences.Editor editor = prefs.edit();

	
	
	//static final String PREFS_NAME = "prefs_name";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diary);

		final Context context = this;
		//Allows user to move to different activity
		Intent intent = getIntent();
		//Enter your diary entry 
		editText = (EditText)findViewById(R.id.item);
		//Saves the diary entry to list
		button =(Button)findViewById(R.id.btnSave);
		//viewList
		entryList=(ListView)findViewById(R.id.entryList);
		//prefs = getSharedPreferences("diary entry",MODE_PRIVATE);
		//populateVenueList();
		//editText.setText(prefs.getString("tag", "default value"));
		button.setOnClickListener(this);	
		editText.setOnKeyListener(this);
	
		//Creates an array list which stores a string 
		//and stores in a view list in a very simple list
		venues = new ArrayList<String>();
		entries = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				venues);
		entryList.setAdapter(entries);
		prefs=PreferenceManager.getDefaultSharedPreferences(context);
        editor=prefs.edit();
        
		//String task = venues.toString();
		//entries.add(task);
		//entries.notifyDataSetChanged();
		//SavePreferences("Lists",task);
		//LoadPreferences();
		
        //allows user to click on a specific item within the list
		entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			
			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, final int i,
					long l) {
				// TODO Auto-generated method stub
				//Sets up the next layout that will be used to represent
				//the Dialog box
				LayoutInflater li = LayoutInflater.from(context);
				View promptsView = li.inflate(R.layout.item_view, null);
				//builds a dialog box
				AlertDialog.Builder adb = new AlertDialog.Builder(context);
				//Sets view to dialog box view
				adb.setView(promptsView);
				//final EditText userInput = (EditText)promptsView
						//.findViewById(R.id.editTextDialogUserInput);
				adb.setCancelable(false);
				//Creates a button that saves a list item
				adb.setPositiveButton("Save Entry", new DialogInterface.OnClickListener() 
				{
					//If button is clicked save that item to list
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//input.setText(userInput.getText());
						
						for(int i=0;i< venues.size();i++)
				        {
				        	editor.putString("val"+i, venues.get(i));
				        }
				        editor.putInt("size", venues.size());
				        editor.commit();
				        entries.notifyDataSetChanged();
						//String input = editText.getText().toString();
						//if(null != input && input.length()>0)
						//{
							//venues.add(input);
							//entries.notifyDataSetChanged();
						//}
					}
					//Creates another button which will delete entry from list
				}).setNegativeButton("Delete Entry", new DialogInterface.OnClickListener()
				{	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//removes the string from the list
						venues.remove(i);
						entries.notifyDataSetChanged();
					}
				});
				AlertDialog ad = adb.create();
				//shows dialog box
				adb.show();
			}
		});
	}
	/*protected void SavePreferences(String key, String value)
	{
		SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = data.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	protected void LoadPreferences()
	{
		SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(this);
		String dataSet = data.getString("Lists", "None Available");
		
		entries.add(dataSet);
		entries.notifyDataSetChanged();
	}*/
	
	//adds a new item to the array adaptor
	//notifies the adapter that this has been added
	//sets the edit text back to default
	private void addItems(String item)
	{
		if(item.length()>0)
		{
			this.entries.add(item);
			this.entries.notifyDataSetChanged();
			this.editText.setText("");
		}
	}
	
	/*private void makeTag(String tag)
	{
		String or = prefs.getString(tag, null);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("tag", tag);
		editor.commit();
	}*/
	
	/*public OnClickListener saveButtonListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if(editText.getText().length()>0)
			{
				makeTag(editText.getText().toString());
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(editText.getWindowToken(),0);
			}
		}
	};*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diary, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction()==KeyEvent.ACTION_DOWN&&keyCode==KeyEvent.KEYCODE_DPAD_CENTER)
			this.addItems(this.editText.getText().toString());
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//if button is pressed adds the edit text string
		//to the listview
		if(v==this.button)
		{
			this.addItems(this.editText.getText().toString());
		}
	}
	
	public void sendMessage(View view)
	{
		//allows user to move to main activity class
		//through button press
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
}
