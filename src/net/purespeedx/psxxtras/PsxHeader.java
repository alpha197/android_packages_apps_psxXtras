package net.purespeedx.psxxtras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.SettingsPreferenceFragment;

public class PsxHeader extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_PSX_BARS = "psx_bars";
    private static final String KEY_PSX_BUTTONS = "psx_buttons";
    private static final String KEY_PSX_USER_INTERFACE = "psx_user_interface";
    private static final String KEY_PSX_LOCKSCREEN = "psx_lockscreen";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.psx_header);
		PreferenceScreen prefSet = getPreferenceScreen();
        boolean hasBars = getResources().getBoolean(
                R.bool.config_show_psx_bars);
		if (!hasBars) {
		     Preference ps = (Preference) findPreference(KEY_PSX_BARS);
			 if (ps != null) prefSet.removePreference(ps);
		}
		
        boolean hasButtons = getResources().getBoolean(
                R.bool.config_show_psx_buttons);
		if (!hasButtons) {
		     Preference ps = (Preference) findPreference(KEY_PSX_BUTTONS);
			 if (ps != null) prefSet.removePreference(ps);
		}

        boolean hasUserinterface = getResources().getBoolean(
                R.bool.config_show_psx_userinterface);
		if (!hasUserinterface) {
		     Preference ps = (Preference) findPreference(KEY_PSX_USER_INTERFACE);
			 if (ps != null) prefSet.removePreference(ps);
		}
        boolean hasLockscreen = getResources().getBoolean(
                R.bool.config_show_psx_lockscreen);
		if (!hasLockscreen) {
		     Preference ps = (Preference) findPreference(KEY_PSX_LOCKSCREEN);
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
