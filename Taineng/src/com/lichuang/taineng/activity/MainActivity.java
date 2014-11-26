package com.lichuang.taineng.activity;


import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



import com.lichuang.taineng.R;
import com.lichuang.taineng.aidl.IMyAidlInterface;
import com.lichuang.taineng.application.SysApplication;
import com.lichuang.taineng.bean.ConfigManager;
import com.lichuang.taineng.bean.MyLog;
import com.lichuang.taineng.bean.MyUtil;
import com.lichuang.taineng.contentprovider.ProviderMeta;
import com.lichuang.taineng.device.DeviceManager;
import com.lichuang.taineng.device.DeviceManager.CmdType;
import com.lichuang.taineng.service.LinkService;
import com.lichuang.taineng.sqlite.SqliteManager;
import com.lichuang.taineng.sqlite.model.HeatValue;


import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;


public class MainActivity extends BaseActivity implements OnClickListener{
	    private TextView time_txt;
	      
	    private LinearLayout heat_llayout;
	    private LinearLayout elec_llayout;
	    private LinearLayout water_llayout;
	    private LinearLayout gas_llayout;
	    private LayoutInflater mInflater;
	    private Timer timer;
	    private TextView heat_xiaohao_txt;
	    private TextView heat_chaobiao_txt;
	    private TextView heat_now_txt;
	    private TextView elec_xiaohao_txt;
	    private TextView elec_chaobiao_txt;
	    private TextView elec_now_txt;
	    
	    private ImageButton system_setup_imbt; //系统设置按钮
	    private ImageButton message_imbt;
	    private ImageButton laifang_imbt;
	    
	    private static double heat_Chaobiao=0;
	    private static double elec_Chaobiao = 0;
	    
	    private IMyAidlInterface myAIDLService; //aidl通讯接口
	    private MyContentObserver myObserver;    //监听采集变化
	    private MyContentObserver2 myObserver2;  //监听抄表变化
	    private boolean isExit;
	    private ConfigManager configManager;
	    
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
	    private Handler mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				DecimalFormat twoDf = new DecimalFormat("#.00");  
				
				switch(msg.what){
				case 100002: //时间改变
					SetTime();
					break;
				case 100004: //热表实时变化
					Bundle bundle = msg.getData();
					String value = bundle.getString("rtvalue");
					heat_now_txt.setText(value+"KWh");
					heat_chaobiao_txt.setText(String.valueOf(heat_Chaobiao)+"KWh");
					double rtValue = Double.parseDouble(value);
					heat_xiaohao_txt.setText(twoDf.format(rtValue-heat_Chaobiao)+"KWh");
					break;
				case 100006:  //热表抄表变化
					Bundle bundle2 = msg.getData();
					String value2 = bundle2.getString("cbvalue");
					heat_Chaobiao = Double.parseDouble(value2);
					heat_chaobiao_txt.setText(value2+"KWh");
					value2 = (String) heat_now_txt.getText();
					heat_xiaohao_txt.setText(twoDf.format(Double.valueOf(value2) - heat_Chaobiao)+"KWh");
					break;
				}
				
			}
	    	
	    };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyLog.LogInfo("taineng Activity", "create");
		configManager = ConfigManager.getInstance(getApplicationContext());
		
		//启动Service
		Intent intent = new Intent(MainActivity.this,LinkService.class);
		startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
        ReadAddressFile();
        ReadConfigFile(); //读取配置信息        
        InitView(); //初始化控件
        AddView();  //添加控件
        QueryDatabase();
        AddObserver();   //添加监听器
		
	}	

