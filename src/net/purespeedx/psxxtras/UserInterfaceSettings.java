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
import com.android.settings.Utils;
import android.preference.PreferenceCategory;
import android.database.ContentObserver;
import android.content.res.Resources;
import java.util.ArrayList;

public class UserInterfaceSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_LIGHT_OPTIONS = "category_light_options";
    private static final String KEY_NOTIFICATION_PULSE = "notification_pulse";
    private static final String KEY_BATTERY_LIGHT = "battery_light";
    private static final String PREF_LESS_NOTIFICATION_SOUNDS = "less_notification_sounds";
    private static final String KEY_QUIET_HOURS = "quiet_hours_settings";
    private static final String KEY_SAFE_HEADSET_VOLUME_WARNING = "safe_headset_volume_warning";
    private static final String KEY_INCREASING_RING = "increasing_ring";
    private static final String KEY_DOUBLE_TAP_TO_SLEEP = "double_tap_sleep";
    
    private static final String[] NEED_VOICE_CAPABILITY = {
            KEY_INCREASING_RING
    };
    
    private PreferenceCategory mLightOptions;
    private PreferenceScreen mNotificationPulse;
    private PreferenceScreen mBatteryPulse;
    private ListPreference mAnnoyingNotifications;
    private CheckBoxPreference mVolumeWarning;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.user_interface_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

        boolean hasDT2S = getResources().getBoolean(
                R.bool.config_show_buttons_powermenu);
		if (!hasDT2S) {
		     Preference ps = (Preference) findPreference(KEY_DOUBLE_TAP_TO_SLEEP);
			 if (ps != null) prefSet.removePreference(ps);
		}
        
        boolean hasQuietHours = getResources().getBoolean(
                R.bool.config_show_userinterface_quiethours);
        if (!hasQuietHours) {
            Preference ps = (Preference) findPreference(KEY_QUIET_HOURS);
            if (ps != null) prefSet.removePreference(ps);
        }
        
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

        if ((mNotificationPulse == null) && (mBatteryPulse == null) && (mLightOptions != null)) {
            prefSet.removePreference(mLightOptions);
            mLightOptions = null;
        }
        
        mAnnoyingNotifications = (ListPreference) findPreference(PREF_LESS_NOTIFICATION_SOUNDS);
        boolean hasAnnoyingNotifications = getResources().getBoolean(
                R.bool.config_show_userinterface_lessnotifications);
        if (mAnnoyingNotifications !=null) {
            if (!hasAnnoyingNotifications) {
                prefSet.removePreference(mAnnoyingNotifications);
                mAnnoyingNotifications = null;
            } else {
                int notificationThreshold = Settings.System.getInt(getContentResolver(),
                        Settings.System.MUTE_ANNOYING_NOTIFICATIONS_THRESHOLD,
                        0);
                mAnnoyingNotifications.setValue(Integer.toString(notificationThreshold));
                mAnnoyingNotifications.setOnPreferenceChangeListener(this);        
            }
        }
        
        mVolumeWarning = (CheckBoxPreference) findPreference(KEY_SAFE_HEADSET_VOLUME_WARNING);
        mVolumeWarning.setChecked(Settings.System.getInt(getContentResolver(),
                    Settings.System.MANUAL_SAFE_MEDIA_VOLUME, 1) == 1);
        mVolumeWarning.setOnPreferenceChangeListener(this);        
		
        if (!Utils.isVoiceCapable(getActivity())) {
            for (String prefKey : NEED_VOICE_CAPABILITY) {
                Preference pref = findPreference(prefKey);
                if (pref != null) {
                    getPreferenceScreen().removePreference(pref);
                }
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
        final String key = preference.getKey();
        if (preference == mAnnoyingNotifications) {
            final int val = Integer.valueOf((String) objValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.MUTE_ANNOYING_NOTIFICATIONS_THRESHOLD, val);
            return true;
        } else if (preference == mVolumeWarning) {
            int volumeWarning = (Boolean) objValue ? 1 : 0;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.MANUAL_SAFE_MEDIA_VOLUME, volumeWarning);
			return true;
        }
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
