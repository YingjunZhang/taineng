package com.lichuang.taineng.activity;


import com.lichuang.taineng.R;

import com.lichuang.taineng.adapter.HeatAnalyseVpAdapter;
import com.lichuang.taineng.aidl.IMyAidlInterface;
import com.lichuang.taineng.bean.MyLog;
import com.lichuang.taineng.bean.MyUtil;
import com.lichuang.taineng.contentprovider.ProviderMeta;
import com.lichuang.taineng.fragment.HeatChaobiaoFrag;
import com.lichuang.taineng.fragment.HeatSetupFrag;
import com.lichuang.taineng.fragment.HeatSetupFrag.WenkongListener;
import com.lichuang.taineng.service.LinkService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.graphics.Color;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;


/**
 * 热表分析页面
 * @author holy
 *
 */
public class HeatAnalyseActivity extends BaseActivity implements OnClickListener,WenkongListener{
	
	private TextView title_txt;
	private ImageButton home_imbt;
	
	private LinearLayout analyse_ll;
	private Button chaobiao_btn;
	private Button setup_btn;
	
	private FragmentManager fragmentManager;
	private IMyAidlInterface myAIDLService; //aidl通讯接口
	private WenkongObserver wenkongObserver;
	
	private boolean isAddressChange = false;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
		}
		
	};
	
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
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.energy_analyse);
		MyLog.LogInfo("taineng analyseActivity", "onCreate");
		AddObserver();
		InitView();
		Intent intent = new Intent(HeatAnalyseActivity.this,LinkService.class);
		bindService(intent,connection,BIND_AUTO_CREATE);
	}
	
	private void AddObserver(){
		wenkongObserver = new WenkongObserver(mHandler);
		getContentResolver().registerContentObserver(ProviderMeta.read_wenkong_uri, true, wenkongObserver);
	}
	
	private void InitView(){
		title_txt=(TextView)findViewById(R.id.title_txt);
		title_txt.setText("热表消耗");
		home_imbt=(ImageButton)findViewById(R.id.home_btn);
		home_imbt.setOnClickListener(this);
		analyse_ll=(LinearLayout)findViewById(R.id.analyse_content_ll);
		
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().add(R.id.analyse_content_ll, HeatChaobiaoFrag.NewInstance(), "chaobiaoFragment").commit();
		
		
		chaobiao_btn = (Button)findViewById(R.id.analyse_chaobiao_btn);
		chaobiao_btn.setOnClickListener(this);
		chaobiao_btn.setText("抄表记录");
		setup_btn = (Button)findViewById(R.id.analyse_setup_btn); 
		setup_btn.setOnClickListener(this);
		setup_btn.setText("设置");
	}	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyLog.LogInfo("taineng analyseActivity", "destroy");
		unbindService(connection);
		getContentResolver().unregisterContentObserver(wenkongObserver);
	}

	

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.home_btn:
			Intent intent = new Intent();
			if (isAddressChange){			
				setResult(MyUtil.heat_address_chcode,intent);			
			}else{
				setResult(32,intent);
			}
			finish();
			break;
		case R.id.analyse_chaobiao_btn:
			chaobiao_btn.setBackgroundResource(R.drawable.daohangtiao);
			setup_btn.setBackgroundResource(R.drawable.daohangtiao_light);
			fragmentManager.beginTransaction().replace(R.id.analyse_content_ll, HeatChaobiaoFrag.NewInstance(), "chaobiaoFragment").commit();
			break;
		case R.id.analyse_setup_btn:
			chaobiao_btn.setBackgroundResource(R.drawable.daohangtiao_light);
			setup_btn.setBackgroundResource(R.drawable.daohangtiao);
			fragmentManager.beginTransaction().replace(R.id.analyse_content_ll, HeatSetupFrag.NewInstance(), "setupFragment").commit();
			break;
		}
	}
	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (isAddressChange){			
			setResult(MyUtil.heat_address_chcode,intent);			
		}else{
			setResult(32,intent);
		}
		finish();
		return false;
	}



	private class WenkongObserver extends ContentObserver{

		public WenkongObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
		}
		
	}

	@Override
	public void sendWenkongCmd(int changeCode) {
		// TODO Auto-generated method stub
		switch(changeCode){
		case MyUtil.read_wenkongfa:
			try {
				myAIDLService.SendCommand(MyUtil.read_wenkongfa);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case MyUtil.heat_address_chcode:
			isAddressChange= true;
			break;
		}
		
	}

	

}
