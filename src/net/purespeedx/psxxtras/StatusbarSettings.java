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
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.SettingsPreferenceFragment;
import android.content.res.Resources;

import net.purespeedx.psxxtras.R;

public class StatusbarSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

	private static final String KEY_SWIPE_FOR_QS = "swipe_for_qs";
	private static final String KEY_BATTERY_ICON = "battery_icon_title";
    
	private CheckBoxPreference mSwipeForQs;
	private ListPreference mBatteryIcon;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.statusbar_settings);
		PreferenceScreen prefSet = getPreferenceScreen();
				
        mSwipeForQs = (CheckBoxPreference) findPreference(KEY_SWIPE_FOR_QS);
		if (mSwipeForQs !=null) {
			mSwipeForQs.setOnPreferenceChangeListener(this);
			mSwipeForQs.setChecked(Settings.System.getInt(getContentResolver(),
                                 Settings.System.QUICK_SETTINGS_QUICK_PULL_DOWN, 0) == 1);
		}
	
        mBatteryIcon = (ListPreference) findPreference(KEY_BATTERY_ICON);
        if (mBatteryIcon !=null) {
            mBatteryIcon.setOnPreferenceChangeListener(this);
            int BatteryValue = Settings.System.getInt(getContentResolver(),
                                 Settings.System.STATUS_BAR_BATTERY_STYLE, 0);
            mBatteryIcon.setValue(String.valueOf(BatteryValue));
            updateBatteryIcon(BatteryValue);            
        }
    }

   private String getBatterystring(int value) {
        Resources res = getResources();
        String menustate = "";
        switch(value){
        case 1:
            menustate=res.getString(R.string.battery_bar_percent);
            break;
        case 2:
            menustate=res.getString(R.string.battery_circle);
            break;
        case 3:
            menustate=res.getString(R.string.battery_circle_percent);
            break;
        default:
            menustate=res.getString(R.string.battery_bar);
        }
        return menustate;
    }
    
    private void updateBatteryIcon(int value) {
        mBatteryIcon.setSummary(getBatterystring(value));
    }

    
	
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {      	
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
	    //Statusbar battery percentage
		if (preference == mSwipeForQs) {
            final int val = (Boolean) newValue ? 1 : 2;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.QUICK_SETTINGS_QUICK_PULL_DOWN, val);
            return true;        
        } else if (preference == mBatteryIcon) {
            final int bValue = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_BATTERY_STYLE, bValue);
            updateBatteryIcon(bValue);
            return true;
        }
            return false;
    }
}
