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

import net.purespeedx.psxxtras.R;

public class BarsSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_STATUSBAR = "psx_statusbar";
    private static final String KEY_NAVBAR = "psx_navbar";

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.bars_settings);
		PreferenceScreen prefSet = getPreferenceScreen();
		
        boolean hasStatusbar = getResources().getBoolean(
                R.bool.config_show_psx_statusbar);
    	if (!hasStatusbar) {
	        Preference ps = (Preference) findPreference(KEY_STATUSBAR);
    		 if (ps != null) prefSet.removePreference(ps);
	    }
		
        boolean hasNavbar = getResources().getBoolean(
                R.bool.config_show_psx_navbar);
    	if (!hasNavbar) {
	        Preference ps = (Preference) findPreference(KEY_NAVBAR);
    		 if (ps != null) prefSet.removePreference(ps);
	    }	
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {      	
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }
}
