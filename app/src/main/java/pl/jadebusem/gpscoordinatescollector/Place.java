package pl.jadebusem.gpscoordinatescollector;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gbielanski on 2016-05-20.
 */
public class Place implements Parcelable {
	private String name;
	private double lat;
	private double lng;

	public Place(){};

	protected Place(Parcel in) {
		name = in.readString();
		lat = in.readDouble();
		lng = in.readDouble();
	}

	public static final Creator<Place> CREATOR = new Creator<Place>() {
		@Override
		public Place createFromParcel(Parcel in) {
			return new Place(in);
		}

		@Override
		public Place[] newArray(int size) {
			return new Place[size];
		}
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeDouble(lat);
		dest.writeDouble(lng);
	}
}
