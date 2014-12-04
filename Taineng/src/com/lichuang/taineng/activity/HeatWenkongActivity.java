package com.lichuang.taineng.activity;

import com.lichuang.taineng.R;
import com.lichuang.taineng.aidl.IMyAidlInterface;
import com.lichuang.taineng.fragment.WenkongEasyFrag;
import com.lichuang.taineng.fragment.WenkongHolidayFrag;
import com.lichuang.taineng.fragment.WenkongJingjiFrag;
import com.lichuang.taineng.fragment.WenkongOutFrag;
import com.lichuang.taineng.service.LinkService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class HeatWenkongActivity extends BaseActivity implements OnClickListener{
	private LinearLayout frag_content_ll;
	private Button easy_model_bt;
	private Button jingji_model_bt;
	private Button out_model_bt;
	private Button holiday_model_bt;
	private IMyAidlInterface myAIDLService; //aidl通讯接口
	 private ServiceConnection connection = new ServiceConnection() {


			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
						
				myAIDLService = IMyAidlInterface.Stub.asInterface(service);
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				
			}


	    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heat_wenkong_setup);
		InitView();
		//启动Service
		Intent intent = new Intent(HeatWenkongActivity.this,LinkService.class);
	    bindService(intent, connection, BIND_AUTO_CREATE);
		onClick(easy_model_bt);
		
	}
	private void InitView(){
//		frag_content_ll=(LinearLayout)findViewById(R.id.heat_wenkong_setup_content_ll);
		easy_model_bt=(Button)findViewById(R.id.heat_wenkong_eashmodel_btn);
		easy_model_bt.setOnClickListener(this);
		jingji_model_bt = (Button)findViewById(R.id.heat_wenkong_economicalmodel_btn);
		jingji_model_bt.setOnClickListener(this);
		out_model_bt=(Button)findViewById(R.id.heat_wenkong_outdoormodel_btn);
		out_model_bt.setOnClickListener(this);
		holiday_model_bt=(Button)findViewById(R.id.heat_wenkong_holidaymodel_btn);
		holiday_model_bt.setOnClickListener(this);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(connection);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.heat_wenkong_eashmodel_btn:
			easy_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_clicked);
			jingji_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_normal);
			out_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_normal);
			holiday_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_normal);
			getFragmentManager().beginTransaction().replace(R.id.heat_wenkong_setup_content_ll, new WenkongEasyFrag()).commit();
			break;
		case R.id.heat_wenkong_economicalmodel_btn:
			jingji_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_clicked);
			easy_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_normal);
			out_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_normal);
			holiday_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_normal);
			getFragmentManager().beginTransaction().replace(R.id.heat_wenkong_setup_content_ll, new WenkongJingjiFrag()).commit();
			break;
		case R.id.heat_wenkong_outdoormodel_btn:
			out_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_clicked);
			jingji_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_normal);
			easy_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_normal);
			holiday_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_normal);
			getFragmentManager().beginTransaction().replace(R.id.heat_wenkong_setup_content_ll, new WenkongOutFrag()).commit();
			break;
		case R.id.heat_wenkong_holidaymodel_btn:
			holiday_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_clicked);
			jingji_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_normal);
			out_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_normal);
			easy_model_bt.setBackgroundResource(R.color.heat_wenkong_setup_normal);
			getFragmentManager().beginTransaction().replace(R.id.heat_wenkong_setup_content_ll, new WenkongHolidayFrag()).commit();
			break;
		}
	}

	private class WenkongContentObserver extends ContentObserver{

		public WenkongContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
		}
		
		
		
	}
	
}
