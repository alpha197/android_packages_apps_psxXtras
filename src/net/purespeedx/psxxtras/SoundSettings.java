package net.purespeedx.psxxtras;


import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import com.android.settings.Utils;

import java.util.ArrayList;
import java.util.List;

import net.purespeedx.psxxtras.R;

public class SoundSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_SAFE_HEADSET_VOLUME_WARNING = "safe_headset_volume_warning";
    private static final String KEY_INCREASING_RING = "increasing_ring";

   
    private static final String[] NEED_VOICE_CAPABILITY = {
            KEY_INCREASING_RING
    };
    
    private CheckBoxPreference mVolumeWarning;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.sound_settings);
        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();
        
        
        mVolumeWarning = (CheckBoxPreference) findPreference(KEY_SAFE_HEADSET_VOLUME_WARNING);
        mVolumeWarning.setChecked(Settings.System.getInt(resolver,
                    Settings.System.MANUAL_SAFE_MEDIA_VOLUME, 1) == 1);
        mVolumeWarning.setOnPreferenceChangeListener(this);        
        
        if (!Utils.isVoiceCapable(getActivity())) {
            for (String prefKey : NEED_VOICE_CAPABILITY) {
                Preference pref = findPreference(prefKey);
                if (pref != null) {
                    prefSet.removePreference(pref);
                }
            }
        }
        
    }
        
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
          return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
    
    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mVolumeWarning) {
            int volumeWarning = (Boolean) objValue ? 1 : 0;
            Settings.System.putInt(resolver,
                    Settings.System.MANUAL_SAFE_MEDIA_VOLUME, volumeWarning);
            return true;
        }
        return false;
    }
        
}
