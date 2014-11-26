package com.lichuang.taineng.activity;

import com.lichuang.taineng.application.SysApplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;



/**
 * 对acitivity进行一层封装，方便进行统一管理
 * Created by holy on 2014/11/6.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
