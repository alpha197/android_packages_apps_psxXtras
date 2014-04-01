package net.purespeedx.psxxtras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.android.settings.SettingsPreferenceFragment;
import android.content.res.Resources;

import net.purespeedx.psxxtras.R;

public class StatusbarSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String STATUS_BAR_SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";
	private static final String KEY_SWIPE_FOR_QS = "swipe_for_qs";
	
	private CheckBoxPreference mStatusBarShowBatteryPercent;
    private ListPreference mSwipeForQs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.statusbar_settings);
		PreferenceScreen prefSet = getPreferenceScreen();
		
		// Statusbar battery percentage
        mStatusBarShowBatteryPercent =
            (CheckBoxPreference) prefSet.findPreference(STATUS_BAR_SHOW_BATTERY_PERCENT);
		if (mStatusBarShowBatteryPercent != null) {
            boolean hasNativBatterypercent = getResources().getBoolean(
                R.bool.config_show_statusbar_native_battery);
	        if (!hasNativBatterypercent) {
    		    prefSet.removePreference(mStatusBarShowBatteryPercent);
				mStatusBarShowBatteryPercent = null;
    	    } else {
                mStatusBarShowBatteryPercent.setChecked((Settings.System.getInt(getContentResolver(),
                    "status_bar_native_battery_percentage", 0) == 1));
                mStatusBarShowBatteryPercent.setOnPreferenceChangeListener(this);
            }
		}
		
        mSwipeForQs = (ListPreference) findPreference(KEY_SWIPE_FOR_QS);
		if (mSwipeForQs !=null) {
			mSwipeForQs.setOnPreferenceChangeListener(this);
			int mSwipeVal = Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                                 Settings.System.SWIPE_FOR_QS, 0);
			updateSwipeForQs(mSwipeVal);	
		}
		
    }

	private void updateSwipeForQs(int value) {
		mSwipeForQs.setValue(String.valueOf(value));
		Resources res = getResources();
        String menustate = "";
        switch(value){
			case 1:
				menustate = res.getString(R.string.swipe_for_qs_right);
				break;
			case 2:
				menustate = res.getString(R.string.swipe_for_qs_left);
				break;
			default:
				menustate = res.getString(R.string.swipe_for_qs_off);
		}
		mSwipeForQs.setSummary(menustate);	
	}
	
	
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {      	
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
	    //Statusbar battery percentage
	    if (preference == mStatusBarShowBatteryPercent) {
            Settings.System.putInt(getContentResolver(),
                    "status_bar_native_battery_percentage",
                    (Boolean) newValue ? 1 : 0);
            return true;
		} else if (preference == mSwipeForQs) {
            final int val = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SWIPE_FOR_QS, val);
            updateSwipeForQs(val);
            return true;        
        }
        return false;
    }
}
