package ca.stevenlyall.comp3160final;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {

	public static int TTS_DATA_CHECK = 1;
	private final String TAG = "MAIN";
	LatLng startLatLng;
	ArrayList<City> cities;
	SensorManager sensorManager;
	Sensor sensor;
	private GoogleMap map;
	private TextToSpeech textToSpeech;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initTTS();
		setUpCities();
		getSensors();

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	private void initTTS() {
		Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(intent, TTS_DATA_CHECK);
	}

	private void setUpCities() {
		startLatLng = new LatLng(49.961381, -118.641357);

		City victoria = new City(new LatLng(48.428421, -123.365644), "Victoria");
		City calgary = new City(new LatLng(51.048615, -114.070846), "Calgary");
		City kamloops = new City(new LatLng(50.674522, -120.327267), "Kamloops");
		cities = new ArrayList<>();
		cities.add(victoria);
		cities.add(calgary);
		cities.add(kamloops);
	}

	private void getSensors() {
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//
// List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_LIGHT);
//		Log.i(TAG, "getSensors: " + sensors.size());
//		for (Sensor s : sensors) {
//			Log.i(TAG, s.toString());
//		}

		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		if (sensor == null) {
			// no light sensor, use proximity sensor
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		}
		Log.i(TAG, "getSensors: SENSOR " + sensor.toString());

		// TODO no sensor events for any sensors?
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);

		Log.i(TAG, "getSensors: registered? " + sensor.toString() + " ");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TTS_DATA_CHECK) {
			Log.i(TAG, "onActivityResult: TTS_DATA_CHECK" + requestCode);
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				Log.i(TAG, "onActivityResult: voice data check passed");
				textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
					@Override
					public void onInit(int status) {
						if (textToSpeech.isLanguageAvailable(Locale.US) >= 0) {
							textToSpeech.setLanguage(Locale.US);
							textToSpeech.setPitch(0.8f);
							textToSpeech.setSpeechRate(1.5f);
							playTTSWelcomeMsg();
						}
					}
				});
			}
			else {
				Log.i(TAG, "onActivityResult: voice data check failed");
				Intent installVoice = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installVoice);
			}
		}
	}

	public void playTTSWelcomeMsg() {
		String message = getString(R.string.welcome_message);
		speak(message);
	}


	private void speak(String message) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			ttsGreater21(message);
		} else {
			ttsUnder20(message);
		}
	}

	@SuppressWarnings("deprecation")
	private void ttsUnder20(String text) {
		HashMap<String, String> map = new HashMap<>();
		map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
		textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, map);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void ttsGreater21(String text) {
		String utteranceId=this.hashCode() + "";
		textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
	}
	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		CameraPosition position = CameraPosition.builder()
				.target(startLatLng)
				.zoom(5).bearing(0) // face north
				.tilt(45)
				.build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(position));

		for (City c: cities) {
			Marker m = map.addMarker(new MarkerOptions().position(c.getLatLng()).icon(BitmapDescriptorFactory.fromResource(R.drawable.frame03)).title(c.getName()).draggable(true));
			c.setCityMarker(m);
		}

		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				String title = marker.getTitle();
				Toast.makeText(getBaseContext(), title, Toast.LENGTH_LONG).show();
				speak(title);
				return false;
			}
		});

		map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
			@Override
			public void onMarkerDragStart(Marker marker) {

			}

			@Override
			public void onMarkerDrag(Marker marker) {

			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				marker.setVisible(false);

			}
		});
	}

	// TODO change map type with sesnor - sensor events not working?
	private void changeMapType() {
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	}

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
