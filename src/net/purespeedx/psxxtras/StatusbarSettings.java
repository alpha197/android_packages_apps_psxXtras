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

public class StatusbarSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

	private static final String KEY_SWIPE_FOR_QS = "swipe_for_qs";
	private static final String KEY_BATTERY_ICON = "statusbar_battery_title";
	private static final String KEY_QS_BATTERY_ICON = "quicksettings_battery_title";
	private static final String KEY_BATTERY_BOLT = "battery_bolt";
	private static final String KEY_BATTERY_EMPTY = "battery_empty";
	private static final String KEY_BATTERY_FULL = "battery_full";
    
    private static final String NOTIFICATION_DRAWER_QS_SETTINGS = "notification_drawer_qs_settings";

    private static final String PREF_NOTI_REMINDER_SOUND = "noti_reminder_sound";
    private static final String PREF_NOTI_REMINDER_ENABLED = "noti_reminder_enabled";
    private static final String PREF_NOTI_REMINDER_INTERVAL = "noti_reminder_interval";
    private static final String PREF_NOTI_REMINDER_RINGTONE = "noti_reminder_ringtone";    
    
	private CheckBoxPreference mSwipeForQs;
	private ListPreference mBatteryIcon;
	private ListPreference mQsBatteryIcon;
	private CheckBoxPreference mBatteryEmpty;
	private CheckBoxPreference mBatteryBolt;
	private CheckBoxPreference mBatteryFull;
    
    private CheckBoxPreference mReminder;
    private ListPreference mReminderMode;
    private ListPreference mReminderInterval;    
    private RingtonePreference mReminderRingtone;    
    
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
        
        mReminder = (CheckBoxPreference) findPreference(PREF_NOTI_REMINDER_ENABLED);
        mReminder.setChecked(Settings.System.getIntForUser(getContentResolver(),
                Settings.System.REMINDER_ALERT_ENABLED, 0, UserHandle.USER_CURRENT) == 1);
        mReminder.setOnPreferenceChangeListener(this);

        mReminderMode = (ListPreference) findPreference(PREF_NOTI_REMINDER_SOUND);
        int mode = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.REMINDER_ALERT_NOTIFY, 0, UserHandle.USER_CURRENT);
        mReminderMode.setValue(String.valueOf(mode));
        mReminderMode.setOnPreferenceChangeListener(this);
        updateReminderModeSummary(mode);

        mReminderRingtone = (RingtonePreference) findPreference(PREF_NOTI_REMINDER_RINGTONE);
        Uri ringtone = null;
        String ringtoneString = Settings.System.getStringForUser(getContentResolver(),
                Settings.System.REMINDER_ALERT_RINGER, UserHandle.USER_CURRENT);
        if (ringtoneString == null) {
            // Value not set, defaults to Default Ringtone
            ringtone = RingtoneManager.getDefaultUri(
                    RingtoneManager.TYPE_RINGTONE);
        } else {
            ringtone = Uri.parse(ringtoneString);
        }
        Ringtone alert = RingtoneManager.getRingtone(getActivity(), ringtone);
        mReminderRingtone.setSummary(alert.getTitle(getActivity()));
        mReminderRingtone.setOnPreferenceChangeListener(this);
        mReminderRingtone.setEnabled(mode != 0);

        mReminderInterval = (ListPreference) findPreference(PREF_NOTI_REMINDER_INTERVAL);
        int interval = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.REMINDER_ALERT_INTERVAL, 0, UserHandle.USER_CURRENT);
        mReminderInterval.setOnPreferenceChangeListener(this);
        updateReminderIntervalSummary(interval);        
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
        } else if (preference == mReminder) {
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.REMINDER_ALERT_ENABLED,
                    (Boolean) newValue ? 1 : 0, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mReminderMode) {
            int mode = Integer.valueOf((String) newValue);
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.REMINDER_ALERT_NOTIFY,
                    mode, UserHandle.USER_CURRENT);
            updateReminderModeSummary(mode);
            mReminderRingtone.setEnabled(mode != 0);
            return true;
        } else if (preference == mReminderRingtone) {
            Uri val = Uri.parse((String) newValue);
            Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), val);
            mReminderRingtone.setSummary(ringtone.getTitle(getActivity()));
            Settings.System.putStringForUser(getContentResolver(),
                    Settings.System.REMINDER_ALERT_RINGER,
                    val.toString(), UserHandle.USER_CURRENT);
            return true;
       } else if (preference == mReminderInterval) {
            int interval = Integer.valueOf((String) newValue);
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.REMINDER_ALERT_INTERVAL,
                    interval, UserHandle.USER_CURRENT);
            updateReminderIntervalSummary(interval);
        }
        return false;
    }
    
    private void updateReminderIntervalSummary(int value) {
        int resId;
        switch (value) {
            case 1000:
                resId = R.string.noti_reminder_interval_1s;
                 break;
            case 2000:
                resId = R.string.noti_reminder_interval_2s;
                break;
            case 2500:
                resId = R.string.noti_reminder_interval_2dot5s;
                break;
            case 3000:
                resId = R.string.noti_reminder_interval_3s;
                break;
            case 3500:
                resId = R.string.noti_reminder_interval_3dot5s;
                break;
            case 4000:
                resId = R.string.noti_reminder_interval_4s;
                break;
            default:
                resId = R.string.noti_reminder_interval_1dot5s;
                break;
        }
        mReminderInterval.setValue(Integer.toString(value));
        mReminderInterval.setSummary(getResources().getString(resId));
    }

    private void updateReminderModeSummary(int value) {
        int resId;
        switch (value) {
            case 1:
                resId = R.string.enabled;
                break;
            case 2:
                resId = R.string.noti_reminder_sound_looping;
                break;
            default:
                resId = R.string.disabled;
                break;
        }
        mReminderMode.setSummary(getResources().getString(resId));
    }    
}
