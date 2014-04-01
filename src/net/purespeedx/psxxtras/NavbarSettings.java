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

public class NavbarSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_NAVIGATION_BAR_HEIGHT = "navigation_bar_height";	
	
	private ListPreference mNavigationBarHeight;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.navbar_settings);
		PreferenceScreen prefSet = getPreferenceScreen();
		
        // Navigationbar height
        mNavigationBarHeight = 
		    (ListPreference) prefSet.findPreference(KEY_NAVIGATION_BAR_HEIGHT);
	    if (mNavigationBarHeight != null) {
            boolean hasNavbarheight = getResources().getBoolean(
                    R.bool.config_show_navbar_height);
    	    if (!hasNavbarheight) {
         	    prefSet.removePreference(mNavigationBarHeight);
				mNavigationBarHeight = null;
	        } else {
                mNavigationBarHeight.setOnPreferenceChangeListener(this);
                int statusNavigationBarHeight = Settings.System.getInt(getActivity().getApplicationContext()
                        .getContentResolver(),
                        Settings.System.NAVIGATION_BAR_HEIGHT, 48);
                mNavigationBarHeight.setValue(String.valueOf(statusNavigationBarHeight));
                mNavigationBarHeight.setSummary(mNavigationBarHeight.getEntry());		
			}
		}
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {      	
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
	    //Statusbar battery percentage
        if (preference == mNavigationBarHeight) {
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
