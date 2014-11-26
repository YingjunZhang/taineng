package com.lichuang.taineng.fragment;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lichuang.taineng.R;
import com.lichuang.taineng.adapter.SpinnerAdapter;
import com.lichuang.taineng.application.SysApplication;
import com.lichuang.taineng.bean.ConfigManager;
import com.lichuang.taineng.bean.MyLog;
import com.lichuang.taineng.bean.MyUtil;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class SystemSetupSchemeFragment extends Fragment implements OnClickListener{
	private Spinner scheme_spi;
	private static final String[] m={"热表方案","电表方案","水表方案","燃气表方案"};
	private Button save_scheme_btn;
	private RadioButton caiji_hour;
	private RadioButton caiji_minute;
	private RadioButton caiji_second;
	private EditText caiji_timespace;
	private RadioButton cunchu_hour;
	private RadioButton cunchu_minute;
	private RadioButton cunchu_second;
	private EditText cunchu_timespace;
	private Button save_btn;
	private CheckBox scheme_ifuse_cb;
	
	
	private String tempStr = "";
	private int ifExist = 0;
	private int caiji_flag= 0;
	private int caiji_jiange=0;
	private int cunchu_flag = 0;
	private int cunchu_jiange = 0;
	private ConfigManager configManager;
	
	/**
	 * 生命一个回调接口让包含该Fragment的activity实现
	 * @author Administrator
	 *
	 */
	public interface OnConfigChangeListener{
		public void onConfigChanged(int code);
	}

	private OnConfigChangeListener configListener;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		 try{
	           configListener =(OnConfigChangeListener)activity;
	       }catch(ClassCastException e){
	    	   throw (new ClassCastException(activity.toString()+"must implement OnConfigChangeListener"));
	       }
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		configManager = ConfigManager.getInstance(getActivity().getApplicationContext());
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.system_setup_scheme_fragment, null);
		InitView(view);
		
		return view;
	}
	
	

	private void InitView(View view){
		scheme_spi = (Spinner)view.findViewById(R.id.system_setup_scheme_spi);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, m);
