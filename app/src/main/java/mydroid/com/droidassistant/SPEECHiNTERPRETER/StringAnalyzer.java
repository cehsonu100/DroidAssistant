package mydroid.com.droidassistant.SPEECHiNTERPRETER;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.net.URI;

import mydroid.com.droidassistant.TODO.GetContactNumber;
import mydroid.com.droidassistant.TODO.WifiToggleService;
import mydroid.com.droidassistant.TTS.TextToSpeechService;

/**
 * Created by Roshni on 6/9/2016.
 */
public class StringAnalyzer {
    Context stt_context;
    Intent stt_intent;
    Intent intent;
    String[] command_list={"turn on wifi","turn off wifi","wifi बंद कर दो","wifi स्टार्ट कर दो" };
    public StringAnalyzer(Context context,Intent intent){
        stt_context=context;
        stt_intent=intent;
    }

    public void inputString(String input)
    {
        String name="";String mobile_number="Not saved";
        int flag=-99,i=0;
        input=input.toLowerCase();
        int command_list_length=command_list.length;
        input=input.replaceAll("[\\-\\+\\.\\^:,]","");;       //replace all apart  [a-zA-Z0-9]

        String[] split_input=input.split(" ");
        for(int j=1; j<split_input.length; j++)
            name=name.concat(split_input[j]).concat(" ");    //take contact name
        name=name.trim();

       // Toast.makeText(stt_context.getApplicationContext(), name, Toast.LENGTH_SHORT).show();

        if(split_input[0].equals("call") || split_input[0].equals("कॉल"))
        {
            //Retrive phone number
            GetContactNumber getContactNumber = new GetContactNumber();
            mobile_number=getContactNumber.getNumber(name,stt_context);

           // Toast.makeText(stt_context.getApplicationContext(), mobile_number, Toast.LENGTH_SHORT).show();
            //if found call the number
            if(!mobile_number.equals("Not saved")){
                intent=new Intent(stt_context,TextToSpeechService.class);
                intent.putExtra("STRING_TO_SAY", "ok, calling " +name);
                stt_context.startService(intent);

                Intent call_intent = new Intent(Intent.ACTION_CALL);
                call_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                String tel="tel:";
                String mobile_number_uri= tel.concat(mobile_number);
                call_intent.setData(Uri.parse(mobile_number_uri));
                stt_context.getApplicationContext().startActivity(call_intent);

            }

            //if not found say error msg
            else{
                intent=new Intent(stt_context,TextToSpeechService.class);
                intent.putExtra("STRING_TO_SAY", "sorry, contact " +name+ " not found");
                stt_context.startService(intent);
            }

        }

        else {

            for(i = 0 ; i < command_list_length ; i++)
            {
                if(input.equals(command_list[i]))
                {
                    flag = i;
                    break;
                }
            }
            switch (flag) {
                case 0:case 1:case 2:case 3:                   //wifiToggle

                    //Start WifiToggleService
                    Intent to_do_intent=new Intent(stt_context, WifiToggleService.class);
                    to_do_intent.putExtra("INPUT", input);
                    stt_context.startService(to_do_intent);

                    //start TTS service
                    intent=new Intent(stt_context,TextToSpeechService.class);
                    intent.putExtra("STRING_TO_SAY", "ok, done");
                    stt_context.startService(intent);
                    break;

                default:                      //Command not found
                    intent=new Intent(stt_context,TextToSpeechService.class);
                    intent.putExtra("STRING_TO_SAY", "Command not found");
                    stt_context.startService(intent);
                    break;
            }
        }
    stopTTSservice();
    }

    public void stopTTSservice(){
        stt_context.stopService(stt_intent);
    }

}
