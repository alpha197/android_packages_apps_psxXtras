/*
* Copyright (C) 2013 MartinRo
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package net.purespeedx.psxxtras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.os.SystemProperties;

import com.android.settings.SettingsPreferenceFragment;

import net.purespeedx.psxxtras.R;

public class PowermenuSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_POWERMENU_REBOOT_PREFS ="powermenu_reboot_prefs";
    private static final String KEY_POWERMENU_SHUTDOWN_PREFS ="powermenu_shutdown_prefs";
    private static final String KEY_POWERMENU_AIRPLANEMODE_PREFS ="powermenu_airplanemode_prefs";
    private static final String KEY_POWERMENU_SILENTMODE_PREFS ="powermenu_silentmode_prefs";
    private static final String KEY_POWERMENU_USERSWITCH_PREFS ="powermenu_userswitch_prefs";
    private static final String KEY_POWERMENU_SCREENSHOT_PREFS ="powermenu_screenshot_prefs";
    private static final String KEY_POWERMENU_TORCH_PREFS ="powermenu_torch_prefs";
    
    private ListPreference mPowermenuRebootPrefs;
    private ListPreference mPowermenuShutdownPrefs;
    private ListPreference mPowermenuAirplanemodePrefs;
    private ListPreference mPowermenuSilentmodePrefs;
    private ListPreference mPowermenuUserswitchPrefs;
    private ListPreference mPowermenuScreenshotPrefs;
    private ListPreference mPowermenuTorchPrefs;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.powermenu_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        final ContentResolver cr = getActivity().getContentResolver();
        
       // Powermenu Reboot selection
        mPowermenuRebootPrefs = (ListPreference) prefSet.findPreference(KEY_POWERMENU_REBOOT_PREFS);
        mPowermenuRebootPrefs.setOnPreferenceChangeListener(this);
        int mPowermenuRebootPrefsValue = Settings.System.getInt(cr,
                                 Settings.System.POWERMENU_REBOOT_PREFS, 2);
        mPowermenuRebootPrefs.setValue(String.valueOf(mPowermenuRebootPrefsValue));
        updatePowermenuRebootPrefs(mPowermenuRebootPrefsValue);

        // Powermenu Shutdown selection
        mPowermenuShutdownPrefs = (ListPreference) prefSet.findPreference(KEY_POWERMENU_SHUTDOWN_PREFS);
        mPowermenuShutdownPrefs.setOnPreferenceChangeListener(this);
        int mPowermenuShutdownPrefsValue = Settings.System.getInt(cr,
                                 Settings.System.POWERMENU_SHUTDOWN_PREFS, 2);
        mPowermenuShutdownPrefs.setValue(String.valueOf(mPowermenuShutdownPrefsValue));
        updatePowermenuShutdownPrefs(mPowermenuShutdownPrefsValue);

        // Powermenu Airplane Mode selection
        mPowermenuAirplanemodePrefs = (ListPreference) prefSet.findPreference(KEY_POWERMENU_AIRPLANEMODE_PREFS);
        mPowermenuAirplanemodePrefs.setOnPreferenceChangeListener(this);
        int mPowermenuAirplanemodePrefsValue = Settings.System.getInt(cr,
                                 Settings.System.POWERMENU_AIRPLANEMODE_PREFS, 2);
        mPowermenuAirplanemodePrefs.setValue(String.valueOf(mPowermenuAirplanemodePrefsValue));
        updatePowermenuAirplanemodePrefs(mPowermenuAirplanemodePrefsValue);

        // Powermenu Torch selection
        mPowermenuTorchPrefs = (ListPreference) prefSet.findPreference(KEY_POWERMENU_TORCH_PREFS);
        mPowermenuTorchPrefs.setOnPreferenceChangeListener(this);
        int mPowermenuTorchPrefsValue = Settings.System.getInt(cr,
                                 Settings.System.POWERMENU_TORCH_PREFS, 2);
        mPowermenuTorchPrefs.setValue(String.valueOf(mPowermenuTorchPrefsValue));
        updatePowermenuTorchPrefs(mPowermenuTorchPrefsValue);
		
        // Powermenu Silentmode selection
        mPowermenuSilentmodePrefs = (ListPreference) prefSet.findPreference(KEY_POWERMENU_SILENTMODE_PREFS);
        mPowermenuSilentmodePrefs.setOnPreferenceChangeListener(this);
        int mPowermenuSilentmodePrefsValue = Settings.System.getInt(cr,
                                 Settings.System.POWERMENU_SILENTMODE_PREFS, 2);
        mPowermenuSilentmodePrefs.setValue(String.valueOf(mPowermenuSilentmodePrefsValue));
        updatePowermenuSilentmodePrefs(mPowermenuSilentmodePrefsValue);

        // Powermenu Userswitch selection
        mPowermenuUserswitchPrefs = (ListPreference) prefSet.findPreference(KEY_POWERMENU_USERSWITCH_PREFS);
	    if (mPowermenuUserswitchPrefs !=null) {
		    if (SystemProperties.getBoolean("fw.power_user_switcher", false)) {
                mPowermenuUserswitchPrefs.setOnPreferenceChangeListener(this);
                int mPowermenuUserswitchPrefsValue = Settings.System.getInt(cr,
                                         Settings.System.POWERMENU_USERSWITCH_PREFS, 2);
                mPowermenuUserswitchPrefs.setValue(String.valueOf(mPowermenuUserswitchPrefsValue));
                updatePowermenuUserswitchPrefs(mPowermenuUserswitchPrefsValue);
    		} else {
	    	    prefSet.removePreference(mPowermenuUserswitchPrefs);
		        mPowermenuUserswitchPrefs = null;
            }
		}

        // Powermenu Screenshot selection
        mPowermenuScreenshotPrefs = (ListPreference) prefSet.findPreference(KEY_POWERMENU_SCREENSHOT_PREFS);
        mPowermenuScreenshotPrefs.setOnPreferenceChangeListener(this);
        int mPowermenuScreenshotPrefsValue = Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                                 Settings.System.POWERMENU_SCREENSHOT_PREFS, 2);
        mPowermenuScreenshotPrefs.setValue(String.valueOf(mPowermenuScreenshotPrefsValue));
        updatePowermenuScreenshotPrefs(mPowermenuScreenshotPrefsValue);

    }

   private String getPowerMenuString(int value) {
        Resources res = getResources();
        String menustate = "";
        switch(value){
        case 1:
            menustate=res.getString(R.string.powermenu_prefs_onlyunlocked);
            break;
        case 2:
            menustate=res.getString(R.string.powermenu_prefs_always);
            break;
        default:
            menustate=res.getString(R.string.powermenu_prefs_never);
        }
        return res.getString(R.string.powermenu_reboot_prefs_summary, menustate);
    }
    
    private void updatePowermenuRebootPrefs(int value) {
        mPowermenuRebootPrefs.setSummary(getPowerMenuString(value));
    }

    private void updatePowermenuShutdownPrefs(int value) {
        mPowermenuShutdownPrefs.setSummary(getPowerMenuString(value));
    }

    private void updatePowermenuAirplanemodePrefs(int value) {
        mPowermenuAirplanemodePrefs.setSummary(getPowerMenuString(value));
    }

    private void updatePowermenuSilentmodePrefs(int value) {
        mPowermenuSilentmodePrefs.setSummary(getPowerMenuString(value));
    }

    private void updatePowermenuUserswitchPrefs(int value) {
   	    if (mPowermenuUserswitchPrefs !=null) {
            mPowermenuUserswitchPrefs.setSummary(getPowerMenuString(value));
		}
    }

    private void updatePowermenuScreenshotPrefs(int value) {
        mPowermenuScreenshotPrefs.setSummary(getPowerMenuString(value));
    }

    private void updatePowermenuTorchPrefs(int value) {
        mPowermenuTorchPrefs.setSummary(getPowerMenuString(value));
    }
       
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mPowermenuRebootPrefs) {
            int mPowermenuRebootPrefsValue = Integer.valueOf((String) objValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.POWERMENU_REBOOT_PREFS, mPowermenuRebootPrefsValue);
            updatePowermenuRebootPrefs(mPowermenuRebootPrefsValue);
            getActivity().recreate();
			return true;
        } else if (preference == mPowermenuShutdownPrefs) {
            int mPowermenuShutdownPrefsValue = Integer.valueOf((String) objValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.POWERMENU_SHUTDOWN_PREFS, mPowermenuShutdownPrefsValue);
            updatePowermenuShutdownPrefs(mPowermenuShutdownPrefsValue);
            getActivity().recreate();
            return true;
        } else if (preference == mPowermenuAirplanemodePrefs) {
            int mPowermenuAirplanemodePrefsValue = Integer.valueOf((String) objValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.POWERMENU_AIRPLANEMODE_PREFS, mPowermenuAirplanemodePrefsValue);
            updatePowermenuAirplanemodePrefs(mPowermenuAirplanemodePrefsValue);
            getActivity().recreate();
            return true;
        } else if (preference == mPowermenuSilentmodePrefs) {
            int mPowermenuSilentmodePrefsValue = Integer.valueOf((String) objValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.POWERMENU_SILENTMODE_PREFS, mPowermenuSilentmodePrefsValue);
            updatePowermenuSilentmodePrefs(mPowermenuSilentmodePrefsValue);
            getActivity().recreate();
            return true;
        } else if (mPowermenuUserswitchPrefs !=null && preference == mPowermenuUserswitchPrefs) {
            int mPowermenuUserswitchPrefsValue = Integer.valueOf((String) objValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.POWERMENU_USERSWITCH_PREFS, mPowermenuUserswitchPrefsValue);
            updatePowermenuUserswitchPrefs(mPowermenuUserswitchPrefsValue);
            getActivity().recreate();
            return true;
        } else if (preference == mPowermenuScreenshotPrefs) {
            int mPowermenuScreenshotPrefsValue = Integer.valueOf((String) objValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.POWERMENU_SCREENSHOT_PREFS, mPowermenuScreenshotPrefsValue);
            updatePowermenuScreenshotPrefs(mPowermenuScreenshotPrefsValue);
            getActivity().recreate();
            return true;
        } else if (preference == mPowermenuTorchPrefs) {
            int mPowermenuTorchPrefsValue = Integer.valueOf((String) objValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.POWERMENU_TORCH_PREFS, mPowermenuTorchPrefsValue);
            updatePowermenuTorchPrefs(mPowermenuTorchPrefsValue);
            getActivity().recreate();
            return true;
        }
        return false;
    }

}

