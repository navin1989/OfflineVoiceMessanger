package edu.cmu.pocketsphinx.demo;

//**************************************************************
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class newActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);

		ListView lViewSMS = (ListView) findViewById(R.id.listViewSMS);

		if (fetchInbox() != null) {
			ArrayAdapter adapter = new ArrayAdapter(this,
					android.R.layout.simple_list_item_1, fetchInbox());
			lViewSMS.setAdapter(adapter);

			lViewSMS.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					String product = ((TextView) view).getText().toString();

					// Launching new Activity on selecting single List Item
					Intent i = new Intent(getApplicationContext(),
							PocketSphinxAndroidDemo.class);
					// sending data to new activity
					i.putExtra("product", product);
					startActivity(i);
				}
			});
		}
	}

	public ArrayList fetchInbox() {
		ArrayList sms = new ArrayList();

		Uri uriSms = Uri.parse("content://sms/inbox");
		Cursor cursor = getContentResolver().query(uriSms,
				new String[] { "_id", "address", "date", "body" }, null, null,
				null);

		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			String address = cursor.getString(1);
			String body = cursor.getString(3);

			System.out.println("====== Mobile number = " + address + "\n");
			System.out.println("===== SMS Text = " + body);

			sms.add("Mobile Number is: " + address + " Your SMS is: " + body);
		}
		return sms;

	}
}