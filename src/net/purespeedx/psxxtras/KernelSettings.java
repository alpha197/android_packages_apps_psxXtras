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
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import com.android.settings.SettingsPreferenceFragment;

import net.purespeedx.psxxtras.Utils.CMDProcessor;
import net.purespeedx.psxxtras.Utils.Constants;
import net.purespeedx.psxxtras.Utils.Helpers;
import net.purespeedx.psxxtras.Utils.KernelHelper;
import java.lang.StringBuilder;
 
import net.purespeedx.psxxtras.R;
 
public class KernelSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String KERNEL_FORCE_FASTCHARGE = "kernel_settings_force_fastcharge";
    private static final String KERNEL_GOVERNOR = "kernel_settings_governor";
    private static final String KERNEL_CPU_MIN = "kernel_settings_cpu_frequency_min";
    private static final String KERNEL_CPU_MAX = "kernel_settings_cpu_frequency_max";
    private static final String KERNEL_CPU_APPLY = "kernel_settings_cpu_frequency_apply";
    private static final String KERNEL_INPUTBOOST_MS = "kernel_settings_cpu_inputboost_ms";
    private static final String KERNEL_INPUTBOOST_FREQ = "kernel_settings_cpu_inputboost_freq";
    private static final String KERNEL_INPUTBOOST_TITLE="kernel_settings_cpu_inputboost_title";
    private static final String KERNEL_BOOST_MS = "kernel_settings_cpu_boost_ms";
    private static final String KERNEL_SYNC_THRESHOLD = "kernel_settings_cpu_sync_threshold";
    private static final String KERNEL_INPUTBOOST_APPLY = "kernel_settings_cpu_boost_apply";
    private static final String KERNEL_MSMHOTPLUG_TITLE="kernel_settings_msmhotplug_title";
    private static final String KERNEL_MSMHOTPLUG_MIN_CPUS_ONLINE="kernel_settings_msmhotplug_min_cpus_online";
    private static final String KERNEL_MSMHOTPLUG_MAX_CPUS_ONLINE="kernel_settings_msmhotplug_max_cpus_online";
    private static final String KERNEL_MSMHOTPLUG_APPLY="kernel_settings_msmhotplug_apply";
    
    private CheckBoxPreference mForceFastcharge;
    private ListPreference mGovernor;
    private ListPreference mCpuMin;
    private ListPreference mCpuMax;
    private EditTextPreference mInputms;
    private EditTextPreference mBoostms;
    private ListPreference mInputfreq;
    private ListPreference mSyncThresFreq;
    private CheckBoxPreference mInputApply;
    private CheckBoxPreference mCpuApply;
    private Context mContext;
    private Resources mResources;
    private ListPreference mMsmHotplugMinCpus;
    private ListPreference mMsmHotplugMaxCpus;
    private CheckBoxPreference mMsmHotplugApply;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        
        addPreferencesFromResource(R.xml.kernel_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

        mResources = getResources();
        
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

        PreferenceCategory inputcat = (PreferenceCategory) prefSet.findPreference(KERNEL_INPUTBOOST_TITLE); 
        if (inputcat != null) {
            //Inputboost ms
            mInputms = 
                (EditTextPreference) inputcat.findPreference(KERNEL_INPUTBOOST_MS);
            if (mInputms != null) {
                if (UpdateInputms(true) != true) {
                    inputcat.removePreference(mInputms);
                    mInputms = null;
                } else {
                    mInputms.setOnPreferenceChangeListener(this);
                }
            }

            //Boost ms
            mBoostms = 
                (EditTextPreference) inputcat.findPreference(KERNEL_BOOST_MS);
            if (mBoostms != null) {
                if (UpdateBoostms(true) != true) {
                    inputcat.removePreference(mBoostms);
                    mBoostms = null;
                } else {
                    mBoostms.setOnPreferenceChangeListener(this);
                }
            }
        
            //Inputboost freq
            mInputfreq = 
                (ListPreference) inputcat.findPreference(KERNEL_INPUTBOOST_FREQ);
            if (mInputfreq != null) {
                if (UpdateInputFreq(true) != true) {
                    inputcat.removePreference(mInputfreq);
                    mInputfreq = null;
                } else {
                    mInputfreq.setOnPreferenceChangeListener(this);
                }
            }

            //Sync threshold freq
            mSyncThresFreq = 
                (ListPreference) inputcat.findPreference(KERNEL_SYNC_THRESHOLD);
            if (mSyncThresFreq != null) {
                if (UpdateSyncFreq(true) != true) {
                    inputcat.removePreference(mSyncThresFreq);
                    mSyncThresFreq = null;
                } else {
                    mSyncThresFreq.setOnPreferenceChangeListener(this);
                }
            }      

            // Input Apply on Boot
            mInputApply =
                (CheckBoxPreference) inputcat.findPreference(KERNEL_INPUTBOOST_APPLY);        
            if (mInputApply != null) {
                if (mInputfreq == null && mInputms == null) {
                    inputcat.removePreference(mInputApply);
                    mInputApply = null;
                } else {
                    UpdateInputApplyOnBoot();
                    mInputApply.setOnPreferenceChangeListener(this);
                }
            }
        
            if (mInputApply == null) {
                prefSet.removePreference(inputcat);            
            }               
        }
        
        PreferenceCategory msm = (PreferenceCategory) prefSet.findPreference(KERNEL_MSMHOTPLUG_TITLE); 
        if (msm != null) {
            //MSM-HotPlug Min Frequency
            mMsmHotplugMinCpus = 
                (ListPreference) msm.findPreference(KERNEL_MSMHOTPLUG_MIN_CPUS_ONLINE);
            if (mMsmHotplugMinCpus != null) {
                if (UpdateMsmCpuMin(true) != true) {
                    msm.removePreference(mMsmHotplugMinCpus);
                    mMsmHotplugMinCpus = null;
                } else {
                    mMsmHotplugMinCpus.setOnPreferenceChangeListener(this);
                }
            }

            //MSM-HotPlug Max Frequency
            mMsmHotplugMaxCpus = 
                (ListPreference) msm.findPreference(KERNEL_MSMHOTPLUG_MAX_CPUS_ONLINE);
            if (mMsmHotplugMaxCpus != null) {
                if (UpdateMsmCpuMax(true) != true) {
                    msm.removePreference(mMsmHotplugMaxCpus);
                    mMsmHotplugMaxCpus = null;
                } else {
                    mMsmHotplugMaxCpus.setOnPreferenceChangeListener(this);
                }
            }
        
            // Apply on Boot
            mMsmHotplugApply =
                (CheckBoxPreference) msm.findPreference(KERNEL_MSMHOTPLUG_APPLY);        
            if (mMsmHotplugApply != null) {
                if (mMsmHotplugMinCpus == null && mMsmHotplugMaxCpus == null) {
                    msm.removePreference(mMsmHotplugApply);
                    mMsmHotplugApply = null;
                } else {
                    UpdateMsmHotPlugApplyOnBoot();
                    mMsmHotplugApply.setOnPreferenceChangeListener(this);
                }
            }
        
            if (mMsmHotplugApply == null) {
                prefSet.removePreference(msm);
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

    public void UpdateMsmHotPlugApplyOnBoot() {
        boolean doApply = (Settings.System.getInt(getContentResolver(), Settings.System.KERNEL_MSMHOTPLUG_APPLY,0) == 1);
        mMsmHotplugApply.setChecked(doApply);
        if (doApply) {
            mMsmHotplugApply.setSummary(R.string.kernel_settings_cpu_frequency_apply_summary_enabled);
        } else {
            mMsmHotplugApply.setSummary(R.string.kernel_settings_cpu_frequency_apply_summary_disabled);
        }
    }
    
    public void UpdateInputApplyOnBoot() {
        boolean doApply = (Settings.System.getInt(getContentResolver(), Settings.System.KERNEL_INPUTBOOST_APPLY,0) == 1);
        mInputApply.setChecked(doApply);
        if (doApply) {
            mInputApply.setSummary(R.string.kernel_settings_cpu_boost_apply_enabled);
        } else {
            mInputApply.setSummary(R.string.kernel_settings_cpu_boost_apply_disabled);
        }
    }

    public void SetInputApplyOnBoot(boolean newValue) {
        int enabled=(newValue ? 1 : 0);
        Settings.System.putInt(getContentResolver(),Settings.System.KERNEL_INPUTBOOST_APPLY, enabled);
        UpdateInputApplyOnBoot();
    }
    
    public void SetCpuApplyOnBoot(boolean newValue) {
        int enabled=(newValue ? 1 : 0);
        Settings.System.putInt(getContentResolver(),Settings.System.KERNEL_CPU_FREQUENCY_APPLY, enabled);
        UpdateCpuApplyOnBoot();
    }

    public void SetMsmHotplugApplyOnBoot(boolean newValue) {
        int enabled=(newValue ? 1 : 0);
        Settings.System.putInt(getContentResolver(),Settings.System.KERNEL_MSMHOTPLUG_APPLY, enabled);
        UpdateMsmHotPlugApplyOnBoot();
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

    public boolean UpdateCpuCount(boolean init,ListPreference List, String Path,String SettingsPath) {
        String mActiveCnt = Helpers.readOneLine(Path);
        if (mActiveCnt == null || mActiveCnt == "") return false;
        if (init == true) {
            String[] cnt =KernelHelper.GetAvailableCpus();
            if (cnt == null) {
                return false;
            }
            List.setEntries(cnt);
            List.setEntryValues(cnt);
        }
        List.setSummary(mResources.getString(R.string.kernel_settings_msmhotplug_summary, mActiveCnt));
        if (SettingsPath != "") {
            String mCurrentCnt = Settings.System.getString(getContentResolver(), SettingsPath) ;
            List.setValue(mCurrentCnt);
        }
        return true;
    }
    
    
    public boolean UpdateFrequency(boolean init,ListPreference List, String Path,String SettingsPath) {
        String mActiveFreq = Helpers.readOneLine(Path);
        if (mActiveFreq == null || mActiveFreq == "") return false;
        if (init == true) {
            String[] freqs =KernelHelper.GetAvailableFrequencies();
            List.setEntries(freqs);
            List.setEntryValues(freqs);
            if (freqs == null) {
                return false;
            }
        }
        List.setSummary(mResources.getString(R.string.kernel_settings_frequency_summary, mActiveFreq));
        if (SettingsPath != "") {
            String mCurrentFreq = Settings.System.getString(getContentResolver(), SettingsPath) ;
            List.setValue(mCurrentFreq);
        }
        return true;
    }
    
    public boolean UpdateMsmCpuMin(boolean init) {
        return UpdateCpuCount(init,mMsmHotplugMinCpus,Constants.MSMHOTPLUG_MIN_CPUS,Settings.System.KERNEL_MSMHOTPLUG_MIN_CPUS);
    }

    public boolean UpdateMsmCpuMax(boolean init) {
        return UpdateCpuCount(init,mMsmHotplugMaxCpus,Constants.MSMHOTPLUG_MAX_CPUS,Settings.System.KERNEL_MSMHOTPLUG_MAX_CPUS);
    }
    
    public boolean UpdateCpuMin(boolean init) {
        return UpdateFrequency(init,mCpuMin,Constants.MIN_FREQ_PATH,Settings.System.KERNEL_CPU_FREQUENCY_MIN);
    }

    public boolean UpdateInputFreq(boolean init) {
        return UpdateFrequency(init,mInputfreq,Constants.CPUBOOST_INPUT_FREQ,Settings.System.KERNEL_INPUTBOOST_FREQ);
    }

    public boolean UpdateSyncFreq(boolean init) {
        return UpdateFrequency(init,mSyncThresFreq,Constants.CPUBOOST_SYNC_THRESHOLD,Settings.System.KERNEL_SYNC_THRESHOLD_FREQ);
    }
    
    public boolean UpdateInputms(boolean init) {
        String mActivems = Helpers.readOneLine(Constants.CPUBOOST_INPUT_MS);
        if (mActivems == null || mActivems == "") return false;
        mInputms.setSummary(mResources.getString(R.string.kernel_settings_cpu_inputboost_ms_summary, mActivems));
        mInputms.setText(mActivems);
        return true;   
    }

    public boolean UpdateBoostms(boolean init) {
        String mActivems = Helpers.readOneLine(Constants.CPUBOOST_BOOST_MS);
        if (mActivems == null || mActivems == "") return false;
        mBoostms.setSummary(mResources.getString(R.string.kernel_settings_cpu_boost_ms_summary, mActivems));
        mBoostms.setText(mActivems);
        return true;   
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

    public void SetMsmCpuMin(String newValue) {
        Settings.System.putString(getContentResolver(),Settings.System.KERNEL_MSMHOTPLUG_MIN_CPUS, newValue);    
        KernelHelper.SetValue(mContext,Constants.MSMHOTPLUG_MIN_CPUS,Settings.System.KERNEL_MSMHOTPLUG_MIN_CPUS);
        UpdateMsmCpuMin(false);
    }

    public void SetMsmCpuMax(String newValue) {
        Settings.System.putString(getContentResolver(),Settings.System.KERNEL_MSMHOTPLUG_MAX_CPUS, newValue);    
        KernelHelper.SetValue(mContext,Constants.MSMHOTPLUG_MAX_CPUS,Settings.System.KERNEL_MSMHOTPLUG_MAX_CPUS);
        UpdateMsmCpuMax(false);
    }
    
    public void SetInputBoostFreq(String newValue) {
        Settings.System.putString(getContentResolver(),Settings.System.KERNEL_INPUTBOOST_FREQ, newValue);    
        KernelHelper.SetValue(mContext,Constants.CPUBOOST_INPUT_FREQ,Settings.System.KERNEL_INPUTBOOST_FREQ);
        UpdateInputFreq(false);
    }

    public void SetSyncFreq(String newValue) {
        Settings.System.putString(getContentResolver(),Settings.System.KERNEL_SYNC_THRESHOLD_FREQ, newValue);    
        KernelHelper.SetValue(mContext,Constants.CPUBOOST_SYNC_THRESHOLD,Settings.System. KERNEL_SYNC_THRESHOLD_FREQ);
        UpdateSyncFreq(false);
    }
    
    public void SetInputBoostms(String newValue) {
        Settings.System.putString(getContentResolver(),Settings.System.KERNEL_INPUTBOOST_MS, newValue);    
        KernelHelper.SetValue(mContext,Constants.CPUBOOST_INPUT_MS,Settings.System.KERNEL_INPUTBOOST_MS);
        UpdateInputms(false);
    }
    
    public void SetBoostms(String newValue) {
        Settings.System.putString(getContentResolver(),Settings.System.KERNEL_CPUBOOST_MS, newValue);    
        KernelHelper.SetValue(mContext,Constants.CPUBOOST_BOOST_MS,Settings.System.KERNEL_CPUBOOST_MS);
        UpdateBoostms(false);
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
        } else if (preference == mInputfreq) {
            SetInputBoostFreq((String) newValue);
            return true;
        } else if (preference == mInputms) {
            SetInputBoostms((String) newValue);          
           return true;
        } else if (preference == mSyncThresFreq) {
            SetSyncFreq((String) newValue);
            return true;
        } else if (preference == mBoostms) {
            SetBoostms((String) newValue);          
           return true;
        } else if (preference == mInputApply) {
            SetInputApplyOnBoot((Boolean) newValue ? true : false);
            return true;
        } else if (preference == mMsmHotplugMinCpus) {
            SetMsmCpuMin((String) newValue);          
           return true;
        } else if (preference == mMsmHotplugMaxCpus) {
            SetMsmCpuMax((String) newValue);          
           return true;
        } else if (preference == mMsmHotplugApply) {
            SetMsmHotplugApplyOnBoot((Boolean) newValue ? true : false);
            return true;
        }      
        
        return false;
    }
    
}
