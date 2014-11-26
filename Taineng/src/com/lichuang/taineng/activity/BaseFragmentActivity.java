package com.lichuang.taineng.activity;

import com.lichuang.taineng.application.SysApplication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class BaseFragmentActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		 SysApplication.getInstance().addActivity(this);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

}
