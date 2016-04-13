package ca.stevenlyall.comp3160final;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by stevenlyall on 16-04-12.
 */

public class City {
	private LatLng latLng;
	private MarkerOptions cityMarker;
	private String name;

	public City(LatLng latLng, String name) {
		this.cityMarker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.frame03));
		this.latLng = latLng;
		this.name = name;
	}

	public MarkerOptions getCityMarker() {
		return cityMarker;
	}

	public void setCityMarker(MarkerOptions cityMarker) {
		this.cityMarker = cityMarker;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
