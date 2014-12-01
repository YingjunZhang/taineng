package com.lichuang.taineng.fragment;

import com.lichuang.taineng.R;
import com.lichuang.taineng.activity.HeatWenkongActivity;
import com.lichuang.taineng.bean.ConfigManager;
import com.lichuang.taineng.bean.MyUtil;
import com.lichuang.taineng.device.ComZeroManager;
import com.lichuang.taineng.device.ComZeroManager.ConnModel;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HeatSetupFrag extends Fragment implements OnClickListener{
	private static HeatSetupFrag setupFrag;
	
	private CheckBox wenkong_isuse_cb;
	private EditText address_edt;
	private Button address_save_btn;
	private TextView wenkong_model_txt;
	private Button wenkong_setup_btn;
	
	private ComZeroManager comZeroManager;
	private WenkongListener wenkongListener;
	private LinearLayout wenkongLinearlayout;
	
	public static HeatSetupFrag NewInstance(){
		if (setupFrag == null){
			setupFrag = new HeatSetupFrag();
			
		}
		return setupFrag;
		
	}
	
	

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		wenkongListener = (WenkongListener)activity;
	}

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		comZeroManager= new ComZeroManager(getActivity().getApplicationContext());
		
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.heat_setup_frag, null);
		InitView(view);
		return view;
	}
	
	private void InitView(View view){
		wenkong_isuse_cb = (CheckBox)view.findViewById(R.id.heat_setup_wenkongisuse_cb);
		wenkong_isuse_cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
			
		});
		address_edt = (EditText)view.findViewById(R.id.heat_setup_address_edt);
		address_save_btn = (Button)view.findViewById(R.id.heat_setup_address_save_btn);
		address_save_btn.setOnClickListener(this);
		wenkongLinearlayout=(LinearLayout)view.findViewById(R.id.heat_setup_wenkong_content_ll);
		wenkong_model_txt = (TextView)view.findViewById(R.id.heat_setup_wenkong_nowmodel_tv);
		wenkong_setup_btn = (Button)view.findViewById(R.id.heat_setup_wenkong_setup_btn);
		wenkong_setup_btn.setOnClickListener(this);
	}



	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.heat_setup_address_save_btn:
			String address = address_edt.getText().toString();
			if(address.equals("")){
				Toast.makeText(getActivity(), "地址不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (wenkong_isuse_cb.isChecked()){
				comZeroManager.SaveConfig(ComZeroManager.ConnModel.温控阀挂接, address);
			}else{
				comZeroManager.SaveConfig(ComZeroManager.ConnModel.采集器透明转发, address);
			}
			wenkongListener.sendWenkongCmd(MyUtil.heat_address_chcode);
					
			break;
		case R.id.heat_setup_wenkong_setup_btn:
			Intent intent = new Intent(getActivity(),HeatWenkongActivity.class);
			getActivity().startActivity(intent);
			break;
		}
	}
	
	public interface WenkongListener{
		public void sendWenkongCmd(int changeCode);
	}

	
}
