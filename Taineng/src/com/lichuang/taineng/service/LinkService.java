package com.lichuang.taineng.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Time;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.lichuang.taineng.aidl.IMyAidlInterface;
import com.lichuang.taineng.application.SysApplication;
import com.lichuang.taineng.bean.ConfigManager;
import com.lichuang.taineng.bean.MyLog;
import com.lichuang.taineng.bean.MyUtil;
import com.lichuang.taineng.broadcast.MyBroadcastReceiver;
import com.lichuang.taineng.contentprovider.ProviderMeta;
import com.lichuang.taineng.device.DeviceManager;
import com.lichuang.taineng.device.DeviceManager.CmdType;
import com.lichuang.taineng.sqlite.SqliteManager;



/**
 * Created by Administrator on 2014/11/7.
 * 此服务会启动串口线程和处理线程
 * 
 * 
 */
public class LinkService extends Service {
    private Thread comThread;
    private Thread analyseThread;
    
    private Timer mTimer;
    private PendingIntent pi;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 100004: //发送命令线程终止，需重启
                    comThread.start();
                    break;
            }
        }
    };
    private ConfigManager configManager;

    @Override
    public IBinder onBind(Intent intent) {
    	MyLog.LogInfo("taineng service ", "bind");
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.LogInfo("taineng service", "create.......");
        configManager = ConfigManager.getInstance(getApplicationContext());
        
        ReadConfigFile();
        comThread = new Thread(new ComRunnalbe(mHandler,getApplicationContext()));
        analyseThread = new Thread(new AnalyseRunnable(getApplicationContext()));
        comThread.start();
        analyseThread.start();      
        SetTimer();     
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.LogInfo("taineng service", "destroy");
        mTimer.cancel();
        DeviceManager.getInstance(getApplicationContext()).SetSFlag(false);
        DeviceManager.getInstance(getApplicationContext()).SetAFlag(false);
        try {
			Thread.currentThread().sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DeviceManager.getInstance(getApplicationContext()).CloseSerialPort();
		}
        
  
        
    }

    /**
     * 设置闹钟服务
     */
//     private void SetAlarmM(){
//         Time time = new Time("GMT+8");
//         time.setToNow();
//         Calendar c=Calendar.getInstance();
//         c.set(Calendar.YEAR,time.year);
//         c.set(Calendar.MONTH,time.month);
//         c.set(Calendar.DAY_OF_MONTH, time.monthDay);
//         c.set(Calendar.HOUR_OF_DAY,time.hour);
//         c.set(Calendar.MINUTE, time.minute+2);
//         c.set(Calendar.SECOND, 0);
//         //设定时间为 2011年6月28日19点50分0秒
//         //c.set(2011, 05,28, 19,50, 0);
//         //也可以写在一行里
//
//         alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//         Intent intent= new Intent("com.lichuang.android.USER_ACTION");
//         intent.setClass(this, MyBroadcastReceiver.class);
//         pi = PendingIntent.getBroadcast(this,0,intent,0);
////         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis()+24*60*60*1000,24*60*60*1000,pi); //第二天零点启动，间隔为一天
//         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),1*60*1000,pi); //测试,三小时 
//     }
     
    /**
     * 根据配置信息启动相应的定时发送命令任务
     */
     private void SetTimer(){
    	 mTimer = new Timer();
    	 int timeSpace = 0;
    	 //启动热表采集定时器
    	 switch(MyUtil.heat_Caiji_Timespace_Flag){
    	 case 1: //秒
    		 timeSpace = 1000;
    		 break;
    	 case 2://分
    		 timeSpace = 60*1000;
    		 break;
    	 case 3: //时
    		 timeSpace = 60 * 60 *1000;
    		 break;
    	 }
    	 mTimer.schedule(new HeatTimerTask(), 300, timeSpace); 
    	
    	 
     }
     

     /**
      * Activity绑定后，与service通信的接口
      */
    private IMyAidlInterface.Stub stub = new IMyAidlInterface.Stub() {

        @Override
        public int SendCommand(int type) throws RemoteException {
            // TODO Auto-generated method stub
        	switch(type){
        	
        	case MyUtil.read_wenkongfa: //读温控阀
        		DeviceManager.getInstance(getApplicationContext()).addCmd(DeviceManager.CmdType.读温控阀);
        		break;
        	}
        	
            return 1;
        }

        @Override
        public int SendMessage(String msg) throws RemoteException {
            // TODO Auto-generated method stub
            return 0;
        }


    };
    
    private class HeatTimerTask extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Time time = new Time();
			time.setToNow();
			String timeStr= time.year+"-";
			if (time.month < 10){
				timeStr = timeStr+"0"+String.valueOf(time.month)+"-";
				
			}else{
				timeStr = timeStr+String.valueOf(time.month)+"-";
			}
			if(time.monthDay < 10){
				timeStr = timeStr+"0"+String.valueOf(time.monthDay);
			}else{
				timeStr = timeStr+String.valueOf(time.monthDay);
			}
			if(time.hour < 10){
				timeStr = timeStr+ " 0"+String.valueOf(time.hour)+":";
			}else{
				timeStr = timeStr+" "+String.valueOf(time.hour)+":";
			}
			if(time.minute < 10){
				timeStr = timeStr+ "0"+String.valueOf(time.minute)+":";
			}else{
				timeStr = timeStr+String.valueOf(time.minute)+":";
			}
			if(time.second < 10){
				timeStr = timeStr+ "0"+String.valueOf(time.second);
			}else{
				timeStr = timeStr+String.valueOf(time.second);
			}
			System.out.println("采集时间:"+timeStr);
			int number=0;
			switch(MyUtil.heat_Caiji_Timespace_Flag){
			case 1: //秒
				number = time.second%MyUtil.heat_Caiji_Timespace;
				if (number == 0){
					DeviceManager.getInstance(getApplicationContext()).addCmd(DeviceManager.CmdType.采热表);
				}
				break;
			case  2: //分
				number = time.minute%MyUtil.heat_Caiji_Timespace;
				if (number == 0){
					DeviceManager.getInstance(getApplicationContext()).addCmd(DeviceManager.CmdType.采热表);
				}
				break;
			case 3: //时
				number = time.hour%MyUtil.heat_Caiji_Timespace;
				if (number == 0){
					DeviceManager.getInstance(getApplicationContext()).addCmd(DeviceManager.CmdType.采热表);
				}
				break;
			}
		}
    	
    }
   
  
   private void ReadConfigFile(){
	  
	  configManager.ReadHeatConfigFile();
	  configManager.ReadGasConfigFile();
	  configManager.ReadWaterConfigFile();  
	  configManager.ReadElecConfigFile();
   }
    

}
