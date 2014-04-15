
package net.purespeedx.psxxtras;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.SettingsPreferenceFragment;

import net.purespeedx.psxxtras.R;

public class DoubleTapToSleepSettings extends SettingsPreferenceFragment {

    private static final String DOUBLE_TAP_SLEEP_STATUS_BAR = "double_tap_sleep_status_bar";
    private static final String DOUBLE_TAP_SLEEP_LOCKSCREEN = "double_tap_sleep_lockscreen";

    private CheckBoxPreference mStatusBarDoubleTapSleepStatusBar;
    private CheckBoxPreference mStatusBarDoubleTapSleepLockscreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.double_tap_sleep_settings);

        // Status bar
        mStatusBarDoubleTapSleepStatusBar = (CheckBoxPreference) getPreferenceScreen()
                .findPreference(DOUBLE_TAP_SLEEP_STATUS_BAR);
        mStatusBarDoubleTapSleepStatusBar.setChecked((Settings.System.getInt(getActivity()
                .getApplicationContext().getContentResolver(),
                Settings.System.DOUBLE_TAP_SLEEP_STATUS_BAR, 0) == 1));

        // Lockscreen
        mStatusBarDoubleTapSleepLockscreen = (CheckBoxPreference) getPreferenceScreen()
                .findPreference(DOUBLE_TAP_SLEEP_LOCKSCREEN);
        mStatusBarDoubleTapSleepLockscreen.setChecked((Settings.System.getInt(getActivity()
                .getApplicationContext().getContentResolver(),
                Settings.System.DOUBLE_TAP_SLEEP_LOCKSCREEN, 0) == 1));
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        // Status bar
        if (preference == mStatusBarDoubleTapSleepStatusBar) {
            value = mStatusBarDoubleTapSleepStatusBar.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.DOUBLE_TAP_SLEEP_STATUS_BAR, value ? 1 : 0);
            return true;
        }

        // Pattern
        else if (preference == mStatusBarDoubleTapSleepLockscreen) {
            value = mStatusBarDoubleTapSleepLockscreen.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.DOUBLE_TAP_SLEEP_LOCKSCREEN, value ? 1 : 0);
            return true;
        }

        return false;
    }
}

