package com.lichuang.taineng.device;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dwin.navy.serialportapi.SerialPortOpt;
import com.lichuang.taineng.bean.MyLog;
import com.lichuang.taineng.bean.MyUtil;

/**
 * com0下只下挂热表
 * @author Administrator
 *
 */
public class ComZeroManager {
	private SerialPortOpt serialPort;
	public static int com0DataBits;
	public static int com0BoundRate;
	public static int com0StopBits;
	public static char com0Parity;
	public static int comSleepTime=600;
	
	private Context mContext;
	private String  configName="comzeroconfig";
	private SharedPreferences sharedPreference;
	private List<String> deviceList;
	private ConnModel connModel;
	private List<Map<String,String>> result;
	private FileInputStream inputStream;   //读串口数据的输入流
	
	public ComZeroManager(Context context){
		serialPort = new SerialPortOpt ();		   
        this.mContext=context;
        sharedPreference = mContext.getSharedPreferences(configName, Context.MODE_MULTI_PROCESS+Context.MODE_PRIVATE);       
       
	}
	
	public void ColoseComdev(){
		if(serialPort.mFd != null){
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		serialPort.closeDev(serialPort.mFd);
    	}
	}
	
	/**
	 * 初始化com0下挂载的设备
	 */
	public void InitDevice(){
		result = new ArrayList<Map<String,String>>();
		deviceList = new ArrayList<String>();
		int flag = sharedPreference.getInt("连接方式", 0);
		if(flag != 0 ){
			switch(flag){
			case 1:
				connModel = ConnModel.采集器透明转发;
				serialPort.mDevNum = 0;//com0
		        serialPort.mDataBits= 8 ;//数据位
		        serialPort.mSpeed= 9600;// 波特率
		        serialPort.mStopBits = 1;//停止位
		        serialPort.mParity='n'; //校验位
		        serialPort.openDev(serialPort.mDevNum); //打开串口
		        serialPort.setSpeed(serialPort.mFd, serialPort.mSpeed);//设置波特率
		        serialPort.setParity(serialPort.mFd, serialPort.mDataBits, serialPort.mStopBits, serialPort.mParity);//设置校验
		        inputStream = new FileInputStream(serialPort.mFd);
				String str = sharedPreference.getString("地址", null);
				if(str != null){
					String[] array = str.split(",");
					for(int i =0;i<array.length;i++){
						deviceList.add(array[i]);
					}
				}
				break;
			case 2:
				connModel = ConnModel.温控阀挂接;
				serialPort.mDevNum = 0;//com0
		        serialPort.mDataBits= 8 ;//数据位
		        serialPort.mSpeed= 2400;// 波特率
		        serialPort.mStopBits = 1;//停止位
		        serialPort.mParity='e'; //校验位
		        serialPort.openDev(serialPort.mDevNum); //打开串口
		        serialPort.setSpeed(serialPort.mFd, serialPort.mSpeed);//设置波特率
		        serialPort.setParity(serialPort.mFd, serialPort.mDataBits, serialPort.mStopBits, serialPort.mParity);//设置校验
		        inputStream = new FileInputStream(serialPort.mFd);
				String str2 = sharedPreference.getString("地址", null);
				if(str2 != null){
					String[] array = str2.split(",");
					for(int i =0;i<array.length;i++){
						deviceList.add(array[i]);
					}
				}
				break;
			}
		}
	}
	
	/**
	 * 发送采热表数据命令
	 * @throws InterruptedException
	 */
	public void SendReadDataCommand() throws InterruptedException {
		result.clear();
		if(connModel != null){
			switch(connModel){
			case 采集器透明转发:
				if(deviceList.size()>0){
					for (int i =0;i<deviceList.size();i++){
						LcRebiao lcRebiao = new LcRebiao();
						byte[] cmd = lcRebiao.GetDataCommand(deviceList.get(i));
						WriteCom(cmd);      	         	
						try {
							Thread.currentThread().sleep(comSleepTime);
							MyLog.LogInfo("taineng send cmd", "开始接收数据");
		                    byte[] buffer = ReadCom(inputStream);
		                    Map<String,String> resultMap = lcRebiao.GetData(buffer);
		                    result.add(resultMap);
						} catch (InterruptedException e) {
								// TODO Auto-generated catch block
							e.printStackTrace();
							throw e;
						}			
                    	
                    }
				}
				break;
			case 温控阀挂接:
				if(deviceList.size()>0){
					for(int i =0;i<deviceList.size();i++){
						LcWenkongfa wenkongfa = new LcWenkongfa();
						byte[] cmd = wenkongfa.getTemperatureControlNuifyCommand(deviceList.get(i));
						WriteCom(cmd);
						try {
							Thread.currentThread().sleep(comSleepTime);
							byte[] buffer = ReadCom(inputStream);
	                    	Map<String,String> resultMap = wenkongfa.analyseTemperatureControlNuifyRespond(buffer);
	                    	result.add(resultMap);
	                    	
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw e;
						}
                    	
					}
				}
				break;
			}
		}
		
	}
	
	/**
	 * 发送读温控阀命令
	 * @throws InterruptedException 
	 */
	public void SendReadWenkongfa() throws InterruptedException{
		result.clear();
		if(connModel != null){
			if(connModel==ConnModel.温控阀挂接){
				if(deviceList.size()>0){
					for(int i =0;i<deviceList.size();i++){
						LcWenkongfa wenkongfa = new LcWenkongfa();
						byte[] cmd = wenkongfa.getReadDataCommand(deviceList.get(0));
						WriteCom(cmd);
						try {
							Thread.currentThread().sleep(comSleepTime);
							byte[] buffer = ReadCom(inputStream);
							Map<String,String> resultMap = wenkongfa.analyseReadDataCommandRespond(buffer);
							result.add(resultMap);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw e;
						}
					}				
					
				}
			}
		}
	}
	
