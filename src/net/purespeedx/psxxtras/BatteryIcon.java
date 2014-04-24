package net.purespeedx.psxxtras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.provider.Settings;

import com.android.settings.SettingsPreferenceFragment;
import android.content.res.Resources;

import net.purespeedx.psxxtras.R;

public class BatteryIcon extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

	private static final String KEY_BATTERY_ICON = "statusbar_battery_title";
	private static final String KEY_QS_BATTERY_ICON = "quicksettings_battery_title";
	private static final String KEY_BATTERY_BOLT = "battery_bolt";
	private static final String KEY_BATTERY_EMPTY = "battery_empty";
	private static final String KEY_BATTERY_FULL = "battery_full";
        
	private ListPreference mBatteryIcon;
	private ListPreference mQsBatteryIcon;
	private CheckBoxPreference mBatteryEmpty;
	private CheckBoxPreference mBatteryBolt;
	private CheckBoxPreference mBatteryFull;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.battery_icon);
		PreferenceScreen prefSet = getPreferenceScreen();
				
        mBatteryIcon = (ListPreference) findPreference(KEY_BATTERY_ICON);
        mQsBatteryIcon = (ListPreference) findPreference(KEY_QS_BATTERY_ICON);
        mBatteryBolt = (CheckBoxPreference) findPreference(KEY_BATTERY_BOLT);
        mBatteryEmpty= (CheckBoxPreference) findPreference(KEY_BATTERY_EMPTY);
        mBatteryFull= (CheckBoxPreference) findPreference(KEY_BATTERY_FULL);
        
        if (mBatteryIcon !=null) {
            mBatteryIcon.setOnPreferenceChangeListener(this);
        }
        if (mQsBatteryIcon !=null) {
            mQsBatteryIcon.setOnPreferenceChangeListener(this);
        }
        if (mBatteryBolt !=null) {
            mBatteryBolt.setOnPreferenceChangeListener(this);
        }
        if (mBatteryEmpty !=null) {
            mBatteryEmpty.setOnPreferenceChangeListener(this);
        }
        if (mBatteryFull !=null) {
            mBatteryFull.setOnPreferenceChangeListener(this);
        }
        
        updateBatteryIcon();            
        
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
        case 4:
            menustate=res.getString(R.string.battery_percent_only);
            break;       
        case 5:
            menustate=res.getString(R.string.battery_hide);
            break;       
        default:
            menustate=res.getString(R.string.battery_bar);
        }
        return menustate;
    }
    
    private void updateBatteryIcon() {
        int BatteryValue = Settings.System.getInt(getContentResolver(),
                                Settings.System.STATUS_BAR_BATTERY_STYLE, 0);
        int QsBatteryValue = Settings.System.getInt(getContentResolver(),
                                Settings.System.QUICKSETTINGS_BATTERY_STYLE, 0);
        boolean BatteryFull = (Settings.System.getInt(getContentResolver(),
                                Settings.System.STATUS_BAR_BATTERY_FULL, 0) == 1);
        boolean BatteryEmpty = (Settings.System.getInt(getContentResolver(),
                                Settings.System.STATUS_BAR_BATTERY_EMPTY, 1) == 1);
        boolean BatteryBolt = (Settings.System.getInt(getContentResolver(),
                                Settings.System.STATUS_BAR_BATTERY_BOLT, 1) == 1);
        if (mBatteryIcon != null) {
            mBatteryIcon.setValue(String.valueOf(BatteryValue));
            mBatteryIcon.setSummary(getBatterystring(BatteryValue));
        }
        if (mQsBatteryIcon != null) {
            mQsBatteryIcon.setValue(String.valueOf(QsBatteryValue));
            mQsBatteryIcon.setSummary(getBatterystring(QsBatteryValue));
        }
        if (mBatteryBolt != null) {
            mBatteryBolt.setChecked(BatteryBolt);
            mBatteryBolt.setEnabled(BatteryValue == 1 || QsBatteryValue == 1);
        }
        if (mBatteryEmpty != null) {
            mBatteryEmpty.setChecked(BatteryEmpty);
            mBatteryEmpty.setEnabled(BatteryValue == 1 || QsBatteryValue == 1);
        }
        if (mBatteryFull != null) {
            mBatteryFull.setChecked(BatteryFull);
            mBatteryFull.setEnabled(BatteryValue == 1 || QsBatteryValue == 1);
        }
    }
 
	
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {      	
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBatteryIcon) {
            final int bValue = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_BATTERY_STYLE, bValue);
            updateBatteryIcon();
            return true;
        } else if (preference == mQsBatteryIcon) {
            final int bValue = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.QUICKSETTINGS_BATTERY_STYLE, bValue);
            updateBatteryIcon();
            return true;
		} else if (preference == mBatteryBolt) {
            final int val = (Boolean) newValue ? 1 : 0;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_BATTERY_BOLT, val);
            updateBatteryIcon();
            return true;        
		} else if (preference == mBatteryEmpty) {
            final int val = (Boolean) newValue ? 1 : 0;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_BATTERY_EMPTY, val);
            updateBatteryIcon();
            return true;        
		} else if (preference == mBatteryFull) {
            final int val = (Boolean) newValue ? 1 : 0;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_BATTERY_FULL, val);
            updateBatteryIcon();
            return true;        
        }
        return false;
    }
    
}
