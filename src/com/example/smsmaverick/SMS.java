/**
 * manage the SMS events
 * 
 * @author Dominik Robellaz
 * @version 1.0
 */
package com.example.smsmaverick;

import android.telephony.SmsManager;
import android.widget.Toast;

public class SMS extends MainActivity {
	
	// class variables
	private SmsManager smsManager;
	
	/**
	 * send a SMS to a defined number
	 */
	public void sendSMS(String PhoneNumber, String Message) {
		
		try {
			// create new SmsManager object and send SMS
			smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(PhoneNumber, null, Message, null, null);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS faild, please try again later!", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}
	}

}
