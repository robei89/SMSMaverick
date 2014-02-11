/**
 * SeekBar listener
 * manage the SeekBar events
 * 
 * @author Dominik Robellaz
 * @version 1.0
 */
package com.example.smsmaverick;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MySeekBarListener implements OnSeekBarChangeListener {

	// class variables
	private TextView TitleTime;
	private TextView TitleCharacter;
	private SeekBar seekBarTime;
	private SeekBar seekBarCharacters;

	private long ValueTime;
	private long ValueCharacter;

	public MySeekBarListener(TextView TitleTime, TextView TitleCharacter,
			SeekBar sbTime, SeekBar sbCharacter) {
		// set the local value
		this.TitleTime = TitleTime;
		this.TitleCharacter = TitleCharacter;
		this.ValueTime = 1;
		this.ValueCharacter = 1;
		this.seekBarCharacters = sbCharacter;
		this.seekBarTime = sbTime;
	}

	/**
	 * displays SeekBar value on change
	 * 
	 * @param seekBar
	 *            the timedelay seekbar
	 * @param progress
	 * @param fromUser
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		// local variables
		double linearValueTime = progress;
		double linearValueCharacter = progress + 2;

		// check if its SeekBar time or SeekBar Character
		if (seekBar.equals(seekBarTime)) {

			// logarithmic SeekBar display
			this.ValueTime = Math.round(Math.pow(10, linearValueTime / 100));

			// check is it seconds or milliseconds
			if (this.ValueTime < 1000) {

				this.TitleTime.setText("Delay between messages: "
						+ this.ValueTime + " ms");
			} else {

				this.TitleTime.setText("Delay between messages: "
						+ this.ValueTime / 1000 + " s");
			}

		} else if (seekBar.equals(seekBarCharacters)) {
			// logarithmic SeekBar display Range 2 - 160
			this.ValueCharacter = Math.round(Math.exp(Math.log(2)
					+ (linearValueCharacter - 2)
					* (Math.log(160) - Math.log(2)) / (160 - 2)));
			this.TitleCharacter.setText("Number of characters: "
					+ this.ValueCharacter);
		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public long getSeekBarValue(SeekBar seekBar) {

		// check if its SeekBar time or SeekBar Character
		if (seekBar.equals(seekBarTime)) {

			return this.ValueTime;
		} else if (seekBar.equals(seekBarCharacters)) {

			return this.ValueCharacter;
		} else {
			return -1;
		}

	}

}
