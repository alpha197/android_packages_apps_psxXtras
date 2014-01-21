package net.purespeedx.psxxtras.Utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		BackgroundIntentService.performAction(context, BackgroundIntentService.ACTION_BOOT_COMPLETE);	
	}

}
