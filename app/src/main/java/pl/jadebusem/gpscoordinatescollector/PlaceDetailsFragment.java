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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;


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
				place.setLng(Float.valueOf(lon.getText().toString()));
				place.setLat(Float.valueOf(lat.getText().toString()));
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
				lon.setText(String.valueOf(location.getLongitude()));
				lat.setText(String.valueOf(location.getLatitude()));
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

	@Override
	public void onResume() {
		super.onResume();
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(),
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_GPS_REQUEST);
			Toast.makeText(getActivity(), "NO GPS PERMISSION", Toast.LENGTH_LONG).show();
			return ;
		}

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

	}

	@Override
	public void onPause() {
		super.onPause();
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return ;
		}

		locationManager.removeUpdates(locationListener);
	}
}
