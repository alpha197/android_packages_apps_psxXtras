package net.purespeedx.psxxtras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.util.DisplayMetrics;
import android.view.DisplayInfo;
import android.view.WindowManager;

import com.android.settings.SettingsPreferenceFragment;

import net.purespeedx.psxxtras.R;

public class LockscreenSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_SEE_THROUGH = "see_through";
    private static final String LOCKSCREEN_MAXIMIZE_WIDGETS = "lockscreen_maximize_widgets";
    private static final String KEY_QUICK_UNLOCK = "quick_unlock";
    
    private CheckBoxPreference mSeeThrough;
    private CheckBoxPreference mMaximizeKeyguardWidgets;
    private CheckBoxPreference mQuickUnlock;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lockscreen_settings);
        PreferenceScreen root = getPreferenceScreen();
       // lockscreen see through
        mSeeThrough = (CheckBoxPreference) root.findPreference(KEY_SEE_THROUGH);
        if (mSeeThrough != null) {
            mSeeThrough.setChecked(Settings.System.getInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_SEE_THROUGH, 0) == 1);
        }
        mMaximizeKeyguardWidgets = (CheckBoxPreference) root.findPreference(LOCKSCREEN_MAXIMIZE_WIDGETS);
        if (mMaximizeKeyguardWidgets != null) {
            if (isTablet()) {
                root.removePreference(root.findPreference(LOCKSCREEN_MAXIMIZE_WIDGETS));
                mMaximizeKeyguardWidgets = null;
            } else {
                mMaximizeKeyguardWidgets.setChecked(Settings.System.getInt(getContentResolver(),
                        Settings.System.LOCKSCREEN_MAXIMIZE_WIDGETS, 0) == 1);
            }
        }
        mQuickUnlock = (CheckBoxPreference) root.findPreference(KEY_QUICK_UNLOCK);
        if (mQuickUnlock != null) {
            mQuickUnlock.setChecked(Settings.System.getInt(getContentResolver(),
                    Settings.System.QUICK_UNLOCK, 0) == 1);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {      
       if (preference == mSeeThrough) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCKSCREEN_SEE_THROUGH,
                    mSeeThrough.isChecked() ? 1 : 0);
            return true;
        } else if (preference == mMaximizeKeyguardWidgets) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_MAXIMIZE_WIDGETS, isToggled(preference) ? 1 : 0);
       } else if (preference == mQuickUnlock) {
            Settings.System.putInt(getContentResolver(), Settings.System.QUICK_UNLOCK,
                    mQuickUnlock.isChecked() ? 1 : 0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean isToggled(Preference pref) {
        return ((CheckBoxPreference) pref).isChecked();
    }
  
    private boolean isTablet() {
        return (getActivity().getApplicationContext().getResources().getConfiguration().screenLayout
            & Configuration.SCREENLAYOUT_SIZE_MASK)
            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }    
}
