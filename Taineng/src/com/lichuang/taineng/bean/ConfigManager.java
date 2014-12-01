package com.lichuang.taineng.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 配置管理
 * @author Holy
 *
 */
public class ConfigManager {
	private SharedPreferences sharedPreference;
	private static ConfigManager configManager;
	
	public ConfigManager(Context context){
		this.sharedPreference = context.getSharedPreferences(MyUtil.system_Config_File, Context.MODE_MULTI_PROCESS+Context.MODE_PRIVATE);
//		this.sharedPreference = sp;
	}
	
	public static ConfigManager getInstance(Context context){
		if (configManager == null){
			configManager = new ConfigManager(context.getApplicationContext());
		}
		return configManager;
	}
	
	
	
	/**
	 * 存储热表采集方案
	 * @param ifExist 方案是否启用
	 * @param caiji_flag 采集时间标志
	 * @param caiji_jiange 采集间隔
	 * @param cunchu_flag  存储时间标志
	 * @param cunchu_jiange 存储间隔
	 */
	public void SaveHeatConfigFile(int ifExist,int caiji_flag,int caiji_jiange,int cunchu_flag,int cunchu_jiange){
		
		Editor editor = sharedPreference.edit();
		editor.putInt("热表存在标志", ifExist);		
		editor.putInt("热表采集时间标志",caiji_flag);
		editor.putInt("热表采集间隔",caiji_jiange);
		editor.putInt("热表存储时间标志",cunchu_flag);
		editor.putInt("热表存储间隔",cunchu_jiange);
		editor.commit();
	}
	
	/**
	 * 读取热表采集方案
	 */
	public void ReadHeatConfigFile(){
		MyUtil.if_Rebiao_Exist = sharedPreference.getInt("热表存在标志", 0);
		MyUtil.heat_Caiji_Timespace =sharedPreference.getInt("热表采集间隔", 5);
		MyUtil.heat_Caiji_Timespace_Flag= sharedPreference.getInt("热表采集时间标志", 2); //默认采集标志为分
		MyUtil.heat_Cunchu_Timespace= sharedPreference.getInt("热表存储间隔", 5);
		MyUtil.heat_Cunchu_Timespace_Flag = sharedPreference.getInt("热表存储时间标志", 2);
	}
	
	/**
	 * 存储燃气表采集方案
	 * @param ifExist       方案是否启用
	 * @param caiji_flag    采集时间标志
	 * @param caiji_jiange  采集间隔
	 * @param cunchu_flag   存储时间标志
	 * @param cunchu_jiange 存储间隔
	 */
	public void SaveGasConfigFile(int ifExist,int caiji_flag,int caiji_jiange,int cunchu_flag,int cunchu_jiange){
		
		Editor editor = sharedPreference.edit();
		editor.putInt("气表存在标志", ifExist);
		editor.putInt("气表采集时间标志",caiji_flag);
		editor.putInt("气表采集间隔",caiji_jiange);
		editor.putInt("气表存储时间标志",cunchu_flag);
		editor.putInt("气表存储间隔",cunchu_jiange);
		editor.commit();
	}
	
	/**
	 * 读取气表采集方案
	 */
	public void ReadGasConfigFile(){
		MyUtil.if_Qibiao_Exist = sharedPreference.getInt("气表存在标志", 0);
		MyUtil.gas_Caiji_Timespace =sharedPreference.getInt("气表采集间隔", 5);
		MyUtil.gas_Caiji_Timespace_Flag= sharedPreference.getInt("气表采集时间标志", 2); //默认采集标志为分
		MyUtil.gas_Cunchu_Timespace= sharedPreference.getInt("气表存储间隔", 5);
		MyUtil.gas_Cunchu_Timespace_Flag = sharedPreference.getInt("气表存储时间标志", 2);
	}
	

	/**
	 * 存储电表采集方案
	 * @param ifExist       方案是否启用
	 * @param caiji_flag    采集时间标志
	 * @param caiji_jiange  采集间隔
	 * @param cunchu_flag   存储时间标志
	 * @param cunchu_jiange 存储间隔
	 */
	public void SaveElecConfigFile(int ifExist,int caiji_flag,int caiji_jiange,int cunchu_flag,int cunchu_jiange){
		
		Editor editor = sharedPreference.edit();
		editor.putInt("电表存在标志", ifExist);
		editor.putInt("电表采集时间标志",caiji_flag);
		editor.putInt("电表采集间隔",caiji_jiange);
		editor.putInt("电表存储时间标志",cunchu_flag);
		editor.putInt("电表存储间隔",cunchu_jiange);
		editor.commit();
	}
	
	/**
	 * 读取电表采集方案
	 */
	public void ReadElecConfigFile(){
		MyUtil.if_Dianbiao_Exist = sharedPreference.getInt("电表存在标志", 0);
		MyUtil.elec_Caiji_Timespace =sharedPreference.getInt("电表采集间隔", 5);
		MyUtil.elec_Caiji_Timespace_Flag= sharedPreference.getInt("电表采集时间标志", 2); //默认采集标志为分
		MyUtil.elec_Cunchu_Timespace= sharedPreference.getInt("电表存储间隔", 5);
		MyUtil.elec_Cunchu_Timespace_Flag = sharedPreference.getInt("电表存储时间标志", 2);
	}
	
	/**
	 * 存储水表采集方案
	 * @param ifExist       方案是否启用
	 * @param caiji_flag    采集时间标志
	 * @param caiji_jiange  采集间隔
	 * @param cunchu_flag   存储时间标志
	 * @param cunchu_jiange 存储间隔
	 */
	public void SaveWaterConfigFile(int ifExist,int caiji_flag,int caiji_jiange,int cunchu_flag,int cunchu_jiange){
		 
		Editor editor = sharedPreference.edit();
		editor.putInt("水表存在标志", ifExist);
		editor.putInt("水表采集时间标志",caiji_flag);
		editor.putInt("水表采集间隔",caiji_jiange);
		editor.putInt("水表存储时间标志",cunchu_flag);
		editor.putInt("水表存储间隔",cunchu_jiange);
		editor.commit();
	}
	
	/**
	 * 读取水表采集方案
	 */
	public void ReadWaterConfigFile(){
		MyUtil.if_Shuibiao_Exist = sharedPreference.getInt("水表存在标志", 0);
		MyUtil.water_Caiji_Timespace =sharedPreference.getInt("水表采集间隔", 5);
		MyUtil.water_Caiji_Timespace_Flag= sharedPreference.getInt("水表采集时间标志", 2); //默认采集标志为分
		MyUtil.water_Cunchu_Timespace= sharedPreference.getInt("水表存储间隔", 5);
		MyUtil.water_Cunchu_Timespace_Flag = sharedPreference.getInt("水表存储时间标志", 2);
	}
	


}
