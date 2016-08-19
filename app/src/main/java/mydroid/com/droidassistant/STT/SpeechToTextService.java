package mydroid.com.droidassistant.STT;

import mydroid.com.droidassistant.R;
import mydroid.com.droidassistant.SPEECHiNTERPRETER.StringAnalyzer;
import mydroid.com.droidassistant.TTS.TextToSpeechService;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class SpeechToTextService extends Service {
    SpeechRecognizer speechRecognizer;
    Notification notification;
    Intent speechRecognizerIntent;
    Intent stt_intent;
    int ONGOING_NOTIFICATION_ID = 101;

    public SpeechToTextService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stt_intent=intent;
        //Initiate speechRecognizer object and start listening
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new SpeechListener(this,stt_intent));
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizer.startListening(speechRecognizerIntent);
        Toast.makeText(this, "Speak now", Toast.LENGTH_LONG).show();

        //start foreground notification
        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.skull)
                .setContentTitle("DroidAssistant")
                .setTicker("DroidAssistant")
                .setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.skull))
                .setContentText(getString(R.string.sticker)).build();
        startForeground(ONGOING_NOTIFICATION_ID, notification);

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this,"SpToText Stoped",Toast.LENGTH_SHORT).show();
        stopForeground(true);
        stopSelf();
    }
}

/*******************************Listioner class *****************************************/
class SpeechListener  implements RecognitionListener {
    ArrayList<String> result;
    Context stt_context;
    Intent stt_intent;
    public SpeechListener(Context c,Intent stt_intent) {
        stt_context=c;
        this.stt_intent=stt_intent;
    }

    @Override
    public void onBeginningOfSpeech()
    {
        Log.d("tag beg ", "onBeginingOfSpeeches"); //$NON-NLS-1$
    }

    @Override
    public void onBufferReceived(byte[] buffer)
    {}

    @Override
    public void onEndOfSpeech()
    {
        Log.d("tag end", "onEndOfSpeeches"); //$NON-NLS-1$
    }

    @Override
    public void onError(int error) {
        String errorMsg = getErrorMsg(error);
        Toast.makeText(stt_context.getApplicationContext(),errorMsg,Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(stt_context,TextToSpeechService.class);
        intent.putExtra("STRING_TO_SAY", errorMsg);
        stt_context.startService(intent);
        stt_context.stopService(stt_intent);
        //SpeechToTextService speechToTextService = new SpeechToTextService();
        //speechToTextService.speechRecognizer.startListening(speechToTextService.speechRecognizerIntent);

    }

    @Override
    public void onEvent(int eventType, Bundle params)
    {}

    @Override
    public void onPartialResults(Bundle partialResults)
    {}

    @Override
    public void onReadyForSpeech(Bundle params)
    {
        Log.d("tag", "onReadyForSpeech"); //$NON-NLS-1$*/
    }

    @Override
    public void onResults(Bundle results)
    {
        result = new ArrayList(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
        int listSize=result.size();
        System.out.println("sdffhfhfghdfhsetewfgbcxbdfggdafsdafgdf" + result);
        if(result!=null) {
            Toast.makeText(stt_context.getApplicationContext(), result.get(0), Toast.LENGTH_LONG).show();
            StringAnalyzer stringAnalyzer = new StringAnalyzer(stt_context,stt_intent);
            stringAnalyzer.inputString(result.get(0));

        }
    }

    @Override
    public void onRmsChanged(float rmsdB)
    {}

    public static String getErrorMsg(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Soryy Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Sorry Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Soryy Insufficient permissions kindly grant all requred permisiion";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Sorry Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "sorry Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "Sorry No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "Sorry RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "Sorry error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "sorry No speech input";
                break;
            default:
                message = "Sorry Didn't understand, please try again.";
                break;
        }
        return message;
    }
}