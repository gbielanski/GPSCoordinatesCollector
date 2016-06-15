package pl.jadebusem.gpscoordinatescollector;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PlaceDetailsFragment extends DialogFragment {

	public static final String USTAWIANIE_POZYCJI = "Ustawianie pozycji";

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item_details_fragment, container, false);
		getDialog().setTitle(USTAWIANIE_POZYCJI);
		String placeName = getArguments().getString("PLACE_NAME");
		TextView textView = (TextView)view.findViewById(R.id.itemName);
		textView.setText(placeName);
		Button closeButton = (Button)view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		Button setButton = (Button)view.findViewById(R.id.set_button);
		setButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		return view;
	}
}
