package net.purespeedx.psxxtras;


import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import java.util.List;
import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.preference.Preference.OnPreferenceChangeListener;

import com.android.settings.R;
import android.preference.PreferenceCategory;
import android.database.ContentObserver;
import android.content.res.Resources;
import java.util.ArrayList;

public class UserInterfaceSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_LIGHT_OPTIONS = "category_light_options";
    private static final String KEY_NOTIFICATION_PULSE = "notification_pulse";
    private static final String KEY_BATTERY_LIGHT = "battery_light";

    private PreferenceCategory mLightOptions;
    private PreferenceScreen mNotificationPulse;
    private PreferenceScreen mBatteryPulse;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.user_interface_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

        mLightOptions = (PreferenceCategory) prefSet.findPreference(KEY_LIGHT_OPTIONS);
        mNotificationPulse = (PreferenceScreen) findPreference(KEY_NOTIFICATION_PULSE);
        if (mNotificationPulse != null) {
            if (!getResources().getBoolean(
                com.android.internal.R.bool.config_intrusiveNotificationLed)) {
                mLightOptions.removePreference(mNotificationPulse);
                mNotificationPulse = null ;
            } else {
                updateLightPulseDescription();
            }
        }

        mBatteryPulse = (PreferenceScreen) findPreference(KEY_BATTERY_LIGHT);
        if (mBatteryPulse != null) {
            if (getResources().getBoolean(
                    com.android.internal.R.bool.config_intrusiveBatteryLed) == false) {
                mLightOptions.removePreference(mBatteryPulse);
                mBatteryPulse = null;
            } else {
                updateBatteryPulseDescription();
            }
        }
    }

    private void updateLightPulseDescription() {
        if (mNotificationPulse != null) {    
            if (Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.NOTIFICATION_LIGHT_PULSE, 0) == 1) {
                mNotificationPulse.setSummary(getString(R.string.notification_light_enabled));
            } else {
                mNotificationPulse.setSummary(getString(R.string.notification_light_disabled));
            }
        }
    }

    private void updateBatteryPulseDescription() {
        if (mBatteryPulse != null) {    
            if (Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.BATTERY_LIGHT_ENABLED, 1) == 1) {
                mBatteryPulse.setSummary(getString(R.string.notification_light_enabled));
            } else {
                mBatteryPulse.setSummary(getString(R.string.notification_light_disabled));
            }
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
    
    @Override
    public void onResume() {
        super.onResume();
        updateLightPulseDescription();
        updateBatteryPulseDescription();
    }    
    
    @Override
    public void onPause() {
        super.onPause();
    }
    
}
