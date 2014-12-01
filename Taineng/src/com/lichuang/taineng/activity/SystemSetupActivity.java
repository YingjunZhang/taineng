package com.lichuang.taineng.activity;

import java.util.ArrayList;
import java.util.List;

import com.lichuang.taineng.R;

import com.lichuang.taineng.aidl.IMyAidlInterface;
import com.lichuang.taineng.bean.MyUtil;
import com.lichuang.taineng.fragment.SystemSetupSchemeFragment;
import com.lichuang.taineng.fragment.SystemSetupSchemeFragment.OnConfigChangeListener;
import com.lichuang.taineng.fragment.SystemSetupSkinFragment;
import com.lichuang.taineng.service.LinkService;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class SystemSetupActivity extends BaseActivity implements OnClickListener,OnConfigChangeListener{
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
    
    private ImageButton home_imbt;
    private TextView title_txt;
    private Button scheme_btn;
    private Button skin_btn;
	 
    private boolean isConfigChange= false; //返回给上层Activity的resultCode

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_setup);
		InitView();
		Intent intent = new Intent(SystemSetupActivity.this,LinkService.class);
		bindService(intent, connection, BIND_AUTO_CREATE);
	}
	
	private void InitView(){
			
		getFragmentManager().beginTransaction().add(R.id.system_setup_scheme_content_ll, new SystemSetupSchemeFragment()).commit();
		home_imbt = (ImageButton)findViewById(R.id.home_btn);
		home_imbt.setOnClickListener(this);
		title_txt = (TextView)findViewById(R.id.title_txt);
		title_txt.setText("系统设置");
		scheme_btn = (Button)findViewById(R.id.system_scheme_setup_btn);
		scheme_btn.setOnClickListener(this);
		skin_btn = (Button)findViewById(R.id.system_pifu_setup_btn);
		skin_btn.setOnClickListener(this);
	}
	
//	private class MOnItemSelectedListener implements OnItemSelectedListener{
//
//		@Override
//		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
//				long arg3) {
//			// TODO Auto-generated method stub
//			switch(position){
//			case 0:
//				getFragmentManager().beginTransaction().replace(R.id.system_setup_scheme_content_ll, new SystemSetupSchemeFragment()).commit();
//				break;
//			case 1:
//				getFragmentManager().beginTransaction().replace(R.id.system_setup_scheme_content_ll, new ElecSchemeFragment()).commit();
//				break;
//			
//			}
//		}
//
//		@Override
//		public void onNothingSelected(AdapterView<?> arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
	
	private List<String> getData(){
        
        List<String> data = new ArrayList<String>();
        data.add("方案设置");
        
         
        return data;
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(connection);
	}

	/**
	 * button点击事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.home_btn:
			Intent intent = new Intent();
			if (isConfigChange){			
				setResult(MyUtil.scheme_chcode,intent);			
			}else{
				setResult(32,intent);
			}
			finish();
			break;
		case R.id.system_scheme_setup_btn:
			getFragmentManager().beginTransaction().replace(R.id.system_setup_scheme_content_ll, new SystemSetupSchemeFragment()).commit();
			scheme_btn.setBackgroundColor(Color.parseColor("#b0eaf1ed"));
			skin_btn.setBackgroundColor(Color.parseColor("#b060686c"));
			break;
		case R.id.system_pifu_setup_btn:
			getFragmentManager().beginTransaction().replace(R.id.system_setup_scheme_content_ll, new SystemSetupSkinFragment()).commit();
			skin_btn.setBackgroundColor(Color.parseColor("#b0eaf1ed"));
			scheme_btn.setBackgroundColor(Color.parseColor("#b060686c"));
			break;
		}
	}
	
	
	
	
	
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (isConfigChange){			
			setResult(MyUtil.scheme_chcode,intent);			
		}else{
			setResult(32,intent);
		}
		finish();
		return false;
	}

	/**
     * 配置文件修改事件
     */
	@Override
	public void onConfigChanged(int code) {
		// TODO Auto-generated method stub
		isConfigChange = true;
		
	}
	

}
