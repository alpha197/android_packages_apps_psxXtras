package net.purespeedx.psxxtras.Utils;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import com.android.settings.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class KernelHelper implements Constants {

	private static String TAG="psxXtras";
	private static String cpuOnline="/sys/devices/system/cpu/cpu1/online";

	public static boolean SetFastCharge(Context context) {
		try {
			String mFastChargePath = Helpers.fastcharge_path();
			if (mFastChargePath != null) {
				int enabled = Settings.System.getInt(context.getContentResolver(), Settings.System.KERNEL_FORCE_FASTCHARGE, 2);
				if (enabled == 1) {
					CMDProcessor.CommandResult cres=new CMDProcessor().su.runWaitFor(
					    "busybox echo 1 > " + mFastChargePath);
					if (!cres.success()) {
						Log.e(TAG,cres.stderr);
					}
					return cres.success();
				} else {
    				CMDProcessor.CommandResult cres=new CMDProcessor().su.runWaitFor(
					    "busybox echo 0 > " + mFastChargePath);
					if (!cres.success()) {
						Log.e(TAG,cres.stderr);
					}
				    return cres.success();
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		    return false;
		}
		return false;
	}
	
	public static boolean SetGovernor(Context context) {
		String governor = Settings.System.getString(context.getContentResolver(), Settings.System.KERNEL_GOVERNOR);
		boolean result = true;
		if (governor !=null && governor !="") {
			for (int i = 0; i < Helpers.getNumOfCpus(); i++) {
				final StringBuilder sb = new StringBuilder();
				if (i > 0) {
					sb.append("echo 1 > ")
						.append(cpuOnline.replace("cpu1", "cpu" + i)).append(";");
				}
				sb.append("echo ").append(governor).append(" > ")
					.append(GOVERNOR_PATH.replace("cpu0", "cpu" + i)).append(";");
				if (i > 0) {
					sb.append("echo 0 > ")
						.append(cpuOnline.replace("cpu1", "cpu" + i)).append(";");
				}
				sb.append("\n");
				CMDProcessor.CommandResult cres=new CMDProcessor().su.runWaitFor(sb.toString());			
				if (!cres.success()) {
					result = false;
					Log.e(TAG,sb.toString());
					Log.e(TAG,cres.stderr);
				}			
			}
			return result;
        } else {
			return false;
		}
	}
	
	public static String[] ReturnSplitted(String Path) {
		String[] result = null;
		try {
			String tempResult = Helpers.readOneLine(Path);
			if (tempResult !=null) result=tempResult.split(" ");
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			result = null;
		}
		return result;
	}
	
	public static String[] GetAvailableGovernors() {
		return ReturnSplitted(GOVERNORS_LIST_PATH);
	}

	public static String[] GetAvailableFrequencies() {
		String[] result=ReturnSplitted(STEPS_PATH);
		if (result != null)
		Arrays.sort(result, new Comparator<String>() {
            @Override
            public int compare(String object1, String object2) {
                return Integer.valueOf(object1).compareTo(Integer.valueOf(object2));
            }
        });		
		return result;
	}
	
	public static boolean SetFrequency(Context context,String Path,String SettingsPath) {
		String freq = Settings.System.getString(context.getContentResolver(), SettingsPath);
		boolean result=true;
		if (freq !=null && freq !="") {
			for (int i = 0; i < Helpers.getNumOfCpus(); i++) {
				final StringBuilder sb = new StringBuilder();
				if (i > 0) {
					sb.append("echo 1 > ")
						.append(cpuOnline.replace("cpu1", "cpu" + i)).append(";");
				}
				sb.append("busybox echo ").append(freq).append(" > ")
					.append(Path.replace("cpu0", "cpu" + i)).append(";");
				if (i > 0) {
					sb.append("echo 0 > ")
						.append(cpuOnline.replace("cpu1", "cpu" + i)).append(";");
				}
				sb.append("\n");
				CMDProcessor.CommandResult cres=new CMDProcessor().su.runWaitFor(sb.toString());			
				if (!cres.success()) {
					result = false;
					Log.e(TAG,sb.toString());
					Log.e(TAG,cres.stderr);
				}
			}
			return result;
        } else {
			return false;
		}
	}
	
	public static void SetOnBoot(Context context) {
		SetFastCharge(context);
		SetGovernor(context);
		if (Settings.System.getInt(context.getContentResolver(), Settings.System.KERNEL_FORCE_FASTCHARGE, 0) == 1) {
			Log.i(TAG, "applying CPU frequencies");
			SetFrequency(context,MIN_FREQ_PATH,Settings.System.KERNEL_CPU_FREQUENCY_MIN);
			SetFrequency(context,MAX_FREQ_PATH,Settings.System.KERNEL_CPU_FREQUENCY_MAX);
		}
	}
}

