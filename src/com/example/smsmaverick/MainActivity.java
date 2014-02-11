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
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// class variables
	private SeekBar timesetup;
	private SeekBar charactersetup;
	private TextView valueText;
	private TextView valueCharacter;
	private MySeekBarListener SBListener;
	private SMS smsfunction;
	private EditText mPhoneNumber;
	private EditText mNumberMessages;

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
		// myMessage = getString(R.string.message);
		valueText = (TextView) findViewById(R.id.timedelay);
		valueCharacter = (TextView) findViewById(R.id.nbrCharactersTitle);
		timesetup = (SeekBar) findViewById(R.id.timesetup);
		charactersetup = (SeekBar) findViewById(R.id.characterSetup);
		mPhoneNumber = (EditText) findViewById(R.id.phonenumber);
		mNumberMessages = (EditText) findViewById(R.id.nbrMessages);

		// create new Listener and SMS object
		SBListener = new MySeekBarListener(valueText, valueCharacter,
				timesetup, charactersetup);
		smsfunction = new SMS();

		// set the listener to the SeekBar
		timesetup.setOnSeekBarChangeListener(SBListener);
		charactersetup.setOnSeekBarChangeListener(SBListener);

		// remove old callback
		mHandler.removeCallbacks(updateGUI);

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

	@Override
	protected void onPause() {
		super.onPause();

		// remove old callback
		mHandler.removeCallbacks(updateGUI);

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

		Integer size = (int) (long) Character;
		myMessage = getFilledString(size, 'a');

		// check if number of messages not empty or 0
		if (mNumberMessages.getText().toString().trim().length() > 0) {
			nbrMessages = Integer
					.parseInt(mNumberMessages.getText().toString());
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

			if (NumberPicker.mCurrent < 10) {
				myMessage = NumberPicker.mCurrent
						+ getFilledString(size - 1, 'd');
			} else {
				myMessage = NumberPicker.mCurrent
						+ getFilledString(size - 2, 'd');
			}

			smsfunction.sendSMS(sendTo, myMessage);

			// toast to display how much sms were sent
			if (Counter == nbrMessages - 1) {

				Context context = getApplicationContext();
				CharSequence text = nbrMessages + " messages were sent";
				Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
				toast.show();
			}

			// start new callback as long as the value NRMESSAGES
			if (Counter < nbrMessages - 1) {

				mHandler.postDelayed(this, DELAY);
				Counter++;
			}
		}
	};

	/**
	 * This Function creates a String with length and declared Char
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