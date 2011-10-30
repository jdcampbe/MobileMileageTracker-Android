package com.deebdroid.mobilemileagetracker;

import com.deebdroid.mobilemileagetracker.MyLocation.LocationResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MobilemileagetrackerActivity extends Activity {
	
	public static final String SITE_URL = "192.168.1.4";
	public static final int SITE_PORT = 8000;
	private static final String PREFS = "Credentials";	//For local prefs storage
	private static final int LOGIN_REQ_CODE = 0;		//For exchanging login data from LoginActivity
	public Context c;
	
	public MyLocation myLocation;
	public LocationResult locationResult;
	public boolean tracking;
	
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	
	//TODO: Remove tracking and related methods. Instead create a second constructor for MyLocation
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        c = this;
        
        //Set this true once a trip is started
        //The location tracking Activity will then receive updates
        tracking = false;
        
        //Uncomment below to 'warm up' gps on app launch
        //setupLocationReceiver();
             
        prefs = getSharedPreferences(PREFS, 0);
        editor = prefs.edit();
        Login();
        /*
        if(prefs.getBoolean("first_launch", true))
        	Login();
        else{
        	TextView tv = (TextView)findViewById(R.id.textView);
        	tv.setText("Hello " + prefs.getString("username", "You")+"!");
        }
        */

        
        //TODO: Create/Set Trip Create/Continue trip button listeners, pass locationResult data to TrackActivity
    }
        
    private OnClickListener createTripListener = new OnClickListener(){
    	
    	public void onClick(View arg){
    		//Intent i = new Intent(c, TrackActivity.class);
    		//startActivityForResult(i, LOGIN_REQ_CODE);
    	}
    };
    
    /*	Going to relocate data tracking to TrackActivity
    private void setupLocationReceiver(){
    	locationResult = new LocationResult(){
            @Override
            public void gotLocation(final Location location){
            	double acc = location.getAccuracy()/3.2808399;
            	Toast.makeText(c, "loc +/ "+String.valueOf(acc)+"ft.",Toast.LENGTH_SHORT).show();
                if(tracking){
                	/*
                	 * Was originally thinking I'd pass location data in an Intent. but this probably won't work once this activity is paused and 
                	 * Track activity is active.
                	Intent i = new Intent(getApplicationContext(), TrackActivity.class);
                	i.putExtra(key, value);
                	startActivity(i);
                	
                }
            }
    	};
            
        myLocation = new MyLocation();
        myLocation.trackLocation(this, locationResult);
    }
   */

    
    private void Login(){
        new AlertDialog.Builder(this)
        .setMessage("Welcome to MobileMileageTracker! \n\n This application tracks your trips and allows you to upload them to www."+SITE_URL+". There, you can view detailed reports and other cool things.\n\n But first, let's grab your account info! ")
        .setPositiveButton("Login", new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        		Intent i = new Intent(c, LoginActivity.class);
        		startActivityForResult(i, LOGIN_REQ_CODE);					
        	}
        })
        .show(); 	
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == LOGIN_REQ_CODE) {
			if (data.hasExtra("username") && data.hasExtra("password")) {
				String username = data.getExtras().getString("username");
				String password = data.getExtras().getString("password");
		        editor.putString("username", username);
		        editor.putString("password", password);
		        editor.putBoolean("first_launch", false);
		        editor.commit();
				Toast.makeText(this, data.getExtras().getString("username")+" logged in.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
    
    public boolean isTracking(){
    	return tracking;
    }
    
    public void setTracking(boolean arg){
    	tracking = arg;
    }
    
}