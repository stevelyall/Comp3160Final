package ca.stevenlyall.comp3160final;

import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

	private final String TAG = "MAIN";

	private GoogleMap map;

	LocationManager locationManager;
	LocationListener locationListener;
	String bestProvider;

	LatLng startLatLng;
	ArrayList<City> cities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		playWelcomeTTSMsg();


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

	private void playWelcomeTTSMsg() {
		// TODO
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
