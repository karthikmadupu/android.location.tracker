package pl.bgadzala.android.location.tracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TrackerActivity extends Activity {
	
	private OnClickListener startServiceListener = new OnClickListener() {
		public void onClick(View v) {
			final TrackerActivity context = TrackerActivity.this;
			Toast.makeText(context, "Starting tracker service", Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(context, AlarmReceiver.class);
			PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.setInexactRepeating(AlarmManager.RTC, 0, AlarmManager.INTERVAL_FIFTEEN_MINUTES, sender);
		}
	};

	private OnClickListener stopServiceListener = new OnClickListener() {
		public void onClick(View v) {
			final TrackerActivity context = TrackerActivity.this;
			Toast.makeText(context, "Stopping tracker service", Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(context, AlarmReceiver.class);
			PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.cancel(sender);
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Button buttonStart = (Button) findViewById(R.id.buttonStart);
		buttonStart.setOnClickListener(this.startServiceListener);

		final Button buttonStop = (Button) findViewById(R.id.buttonStop);
		buttonStop.setOnClickListener(this.stopServiceListener);

	}

}