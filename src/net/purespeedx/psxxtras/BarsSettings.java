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
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class BarsSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String STATUS_BAR_SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";
	
	private CheckBoxPreference mStatusBarShowBatteryPercent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.bars_settings);
		PreferenceScreen prefSet = getPreferenceScreen();
		
		// Statusbar battery percentage
        mStatusBarShowBatteryPercent =
            (CheckBoxPreference) prefSet.findPreference(STATUS_BAR_SHOW_BATTERY_PERCENT);
        mStatusBarShowBatteryPercent.setChecked((Settings.System.getInt(getContentResolver(),
            "status_bar_native_battery_percentage", 0) == 1));
        mStatusBarShowBatteryPercent.setOnPreferenceChangeListener(this);
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
        }
        return false;
    }
}
