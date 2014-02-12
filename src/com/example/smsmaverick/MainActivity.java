/**
 * Main Activity of the program
 * 
 * @author Dominik Robellaz
 * @version 1.0
 */

package com.example.smsmaverick;

import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// preferences
	public static final String PREFS_NAME = "MyPrefsFile";

	// class variables
	private SeekBar timesetup;
	private SeekBar charactersetup;

	private Button sendButton;

	private TextView valueText;
	private TextView valueCharacter;

	private MySeekBarListener SBListener;

	private SMS smsfunction;

	private EditText mPhoneNumber;
	private EditText mNumberMessages;
	private EditText numberPicker;

	private String sendTo;
	private String myMessage;

	private long DELAY = 5000;
	private long Character = 1;

	private int Counter = 0;
	private int nbrPhone = 10;
	private int nbrMessages = 0;

	// handler object for message handling
	private Handler mHandler = new Handler();

	/**
	 * is called when the activity first starts up
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get all resources
		valueText = (TextView) findViewById(R.id.timedelay);
		valueCharacter = (TextView) findViewById(R.id.nbrCharactersTitle);
		timesetup = (SeekBar) findViewById(R.id.timesetup);
		charactersetup = (SeekBar) findViewById(R.id.characterSetup);
		mPhoneNumber = (EditText) findViewById(R.id.phonenumber);
		mNumberMessages = (EditText) findViewById(R.id.nbrMessages);
		numberPicker = (EditText) findViewById(R.id.timepicker_input);
		sendButton = (Button) findViewById(R.id.sendButton);

		// create new Listener and SMS object
		SBListener = new MySeekBarListener(valueText, valueCharacter,
				timesetup, charactersetup);
		smsfunction = new SMS();

		// set the listener to the SeekBar
		timesetup.setOnSeekBarChangeListener(SBListener);
		charactersetup.setOnSeekBarChangeListener(SBListener);

		// remove old callback
		mHandler.removeCallbacks(updateGUI);

		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		// set the restored preferences
		mPhoneNumber.setText(settings.getString("phone", ""));
		mNumberMessages.setText(settings.getString("nMessages", ""));
		valueText.setText(settings.getString("DelayTit",
				"Delay between messages: 100 ms"));
		valueCharacter.setText(settings.getString("CharacterTit",
				"Number of characters: 2"));
		timesetup.setProgress(settings.getInt("DelayValue", 0));
		charactersetup.setProgress(settings.getInt("CharacterValue", 0));
		numberPicker.setText(settings.getString("NumberPicker", ""));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		// remove old callback
		mHandler.removeCallbacks(updateGUI);

	}

	/**
	 * This function makes no orientation change aviable
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * here is declared whats happend when the App is hidden.
	 */
	@Override
	protected void onPause() {
		super.onPause();

		// save preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();

		editor.putString("phone", mPhoneNumber.getText().toString());
		editor.putString("nMessages", mNumberMessages.getText().toString());
		editor.putString("DelayTit", valueText.getText().toString());
		editor.putString("CharacterTit", valueCharacter.getText().toString());
		editor.putInt("DelayValue", timesetup.getProgress());
		editor.putInt("CharacterValue", charactersetup.getProgress());
		editor.putString("NumberPicker", numberPicker.getText().toString());

		// Commit the edits!
		editor.commit();

	}

	/**
	 * handler for the button send calls sendSms function
	 * 
	 * @param view
	 *            the active view
	 */
	public void sendMessages(View view) {

		// write SeekBar value and remove old callback and set counter to 0
		DELAY = SBListener.getSeekBarValue(timesetup);
		Character = SBListener.getSeekBarValue(charactersetup);
		mHandler.removeCallbacks(updateGUI);
		Counter = 0;

		sendButton.setEnabled(false);

		// check if number of messages not empty or 0
		if (mNumberMessages.getText().toString().trim().length() > 0) {
			try {
				nbrMessages = Integer.parseInt(mNumberMessages.getText()
						.toString());
				if (nbrMessages != 0) {

					if (mPhoneNumber.getText().length() == nbrPhone) {
						// get PhoneNumber and number of messages
						sendTo = mPhoneNumber.getText().toString();

						// call Callback function
						mHandler.postDelayed(updateGUI, 0);

					} else {

						// toast: the PhoneNumber length is not ok
						Context context = getApplicationContext();
						CharSequence text = "Phonenumber not aviable";
						Toast toast = Toast.makeText(context, text,
								Toast.LENGTH_SHORT);
						toast.show();
					}

				} else {

					// toast: the number of messages is not correct
					Context context = getApplicationContext();
					CharSequence text = "No messages send. Please check number of messages.";
					Toast toast = Toast.makeText(context, text,
							Toast.LENGTH_SHORT);
					toast.show();
				}
			} catch (Exception e) {

				// toast: the number of messages is to long
				Context context = getApplicationContext();
				CharSequence text = "No messages send. This are too many messages.";
				Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
				toast.show();
			}

		} else {

			// toast: the number of messages is not correct
			Context context = getApplicationContext();
			CharSequence text = "No messages send. Please check number of messages.";
			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * Handler for the button stop. It removes all callbacks in the loop.
	 * 
	 * @param view
	 *            the active view
	 */
	public void stopSend(View view) {

		// toast to show what happened
		Context context = getApplicationContext();
		CharSequence text = "The sending was terminated.";
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();

		// set sendButton visible on
		sendButton.setEnabled(true);

		// remove old callback
		mHandler.removeCallbacks(updateGUI);
	}

	/**
	 * define new callback object
	 */
	private Runnable updateGUI = new Runnable() {

		@Override
		public void run() {

			// create a String with defined length and create new SMS object and
			// send it
			Integer size = (int) (long) Character;
			int locValue = Integer.parseInt(numberPicker.getText().toString());

			if (locValue < 10) {
				myMessage = locValue + getFilledString(size - 1, 'd');
			} else {
				myMessage = locValue + getFilledString(size - 2, 'd');
			}

			smsfunction.sendSMS(sendTo, myMessage);

			Context context = getApplicationContext();
			CharSequence text = Counter + 1 + ". SMS";
			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();

			// start new callback as long as the value NRMESSAGES
			if (Counter < nbrMessages - 1) {

				mHandler.postDelayed(this, DELAY);

				Counter++;

			} else if (Counter == nbrMessages - 1) {
				// set sendButton visible on
				sendButton.setEnabled(true);
			}
		}
	};

	/**
	 * This Function creates a String with wished length and declared Char
	 * 
	 * @param length
	 * @param charToFill
	 * @return String with length characters
	 */
	protected String getFilledString(int length, char charToFill) {
		if (length > 0) {
			char[] array = new char[length];
			Arrays.fill(array, charToFill);
			return new String(array);
		}
		return "";
	}
}