//		adapter.setDropDownViewResource(R.layout.spinner_item);
		scheme_spi.setAdapter(new SpinnerAdapter(getActivity(),R.layout.spinner_item,m));
		scheme_spi.setOnItemSelectedListener(new SpiItemChangeListener());
		caiji_hour = (RadioButton)view.findViewById(R.id.caiji_hour_rdb);
		caiji_minute = (RadioButton)view.findViewById(R.id.caiji_minute_rdb);
		caiji_second = (RadioButton)view.findViewById(R.id.caiji_second_rdb);
		caiji_timespace = (EditText)view.findViewById(R.id.caiji_timespace_edt);
		cunchu_hour = (RadioButton)view.findViewById(R.id.cunchu_hour_rdb);
		cunchu_minute = (RadioButton)view.findViewById(R.id.cunchu_minute_rdb);
		cunchu_second = (RadioButton)view.findViewById(R.id.cunchu_second_rdb);
		cunchu_timespace = (EditText)view.findViewById(R.id.cunchu_timespace_edt);
		save_btn=(Button)view.findViewById(R.id.scheme_save_btn);
		save_btn.setOnClickListener(this);
		scheme_ifuse_cb = (CheckBox)view.findViewById(R.id.scheme_ifuse_cb);
		
		if(MyUtil.if_Rebiao_Exist == 1){
			scheme_ifuse_cb.setChecked(true);
		}else{
			scheme_ifuse_cb.setChecked(false);
		}
		
		switch(MyUtil.heat_Caiji_Timespace_Flag){
		case 1: //秒
			caiji_second.setChecked(true);
			break;
		case 2:
			caiji_minute.setChecked(true);
			break;
		case 3:
			caiji_hour.setChecked(true);
			break;
		}
		caiji_timespace.setText(String.valueOf(MyUtil.heat_Caiji_Timespace));
		switch(MyUtil.heat_Cunchu_Timespace_Flag){
		case 1: //秒
			cunchu_second.setChecked(true);
			break;
		case 2:
			cunchu_minute.setChecked(true);
			break;
		case 3:
			cunchu_hour.setChecked(true);
			break;
		}
		cunchu_timespace.setText(String.valueOf(MyUtil.heat_Cunchu_Timespace));
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.scheme_save_btn:
			int scheme_flag= scheme_spi.getSelectedItemPosition();
			
			switch(scheme_flag){
			case 0:    //热表方案
				ReadInput();
				configManager.SaveHeatConfigFile(ifExist,caiji_flag,caiji_jiange,cunchu_flag,cunchu_jiange);
				
//				ReadConfigFile(); //返回主界面后会重新读取一次，所以这里不用重新读取
				configListener.onConfigChanged(MyUtil.heat_scheme_chcode);//通知Service重新读取配置文件
				
				break;
			case 1:   //电表方案
				ReadInput();
				
				configManager.SaveElecConfigFile(ifExist,caiji_flag,caiji_jiange,cunchu_flag,cunchu_jiange);
//				ReadConfigFile();
				configListener.onConfigChanged(MyUtil.elec_scheme_chcode);
				
				break;
			case 2:   //修改水表方案
				ReadInput();
				configManager.SaveWaterConfigFile(ifExist,caiji_flag,caiji_jiange,cunchu_flag,cunchu_jiange);
//				ReadConfigFile();
				configListener.onConfigChanged(MyUtil.water_shceme_chcode);
				break;
			case 3:   //修改气表方案
				ReadInput();
				configManager.SaveGasConfigFile(ifExist,caiji_flag,caiji_jiange,cunchu_flag,cunchu_jiange);
//				ReadConfigFile();
				configListener.onConfigChanged(MyUtil.gas_shceme_chcode);
				break;
				
			}
			break;
		}
	}
	
	/**
	 * 判断一个字符串是否全是数字
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str)
	{
		MyLog.LogInfo("taineng setup fragment", str);
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() )
		{
			MyLog.LogInfo("taineng setup fragment", "不是数字");
			return false;
			
		}
		MyLog.LogInfo("taineng setup fragment", "是数字");
			return true;
	}
	
	private class SpiItemChangeListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			MyLog.LogInfo("Taieng setup activity", "Spinner item selected item chanage"+ position);
			switch(position){
			case 0: //热表方案 
				if(MyUtil.if_Rebiao_Exist == 1){
					scheme_ifuse_cb.setChecked(true);
				}else{
					scheme_ifuse_cb.setChecked(false);
				}
				
				switch(MyUtil.heat_Caiji_Timespace_Flag){
				case 1: //秒
					caiji_second.setChecked(true);
					break;
				case 2:
					caiji_minute.setChecked(true);
					break;
				case 3:
					caiji_hour.setChecked(true);
					break;
				}
				caiji_timespace.setText(String.valueOf(MyUtil.heat_Caiji_Timespace));
				switch(MyUtil.heat_Cunchu_Timespace_Flag){
				case 1: //秒
					cunchu_second.setChecked(true);
					break;
				case 2:
					cunchu_minute.setChecked(true);
					break;
				case 3:
					cunchu_hour.setChecked(true);
					break;
				}
				cunchu_timespace.setText(String.valueOf(MyUtil.heat_Cunchu_Timespace));
				break;
			case 1:  //电表方案
				if(MyUtil.if_Dianbiao_Exist == 1){
					scheme_ifuse_cb.setChecked(true);
				}else{
					scheme_ifuse_cb.setChecked(false);
				}
				
				switch(MyUtil.elec_Caiji_Timespace_Flag){
				case 1: //秒
					caiji_second.setChecked(true);
					break;
				case 2:
					caiji_minute.setChecked(true);
					break;
				case 3:
					caiji_hour.setChecked(true);
					break;
				}
				caiji_timespace.setText(String.valueOf(MyUtil.elec_Caiji_Timespace));
				switch(MyUtil.elec_Cunchu_Timespace_Flag){
				case 1: //秒
					cunchu_second.setChecked(true);
					break;
				case 2:
					cunchu_minute.setChecked(true);
					break;
				case 3:
					cunchu_hour.setChecked(true);
					break;
				}
				cunchu_timespace.setText(String.valueOf(MyUtil.elec_Cunchu_Timespace));
				break;
			case 2: //水表
				if(MyUtil.if_Shuibiao_Exist == 1){
					scheme_ifuse_cb.setChecked(true);
				}else{
					scheme_ifuse_cb.setChecked(false);
				}
			
				switch(MyUtil.water_Caiji_Timespace_Flag){
				case 1: //秒
					caiji_second.setChecked(true);
					break;
				case 2:
					caiji_minute.setChecked(true);
					break;
				case 3:
					caiji_hour.setChecked(true);
					break;
				}
				caiji_timespace.setText(String.valueOf(MyUtil.water_Caiji_Timespace));
				switch(MyUtil.water_Cunchu_Timespace_Flag){
				case 1: //秒
					cunchu_second.setChecked(true);
					break;
				case 2:
					cunchu_minute.setChecked(true);
					break;
				case 3:
					cunchu_hour.setChecked(true);
					break;
				}
				cunchu_timespace.setText(String.valueOf(MyUtil.water_Cunchu_Timespace));
				break;
			case 3: //气表
				if(MyUtil.if_Qibiao_Exist == 1){
					scheme_ifuse_cb.setChecked(true);
				}else{
					scheme_ifuse_cb.setChecked(false);
				}
				
				switch(MyUtil.gas_Caiji_Timespace_Flag){
				case 1: //秒
					caiji_second.setChecked(true);
					break;
				case 2:
					caiji_minute.setChecked(true);
					break;
				case 3:
					caiji_hour.setChecked(true);
					break;
				}
				caiji_timespace.setText(String.valueOf(MyUtil.gas_Caiji_Timespace));
				switch(MyUtil.elec_Cunchu_Timespace_Flag){
				case 1: //秒
					cunchu_second.setChecked(true);
					break;
				case 2:
					cunchu_minute.setChecked(true);
					break;
				case 3:
					cunchu_hour.setChecked(true);
					break;
				}
				cunchu_timespace.setText(String.valueOf(MyUtil.gas_Cunchu_Timespace));
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * 读取配置文件
	 */
	private void ReadConfigFile(){
		   SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences(MyUtil.system_Config_File, Context.MODE_MULTI_PROCESS+Context.MODE_PRIVATE); 
		   MyUtil.if_Rebiao_Exist = sp.getInt("热表存在标志", 1);
		   MyUtil.heat_Caiji_Timespace =sp.getInt("热表采集间隔", 5);
		   MyUtil.heat_Caiji_Timespace_Flag= sp.getInt("热表采集时间标志", 2); //默认采集标志为分
		   MyUtil.heat_Cunchu_Timespace= sp.getInt("热表存储间隔", 5);
		   MyUtil.heat_Cunchu_Timespace_Flag = sp.getInt("热表存储时间标志", 2);
		   MyUtil.if_Dianbiao_Exist = sp.getInt("电表存在标志", 0);
		   MyUtil.elec_Caiji_Timespace =sp.getInt("电表采集间隔", 5);
		   MyUtil.elec_Caiji_Timespace_Flag= sp.getInt("电表采集时间标志", 2); //默认采集标志为分
		   MyUtil.elec_Cunchu_Timespace= sp.getInt("电表存储间隔", 5);
		   MyUtil.elec_Cunchu_Timespace_Flag = sp.getInt("电表存储时间标志", 2);
		   MyUtil.if_Shuibiao_Exist = sp.getInt("水表存在标志", 0);
		   MyUtil.water_Caiji_Timespace =sp.getInt("水表采集间隔", 5);
		   MyUtil.water_Caiji_Timespace_Flag= sp.getInt("水表采集时间标志", 2); //默认采集标志为分
		   MyUtil.water_Cunchu_Timespace= sp.getInt("水表存储间隔", 5);
		   MyUtil.water_Cunchu_Timespace_Flag = sp.getInt("水表存储时间标志", 2);
		   MyUtil.if_Qibiao_Exist = sp.getInt("气表存在标志", 0);
		   MyUtil.gas_Caiji_Timespace =sp.getInt("气表采集间隔", 5);
		   MyUtil.gas_Caiji_Timespace_Flag= sp.getInt("气表采集时间标志", 2); //默认采集标志为分
		   MyUtil.gas_Cunchu_Timespace= sp.getInt("气表存储间隔", 5);
		   MyUtil.gas_Cunchu_Timespace_Flag = sp.getInt("气表存储时间标志", 2);
	   }
	
	
	/**
	 * 用户保存方案更改时读取用户输入
	 */
	private void ReadInput(){
		
		if(scheme_ifuse_cb.isChecked()){
			ifExist = 1;
		}else{
			ifExist = 0;
		}

		tempStr = caiji_timespace.getText().toString();
		if (isNumeric(tempStr)){
			caiji_jiange = Integer.valueOf(tempStr);
			if(caiji_hour.isChecked()){
				caiji_flag= 3;
				if(caiji_jiange>23 || caiji_jiange<0){
					Toast.makeText(getActivity(), "采集间隔必须大于等于0且小于24", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if(caiji_minute.isChecked()){
				caiji_flag=2;
				if(caiji_jiange>59 || caiji_jiange<0){
					Toast.makeText(getActivity(), "采集间隔必须大于等于0且小于60", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if(caiji_second.isChecked()){
				caiji_flag=1;
				if(caiji_jiange>59 || caiji_jiange<0){
					Toast.makeText(getActivity(), "采集间隔必须大于等于0且小于60", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			
			
		}else{
			Toast.makeText(getActivity(), "采集间隔必须是数字", Toast.LENGTH_SHORT).show();
			return;
		}
		tempStr = cunchu_timespace.getText().toString();
		if (isNumeric(tempStr)){
			cunchu_jiange = Integer.valueOf(tempStr);
			if(cunchu_hour.isChecked()){
				cunchu_flag= 3;
				if(cunchu_jiange>23 || cunchu_jiange<0){
					Toast.makeText(getActivity(), "存储间隔必须大于等于0且小于24", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if(cunchu_minute.isChecked()){
				cunchu_flag=2;
				if(cunchu_jiange>59 || cunchu_jiange<0){
					Toast.makeText(getActivity(), "存储间隔必须大于等于0且小于60", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if(cunchu_second.isChecked()){
				cunchu_flag=1;
				if(cunchu_jiange>59 || cunchu_jiange<0){
					Toast.makeText(getActivity(), "存储间隔必须大于等于0且小于60", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			
			
		}else{
			Toast.makeText(getActivity(), "存储间隔必须是数字", Toast.LENGTH_SHORT).show();
			return;
		}
	}
	
	/**
	 * 存储热表采集方案
	 * @param ifExist 方案是否启用
	 * @param caiji_flag 采集时间标志
	 * @param caiji_jiange 采集间隔
	 * @param cunchu_flag  存储时间标志
	 * @param cunchu_jiange 存储间隔
	 */
	private void SaveHeatConfigFile(int ifExist,int caiji_flag,int caiji_jiange,int cunchu_flag,int cunchu_jiange){
		
	}
	

	/**
	 * 存储电表采集方案
	 * @param ifExist       方案是否启用
	 * @param caiji_flag    采集时间标志
	 * @param caiji_jiange  采集间隔
	 * @param cunchu_flag   存储时间标志
	 * @param cunchu_jiange 存储间隔
	 */
	private void SaveElecConfigFile(int ifExist,String address,int caiji_flag,int caiji_jiange,int cunchu_flag,int cunchu_jiange){
		SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences(MyUtil.system_Config_File, Context.MODE_MULTI_PROCESS+Context.MODE_PRIVATE); 
		Editor editor = sp.edit();
		editor.putInt("电表存在标志", ifExist);
		editor.putString("电表地址", address);
		editor.putInt("电表采集时间标志",caiji_flag);
		editor.putInt("电表采集间隔",caiji_jiange);
		editor.putInt("电表存储时间标志",cunchu_flag);
		editor.putInt("电表存储间隔",cunchu_jiange);
		editor.commit();
	}
	
	/**
	 * 存储水表采集方案
	 * @param ifExist       方案是否启用
	 * @param caiji_flag    采集时间标志
	 * @param caiji_jiange  采集间隔
	 * @param cunchu_flag   存储时间标志
	 * @param cunchu_jiange 存储间隔
	 */
	private void SaveWaterConfigFile(int ifExist,String address,int caiji_flag,int caiji_jiange,int cunchu_flag,int cunchu_jiange){
		SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences(MyUtil.system_Config_File, Context.MODE_MULTI_PROCESS+Context.MODE_PRIVATE); 
		Editor editor = sp.edit();
		editor.putInt("水表存在标志", ifExist);
		editor.putString("水表地址", address);
		editor.putInt("水表采集时间标志",caiji_flag);
		editor.putInt("水表采集间隔",caiji_jiange);
		editor.putInt("水表存储时间标志",cunchu_flag);
		editor.putInt("水表存储间隔",cunchu_jiange);
		editor.commit();
	}
	
	/**
	 * 存储燃气表采集方案
	 * @param ifExist       方案是否启用
	 * @param caiji_flag    采集时间标志
	 * @param caiji_jiange  采集间隔
	 * @param cunchu_flag   存储时间标志
	 * @param cunchu_jiange 存储间隔
	 */
	private void SaveGasConfigFile(int ifExist,String address,int caiji_flag,int caiji_jiange,int cunchu_flag,int cunchu_jiange){
		SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences(MyUtil.system_Config_File, Context.MODE_MULTI_PROCESS+Context.MODE_PRIVATE); 
		Editor editor = sp.edit();
		editor.putInt("气表存在标志", ifExist);
		editor.putString("气表地址", address);
		editor.putInt("气表采集时间标志",caiji_flag);
		editor.putInt("气表采集间隔",caiji_jiange);
		editor.putInt("气表存储时间标志",cunchu_flag);
		editor.putInt("气表存储间隔",cunchu_jiange);
		editor.commit();
	}


}
