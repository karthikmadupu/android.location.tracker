package pl.bgadzala.android.location.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		acquireWakeLockForService(context);
		startTrackerServier(context);
	}

	private void acquireWakeLockForService(Context context) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LocationWakeLock");
		wakeLock.acquire();
	}

	private void startTrackerServier(Context context) {
		context.startService(new Intent(context, TrackerService.class));
	}
}
