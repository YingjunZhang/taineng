package com.lichuang.taineng.adapter;

import com.lichuang.taineng.fragment.HeatChaobiaoFrag;
import com.lichuang.taineng.fragment.HeatSetupFrag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HeatAnalyseVpAdapter extends FragmentPagerAdapter{

	public HeatAnalyseVpAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}



	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch(position){
		case 0:
			
			
		case 1:
			
		default:
			return null;
		}
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
