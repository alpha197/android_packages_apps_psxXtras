package net.purespeedx.psxxtras;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.SettingsPreferenceFragment;

import net.purespeedx.psxxtras.Utils.CMDProcessor;
import net.purespeedx.psxxtras.Utils.Constants;
import net.purespeedx.psxxtras.Utils.Helpers;

public class PsxHeader extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_PSX_BARS = "psx_bars";
    private static final String KEY_PSX_BUTTONS = "psx_buttons";
    private static final String KEY_PSX_USER_INTERFACE = "psx_user_interface";
    private static final String KEY_PSX_LOCKSCREEN = "psx_lockscreen";
    private static final String KEY_PSX_KERNEL_SETTINGS = "psx_kernel_settings";
	private static final String KEY_PSX_SUPERSU = "psx_supersu";
	private OnSharedPreferenceChangeListener KernelPrefListner;
	private Preference kernelSettings;
	private SharedPreferences mSharedPrefs;
	private Preference supersu;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	
        addPreferencesFromResource(R.xml.psx_header);
		PreferenceScreen prefSet = getPreferenceScreen();
		mSharedPrefs=PreferenceManager.getDefaultSharedPreferences(getActivity());
		
        boolean hasBars = getResources().getBoolean(
                R.bool.config_show_psx_bars);
		if (!hasBars) {
		     Preference ps = (Preference) findPreference(KEY_PSX_BARS);
			 if (ps != null) prefSet.removePreference(ps);
		}
		
        boolean hasButtons = getResources().getBoolean(
                R.bool.config_show_psx_buttons);
		if (!hasButtons) {
		     Preference ps = (Preference) findPreference(KEY_PSX_BUTTONS);
			 if (ps != null) prefSet.removePreference(ps);
		}

        boolean hasUserinterface = getResources().getBoolean(
                R.bool.config_show_psx_userinterface);
		if (!hasUserinterface) {
		     Preference ps = (Preference) findPreference(KEY_PSX_USER_INTERFACE);
			 if (ps != null) prefSet.removePreference(ps);
		}
        boolean hasLockscreen = getResources().getBoolean(
                R.bool.config_show_psx_lockscreen);
		if (!hasLockscreen) {
		     Preference ps = (Preference) findPreference(KEY_PSX_LOCKSCREEN);
			 if (ps != null) prefSet.removePreference(ps);
		}
		
		kernelSettings = (Preference) findPreference(KEY_PSX_KERNEL_SETTINGS);
		if (kernelSettings != null) {
			KernelPrefListner = new OnSharedPreferenceChangeListener(){
				public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
					UpdateKernelSettings();
				}
			};
			mSharedPrefs.registerOnSharedPreferenceChangeListener(KernelPrefListner);
			kernelSettings.setEnabled(false);
			Startup suTask=new Startup();
			suTask.setContext(prefSet.getContext());
			suTask.setSharedPreferences(mSharedPrefs);
			suTask.execute();
		}
		
		supersu = (Preference) findPreference(KEY_PSX_SUPERSU);
		if (supersu != null) {
		    boolean supported = false;
            try {
                supported = (getPackageManager().getPackageInfo("eu.chainfire.supersu", 0).versionCode >= 185);
            } catch (PackageManager.NameNotFoundException e) {
            }
            if (!supported) {
			    prefSet.removePreference(supersu);
			}
		}	
    }
	
	private void UpdateKernelSettings() {
		boolean suConfirmed = mSharedPrefs.getBoolean("su_confirmed", false);
		if (kernelSettings != null) {
			kernelSettings.setEnabled(suConfirmed);
		}
	}

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {      	
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }
	
	@Override     
	public void onResume() {
		super.onResume();          
		if (KernelPrefListner != null && mSharedPrefs != null) mSharedPrefs.registerOnSharedPreferenceChangeListener(KernelPrefListner);     
		UpdateKernelSettings();
	}



	@Override     
	public void onPause() {         
		super.onPause();          
		if (KernelPrefListner != null && mSharedPrefs != null) mSharedPrefs.unregisterOnSharedPreferenceChangeListener(KernelPrefListner);
	}
	
	private class Startup extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog = null;
        private Context context = null;
		private Preference kernelSettings;
		private SharedPreferences mPreferences;

		public void setKernelSettings(Preference NewValue) {
			this.kernelSettings = NewValue;
		}
		
		public void setSharedPreferences(SharedPreferences NewValue) {
			this.mPreferences = NewValue;
		}
				
        public Startup setContext(Context context) {
                this.context = context;
                return this;
        }

        @Override
        protected void onPreExecute() {
                // We're creating a progress dialog here because we want the user to wait.
                // If in your app your user can just continue on with clicking other things,
                // don't do the dialog thing.

                dialog = new ProgressDialog(context);
                dialog.setTitle("psxXtras");
                dialog.setMessage("Checking Superuser...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
			boolean suConfirmed = mPreferences.getBoolean("su_confirmed", false);
			if (!suConfirmed) {
			    boolean canSu = Helpers.checkSu();
			    boolean canBb = Helpers.checkBusybox();
				suConfirmed = (canSu & canBb);
				SharedPreferences.Editor e = mPreferences.edit();
				e.putBoolean("su_confirmed", suConfirmed);
				e.commit();
			}
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();                        
                        
        }                
    }
}