//	private void ReadPingmu(){
//		DisplayMetrics metric = new DisplayMetrics(); 
//        getWindowManager().getDefaultDisplay().getMetrics(metric); 
//        int width = metric.widthPixels;  // 屏幕宽度（像素） 
//        int height = metric.heightPixels;  // 屏幕高度（像素） 
//        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5） 
//        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240） 
//        double diagonalPixels = Math.sqrt(Math.pow(width, 2)+Math.pow(height, 2)) ; 
//       	double screenSize = diagonalPixels/(160*density) ; 
//       	MyLog.LogInfo("Mainactivity", "width="+width+";height"+height+"densityDpi="+densityDpi+";screenSize="+screenSize);
//	}



	/**
	 * 初始化控件
	 */
	private void InitView(){
		timer = new Timer();
		mInflater = LayoutInflater.from(this);
		time_txt = (TextView)findViewById(R.id.time_txt);	
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(100002);
			}
			
		}, 2000, 5*1000);
		
		heat_llayout=(LinearLayout)findViewById(R.id.mainpage_heat_ll);
		heat_llayout.setOnClickListener(this);
		elec_llayout=(LinearLayout)findViewById(R.id.mainpage_elec_ll);
		elec_llayout.setOnClickListener(this);
		water_llayout=(LinearLayout)findViewById(R.id.mainpage_water_ll);
		water_llayout.setOnClickListener(this);
		gas_llayout=(LinearLayout)findViewById(R.id.mainpage_gas_ll);
		gas_llayout.setOnClickListener(this);
		
		system_setup_imbt = (ImageButton)findViewById(R.id.setup_imbt);
		system_setup_imbt.setOnClickListener(this);
		message_imbt = (ImageButton)findViewById(R.id.message_imbt);
		message_imbt.setOnClickListener(this);
		laifang_imbt = (ImageButton)findViewById(R.id.people_imbt);
		laifang_imbt.setOnClickListener(this);
	}
	
	private void SetTime(){
		Time time = new Time();
		time.setToNow();
		String tempStr = "";
		if(time.hour < 10){
			tempStr = "0"+time.hour+":";
			
		}else{
			tempStr =tempStr+ time.hour+":";
		}
		if(time.minute < 10){
			tempStr = tempStr + "0"+time.minute;
		}else{
			tempStr = tempStr +time.minute;
		}
		
		tempStr=tempStr+" ";
		switch(time.weekDay){
		case 0:
			tempStr=tempStr+"周日";
			
			break;
		case 1:
			tempStr=tempStr+"周一";
			break;
		case 2:
			tempStr=tempStr+"周二";
			break;
		case 3:
			tempStr=tempStr+"周三";
			break;
		case 4:
			tempStr=tempStr+"周四";
			break;
		case 5:
			tempStr=tempStr+"周五";
			break;
		case 6:
			tempStr=tempStr+"周六";
			break;
				
		}
		tempStr= tempStr+ " ";
		tempStr = tempStr+time.year+"-";
		
		if (time.month < 10){
			tempStr = tempStr+"0"+String.valueOf(time.month)+"-";
			
		}else{
			tempStr = tempStr+String.valueOf(time.month)+"-";
		}
		if(time.monthDay < 10){
			tempStr = tempStr+"0"+String.valueOf(time.monthDay);
		}else{
			tempStr = tempStr+String.valueOf(time.monthDay);
		}
		
		time_txt.setText(tempStr);
	}
	
	private void ReadAddressFile(){
		configManager.ReadHeatAddress();
		configManager.ReadGasAddress();
		configManager.ReadWaterAddress();
		configManager.ReadElecAddress();
	}
	
	private void ReadConfigFile(){		 
		 configManager.ReadHeatConfigFile();		 
		 configManager.ReadGasConfigFile();		 
		 configManager.ReadWaterConfigFile();		 
		 configManager.ReadElecConfigFile();
	   }
	
	private void AddView(){
		//加载热表
		
		
		if(MyUtil.if_Rebiao_Exist == 1){
			heat_llayout.setVisibility(View.VISIBLE);
			heat_xiaohao_txt= (TextView)findViewById(R.id.heat_xiaohao_txt);
			heat_chaobiao_txt=(TextView)findViewById(R.id.heat_chaobiao_Txt);
			heat_now_txt = (TextView)findViewById(R.id.heat_now_txt);
			
		}
		if(MyUtil.if_Dianbiao_Exist == 1){
			elec_llayout.setVisibility(View.VISIBLE);
			elec_xiaohao_txt =(TextView)findViewById(R.id.elec_xiaohao_txt);
			elec_chaobiao_txt=(TextView)findViewById(R.id.elec_chaobiao_Txt);
			elec_now_txt = (TextView)findViewById(R.id.elec_now_txt); 
		}
		if(MyUtil.if_Shuibiao_Exist == 1){
			water_llayout.setVisibility(View.VISIBLE);
		}
		if(MyUtil.if_Qibiao_Exist == 1){
			gas_llayout.setVisibility(View.VISIBLE);
		}
	}
	
	private void AddObserver(){
		ContentResolver resolver = this.getContentResolver();  
		myObserver =  new MyContentObserver(mHandler);
		myObserver2 = new MyContentObserver2(mHandler);
		resolver.registerContentObserver(ProviderMeta.hrtUri, true,myObserver); //监听实时数据变化
		resolver.registerContentObserver(ProviderMeta.hrcUri, true, myObserver2);//监听抄表数据变化
	}
	
	//
	private void QueryDatabase(){
		DecimalFormat twoDf = new DecimalFormat("#.00");  
		if (MyUtil.if_Rebiao_Exist == 1){
			List<HeatValue> result = SqliteManager.getInstance(getApplicationContext()).QueryHeat("select * from HeatChaobiao order by id desc limit(1)");
			MyLog.LogInfo("taineng activity", "查询数据库");
			if(result.size()>0){
				MyLog.LogInfo("taineng activity", "查询到数据");
				HeatValue hv = result.get(0);
				String tempStr = hv.nowHeat;
				heat_Chaobiao = Double.parseDouble(tempStr.substring(0,tempStr.length()-3));
			}
			heat_chaobiao_txt.setText(String.valueOf(heat_Chaobiao)+"KWh");
			result = SqliteManager.getInstance(getApplicationContext()).QueryHeat("select * from HeatRealtime order by id desc limit(1)");
			if(result.size()> 0){
				HeatValue hv = result.get(0);
				String tempStr = hv.nowHeat;
				Double nowHeat = Double.parseDouble(tempStr.substring(0,tempStr.length()-3));
				heat_now_txt.setText(String.valueOf(nowHeat)+"KWh");
				heat_xiaohao_txt.setText(twoDf.format(nowHeat-heat_Chaobiao)+"KWh");
			}else{
				heat_now_txt.setText("0KWh");
				heat_xiaohao_txt.setText(String.valueOf(0-heat_Chaobiao)+"KWh");
			}
			
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyLog.LogInfo("taineng Activity", "destroy");
		timer.cancel();
		unbindService(connection);
		getContentResolver().unregisterContentObserver(myObserver);
		getContentResolver().unregisterContentObserver(myObserver2);
	}



	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(resultCode){
		case MyUtil.scheme_chcode:
			Toast.makeText(MainActivity.this , "配置方案已经更改，请重启软件", Toast.LENGTH_SHORT).show();
			break;
		
		}
	}





	/**
	 * 热表实时数据
	 * @author holy
	 *
	 */
    private class MyContentObserver extends ContentObserver{
    	private Handler mHandler;

		public MyContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
			mHandler = handler;
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			SharedPreferences sp = getApplicationContext().getSharedPreferences(ProviderMeta.fileName, Context.MODE_MULTI_PROCESS+Context.MODE_PRIVATE);
			if (sp.contains(ProviderMeta.dbhrt)){
				MyLog.LogInfo("taineng observer", "监听到实时变化");
				String rtStr = sp.getString(ProviderMeta.dbhrt,"nothing");
				if(!rtStr.equals("nothing")){
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString("rtvalue", rtStr);
					msg.setData(b);
					msg.what = 100004;
					mHandler.sendMessage(msg);
					
				}
				
			}
			
		}

		
    	
		
    }

    /**
     * 热表抄表数据
     * @author holy
     */
    private class MyContentObserver2 extends ContentObserver{
    	private Handler mHandler;

		public MyContentObserver2(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
			mHandler = handler;
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			SharedPreferences sp = getApplicationContext().getSharedPreferences(ProviderMeta.fileName, Context.MODE_MULTI_PROCESS+Context.MODE_PRIVATE);
			if (sp.contains(ProviderMeta.dbhrt)){
				MyLog.LogInfo("taineng observer", "监听到抄表变化"+sp.getString(ProviderMeta.dbrc,"nothing"));
				String tempStr = sp.getString(ProviderMeta.dbrc,"nothing");
				if(! tempStr.equals("nothing")){
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("cbvalue", tempStr);
					msg.setData(bundle);
					msg.what = 100006;
					mHandler.sendMessage(msg);
					
				}
			}
			
		}

		
    	
		
    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.mainpage_heat_ll:
			Intent intent= new Intent(MainActivity.this,HeatAnalyseActivity.class);
			startActivity(intent);
			break;
		case R.id.mainpage_elec_ll:
			break;
		case R.id.mainpage_water_ll:
			break;
		case R.id.mainpage_gas_ll:
			break;
		case R.id.setup_imbt:
			Intent intent2 = new Intent(MainActivity.this,SystemSetupActivity.class);
			startActivityForResult(intent2,10001);
			break;
		case R.id.message_imbt:
			Intent intent3 = new Intent(MainActivity.this,MessageActivity.class);
			startActivity(intent3);
			break;
		case R.id.people_imbt:
			Intent intent4 = new Intent(MainActivity.this,LaifangActivity.class);
			startActivity(intent4);
			break;
		}
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)  {    
	  	      exitBy2Click();    //双击退出    
	  	}
		return false;
	}
	
	/**
	 * 双击退出事件
	 */
	private void exitBy2Click() {  
	    Timer tExit = null;  
	    if (isExit == false) {  
	        isExit = true; // 
	        Toast.makeText(this, "请按返回键退出", Toast.LENGTH_SHORT).show();  
	        tExit = new Timer();  
	        tExit.schedule(new TimerTask() {  
	            @Override  
	            public void run() {  
	                isExit = false; //
	            }  
	        }, 2000); //
	  
	    } else {  
	    	SysApplication.getInstance().exit();
	    } 
	}
	

}
