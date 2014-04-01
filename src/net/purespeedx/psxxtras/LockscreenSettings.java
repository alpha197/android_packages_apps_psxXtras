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

import com.android.settings.SettingsPreferenceFragment;

import net.purespeedx.psxxtras.R;

public class LockscreenSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_SEE_THROUGH = "see_through";

    private CheckBoxPreference mSeeThrough;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lockscreen_settings);
        PreferenceScreen root = getPreferenceScreen();
        boolean hasLockscreenNotification = getResources().getBoolean(
                R.bool.config_show_lockscreen_notification);
    	if (!hasLockscreenNotification) {
	        Preference ps = (Preference) findPreference("lockscreen_notifications");
    		 if (ps != null) root.removePreference(ps);
	    }	
       // lockscreen see through
        mSeeThrough = (CheckBoxPreference) root.findPreference(KEY_SEE_THROUGH);
        if (mSeeThrough != null) {
            boolean hasLockscreenSeeThrough = getResources().getBoolean(
                    R.bool.config_show_lockscreen_seethrough);
            if (!hasLockscreenSeeThrough) {
                root.removePreference(mSeeThrough);
                mSeeThrough = null;
            } else {
                mSeeThrough.setChecked(Settings.System.getInt(getContentResolver(),
                        Settings.System.LOCKSCREEN_SEE_THROUGH, 0) == 1);
            }
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {      
       if (preference == mSeeThrough) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCKSCREEN_SEE_THROUGH,
                    mSeeThrough.isChecked() ? 1 : 0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // TODO Auto-generated method stub
        return false;
    }
}
