package com.lichuang.taineng.service;

import com.lichuang.taineng.application.SysApplication;
import com.lichuang.taineng.device.DeviceManager;

import android.content.Context;
import android.os.Handler;



/**
 * Created by Administrator on 2014/11/7.
 */
public class ComRunnalbe implements Runnable {
    private Handler mHandler;
    private Context mContext;

    public ComRunnalbe(Handler handler,Context context){
         this.mHandler=handler;
         mContext = context;
    }

    @Override
    public void run() {
        try {
            DeviceManager.getInstance(mContext).sendComd();
        } catch (InterruptedException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(100004);
        }
    }
}
