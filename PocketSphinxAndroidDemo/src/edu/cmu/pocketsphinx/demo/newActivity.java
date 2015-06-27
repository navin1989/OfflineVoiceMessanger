package edu.cmu.pocketsphinx.demo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class newActivity extends Activity {
	ListView mlistView;
	ArrayList<Message> sms = new ArrayList<Message>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);

		mlistView = (ListView) findViewById(R.id.msgView);
		populateMessageList();

	}
	
	public void populateMessageList() {
		fetchInboxMessages();

		if (fetchInboxMessages() != null) {

			mlistView.setAdapter(new datalist(newActivity.this));
		} else
		{

			Toast.makeText(getApplicationContext(), "No SMS", 4).show();
		}

		//_______________________________________________________________________________

	     	 mlistView.setOnItemClickListener(new OnItemClickListener() {
			 @Override
			 public void onItemClick(AdapterView<?> parent, View view,
			 int position, long id) {
		     fetchInboxMessages();		     
			 String product = (fetchInboxMessage()).toString();
			 
			 // Launching new Activity on selecting single List Item
			 Intent i = new Intent(getApplicationContext(),
			 PocketSphinxAndroidDemo.class);
			 // sending data to new activity
			 i.putExtra("product", product);
			 startActivity(i);
			 }
			 });
			Button btn = (Button) findViewById(R.id.btnCopy);
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					StartMain();
				}

				private void StartMain() {
					// TODO Auto-generated method stub
					startActivity(new Intent(newActivity.this,
							PocketSphinxAndroidDemo.class));
				}
			});
	}
	
	
	public ArrayList fetchInboxMessage() {

		ArrayList sms = new ArrayList();
		 
        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"},null,null,null);
 
        cursor.moveToFirst();
        while  (cursor.moveToNext())
        {
               String address = cursor.getString(1);
               String body = cursor.getString(3);
 
               sms.add(body);
        }
        return sms;
 
    }

	
//_______________________________________________________________________________________________________________________________________
	public ArrayList<Message> fetchInboxMessages() {

		Uri muriSms = Uri.parse("content://sms/inbox");
		Cursor mcursor = getContentResolver().query(muriSms,
				new String[] { "_id", "address", "date", "body" }, null, null,
				null);
		mcursor.moveToFirst();
		while (mcursor.moveToNext()) {

			Message mMessage = new Message();
			mMessage.setmAddress(mcursor.getString(mcursor
					.getColumnIndex("address")));
			mMessage.setmBody(mcursor.getString(mcursor.getColumnIndex("body")));
			mMessage.setmDate(mcursor.getString(mcursor.getColumnIndex("date")));

			sms.add(mMessage);

		}

		return sms;

	}

	// Set the Adapter for Listview

	class datalist extends BaseAdapter {

		public datalist(newActivity mainActivity) {

		}

		@Override
		public int getCount() {

			return sms.size();
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflator = getLayoutInflater();
			View row;
			row = inflator.inflate(R.layout.listadapter, parent, false);
			ImageView img1 = (ImageView) row.findViewById(R.id.inboxImage);
			TextView txt1 = (TextView) row.findViewById(R.id.txtBody);
			TextView txt2 = (TextView) row.findViewById(R.id.txtSender);

			Long timestamp = Long.parseLong(sms.get(position).getmDate());
			Calendar mcalendar = Calendar.getInstance();
			mcalendar.setTimeInMillis(timestamp);
			DateFormat mformatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

			txt1.setText(sms.get(position).getmBody());
			txt2.setText("Sent by" + sms.get(position).getmAddress() + "\n"
					+ mformatter.format(mcalendar.getTime()));

			return row;
		}

	}

}
