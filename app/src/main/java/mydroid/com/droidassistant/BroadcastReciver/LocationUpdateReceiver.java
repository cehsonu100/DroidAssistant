package mydroid.com.droidassistant.BroadcastReciver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderApi;

import mydroid.com.droidassistant.R;
import mydroid.com.droidassistant.database.DBhelper;
import mydroid.com.droidassistant.database.SQLController;


public class LocationUpdateReceiver extends BroadcastReceiver {
    double startLatitude=0.0,startLongitude=0.0;
    float radius=0;
    String mode="";
    SQLController sqlController;
    DBhelper dbHelper;
    Cursor cursor;

    double endLatitude=0.0,endLongitude=0.0;
    Location mLastLocation;
    float[] distanceBetweenResult = new float[1];
    AudioManager myAudioManager;
    Context context;
    NotificationManager notificationManager;
    NotificationCompat.Builder nBuilder;
    final int notificationID = 1234;
    public LocationUpdateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nBuilder = new NotificationCompat.Builder(context);
        nBuilder.setContentTitle("DroidAssistant");
        nBuilder.setTicker("DroidAssistant");
        nBuilder.setSmallIcon(R.drawable.robert_icono);
        nBuilder.setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.robert_icono));
        nBuilder.setAutoCancel(true);


        mLastLocation = intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);
        if(mLastLocation==null) {
        }
        else {
            endLatitude=mLastLocation.getLatitude();
            endLongitude=mLastLocation.getLongitude();
            fetchLocationDB();
        }
    }

    public void fetchLocationDB() {
        sqlController = new SQLController(context);
        sqlController.open();
        cursor = sqlController.fetch();
        if(cursor.moveToFirst()) {
            do{
                startLatitude=cursor.getDouble(cursor.getColumnIndex(DBhelper.LATITUDE));
                startLongitude=cursor.getDouble(cursor.getColumnIndex(DBhelper.LONGITUDE));
                mode=cursor.getString(cursor.getColumnIndex(DBhelper.MODE));
                radius=cursor.getFloat(cursor.getColumnIndex(DBhelper.RADIUS));
                Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, distanceBetweenResult);
                checkLocationStatus();
            }while (cursor.moveToNext());
        }
        cursor.close();

    }

    public void checkLocationStatus() {
        if(distanceBetweenResult[0]<=radius) {
            if (mode.equals("Silent")){
                myAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                nBuilder.setContentText("Silent Mode Enabled");
                notificationManager.notify(notificationID,nBuilder.build());

            }
            if (mode.equals("Vibrate")){
                myAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                nBuilder.setContentText("Vibration Mode Enabled");
                notificationManager.notify(notificationID, nBuilder.build());

            }
            if (mode.equals("Normal")){
                myAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                nBuilder.setContentText("Normal Mode Enabled");
                notificationManager.notify(notificationID, nBuilder.build());
                //Toast.makeText(context, "Normal Mode Enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

