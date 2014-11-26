package com.lichuang.taineng.broadcast;

import com.lichuang.taineng.aidl.IMyAidlInterface;
import com.lichuang.taineng.application.SysApplication;
import com.lichuang.taineng.bean.MyLog;
import com.lichuang.taineng.device.DeviceManager;
import com.lichuang.taineng.device.DeviceManager.CmdType;
import com.lichuang.taineng.service.LinkService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;


/**
 * Created by Administrator on 2014/11/7.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.lichuang.android.USER_ACTION")){
        	
        	Intent serviceIntent= new Intent(context,LinkService.class);
        	IMyAidlInterface myAidlInterface = IMyAidlInterface.Stub.asInterface(peekService(context, serviceIntent));
        	MyLog.LogInfo("taineng broadcast", "收到广播");
            try {
				myAidlInterface.SendCommand(0);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}