	/**
	 * 发送写温控阀命令
	 * @param data
	 * @throws InterruptedException 
	 */
	public void SendWriteWenkongfa(byte[] data) throws InterruptedException{
		result.clear();
		if(connModel != null){
			if(connModel==ConnModel.温控阀挂接){
				if(deviceList.size()>0){
					for(int i =0;i<deviceList.size();i++){
						LcWenkongfa wenkongfa = new LcWenkongfa();
						byte[] cmd = wenkongfa.getWriteDataCommand(deviceList.get(i), data);
						WriteCom(cmd);
						try {
							Thread.currentThread().sleep(comSleepTime);
							byte[] buffer = ReadCom(inputStream);
							boolean resultValue = wenkongfa.analyseWriteDataRespond(buffer);
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw e;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param cmodel  连接方式
	 * @param address   地址之间用逗号分割
	 */
	public void SaveConfig(ConnModel cmodel,String address){
		Editor editor = sharedPreference.edit();
		editor.clear();
		switch(cmodel){
		case 采集器透明转发:
			editor.putInt("连接方式", 1);
			break;
		case 温控阀挂接:
			editor.putInt("连接方式", 2);
			break;
		}
		
		editor.putString("地址", address);
		
		editor.commit();
	}
	
	 private void WriteCom(byte[] cmd){
 		serialPort.writeBytes(cmd);	
 	
	 }
 
	 private byte[] ReadCom(InputStream inStream){
		 int bytesRead = 0;
		 int bytesToRead= 61;
		
		 byte[] data = new byte[bytesToRead];
		 try {
			 MyLog.LogInfo("taineng device", "可读字节数"+ inStream.available());
		 } catch (IOException e1) {
			// TODO Auto-generated catch block
			 e1.printStackTrace();
		 }
		 while(bytesRead < bytesToRead){			
				try {
					int result = inStream.read(data, bytesRead, bytesToRead - bytesRead);
					if(result == -1) break;
					bytesRead += result;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		 }	
		 return data;
	 }
	
	public static enum ConnModel{
		采集器透明转发,   //对应1
		温控阀挂接    //对应2
	}
	public static enum DeviceEnum{
		热表,
		温控阀
	}
	
	/**
	 * 将读到的多个热表的数据进行综合并返回结果
	 * @return
	 */
	public Map<String,String> MultiHeatResult(){
		Map<String,String> resultMap = new HashMap<String,String>();
		double nowHeat=0;    //当前热量
		double instanFlow=0; //瞬时流量
		double leijiFlow=0;  //累计流量
		double gsTemperature=0; //供水温度
		double hsTemperature=0; //回水温度
		if(connModel != null){
			switch(connModel){
			case 采集器透明转发:
				if(result.size()>0){
					for(int i=0;i<result.size();i++){
						Map<String,String> tempMap = result.get(i);
						nowHeat=nowHeat+Double.parseDouble(tempMap.get(LcRebiao.nowHeat));
						instanFlow = instanFlow+Double.parseDouble(tempMap.get(LcRebiao.instanFlow));
						leijiFlow = leijiFlow + Double.parseDouble(tempMap.get(LcRebiao.leijiFlow));
						gsTemperature = gsTemperature+Double.parseDouble(tempMap.get(LcRebiao.gsTemperature));
						hsTemperature = hsTemperature+Double.parseDouble(tempMap.get(LcRebiao.hsTemperature));
					}
					gsTemperature = gsTemperature/result.size();
					hsTemperature = hsTemperature/result.size();
					resultMap.put(LcRebiao.nowHeat, String.valueOf(nowHeat)+"kwh");
					resultMap.put(LcRebiao.instanFlow, String.valueOf(instanFlow)+"m³/h");
					resultMap.put(LcRebiao.leijiFlow, String.valueOf(leijiFlow)+"m³");
					resultMap.put(LcRebiao.gsTemperature, String.valueOf(gsTemperature)+"℃");
					resultMap.put(LcRebiao.hsTemperature, String.valueOf(hsTemperature)+"℃");
						
				}
				break;
			case 温控阀挂接:
				if(result.size()>0){
					for(int i =0;i<result.size();i++){
						Map<String,String> tempMap = result.get(i);
						nowHeat=nowHeat+Double.parseDouble(tempMap.get(LcWenkongfa.cueernt_heat));
						instanFlow = instanFlow+Double.parseDouble(tempMap.get(LcWenkongfa.instanFlow));
						leijiFlow = leijiFlow + Double.parseDouble(tempMap.get(LcWenkongfa.leijiFlow));
						gsTemperature = gsTemperature+Double.parseDouble(tempMap.get(LcWenkongfa.gsTemperature));
						hsTemperature = hsTemperature+Double.parseDouble(tempMap.get(LcWenkongfa.hsTemperature));
					}
					gsTemperature = gsTemperature/result.size();
					hsTemperature= hsTemperature/result.size();
					resultMap.put(LcWenkongfa.cueernt_heat, String.valueOf(nowHeat)+"kwh");
					resultMap.put(LcWenkongfa.instanFlow, String.valueOf(instanFlow)+"m³/h");
					resultMap.put(LcWenkongfa.leijiFlow, String.valueOf(leijiFlow)+"m³");
					resultMap.put(LcWenkongfa.gsTemperature, String.valueOf(gsTemperature)+"℃");
					resultMap.put(LcWenkongfa.hsTemperature, String.valueOf(hsTemperature)+"℃");
					
				}
				break;
			}
		}
		
		return resultMap;
	}

}
