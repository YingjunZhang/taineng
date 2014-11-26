package com.lichuang.taineng.service;

import android.content.Context;

import com.lichuang.taineng.application.SysApplication;
import com.lichuang.taineng.device.DeviceManager;



/**
 * Created by Administrator on 2014/11/7.
 */
public class AnalyseRunnable implements Runnable {
	private Context mContext;
	
	public AnalyseRunnable(Context context){
		mContext = context;
	}
	
    @Override
    public void run() {
        DeviceManager.getInstance(mContext).AnalyseData();
    }
}
