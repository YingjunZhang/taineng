package com.lichuang.taineng.fragment;

import com.lichuang.taineng.R;
import com.lichuang.taineng.adapter.HeatCbAdapter;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HeatChaobiaoFrag extends Fragment{
	private ListView chaobiao_lv;
	private static HeatChaobiaoFrag cbFrag;
	
	public static Fragment NewInstance(){
		if (cbFrag == null){
			cbFrag= new HeatChaobiaoFrag();
		}
		return cbFrag;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.heat_chaobiao_frag, null);
		InitView(view);
		return view;
	}
	private void InitView(View view){
		chaobiao_lv=(ListView)view.findViewById(R.id.heat_chaobiao_lv);
		HeatCbAdapter cbAdapter = new HeatCbAdapter(getActivity().getApplicationContext());
		chaobiao_lv.setAdapter(cbAdapter);
	}

}
