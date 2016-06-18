package pl.jadebusem.gpscoordinatescollector;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.math.BigDecimal;


public class PlaceDetailsFragment extends DialogFragment {

	public static final String USTAWIANIE_POZYCJI = "Ustawianie pozycji";
	private static final int MY_GPS_REQUEST = 1;
	private Button setButton;
	private Button resetButton;
	private LocationManager locationManager;
	private LocationListener locationListener;
	TextView lon;
	TextView lat;
	private Firebase mRootref;
	private Place place;
	private int position;
	private Location curentLocation;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item_details_fragment, container, false);
		getDialog().setTitle(USTAWIANIE_POZYCJI);
		place = getArguments().getParcelable("PLACE");

		position = getArguments().getInt("POSITION");
		TextView textView = (TextView) view.findViewById(R.id.itemName);
		textView.setText(place.getName());
	    mRootref = new Firebase(AllPlacesList.DB_URL);

		Button closeButton = (Button) view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		setButton = (Button) view.findViewById(R.id.set_button);
		setButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Firebase mPlaceRef = mRootref.child(Integer.valueOf(position).toString());
				place.setLng(Double.valueOf(lon.getText().toString()));
				place.setLat(Double.valueOf(lat.getText().toString()));
				mPlaceRef.setValue(place);
				dismiss();
			}
		});

		resetButton = (Button) view.findViewById(R.id.reset_button);
		resetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Firebase mPlaceRef = mRootref.child(Integer.valueOf(position).toString());
				place.setLng(0);
				place.setLat(0);
				mPlaceRef.setValue(place);
				dismiss();
			}
		});

		lon = (TextView)view.findViewById(R.id.lon);
		lat = (TextView)view.findViewById(R.id.lat);
		lon.setText(String.valueOf(place.getLng()));
		lat.setText(String.valueOf(place.getLat()));
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {

				if(isBetterLocation(location, curentLocation)) {
					lon.setText(getFormatedLocation(location.getLongitude()));
					lat.setText(getFormatedLocation(location.getLatitude()));
					curentLocation = location;
				}
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}
		};

		return view;
	}

	private String getFormatedLocation(double location) {
		return new BigDecimal(location).setScale(6, BigDecimal.ROUND_HALF_UP).toString();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(),
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_GPS_REQUEST);
			return ;
		}

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

	}

	@Override
	public void onPause() {
		super.onPause();
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(),
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_GPS_REQUEST);
			return ;
		}

		locationManager.removeUpdates(locationListener);
	}

	private static final int TWO_MINUTES = 1000 * 60 * 2;

	/** Determines whether one Location reading is better than the current Location fix
	 * @param location  The new Location that you want to evaluate
	 * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	 */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
}
