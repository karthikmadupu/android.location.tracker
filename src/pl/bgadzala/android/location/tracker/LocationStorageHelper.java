package pl.bgadzala.android.location.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

public class LocationStorageHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "pl.bgadzala.android.location.tracker.db";
	private static final int DATABASE_VERSION = 1;

	private static final String LOCATION_TABLE_NAME = "location";
	private static final String LOCATION_KEY_TIMESTAMP = "timestamp";
	private static final String LOCATION_KEY_PROVIDER = "provider";
	private static final String LOCATION_KEY_LATITUDE = "latitude";
	private static final String LOCATION_KEY_LONGITUDE = "longitude";
	// @formatter:off
    private static final String LOCATION_TABLE_CREATE =
    		"CREATE TABLE " + LOCATION_TABLE_NAME + " (" +
    				LOCATION_KEY_TIMESTAMP + " INTEGER, " +
    				LOCATION_KEY_PROVIDER + " TEXT, " +
    				LOCATION_KEY_LATITUDE + " TEXT, " +
    				LOCATION_KEY_LONGITUDE + " TEXT);";
    // @formatter: on
    
	public LocationStorageHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(LOCATION_TABLE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	public void insert(Location location) {
		ContentValues values = new ContentValues();
		values.put(LOCATION_KEY_PROVIDER, location.getProvider());
		values.put(LOCATION_KEY_TIMESTAMP, location.getTime());
		values.put(LOCATION_KEY_LATITUDE, String.valueOf(location.getLatitude()));
		values.put(LOCATION_KEY_LONGITUDE, String.valueOf(location.getLongitude()));
		
		SQLiteDatabase db = getWritableDatabase();
		try {
			if (db.insert(LOCATION_TABLE_NAME, null, values) == -1) {
				throw new SQLException("Cannot insert [" + location + "]");
			}
		} finally {
			db.close();
		}
	}

}
