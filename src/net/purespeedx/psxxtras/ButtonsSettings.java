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
import android.preference.PreferenceScreen;
import android.content.ContentResolver;
import android.preference.ListPreference;

public class ButtonsSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_POWER_MENU = "power_menu";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.buttons_settings);
        
		ContentResolver resolver = getActivity().getContentResolver();	
		PreferenceScreen prefSet = getPreferenceScreen();

        boolean hasPowermenu = getResources().getBoolean(
                R.bool.config_show_buttons_powermenu);
		if (!hasPowermenu) {
		     Preference ps = (Preference) findPreference(KEY_POWER_MENU);
			 if (ps != null) prefSet.removePreference(ps);
		}
		
	}


    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {      
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        // TODO Auto-generated method stub
        return false;
    }
	
}
