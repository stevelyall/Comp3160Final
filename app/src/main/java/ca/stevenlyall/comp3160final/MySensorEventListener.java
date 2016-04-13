package ca.stevenlyall.comp3160final;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by stevenlyall on 16-04-12.
 */
public class MySensorEventListener implements SensorEventListener {

	private final String TAG = "SensorEvent";

	@Override
	public void onSensorChanged(SensorEvent event) {
		Log.i(TAG, "onSensorChanged: event");
		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			float value = event.values[0];
			Log.i(TAG, "onSensorChanged: SENSOR" + event.sensor.getName());
			Log.i(TAG, "onSensorChanged: SENSOR x " + value);
		} else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			float value = event.values[0];
			Log.i(TAG, "onSensorChanged: SENSOR" + event.sensor.getName());
			Log.i(TAG, "onSensorChanged: SENSOR x " + value);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.i(TAG, "onAccuracyChanged: SESNOR " + sensor.getName() + " accuracy " + accuracy);

	}


}
