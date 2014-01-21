package net.purespeedx.psxxtras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import net.purespeedx.psxxtras.Utils.CMDProcessor;
import net.purespeedx.psxxtras.Utils.Constants;
import net.purespeedx.psxxtras.Utils.Helpers;
import java.lang.StringBuilder;
 
public class KernelSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KERNEL_FORCE_FASTCHARGE = "kernel_settings_force_fastcharge";
    
    private CheckBoxPreference mForceFastcharge;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.kernel_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

	    Resources res = getResources();
		
		// Force Fastcharge
        mForceFastcharge =
            (CheckBoxPreference) prefSet.findPreference(KERNEL_FORCE_FASTCHARGE);
		String mFastChargePath = Helpers.fastcharge_path();
		if (mForceFastcharge != null) {
	        if (mFastChargePath == null) {
    		    prefSet.removePreference(mForceFastcharge);
				mForceFastcharge = null;
    	    } else {
				UpdateFastCharge();
                mForceFastcharge.setOnPreferenceChangeListener(this);
            }
		}
        
    }
	
	public void SetFastCharge(boolean newValue) {
	    int enabled=(newValue ? 1 : 2);
		String mFastChargePath = Helpers.fastcharge_path();
        if (mFastChargePath != null) {
		    if (enabled == 1) {
		        new CMDProcessor().su.runWaitFor(
                "busybox echo 1 > " + mFastChargePath);
		    } else {
		        new CMDProcessor().su.runWaitFor(
                "busybox echo 0 > " + mFastChargePath);
				enabled = 2;
			}
		} else {
		  enabled = 0;
		}
    	Settings.System.putInt(getContentResolver(),"kernel_force_fastcharge", enabled);
	    UpdateFastCharge();
	}

    public void UpdateFastCharge() {
        int currentstate= Settings.System.getInt(getContentResolver(), Settings.System.KERNEL_FORCE_FASTCHARGE, 0) ;
		if (currentstate == 0) {
		    String mFastChargePath = Helpers.fastcharge_path();
            if (mFastChargePath != null) {
    		    currentstate = 2;
	    	}
		}
        mForceFastcharge.setEnabled((currentstate > 0));
        mForceFastcharge.setChecked((currentstate == 1));
        switch(currentstate){
        case 0:
            mForceFastcharge.setSummary(R.string.kernel_settings_force_fastcharge_summary_unsupported);
			break;
        case 1:
            mForceFastcharge.setSummary(R.string.kernel_settings_force_fastcharge_summary_on);
			break;
        case 2:
            mForceFastcharge.setSummary(R.string.kernel_settings_force_fastcharge_summary_off);
			break;
        }
    }
       

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {          
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        //Fastcharge
        if (preference == mForceFastcharge) {
            SetFastCharge((Boolean) newValue ? true : false);
            return true;
        }
        return false;
    }
	
}
