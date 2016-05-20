package pl.jadebusem.gpscoordinatescollector;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PlaceDetailsFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item_details_fragment, container, false);

		String placeName = getArguments().getString("PLACE_NAME");
		TextView textView = (TextView)view.findViewById(R.id.itemName);
		textView.setText(placeName);
		Button closeButton = (Button)view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getFragmentManager().beginTransaction().hide(PlaceDetailsFragment.this).commit();
			}
		});

		Button setButton = (Button)view.findViewById(R.id.set_button);
		setButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getFragmentManager().beginTransaction().hide(PlaceDetailsFragment.this).commit();
			}
		});

		return view;
	}
}
