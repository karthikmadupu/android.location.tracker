package pl.bgadzala.android.location.tracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

/**
 * @author	BGadzala
 * @since	0.1.0
 */
public class TrackerService extends Service {

	private Looper serviceLooper;
	private ServiceHandler serviceHandler;

	private final class ServiceHandler extends Handler {
		private LocationListener locationListener;

		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			createLocationListener();
			registerLocationListener();
			sleep(5);
			unregisterLocationListener();
			stopSelf(msg.arg1);
		}

		/**
		 * Creates location listener. To start receiving location {@link #registerLocationListener()}
		 * should be invoked.
		 */
		private void createLocationListener() {
			this.locationListener = new LocationListener() {
				public void onStatusChanged(String provider, int status, Bundle extras) {
				}

				public void onProviderEnabled(String provider) {
				}

				public void onProviderDisabled(String provider) {
				}

				public void onLocationChanged(Location location) {
				}
			};
		}

		/**
		 * Registers location listener to the acquired location manager. After registration
		 * location will be received until {@link #unregisterLocationListener()} is invoked.
		 */
		private void registerLocationListener() {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			// TODO: from configuration
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this.locationListener);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.locationListener);
			locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this.locationListener);
		}

		/**
		 * Unregisters location listener from the acquired location manager. After unregistering
		 * location won't be received until {@link #registerLocationListener()} is invoked.
		 */
		private void unregisterLocationListener() {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.removeUpdates(this.locationListener);
		}

		private void sleep(long seconds) {
			long endTime = System.currentTimeMillis() + seconds * 1000;
			while (System.currentTimeMillis() < endTime) {
				synchronized (this) {
					try {
						wait(endTime - System.currentTimeMillis());
					} catch (Exception e) {
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		HandlerThread thread = new HandlerThread("LocationTrackerThread", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		this.serviceLooper = thread.getLooper();
		this.serviceHandler = new ServiceHandler(this.serviceLooper);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

		Message msg = this.serviceHandler.obtainMessage();
		msg.arg1 = startId;
		this.serviceHandler.sendMessage(msg);

		return START_STICKY;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

}
