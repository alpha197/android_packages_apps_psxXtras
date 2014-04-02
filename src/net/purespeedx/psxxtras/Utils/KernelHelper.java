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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class KernelHelper implements Constants {

    private static String TAG="psxXtras";
    private static String cpuOnline="/sys/devices/system/cpu/cpu1/online";

    private static boolean FileExists(String Path) {
        return new File(Path).exists();
    }

    /*
    private static boolean Execute(String Command) {
        CMDProcessor.CommandResult cres=new CMDProcessor().su.runWaitFor(Command);
        if (!cres.success()) {
            Log.e(TAG,Command);
            if (cres.stderr != null && cres.stderr != "") {
                Log.e(TAG,cres.stderr);
            } else {
                Log.e(TAG,"failed without error msg");
            }
            return false;
        }
        return true;
    }
    */
    
    public static boolean SetFastCharge(Context context) {
        try {
            String mFastChargePath = Helpers.fastcharge_path();
            if (mFastChargePath != null) {
                int enabled = Settings.System.getInt(context.getContentResolver(), Settings.System.KERNEL_FORCE_FASTCHARGE, 2);
                return SetValueDirect(mFastChargePath,String.valueOf(enabled));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return false;
    }
      
    public static boolean SetValueDirect(String Path,String value) {
        try {
            if (value != null && value != "") {
                boolean result=Helpers.writeOneLine(Path,value);
                // if (result == false) result=Execute("busybox echo " + value + " > " + Path);
                return result;
            } else {
                return false;
            }    
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }
    
    public static boolean SetValue(Context context,String Path,String SettingsPath) {
        try {
            if (Path != null) {
                String value = Settings.System.getString(context.getContentResolver(), SettingsPath);
                return SetValueDirect(Path,value);
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
                final String Path = GOVERNOR_PATH.replace("cpu0", "cpu" + i);
                if (FileExists(Path)) {
                    result =SetValueDirect(Path,governor);
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
    
    public static String[] GetAvailableCpus() {
        String[] result =null;
        try {
            int Count=Helpers.getNumOfCpus();
            int cur = 0;
            result = new String[Count];
            while(cur < Count)
            {
                result[cur]= String.valueOf(cur + 1);
                cur++;
            }                    
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            result = null;
        }              
        return result;
    }
    
    public static boolean SetFrequency(Context context,String Path,String SettingsPath) {
        String freq = Settings.System.getString(context.getContentResolver(), SettingsPath);
        boolean result=true;
        if (freq !=null && freq !="") {
            for (int i = 0; i < Helpers.getNumOfCpus(); i++) {
                final String mPath = Path.replace("cpu0", "cpu" + i);
                if (FileExists(mPath)) {
                    result=SetValueDirect(mPath,freq);
                }
            }
            return result;
        } else {
            return false;
        }
    }
    
    public static void SetOnBoot(Context context) {
        Log.i(TAG, "Checking an Applying kernelsettings");
        SetFastCharge(context);
        SetGovernor(context);
        if (Settings.System.getInt(context.getContentResolver(), Settings.System.KERNEL_CPU_FREQUENCY_APPLY, 0) == 1) {
            Log.i(TAG, "applying CPU frequencies");
            SetFrequency(context,MIN_FREQ_PATH,Settings.System.KERNEL_CPU_FREQUENCY_MIN);
            SetFrequency(context,MAX_FREQ_PATH,Settings.System.KERNEL_CPU_FREQUENCY_MAX);
        }
        if (Settings.System.getInt(context.getContentResolver(), Settings.System.KERNEL_INPUTBOOST_APPLY, 0) == 1) {
            Log.i(TAG, "applying CPU Boost Settings");
            SetValue(context,CPUBOOST_INPUT_FREQ,Settings.System.KERNEL_INPUTBOOST_FREQ);
            SetValue(context,CPUBOOST_INPUT_MS,Settings.System.KERNEL_INPUTBOOST_MS);
            SetValue(context,CPUBOOST_BOOST_MS,Settings.System.KERNEL_CPUBOOST_MS);
            SetValue(context,CPUBOOST_SYNC_THRESHOLD,Settings.System.KERNEL_SYNC_THRESHOLD_FREQ);            
        }
        if (Settings.System.getInt(context.getContentResolver(), Settings.System.KERNEL_MSMHOTPLUG_APPLY, 0) == 1) {
            Log.i(TAG, "applying MSM-Hotplug Settings");
            SetValue(context,MSMHOTPLUG_MIN_CPUS,Settings.System.KERNEL_MSMHOTPLUG_MIN_CPUS);
            SetValue(context,MSMHOTPLUG_MAX_CPUS,Settings.System.KERNEL_MSMHOTPLUG_MAX_CPUS);
        }
    }
}

