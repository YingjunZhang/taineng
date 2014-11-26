package com.lichuang.taineng.adapter;

import com.lichuang.taineng.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String>{
	private Context mContext;
	private String[] m;

	public SpinnerAdapter(Context context, int resource, String[] objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		mContext = context;
		m = objects;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(
                    R.layout.spinner_item, parent, false);
        }

        TextView tv = (TextView) convertView
                .findViewById(android.R.id.text1);
        tv.setText(m[position]);
//        tv.setTextColor(Color.BLUE);
        
        return convertView;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 if (convertView == null) {
	            LayoutInflater inflater = LayoutInflater.from(mContext);
	            convertView = inflater.inflate(
	                    R.layout.spinner_item, parent, false);
	        }

	        // android.R.id.text1 is default text view in resource of the android.
	        // android.R.layout.simple_spinner_item is default layout in resources of android.

	        TextView tv = (TextView) convertView
	                .findViewById(android.R.id.text1);
	        tv.setText(m[position]);
//	        tv.setTextColor(Color.BLUE);
	       
	        return convertView;
	}

	
	
}
