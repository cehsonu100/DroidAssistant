package mydroid.com.droidassistant.TODO;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WifiToggleService extends IntentService {
    public static final String EXTRA_MESSAGE = "message";
    public WifiToggleService() {
        super("WifiToggleService");
    }

    @Override
    public void onHandleIntent(Intent intent){
        WifiManager wifiManager=(WifiManager)this.getSystemService(Context.WIFI_SERVICE);
        String text=intent.getStringExtra("INPUT");
        text=text.toLowerCase();
        if(text.equals("turn on wifi")||text.equals("wifi स्टार्ट कर दो")){
            wifiManager.setWifiEnabled(true);
        }
        if(text.equals("turn off wifi") || text.equals("wifi बंद कर दो") ){
            wifiManager.setWifiEnabled(false);
        }

    }
}
