package pl.jadebusem.gpscoordinatescollector;

/**
 * Created by gbielanski on 2016-05-20.
 */
public class Place {
	private String name;
	private float lat;
	private float lng;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}
}
