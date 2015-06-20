package edu.cmu.pocketsphinx.demo;

import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PocketSphinxAndroidDemo extends Activity implements
		OnTouchListener, RecognitionListener {
	static {
		System.loadLibrary("pocketsphinx_jni");
	}
	/**
	 * Recognizer task, which runs in a worker thread.
	 */
	RecognizerTask rec;
	/**
	 * Thread in which the recognizer task runs.
	 */
	Thread rec_thread;
	/**
	 * Time at which current recognition started.
	 */
	Date start_date;
	/**
	 * Number of seconds of speech.
	 */
	float speech_dur;
	/**
	 * Are we listening?
	 */
	boolean listening;
	/**
	 * Progress dialog for final recognition.
	 */
	ProgressDialog rec_dialog;
	/**
	 * Performance counter view.
	 */
	TextView performance_text;
	/**
	 * Editable text view.
	 */
	EditText edit_text;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			start_date = new Date();
			this.listening = true;
			this.rec.start();
			break;
		case MotionEvent.ACTION_UP:
			Date end_date = new Date();
			long nmsec = end_date.getTime() - start_date.getTime();
			this.speech_dur = (float) nmsec / 1000;
			if (this.listening) {
				Log.d(getClass().getName(), "Showing Dialog");
				this.rec_dialog = ProgressDialog.show(
						PocketSphinxAndroidDemo.this, "",
						"Recognizing speech...", true);
				this.rec_dialog.setCancelable(false);
				this.listening = false;
			}
			this.rec.stop();
			break;
		default:
			;
		}
		/* Let the button handle its own state */
		return false;
	}

	TextToSpeech ttobj;
	private EditText write;
	private Button btnNormal;
	Button sendBtn;
	EditText txtphoneNo;
	EditText txtMessage;
	
	private Button btnContacts;
	private TextView txtContacts;
	

	public static final int PICK_CONTACT = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.rec = new RecognizerTask();
		this.rec_thread = new Thread(this.rec);
		this.listening = false;
		Button b = (Button) findViewById(R.id.btnRecord);
		b.setOnTouchListener(this);
		this.performance_text = (TextView) findViewById(R.id.PerformanceText);
		this.edit_text = (EditText) findViewById(R.id.txtMessage);
		this.rec.setRecognitionListener(this);
		this.rec_thread.start();
		
		
		//send button instantiating
		
		sendBtn = (Button) findViewById(R.id.btnSend);
		txtphoneNo = (EditText) findViewById(R.id.txtPhonenum);
		txtMessage = (EditText) findViewById(R.id.txtMessage);

		sendBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				sendSMSMessage();
			}
		});

			
	
		//instantiating inbox button
	      
		Button btn = (Button) findViewById(R.id.btnInbox);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startsecond();
			}
		});

			

		// Instantiaon of button text to speech
		
		setupMessageButton();
		Button messageButton = (Button) findViewById(R.id.btnSpeak);
		write = (EditText) findViewById(R.id.txtMessage);
		ttobj = new TextToSpeech(getApplicationContext(),
				new TextToSpeech.OnInitListener() {
					@Override
					public void onInit(int status) {
						if (status != TextToSpeech.ERROR) {
							ttobj.setLanguage(Locale.UK);
						}
					}

				});

		btnNormal = (Button) findViewById(R.id.btnInbox);
		btnNormal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openNormalDialog();
			}

		});
		

		// Instantiation for contact browsing
		
		btnContacts = (Button) findViewById(R.id.btnContacts);
		txtContacts = (TextView) findViewById(R.id.txtPhonenum);
		btnContacts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						People.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);
			}
		});
	}

	protected void startsecond() {
		// TODO Auto-generated method stub
		startActivity(new Intent(PocketSphinxAndroidDemo.this,
				newActivity.class));

	}

	private void setupMessageButton() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		if (ttobj != null) {
			ttobj.stop();
			ttobj.shutdown();
		}
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.main, menu);
		return true;

	}

	private void openNormalDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setTitle(R.string.dialog_title);
		builder.setMessage("Others Can Hear Your Message");
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						((AlertDialog) dialog).dismiss();
					}
				});

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				String toSpeak = write.getText().toString();
				Toast.makeText(getApplicationContext(), toSpeak,
						Toast.LENGTH_SHORT).show();
				ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

			}
		});

		builder.show();

	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (PICK_CONTACT):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = managedQuery(contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String name = c
							.getString(c
									.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
					txtContacts.setText(name);
				}
			}
			break;
		}
	}

	protected void sendSMSMessage() {
		Log.i("Send SMS", "");

		String phoneNo = txtphoneNo.getText().toString();
		String message = txtMessage.getText().toString();

		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, null, message, null, null);
			Toast.makeText(getApplicationContext(), "SMS sent.",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS faild, check fields and try again.", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}
	}

	/** Called when partial results are generated. */
	@Override
	public void onPartialResults(Bundle b) {
		final PocketSphinxAndroidDemo that = this;
		final String hyp = b.getString("hyp");
		that.edit_text.post(new Runnable() {
			@Override
			public void run() {
				that.edit_text.setText(hyp);
			}
		});
	}

	/** Called with full results are generated. */
	@Override
	public void onResults(Bundle b) {
		final String hyp = b.getString("hyp");
		final PocketSphinxAndroidDemo that = this;
		this.edit_text.post(new Runnable() {
			@Override
			public void run() {
				that.edit_text.setText(hyp);
				Date end_date = new Date();
				long nmsec = end_date.getTime() - that.start_date.getTime();
				float rec_dur = (float) nmsec / 1000;
				that.performance_text.setText(String.format(
						"%.2f seconds %.2f xRT", that.speech_dur, rec_dur
								/ that.speech_dur));
				Log.d(getClass().getName(), "Hiding Dialog");
				that.rec_dialog.dismiss();
			}
		});
	}

	@Override
	public void onError(int err) {
		final PocketSphinxAndroidDemo that = this;
		that.edit_text.post(new Runnable() {
			@Override
			public void run() {
				that.rec_dialog.dismiss();
			}
		});
	}
}