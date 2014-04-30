package net.purespeedx.psxxtras;

import android.app.Activity;
import android.content.ContentResolver;
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
import android.widget.Toast;

import com.android.settings.SettingsPreferenceFragment;

import net.purespeedx.psxxtras.R;

public class NavbarSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_NAVIGATION_BAR_HEIGHT = "navigation_bar_height";	
    private static final String KILL_APP_LONGPRESS_BACK = "kill_app_longpress_back";
	
	private ListPreference mNavigationBarHeight;
    private CheckBoxPreference mKillAppLongpressBack;	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.navbar_settings);
		PreferenceScreen prefSet = getPreferenceScreen();
		ContentResolver resolver = getActivity().getContentResolver();
        
        // Navigationbar height
        mNavigationBarHeight = 
		    (ListPreference) prefSet.findPreference(KEY_NAVIGATION_BAR_HEIGHT);
	    if (mNavigationBarHeight != null) {
            mNavigationBarHeight.setOnPreferenceChangeListener(this);
            int statusNavigationBarHeight = Settings.System.getInt(resolver,
                    Settings.System.NAVIGATION_BAR_HEIGHT, 48);
            mNavigationBarHeight.setValue(String.valueOf(statusNavigationBarHeight));
            mNavigationBarHeight.setSummary(mNavigationBarHeight.getEntry());		
		}
        
        mKillAppLongpressBack = (CheckBoxPreference) prefSet.findPreference(KILL_APP_LONGPRESS_BACK);
        mKillAppLongpressBack.setChecked(Settings.System.getInt(
                resolver, Settings.System.KILL_APP_LONGPRESS_BACK, 0) != 0);    
    }

    
   private void writeKillAppLongpressBackOptions() {
        Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.KILL_APP_LONGPRESS_BACK,
                mKillAppLongpressBack.isChecked() ? 1 : 0);
    }    
      
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {      	
        if (preference == mKillAppLongpressBack) {
            writeKillAppLongpressBackOptions();
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
	    ContentResolver resolver = getActivity().getApplicationContext().getContentResolver();
        if (preference == mNavigationBarHeight) {
            int statusNavigationBarHeight = Integer.valueOf((String) newValue);
            int index = mNavigationBarHeight.findIndexOfValue((String) newValue);
            Settings.System.putInt(resolver,
                    Settings.System.NAVIGATION_BAR_HEIGHT, statusNavigationBarHeight);
            mNavigationBarHeight.setSummary(mNavigationBarHeight.getEntries()[index]);
            return true;
        }
        return false;
    }
}
