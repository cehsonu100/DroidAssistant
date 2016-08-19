package mydroid.com.droidassistant;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import mydroid.com.droidassistant.database.SQLController;


public class PlaceRingerVolumeSetter extends AppCompatActivity {

    private static final int REQUEST_PLACE_PICKER = 1;
    private SQLController dbController;
    //UI component declaration
    TextView mLatitudeText;
    TextView mLongitudeText;
    EditText place_name_view;
    EditText take_radius_input;
    RadioGroup radioGroupMode;
    RadioButton radioButtonMode;
    String mode = "Normal";
    /**
     * Global Variable
     */
    String place_name="xyz";
    double startLatitude = 00.00, startLongitude = 00.00;
    float radius = (float) 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_ringer_volume_setter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFloatingshowLocationClicked();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbController=new SQLController(this);
        dbController.open();
        mLatitudeText=(TextView)findViewById(R.id.mLatitudeText);
        mLongitudeText=(TextView)findViewById(R.id.mLongitudeText);
        place_name_view = (EditText)findViewById(R.id.place_name);
        take_radius_input = (EditText)findViewById(R.id.radius_input);
        radioGroupMode = (RadioGroup) findViewById(R.id.mode);

    }

    private void updateUI(){
        mLatitudeText.setText(String.valueOf(startLatitude));
        mLongitudeText.setText(String.valueOf(startLongitude));
    }

    /*
     ******place Picker Works here
     */
    public void pickPlace(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;
        try {
            intent = builder.build(this);
            startActivityForResult(intent,REQUEST_PLACE_PICKER);
        }
        catch (GooglePlayServicesRepairableException e){
            e.printStackTrace();
        }
        catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                LatLng mplace=place.getLatLng();
                startLatitude=mplace.latitude;
                startLongitude=mplace.longitude;
                updateUI();
            }
        }
    }

    public void onAddLocationClicked(View view) {
        place_name = place_name_view.getText().toString();
        radius =Float.valueOf(take_radius_input.getText().toString());
        String mode = ((RadioButton)this.findViewById(radioGroupMode.getCheckedRadioButtonId())).getText().toString();
        //dbController=new SQLController(this);
        // dbController.open();
        dbController.insert(place_name, startLatitude, startLongitude, radius, mode);
        //dbController.close();
        Log.i("Insert staus ", "inserted");
        Intent intent = new Intent(this,ShowSavedLocationActivity.class);
        startActivity(intent);
    }

    public void onFloatingshowLocationClicked() {
        Intent intent = new Intent(this,ShowSavedLocationActivity.class); //going to middle activity DisplayDB.class
        startActivity(intent);
        //dbController.close();

    }
}

