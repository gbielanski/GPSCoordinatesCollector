package pl.jadebusem.gpscoordinatescollector;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import java.io.FileReader;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AllPlacesList extends AppCompatActivity {

	public static final String DB_URL = "https://gpscollector.firebaseio.com/places";
	@Bind(R.id.toolbar)
	protected Toolbar toolbar;

	@Bind(R.id.all_places_list)
	protected ListView allPlacesListView;

	private Firebase mRootref;
	private FirebaseListAdapter<Place> adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_places_list);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);

		Firebase.setAndroidContext(this);
		mRootref = new Firebase(DB_URL);
	}

	@Override
	protected void onStart() {
		super.onStart();
		adapter = new FirebaseListAdapter<Place>(this, Place.class, android.R.layout.simple_list_item_2, mRootref) {
			@Override
			protected void populateView(View view, Place place, int i) {
				TextView textView1 = (TextView)view.findViewById(android.R.id.text1);
				TextView textView2 = (TextView)view.findViewById(android.R.id.text2);
				textView1.setText(place.getName());
				textView2.setText(place.getLat() + " " + place.getLng());
			}
		};

		allPlacesListView.setAdapter(adapter);
		allPlacesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				String location = adapter.getItem(position).getName();
				Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location);
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				if (mapIntent.resolveActivity(getPackageManager()) != null) {
					startActivity(mapIntent);
				}
				return true;
			}
		});

		allPlacesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Place place = adapter.getItem(position);
				Bundle placeToShow = new Bundle();
				placeToShow.putParcelable("PLACE", place);

				DialogFragment fragment = new PlaceDetailsFragment();
				fragment.setArguments(placeToShow);
				FragmentManager fragmentManager = getFragmentManager();
				fragment.show(fragmentManager,"fsdf");

			}
		});
	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_all_places_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
