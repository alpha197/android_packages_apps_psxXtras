package net.purespeedx.psxxtras.Utils;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.preference.PreferenceManager;
import android.os.Bundle;

import com.android.settings.R;

public class BackgroundIntentService extends IntentService {
    // you could provide more options here, should you need them
    public static final String ACTION_BOOT_COMPLETE                 = "boot_complete";

        
    public static void performAction(Context context, String action) {
        performAction(context, action, null);                
    }

	public static void performAction(Context context, String action, Bundle extras) {
              
        if ((context == null) || (action == null) || action.equals("") ) return;
        Intent svc = new Intent(context, BackgroundIntentService.class);
        svc.setAction(action);
        if (extras != null)        svc.putExtras(extras);
        context.startService(svc);
    }
                                        
    public BackgroundIntentService() {
        super("BackgroundIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();                
        if ((action == null) || (action.equals(""))) return;
         
        if (action.equals(ACTION_BOOT_COMPLETE)) {
            onBootComplete();
        }
    }
        
    protected void onBootComplete() {
		Context context = (Context) this;
		SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	    if (mPreferences == null) return;
		boolean firstrun = mPreferences.getBoolean("su_confirmed", false);
		if (firstrun == false) 	return;
		boolean canSu = Helpers.checkSu();
        boolean canBb = Helpers.checkBusybox();
		if (canSu & canBb) {
			// check and restore fast charge state if kernel supports fastcharge
			KernelHelper.SetOnBoot(context);
		}
	}

}