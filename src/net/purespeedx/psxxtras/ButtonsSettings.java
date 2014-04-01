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

import com.android.settings.SettingsPreferenceFragment;

import net.purespeedx.psxxtras.R;
public class ButtonsSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_POWER_MENU = "power_menu";
    private static final String KEY_VOLUME_ROCKER_CATEGORY = "pref_volume_rocker_category";
    private static final String KEY_VOLUME_WAKE = "pref_volume_wake";
    private static final String KEY_VOLBTN_MUSIC_CTRL = "volbtn_music_controls";    

    private CheckBoxPreference mVolumeWake;
    private CheckBoxPreference mVolBtnMusicCtrl;    
    
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

        boolean hasVolumeWake = getResources().getBoolean(
                R.bool.config_show_volumerocker_wake);
        mVolumeWake = (CheckBoxPreference) findPreference(KEY_VOLUME_WAKE);
        if (mVolumeWake != null) {
            if (!hasVolumeWake) {
                prefSet.removePreference(mVolumeWake);       
                mVolumeWake = null;
            } else {
                mVolumeWake.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                            Settings.System.VOLUME_WAKE_SCREEN, 0) == 1);
            }
        }

        boolean hasVolumeControl = getResources().getBoolean(
                R.bool.config_show_volumerocker_musiccontrol);  
        mVolBtnMusicCtrl = (CheckBoxPreference) findPreference(KEY_VOLBTN_MUSIC_CTRL);
        if (mVolBtnMusicCtrl != null) {
            if (!hasVolumeControl) {               
                prefSet.removePreference(mVolBtnMusicCtrl);       
                mVolBtnMusicCtrl = null;        
            } else {
                mVolBtnMusicCtrl.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                        Settings.System.VOLBTN_MUSIC_CONTROLS, 0) == 1);
            }
        }
	}

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mVolumeWake) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.VOLUME_WAKE_SCREEN,
                    mVolumeWake.isChecked()
                    ? 1 : 0);
         } else if (preference == mVolBtnMusicCtrl) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.VOLBTN_MUSIC_CONTROLS,
                    mVolBtnMusicCtrl.isChecked()
                    ? 1 : 0);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        // TODO Auto-generated method stub
        return false;
    }
	
}
