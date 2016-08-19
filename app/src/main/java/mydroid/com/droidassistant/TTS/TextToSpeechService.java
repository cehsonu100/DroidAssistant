package mydroid.com.droidassistant.TTS;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

public class TextToSpeechService extends Service implements TextToSpeech.OnInitListener{
    private String string_to_say;
    private TextToSpeech mTTS;
    private static final String TAG="TTSService";


    public TextToSpeechService() {
    }

    @Override
    public IBinder onBind(Intent arg0) { return null; }


    @Override
    public void onCreate() {
        mTTS = new TextToSpeech(this, this);  // OnInitListener
        mTTS.setSpeechRate(0.9f);
        //Toast.makeText(this.getApplicationContext(), "tts speak", Toast.LENGTH_SHORT).show();
        //super.onCreate();
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        //super.onDestroy();
    }
    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        Log.v(TAG, "onstart_service");
        string_to_say=intent.getStringExtra("STRING_TO_SAY");
        speakOut(string_to_say);

        //super.onStartCommand(intent, flags, startId);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            //#*
            speakOut(string_to_say);
            while(mTTS.isSpeaking());
            stopSelf();

        }
        else {
            Toast.makeText(this, "Could not initialize TextToSpeech, kindly check TTS settings", Toast.LENGTH_SHORT).show();
        }
    }

    private void speakOut(String str) {
        mTTS.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }

}




  /*   #*
    int result = mTts.setLanguage(Locale.US);
            // if (result == TextToSpeech.LANG_MISSING_DATA ||
            // result == TextToSpeech.LANG_NOT_SUPPORTED) {
            //Log.v(TAG, "Language is not available.");
            // } else { */