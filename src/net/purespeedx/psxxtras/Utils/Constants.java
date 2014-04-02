/*
 * Performance Control - An Android CPU Control application Copyright (C) 2012
 * James Roberts
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.purespeedx.psxxtras.Utils;

public interface Constants {
    
    public static final String TAG = "psxXtras-KernelSettings";
    public static final String NOT_FOUND = "not found";
    
    // CPU settings
    public static final String CPU_PATH = "/sys/devices/system/cpu/cpu";
    public static final String CPU_FREQ_TAIL = "/cpufreq/scaling_cur_freq";
    public static final String CUR_CPU_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
    public static final String MAX_FREQ_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
    public static final String TEGRA_MAX_FREQ_PATH = "/sys/module/cpu_tegra/parameters/cpu_user_cap";
    public static final String MIN_FREQ_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
    public static final String STEPS_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
    public static final String GOVERNORS_LIST_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
    public static final String GOVERNOR_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
    public static final String[] IO_SCHEDULER_PATH = {"/sys/block/mmcblk0/queue/scheduler", "/sys/block/mmcblk1/queue/scheduler"};
    //Dynamic frequency scaling
    public static final String DYN_MAX_FREQ_PATH = "/sys/power/cpufreq_max_limit";
    public static final String DYN_MIN_FREQ_PATH = "/sys/power/cpufreq_min_limit";

    public static final String NUM_OF_CPUS_PATH = "/sys/devices/system/cpu/present";

    public static final String PREF_MAX_CPU = "pref_max_cpu";
    public static final String PREF_MIN_CPU = "pref_min_cpu";
    public static final String PREF_GOV = "pref_gov";
    public static final String PREF_IO = "pref_io";
    public static final String CPU_SOB = "cpu_sob";
    public static final String GOV_SOB = "gov_settings_sob";
    public static final String GOV_SETTINGS = "gov_settings";
    public static final String GOV_NAME = "gov_name";
    public static final String GOV_SETTINGS_PATH = "/sys/devices/system/cpu/cpufreq/";

    // CPU info
    public static String KERNEL_INFO_PATH = "/proc/version";
    public static String CPU_INFO_PATH = "/proc/cpuinfo";
    public static String MEM_INFO_PATH = "/proc/meminfo";

    // Time in state
    public static final String TIME_IN_STATE_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
    public static final String TIME_IN_STATE_TAIL = "/cpufreq/stats/time_in_state";
    public static final String PREF_OFFSETS = "pref_offsets";
    // Battery
    public static final String BAT_VOLT_PATH = "/sys/class/power_supply/battery/voltage_now";

    // Other settings
    public static final String MINFREE_PATH = "/sys/module/lowmemorykiller/parameters/minfree";
    public static final String MINFREE_ADJ_PATH = "/sys/module/lowmemorykiller/parameters/adj";
    public static final String READ_AHEAD_PATH = "/sys/block/mmcblk0/bdi/read_ahead_kb";

    //------ KSM
    public static final String KSM_RUN_PATH = "/sys/kernel/mm/ksm/run";
    public static final String KSM_FULLSCANS_PATH = "/sys/kernel/mm/ksm/full_scans";
    public static final String KSM_PAGESSHARED_PATH = "/sys/kernel/mm/ksm/pages_shared";
    public static final String KSM_PAGESSHARING_PATH = "/sys/kernel/mm/ksm/pages_sharing";
    public static final String KSM_PAGESTOSCAN_PATH = "/sys/kernel/mm/ksm/pages_to_scan";
    public static final String KSM_PAGESUNSHERED_PATH = "/sys/kernel/mm/ksm/pages_unshared";
    public static final String KSM_PAGESVOLATILE_PATH = "/sys/kernel/mm/ksm/pages_volatile";
    public static final String KSM_SLEEP_PATH = "/sys/kernel/mm/ksm/sleep_millisecs";
    public static final String PREF_RUN_KSM = "pref_run_ksm";
    public static final String KSM_SOB = "ksm_boot";

    //------ DoNotKillProc
    public static final String USER_PROC_PATH = "/sys/module/lowmemorykiller/parameters/donotkill_proc";
    public static final String SYS_PROC_PATH = "/sys/module/lowmemorykiller/parameters/donotkill_sysproc";
    public static final String USER_PROC_NAMES_PATH = "/sys/module/lowmemorykiller/parameters/donotkill_proc_names";
    public static final String USER_SYS_NAMES_PATH = "/sys/module/lowmemorykiller/parameters/donotkill_sysproc_names";
    public static final String USER_PROC_SOB = "user_proc_boot";
    public static final String SYS_PROC_SOB = "sys_proc_boot";
    public static final String PREF_USER_PROC = "pref_user_proc";
    public static final String PREF_SYS_PROC = "pref_sys_proc";
    public static final String PREF_USER_NAMES = "pref_user_names_proc";
    public static final String PREF_SYS_NAMES = "pref_sys_names_proc";
    //-------BLX---------
    public static final String PREF_BLX = "pref_blx";
    public static final String BLX_PATH = "/sys/class/misc/batterylifeextender/charging_limit";
    public static final String BLX_SOB = "blx_sob";
    //-------DFsync---------
    public static final String DSYNC_PATH = "/sys/kernel/dyn_fsync/Dyn_fsync_active";
    public static final String PREF_DSYNC = "pref_dsync";
    //-------BL----
    public static final String PREF_BLTIMEOUT = "pref_bltimeout";
    public static final String BLTIMEOUT_SOB = "bltimeout_sob";
    public static final String PREF_BLTOUCH = "pref_bltouch";
    public static final String BL_TIMEOUT_PATH = "/sys/class/misc/notification/bl_timeout";
    public static final String BL_TOUCH_ON_PATH = "/sys/class/misc/notification/touchlight_enabled";
    //-------BLN---------
    public static final String PREF_BLN = "pref_bln";

    // VM settings
    public static final String PREF_DIRTY_RATIO = "pref_dirty_ratio";
    public static final String PREF_DIRTY_BACKGROUND = "pref_dirty_background";
    public static final String PREF_DIRTY_EXPIRE = "pref_dirty_expire";
    public static final String PREF_DIRTY_WRITEBACK = "pref_dirty_writeback";
    public static final String PREF_MIN_FREE_KB = "pref_min_free_kb";
    public static final String PREF_OVERCOMMIT = "pref_overcommit";
    public static final String PREF_SWAPPINESS = "pref_swappiness";
    public static final String PREF_VFS = "pref_vfs";
    public static final String DIRTY_RATIO_PATH = "/proc/sys/vm/dirty_ratio";
    public static final String DIRTY_BACKGROUND_PATH = "/proc/sys/vm/dirty_background_ratio";
    public static final String DIRTY_EXPIRE_PATH = "/proc/sys/vm/dirty_expire_centisecs";
    public static final String DIRTY_WRITEBACK_PATH = "/proc/sys/vm/dirty_writeback_centisecs";
    public static final String MIN_FREE_PATH = "/proc/sys/vm/min_free_kbytes";
    public static final String OVERCOMMIT_PATH = "/proc/sys/vm/overcommit_ratio";
    public static final String SWAPPINESS_PATH = "/proc/sys/vm/swappiness";
    public static final String VFS_CACHE_PRESSURE_PATH = "/proc/sys/vm/vfs_cache_pressure";
    public static final String VM_SOB = "vm_sob";

    // Voltage control
    public static final String VOLTAGE_SOB = "voltage_sob";
    public static final String UV_MV_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table";
    public static final String VDD_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/vdd_levels";
    public static final String COMMON_VDD_PATH = "/sys/devices/system/cpu/cpufreq/vdd_levels";
    public static final String VDD_SYSFS_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/vdd_sysfs_levels";

    //Freezer
    public static final String PREF_FRREZE = "freeze_packs";
    public static final String PREF_UNFRREZE = "unfreeze_packs";

    //CPU Boost driver
    public static final String CPUBOOST_INPUT_FREQ ="/sys/module/cpu_boost/parameters/input_boost_freq";
    public static final String CPUBOOST_INPUT_MS="/sys/module/cpu_boost/parameters/input_boost_ms";
    public static final String CPUBOOST_BOOST_MS="/sys/module/cpu_boost/parameters/boost_ms";
    public static final String CPUBOOST_SYNC_THRESHOLD="/sys/module/cpu_boost/parameters/sync_threshold";
    
    //MSM-Hotplug
    public static final String CPUS_POSSIBLE = "/sys/devices/system/cpu/possible";
    public static final String MSMHOTPLUG_MIN_CPUS = "/sys/module/msm_hotplug/min_cpus_online";
    public static final String MSMHOTPLUG_MAX_CPUS = "/sys/module/msm_hotplug/max_cpus_online";
    
}



