package net.purespeedx.psxxtras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import net.purespeedx.psxxtras.Utils.CMDProcessor;
import net.purespeedx.psxxtras.Utils.Constants;
import net.purespeedx.psxxtras.Utils.Helpers;
import net.purespeedx.psxxtras.Utils.KernelHelper;
import java.lang.StringBuilder;
 
public class KernelSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KERNEL_FORCE_FASTCHARGE = "kernel_settings_force_fastcharge";
    private static final String KERNEL_GOVERNOR = "kernel_settings_governor";
    private static final String KERNEL_CPU_MIN = "kernel_settings_cpu_frequency_min";
    private static final String KERNEL_CPU_MAX = "kernel_settings_cpu_frequency_max";
    private static final String KERNEL_CPU_APPLY = "kernel_settings_cpu_frequency_apply";
    
	
    private CheckBoxPreference mForceFastcharge;
    private ListPreference mGovernor;
    private ListPreference mCpuMin;
    private ListPreference mCpuMax;
    private CheckBoxPreference mCpuApply;
	private Context mContext;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		mContext = getActivity();
		
        addPreferencesFromResource(R.xml.kernel_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

	    Resources res = getResources();
		
		// Force Fastcharge
        mForceFastcharge =
            (CheckBoxPreference) prefSet.findPreference(KERNEL_FORCE_FASTCHARGE);		
		String mFastChargePath = Helpers.fastcharge_path();
		if (mForceFastcharge != null) {
	        if (mFastChargePath == null) {
    		    prefSet.removePreference(mForceFastcharge);
				mForceFastcharge = null;
    	    } else {
				UpdateFastCharge();
                mForceFastcharge.setOnPreferenceChangeListener(this);
            }
		}
		
		//Governor
		mGovernor = 
			(ListPreference) prefSet.findPreference(KERNEL_GOVERNOR);
		if (mGovernor != null) {
			if (UpdateGovernor(true) != true) {
				prefSet.removePreference(mGovernor);
				mGovernor = null;
			} else {
				mGovernor.setOnPreferenceChangeListener(this);
			}
        }

		//Min Frequency
		mCpuMin = 
			(ListPreference) prefSet.findPreference(KERNEL_CPU_MIN);
		if (mCpuMin != null) {
			if (UpdateCpuMin(true) != true) {
				prefSet.removePreference(mCpuMin);
				mCpuMin = null;
			} else {
				mCpuMin.setOnPreferenceChangeListener(this);
			}
        }
		//Max Frequency
		mCpuMax = 
			(ListPreference) prefSet.findPreference(KERNEL_CPU_MAX);
		if (mCpuMax != null) {
			if (UpdateCpuMax(true) != true) {
				prefSet.removePreference(mCpuMax);
				mCpuMax = null;
			} else {
				mCpuMax.setOnPreferenceChangeListener(this);
			}
        }

		// Apply on Boot
        mCpuApply =
            (CheckBoxPreference) prefSet.findPreference(KERNEL_CPU_APPLY);		
		if (mCpuApply != null) {
	        if (mCpuMax == null && mCpuMin == null) {
    		    prefSet.removePreference(mCpuApply);
				mCpuApply = null;
    	    } else {
				UpdateCpuApplyOnBoot();
                mCpuApply.setOnPreferenceChangeListener(this);
            }
		}
		
    }
	
	public void UpdateCpuApplyOnBoot() {
        boolean doApply = (Settings.System.getInt(getContentResolver(), Settings.System.KERNEL_CPU_FREQUENCY_APPLY,0) == 1);
        mCpuApply.setChecked(doApply);
        if (doApply) {
            mCpuApply.setSummary(R.string.kernel_settings_cpu_frequency_apply_summary_enabled);
		} else {
            mCpuApply.setSummary(R.string.kernel_settings_cpu_frequency_apply_summary_disabled);
		}
	}
	
	public void SetCpuApplyOnBoot(boolean newValue) {
	    int enabled=(newValue ? 1 : 0);
    	Settings.System.putInt(getContentResolver(),Settings.System.KERNEL_CPU_FREQUENCY_APPLY, enabled);
	    UpdateCpuApplyOnBoot();
	}
	
	
	public boolean UpdateGovernor(boolean init) {
		if (init == true) {
			String[] mAvailableGovernors =KernelHelper.GetAvailableGovernors();
			mGovernor.setEntries(mAvailableGovernors);
			mGovernor.setEntryValues(mAvailableGovernors);
			if (mAvailableGovernors == null) {
				return false;
			}
		}
		String mActiveGovernor = Helpers.readOneLine(Constants.GOVERNOR_PATH);
        String mCurrentGovernor = Settings.System.getString(getContentResolver(), Settings.System.KERNEL_GOVERNOR) ;
		
		mGovernor.setSummary(mActiveGovernor);
		mGovernor.setValue(mCurrentGovernor);
		return true;
	}

	public boolean UpdateFrequency(boolean init,ListPreference List, String Path,String SettingsPath) {
		if (init == true) {
			String[] freqs =KernelHelper.GetAvailableFrequencies();
			List.setEntries(freqs);
			List.setEntryValues(freqs);
			if (freqs == null) {
				return false;
			}
		}
		String mActiveFreq = Helpers.readOneLine(Path);
        String mCurrentFreq = Settings.System.getString(getContentResolver(), SettingsPath) ;
		List.setSummary(mActiveFreq);
		List.setValue(mCurrentFreq);
		return true;
	}
	
	public boolean UpdateCpuMin(boolean init) {
		return UpdateFrequency(init,mCpuMin,Constants.MIN_FREQ_PATH,Settings.System.KERNEL_CPU_FREQUENCY_MIN);
	}

	public boolean UpdateCpuMax(boolean init) {
		return UpdateFrequency(init,mCpuMax,Constants.MAX_FREQ_PATH,Settings.System.KERNEL_CPU_FREQUENCY_MAX);
	}
		
	public void SetFrequency(String newValue,ListPreference List, String Path,String SettingsPath) {
    	Settings.System.putString(getContentResolver(),SettingsPath, newValue);	
		KernelHelper.SetFrequency(mContext,Path,SettingsPath);
		UpdateFrequency(false,List,Path,SettingsPath);
	}

	public void SetMinCpu(String newValue) {
		SetFrequency(newValue,mCpuMin,Constants.MIN_FREQ_PATH,Settings.System.KERNEL_CPU_FREQUENCY_MIN);
	}

	public void SetMaxCpu(String newValue) {
		SetFrequency(newValue,mCpuMax,Constants.MAX_FREQ_PATH,Settings.System.KERNEL_CPU_FREQUENCY_MAX);
	}

	public void SetGovernor(String newValue) {
    	Settings.System.putString(getContentResolver(),Settings.System.KERNEL_GOVERNOR, newValue);	
		KernelHelper.SetGovernor(mContext);
		UpdateGovernor(false);
	}
	
	public void SetFastCharge(boolean newValue) {
	    int enabled=(newValue ? 1 : 2);
    	Settings.System.putInt(getContentResolver(),Settings.System.KERNEL_FORCE_FASTCHARGE, enabled);
		KernelHelper.SetFastCharge(mContext);
	    UpdateFastCharge();
	}

    public void UpdateFastCharge() {
        int currentstate= Settings.System.getInt(getContentResolver(), Settings.System.KERNEL_FORCE_FASTCHARGE, 0) ;
		if (currentstate == 0) {
		    String mFastChargePath = Helpers.fastcharge_path();
            if (mFastChargePath != null) {
    		    currentstate = 2;
	    	}
		}
        mForceFastcharge.setEnabled((currentstate > 0));
        mForceFastcharge.setChecked((currentstate == 1));
        switch(currentstate){
        case 0:
            mForceFastcharge.setSummary(R.string.kernel_settings_force_fastcharge_summary_unsupported);
			break;
        case 1:
            mForceFastcharge.setSummary(R.string.kernel_settings_force_fastcharge_summary_on);
			break;
        case 2:
            mForceFastcharge.setSummary(R.string.kernel_settings_force_fastcharge_summary_off);
			break;
        }
    }
       
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {          
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        //Fastcharge
        if (preference == mForceFastcharge) {
            SetFastCharge((Boolean) newValue ? true : false);
            return true;
        } else if (preference == mGovernor) {
			SetGovernor((String) newValue);
			return true;
        } else if (preference == mCpuMin) {
			SetMinCpu((String) newValue);
			return true;
        } else if (preference == mCpuMax) {
			SetMaxCpu((String) newValue);
			return true;
        } else if (preference == mCpuApply) {
            SetCpuApplyOnBoot((Boolean) newValue ? true : false);
            return true;
		}	
        return false;
    }
	
}
