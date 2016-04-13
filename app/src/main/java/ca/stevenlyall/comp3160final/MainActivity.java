package ca.stevenlyall.comp3160final;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

	private final String TAG = "MAIN";

	private GoogleMap map;
	LatLng startLatLng;
	ArrayList<City> cities;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initTTS();


		startLatLng = new LatLng(49.961381, -118.641357);

		City victoria = new City(new LatLng(48.428421, -123.365644), "Victoria");
		City calgary = new City(new LatLng(51.048615, -114.070846), "Calgary");
		City kamloops = new City(new LatLng(50.674522, -120.327267), "Kamloops");
		cities = new ArrayList<>();
		cities.add(victoria);
		cities.add(calgary);
		cities.add(kamloops);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	public static int TTS_DATA_CHECK = 1;
	private TextToSpeech textToSpeech;
	private boolean ttsInitialized;

	private void initTTS() {
		Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(intent, TTS_DATA_CHECK);

		// TODO
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
							textToSpeech.setSpeechRate(1.1f);
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
				.zoom(5)
				.bearing(90)
				.tilt(45)
				.build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(position));

		for (City c: cities) {
			map.addMarker(c.getCityMarker());
		}

	}
}
