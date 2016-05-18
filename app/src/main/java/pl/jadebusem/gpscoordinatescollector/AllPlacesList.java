package pl.jadebusem.gpscoordinatescollector;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

	@Bind(R.id.fab)
	protected FloatingActionButton fab;
	private Firebase mRootref;
	private FirebaseListAdapter<String> adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_places_list);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		Firebase.setAndroidContext(this);
		mRootref = new Firebase(DB_URL);
	}

	@Override
	protected void onStart() {
		super.onStart();
		adapter = new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_list_item_1, mRootref) {
			@Override
			protected void populateView(View view, String s, int i) {
				TextView textView = (TextView)view.findViewById(android.R.id.text1);
				textView.setText(s);
			}
		};

		allPlacesListView.setAdapter(adapter);
		allPlacesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				String location = adapter.getItem(position);
				Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location);
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				if (mapIntent.resolveActivity(getPackageManager()) != null) {
					startActivity(mapIntent);
				}
				Log.v("MAPAPA", "setOnItemLongClickListener");
				return true;
			}
		});

		allPlacesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String place_name = adapter.getItem(position);
				Bundle placeToShow = new Bundle();
				placeToShow.putString("PLACE_NAME", place_name);

				Fragment fragment = new PlaceDetailsFragment();
				fragment.setArguments(placeToShow);

				getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

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
