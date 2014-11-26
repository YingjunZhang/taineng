package com.lichuang.taineng.fragment;

import com.lichuang.taineng.R;
import com.lichuang.taineng.activity.HeatWenkongActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class HeatSetupFrag extends Fragment implements OnClickListener{
	private static HeatSetupFrag setupFrag;
	
	private Button wenkong_setup_btn;
	
	public static HeatSetupFrag NewInstance(){
		if (setupFrag == null){
			setupFrag = new HeatSetupFrag();
			
		}
		return setupFrag;
		
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.heat_setup_frag, null);
		wenkong_setup_btn = (Button)view.findViewById(R.id.heat_setup_wenkong_setup_btn);
		wenkong_setup_btn.setOnClickListener(this);
		return view;
	}



	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.heat_setup_address_save_btn:
			break;
		case R.id.heat_setup_wenkong_setup_btn:
			Intent intent = new Intent(getActivity(),HeatWenkongActivity.class);
			getActivity().startActivity(intent);
			break;
		}
	}

	
}
