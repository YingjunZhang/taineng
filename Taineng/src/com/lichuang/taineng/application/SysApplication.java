package com.lichuang.taineng.application;

import java.util.LinkedList;
import java.util.List;

import com.lichuang.taineng.bean.ConfigManager;
import com.lichuang.taineng.bean.MyLog;
import com.lichuang.taineng.bean.MyUtil;
import com.lichuang.taineng.device.DeviceManager;
import com.lichuang.taineng.sqlite.SqliteManager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class SysApplication extends Application{
	
	    
	    //创建一个队列装载打开的activity
	    private List<Activity> mList = new LinkedList<Activity>();
	    //创建一个application 的单例
	    private static SysApplication instance;
	
	   
	    
	    //默认构造函数
	    public SysApplication(){}
	    //返回application单例
	    public synchronized static SysApplication getInstance(){
	        if (null == instance) {
	            instance = new SysApplication();
	            
	        }
	        return instance;
	    }
	    
	
	    // add Activity
	    public void addActivity(Activity activity) {
	        mList.add(activity);
	    }
	    //退出程序时调用，并关闭所有打开的activity
	    public void exit() {
	        try {
	            for (Activity activity:mList) {
	                if (activity != null)
	                    activity.finish();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            System.exit(0);
	        }
	    }

	    @Override
	    public void onCreate() {
	        super.onCreate();
	        MyLog.LogInfo("taineng application", "application create");
	        
	    }

	    @Override
	    public void onLowMemory() {
	        super.onLowMemory();
	        System.gc();
	    }

	    @Override
	    public void onTerminate() {
	        super.onTerminate();
	       
	    }

}
