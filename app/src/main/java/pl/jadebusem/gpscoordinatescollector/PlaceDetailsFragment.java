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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceDetailsFragment extends DialogFragment {

	public static final String USTAWIANIE_POZYCJI = "Ustawianie pozycji";
	private Button setButton;
	private LocationManager locationManager;
	private LocationListener locationListener;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item_details_fragment, container, false);
		getDialog().setTitle(USTAWIANIE_POZYCJI);
		String placeName = getArguments().getString("PLACE_NAME");
		TextView textView = (TextView) view.findViewById(R.id.itemName);
		textView.setText(placeName);
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
				dismiss();
			}
		});

		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				if(getActivity() != null)
					Toast.makeText(getActivity(), "Location: " + location.toString(), Toast.LENGTH_LONG).show();
				else
					Log.e("ACTIVITY", "WAS NuLL");
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
			// to handle the case where the user grants the permission. See the documentation
			// for Activity#requestPermissions for more details.
			return ;
		}

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

	}

	@Override
	public void onPause() {
		super.onPause();
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// to handle the case where the user grants the permission. See the documentation
			// for Activity#requestPermissions for more details.
			return ;
		}

		locationManager.removeUpdates(locationListener);
	}
}
