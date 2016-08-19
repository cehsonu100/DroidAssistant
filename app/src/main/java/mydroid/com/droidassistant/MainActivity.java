package mydroid.com.droidassistant;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


import java.util.Calendar;

import mydroid.com.droidassistant.BroadcastReciver.LocationUpdateReceiver;
import mydroid.com.droidassistant.STT.SpeechToTextService;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener{
    Intent stt_intent;

    long minTime=8000;
    float minDistance=(float)0.0;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000*1;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS=(1000*1)/2;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSpeakClicked();
            }
        });
        onSpeakClicked();
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * initialze locationRequest object
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Intent serviceIntent = new Intent(this, LocationUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, pendingIntent);

    }

    //location button onClick
    public void onLocationShowClicked(View view) {
        Intent intent = new Intent(this,PlaceRingerVolumeSetter.class);
        startActivity(intent);
    }
    public void onReminderClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis()  + 60 * 60 * 1000;

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(CalendarContract.Events.TITLE, "event name");
        intent.putExtra(CalendarContract.Events.DESCRIPTION,  "This is a sample description");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Guest House");
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

        startActivity(intent);
    }
    public void onSpeakClicked() {
        stt_intent= new Intent(this,SpeechToTextService.class);
        startService(stt_intent);
    }

    public void onShowCommandListClicked(View view) {
        Intent intent = new Intent(this,ShowCommandActivity.class);
        startActivity(intent);
    }


    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "connection suspended...", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
