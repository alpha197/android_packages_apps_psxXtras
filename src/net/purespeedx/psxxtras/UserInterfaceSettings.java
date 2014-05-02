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

public class UserInterfaceSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String OMNISWITCH_CATEGORY = "category_omniswitch";
    private static final String RECENTS_USE_OMNISWITCH = "recents_use_omniswitch";
    private static final String OMNISWITCH_START_SETTINGS = "omniswitch_start_settings";

   // Package name of the omnniswitch app
    public static final String OMNISWITCH_PACKAGE_NAME = "org.omnirom.omniswitch";
    // Intent for launching the omniswitch settings actvity
    public static Intent INTENT_OMNISWITCH_SETTINGS = new Intent(Intent.ACTION_MAIN)
            .setClassName(OMNISWITCH_PACKAGE_NAME, OMNISWITCH_PACKAGE_NAME + ".SettingsActivity");    

    
    private CheckBoxPreference mRecentsUseOmniSwitch;
    private Preference mOmniSwitchSettings;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.user_interface_settings);
        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();
               
        PreferenceCategory mOmniSwitch = (PreferenceCategory) prefSet.findPreference(OMNISWITCH_CATEGORY);
        if (!isOmniSwitchInstalled()) {
            prefSet.removePreference(mOmniSwitch);
            mRecentsUseOmniSwitch = null;
            mOmniSwitchSettings = null;
        } else {
            mRecentsUseOmniSwitch = (CheckBoxPreference)
                    prefSet.findPreference(RECENTS_USE_OMNISWITCH);

            try {
                mRecentsUseOmniSwitch.setChecked(Settings.System.getInt(resolver,
                        Settings.System.RECENTS_USE_OMNISWITCH) == 1);
            } catch(SettingNotFoundException e){

            }
            mRecentsUseOmniSwitch.setOnPreferenceChangeListener(this);

            mOmniSwitchSettings = (Preference)
                    prefSet.findPreference(OMNISWITCH_START_SETTINGS);
            mOmniSwitchSettings.setEnabled(isOmniSwitchInstalled());
            mOmniSwitchSettings.setSummary(isOmniSwitchInstalled() ?
                    getResources().getString(R.string.omniswitch_start_settings_summary) :
                    getResources().getString(R.string.omniswitch_not_installed_summary));        
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mOmniSwitchSettings){
            startActivity(INTENT_OMNISWITCH_SETTINGS);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mRecentsUseOmniSwitch) {
            boolean value = (Boolean) objValue;

            Settings.System.putInt(
                    resolver, Settings.System.RECENTS_USE_OMNISWITCH, value ? 1 : 0);
            return true;
        }
        return false;
    }
           
    private boolean isOmniSwitchInstalled() {
        final PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(OMNISWITCH_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }    
}
