package com.example.newproject;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;



public class MainActivity extends Activity implements OnItemSelectedListener, 
OnClickListener, LocationListener
{

	private GoogleMap map;
	
	//Markers markers = new Markers();
	private Spinner spinner;
	private ImageButton button;
	private Button userLocation;
	private EditText editLat;
	private EditText editLng;
	private String strLat;
	private String strLng;
	//private TextView latitudeField;
	//private TextView longitudeField;
	LocationManager locationManager;
	private String provider;
	Location currentLocation;
	LocationClient locationClient;
	LocationRequest locationRequest;
	boolean updatesRequested;
	Context context;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	//creates latitude and longitudes of all previous commonwealth games
	private LatLng GLASGOW = new LatLng(55.8580, -4.2590);
	private LatLng DELHI = new LatLng(28.6100, 77.2300);
	private LatLng MELBOURNE = new LatLng(-37.8136, 144.9631);
	private LatLng MANCHESTER = new LatLng(53.4667, -2.2333);
	private LatLng KUALALUMPUR = new LatLng(3.1357, 101.6880);
	private LatLng VICTORIA = new LatLng(48.4222, -123.3657);
	private LatLng AUCKLAND = new LatLng(-36.8404, 174.7399);
	private LatLng EDINBURGH = new LatLng(55.9531, -3.1889);
	private LatLng BRISBANE = new LatLng(-27.4679, 153.0278);
	private LatLng EDMUNTON = new LatLng(53.5333, -113.5000);
	private LatLng CHRISTCHURCH = new LatLng(-43.5300, 172.6203);

	//private GoogleMapOptions options;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Intent set up to switch back this activity from another activity
        getIntent();
        //Sets up the map fragment
        map = ((MapFragment) 
        		getFragmentManager().findFragmentById(R.id.map)).getMap();
        //Enables gps 
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        
		button = (ImageButton) findViewById(R.id.map_change);
		button.setOnClickListener(
				new View.OnClickListener() {
					int clicks=0;
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int count = ++clicks;
						if(v == button)
						{
							//uses two integers to count the amount
							//of times the button is clicked
							//Map Type changes based on the number of clicks
							if(count == 0)
							{
								map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
							}
							if(count == 1)
							{
								map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
							}
							if(count == 2)
							{
								map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
							}
							if(count == 3)
							{
								map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
								//resets clicks back to first Map Type
								clicks = -1;
							}
						}
					}
				});
		editLng = (EditText) findViewById(R.id.user_lng);
		editLat = (EditText) findViewById(R.id.user_lat);
		userLocation = (Button) findViewById(R.id.user_location);
		userLocation.setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//String is set to the edit text and converts text to string
						strLat = (editLat.getText().toString());
						strLng = (editLng.getText().toString());
						//Converts the double to a String which will enter the edit text
						double numLat = Double.parseDouble(strLat); 
						double numLng = Double.parseDouble(strLng);
						
						
						/*if(currentLocation != null)
						{
						if(TextUtils.isEmpty(strLat))
						{
								numLat = currentLocation.getLatitude();
							}else{
								numLat = Double.parseDouble(strLat); 
							}
							if(TextUtils.isEmpty(strLng))
							{
								numLng = currentLocation.getLongitude();
							}else{
								numLng = Double.parseDouble(strLng);
							}*/
							if(v == userLocation)
							{
								//Updates camera position based on latitude and
								//longitude enter into edit text fields by user
								//pressing button moves camera to that position 
								//adding a marker to said location
								LatLng pos = new LatLng(numLat, numLng);
								map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 10));
								map.addMarker(new MarkerOptions()
									.position(pos)
									.title("Your Location"));
							}
					}
				});
		
		//Uses location manager to find users location
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		//Checks for best provider of GPS
		provider = locationManager.getBestProvider(criteria, false);
		//Finds last known location
		Location location = locationManager.getLastKnownLocation(provider);
		//If GPS is on find users location
		boolean enabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!enabled)
		{
			Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(i);
		}
		if(location !=null)
		{
			//prints out line saying that gpslocation cannot be enabled
			System.out.println("Provider "+ provider + "has been selected.");
			onLocationChanged(location);
		}
		
		//Creates marker using latitude and longitude
		//takes the position and adds a custom icon to that position
		//gives a description and title of the events at this location
        final LatLng SECC = new LatLng(55.8607,-4.2871);
        map.addMarker(new MarkerOptions()
        .position(SECC)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.boxingicon))
        .snippet("Events: Boxing, Gymnastics, " + "" +
        		"Judo, Netball, " + "Wrestiling, Weightlifting")
        .title("SECC"));

        final LatLng SHOOTING = new LatLng(56.499, -2.7543);
        map.addMarker(new MarkerOptions()
        .position(SHOOTING)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.shootingicon))
        .snippet("Events: Clay Target , Full Bore, " +
        		"Pistol & Small Bore")
        .title("Barry Buddon Shooting Centre"));
    	
        final LatLng PARKHEAD = new LatLng(55.8497, -4.2055);
        map.addMarker(new MarkerOptions()
        .position(PARKHEAD)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.fireworksicon))
        .snippet("Opening Ceremony")
        .title("Celtic Park"));
		
    	final LatLng CATHKIN = new LatLng(55.79434, -4.2193);
        map.addMarker(new MarkerOptions()
        .position(CATHKIN)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mountainbikingicon))
        .snippet("Events: Mountain Biking")
        .title("Cathkin Braes Mountain Trail"));
    	
    	final LatLng VELODROME = new LatLng(55.847, -4.2076);
        map.addMarker(new MarkerOptions()
        .position(VELODROME)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.cyclingicon))
        .snippet("Events: Cycling & Badminton")
        .title("Chris Hoy Velodrome & Emirates Arena"));
    	
    	final LatLng HOCKEY = new LatLng(55.8447, -4.236);
        map.addMarker(new MarkerOptions()
        .position(HOCKEY)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.hockeyicon))
        .snippet("Events: Hockey")
        .title("National Hockey Centre"));
    	
        final LatLng HAMPDEN = new LatLng(55.8255, -4.2520);
        map.addMarker(new MarkerOptions()
        .position(HAMPDEN)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.athleticsicon))
        .snippet("Events: Athletics")
        .title("Hampden Park"));
    	
    	final LatLng IBROX = new LatLng(55.853,-4.309);
        map.addMarker(new MarkerOptions()
        .position(IBROX)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.rugbyicon))
        .snippet("Events: Rugby Sevens")
        .title("Ibrox Stadium"));
    
    	final LatLng KELVINGROVE = new LatLng(55.867,-4.2871);
        map.addMarker(new MarkerOptions()
        .position(KELVINGROVE)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bowlsicon))
        .snippet("Events: Lawn Bowls")
        .title("Kelvingrove Lawn Bowls"));
    	
    	final LatLng SCOTSTOUN = new LatLng(55.8813,-4.3405);
        map.addMarker(new MarkerOptions()
        .position(SCOTSTOUN)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.tabletennisicon))
        .snippet("Events: Table Tennis & Squash")
        .title("Scotstoun Sports Centre"));
    	
    	final LatLng TOLLCROSS = new LatLng(55.845, -4.177);
        map.addMarker(new MarkerOptions()
        .position(TOLLCROSS)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.swimmingicon))
        .snippet("Events: Swimming")
        .title("Tollcross International Swimming Centre"));
    	
        final LatLng STRATHCLYDE = new LatLng(55.7971971, -4.0342997);
        map.addMarker(new MarkerOptions()
        .position(STRATHCLYDE)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.triathlonicon))
        .snippet("Events: Triathlon")
        .title("Strathclyde Country Park"));
    	
    	final LatLng EDINBURGH = new LatLng(55.939, -3.172);
        map.addMarker(new MarkerOptions()
        .position(EDINBURGH)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.swimmingicon))
        .snippet("Events: Diving")
        .title("Royal Commonwealth Pool"));
        
        //Finds spinner on screen
        spinner = (Spinner) findViewById(R.id.games_spinner);
     // Create an ArrayAdapter using the string array and a default spinner layout
     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
             R.array.games_array, android.R.layout.simple_spinner_item);
     // Specify the layout of the spinner
     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     // Apply the adapter to the spinner
     spinner.setAdapter(adapter);
     spinner.setOnItemSelectedListener(this);
    }
    
     //Sends message to a button which switches screen to another activity
    public void sendMessage(View view)
	{
		Intent intent = new Intent(this, DiaryActivity.class);
		startActivity(intent);
	}
  

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
			//While none are selected the camera sets on Glasgow
			//if user changes spinner to different location
			//this allows camera to return to glasgow
			//by selecting it within spinner
			map.moveCamera(CameraUpdateFactory.newLatLng(GLASGOW));
		
		//Selects Delhi from the string array and moves camera to 
		//the designated delhi position. Sets up marker the same as previous
		if(spinner.getSelectedItem().toString().equals("Delhi(2010)"))
		{
			map.moveCamera(CameraUpdateFactory.newLatLng(DELHI));
			map.addMarker(new MarkerOptions()
				.position(DELHI)
				.snippet("Scottish Medals: 26")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.indiaicon))
				.title("Dehli 2010"));
		}
		
		else if(spinner.getSelectedItem().toString().equals("Melbourne(2006)"))
		{
			map.addMarker(new MarkerOptions()
				.position(MELBOURNE)
				.snippet("Scottish Medals: 29")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.australiaicon))
				.title("Melbourd 2006"));
				map.moveCamera(CameraUpdateFactory.newLatLng(MELBOURNE));
		}
		
		else if(spinner.getSelectedItem().toString().equals("Manchester(2002)"))
		{
			map.addMarker(new MarkerOptions()
				.position(MANCHESTER)
				.snippet("Scottish Medals: 29")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.englandicon))
				.title("Manchester 2002"));
				map.moveCamera(CameraUpdateFactory.newLatLng(MANCHESTER));
		}
		
		else if(spinner.getSelectedItem().toString().equals("Kuala Lumpur(1998)"))
		{
			map.addMarker(new MarkerOptions()
				.position(KUALALUMPUR)
				.snippet("Scottish Medals: 12")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.malaysiaicon))
				.title("Kuala Lumpur 1998"));
				map.moveCamera(CameraUpdateFactory.newLatLng(KUALALUMPUR));
		}
		
		else if(spinner.getSelectedItem().toString().equals("Victoria(1994)"))
		{
			map.addMarker(new MarkerOptions()
				.position(VICTORIA)
				.snippet("Scottish Medals: 20")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.australiaicon))
				.title("Victoria 1994"));
				map.moveCamera(CameraUpdateFactory.newLatLng(VICTORIA));
		}
		
		else if(spinner.getSelectedItem().toString().equals("Auckland(1990)"))
		{
			 map.addMarker(new MarkerOptions()
				.position(AUCKLAND)
				.snippet("Scottish Medals: 22")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.newzealandicon))
				.title("Auckland 1990"));
				map.moveCamera(CameraUpdateFactory.newLatLng(AUCKLAND));
		}
		
		else if(spinner.getSelectedItem().toString().equals("Edinburgh(1986)"))
		{
			map.addMarker(new MarkerOptions()
				.position(EDINBURGH)
				.snippet("Scottish Medals: 33")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.scotlandicon))
				.title("Edinburgh 1986"));
				map.moveCamera(CameraUpdateFactory.newLatLng(EDINBURGH));
		}
		
		else if(spinner.getSelectedItem().toString().equals("Brisbane(1982)"))
		{
			map.addMarker(new MarkerOptions()
				.position(BRISBANE)
				.snippet("Scottish Medals: 26")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.australiaicon))
				.title("Brisbane 1982"));
				map.moveCamera(CameraUpdateFactory.newLatLng(BRISBANE));
		}
		
		else if(spinner.getSelectedItem().toString().equals("Edmunton(1978)"))
		{	
			map.addMarker(new MarkerOptions()
				.position(EDMUNTON)
				.snippet("Scottish Medals: 14")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.canadaicon))
				.title("Edmunton 1978"));
				map.moveCamera(CameraUpdateFactory.newLatLng(EDMUNTON));
		}
		
		else if(spinner.getSelectedItem().toString().equals("Christchurch(1974)"))
		{
			map.addMarker(new MarkerOptions()
				.position(CHRISTCHURCH)
				.snippet("Scottish Medals: 19")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.newzealandicon))
				.title("Christchurch 1974"));
				map.moveCamera(CameraUpdateFactory.newLatLng(CHRISTCHURCH));
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	@Override
	protected void onResume()
	{
		super.onResume();
		//Finds users location through GPS when app starts up
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		//stops requestion GPS when the app is stopped
		locationManager.removeUpdates(this);
	}
	//public void AddMarker(MarkerOptions markerOptions, LatLng position, String snippet, String title)
	//{
		//markerOptions = new MarkerOptions();
		//position = LatLng;
		//snippet = "";
		//title = "";
	//}
	
	public void onClick(View v)
	{
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		int lat = (int)(location.getLatitude());
		int lng = (int)(location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		//Creates message saying GPS provider disabled
		Toast.makeText(this, "Disabled provider" + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		//Creates message saying GPS provider enabled
		Toast.makeText(this, "Enabled new provider" + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}