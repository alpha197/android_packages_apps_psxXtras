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

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class BarsSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String STATUS_BAR_SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";
    private static final String KEY_NAVIGATION_BAR_HEIGHT = "navigation_bar_height";	
	
	private CheckBoxPreference mStatusBarShowBatteryPercent;
	private ListPreference mNavigationBarHeight;
	
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

        // Navigationbar height		
        mNavigationBarHeight = 
		    (ListPreference) prefSet.findPreference(KEY_NAVIGATION_BAR_HEIGHT);
        mNavigationBarHeight.setOnPreferenceChangeListener(this);
        int statusNavigationBarHeight = Settings.System.getInt(getActivity().getApplicationContext()
                .getContentResolver(),
                Settings.System.NAVIGATION_BAR_HEIGHT, 48);
        mNavigationBarHeight.setValue(String.valueOf(statusNavigationBarHeight));
        mNavigationBarHeight.setSummary(mNavigationBarHeight.getEntry());		
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
        } else if (preference == mNavigationBarHeight) {
            int statusNavigationBarHeight = Integer.valueOf((String) newValue);
            int index = mNavigationBarHeight.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.NAVIGATION_BAR_HEIGHT, statusNavigationBarHeight);
            mNavigationBarHeight.setSummary(mNavigationBarHeight.getEntries()[index]);
            return true;
        }
        return false;
    }
}
