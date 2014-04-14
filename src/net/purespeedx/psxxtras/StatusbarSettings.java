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

	private static final String KEY_SWIPE_FOR_QS = "swipe_for_qs";
	
	private CheckBoxPreference mSwipeForQs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.statusbar_settings);
		PreferenceScreen prefSet = getPreferenceScreen();
				
        mSwipeForQs = (CheckBoxPreference) findPreference(KEY_SWIPE_FOR_QS);
		if (mSwipeForQs !=null) {
			mSwipeForQs.setOnPreferenceChangeListener(this);
			mSwipeForQs.setChecked(Settings.System.getInt(getContentResolver(),
                                 Settings.System.QUICK_SETTINGS_QUICK_PULL_DOWN, 0) == 1);
		}
		
    }

	
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {      	
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
	    //Statusbar battery percentage
		if (preference == mSwipeForQs) {
            final int val = (Boolean) newValue ? 1 : 2;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.QUICK_SETTINGS_QUICK_PULL_DOWN, val);
            return true;        
        }
        return false;
    }
}
