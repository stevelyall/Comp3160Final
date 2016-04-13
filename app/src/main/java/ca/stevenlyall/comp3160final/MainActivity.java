package ca.stevenlyall.comp3160final;

import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

	private final String TAG = "MAIN";

	private GoogleMap map;

	LocationManager locationManager;
	LocationListener locationListener;
	String bestProvider;

	LatLng startLatLng, victoria, calgary, kamloops;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		playWelcomeTTSMsg();

		startLatLng = new LatLng(49.961381, -118.641357);
		victoria = new LatLng(48.428421, -123.365644);
		calgary = new LatLng(51.048615, -114.070846);


		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	private void playWelcomeTTSMsg() {
		// TODO
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;

	}
}
