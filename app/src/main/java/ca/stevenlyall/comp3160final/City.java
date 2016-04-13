package ca.stevenlyall.comp3160final;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by stevenlyall on 16-04-12.
 */

public class City {
	private LatLng latLng;
	private Marker cityMarker;
	private String name;

	public City(LatLng latLng, String name) {
		this.latLng = latLng;
		this.name = name;
	}

	public Marker getCityMarker() {
		return cityMarker;
	}

	public void setCityMarker(Marker cityMarker) {
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
