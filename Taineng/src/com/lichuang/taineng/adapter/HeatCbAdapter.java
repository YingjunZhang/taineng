package com.lichuang.taineng.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.lichuang.taineng.R;
import com.lichuang.taineng.bean.MyLog;
import com.lichuang.taineng.sqlite.SqliteManager;
import com.lichuang.taineng.sqlite.model.HeatValue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 热表抄表记录页面数据适配器
 * @author Holy
 *
 */
public class HeatCbAdapter extends BaseAdapter{
	private List<HeatValue> dataList;
	private LayoutInflater mInflater;
	private String tempStr;
	
	public HeatCbAdapter(Context context){
		dataList = SqliteManager.getInstance(context).QueryHeat("select * from HeatChaobiao");
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size()+1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(position == 0){
			if(convertView == null){
				convertView=mInflater.inflate(R.layout.heat_chaobiao_listitem_title, null);
			}
			return convertView;
		}else{
			DecimalFormat twoDf = new DecimalFormat("#.00");  
			if (convertView == null){
				convertView = mInflater.inflate(R.layout.heat_chaobiao_listitem, null);
				viewHolder = new ViewHolder();
				viewHolder.id=(TextView)convertView.findViewById(R.id.heat_chaobiao_jilu_id_txt);
				viewHolder.cb=(TextView)convertView.findViewById(R.id.heat_chaobiao_jilu_cbvalue_txt);
				viewHolder.yn=(TextView)convertView.findViewById(R.id.heat_chaobiao_jilu_ynvalue_txt);
				viewHolder.time=(TextView)convertView.findViewById(R.id.heat_chaobiao_jilu_time_txt);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			HeatValue hv = dataList.get(position-1);
			MyLog.LogInfo("taineng cb adapter", "real time:"+hv.realTime);
			viewHolder.id.setText(String.valueOf(hv._id));
			viewHolder.cb.setText(hv.nowHeat);
			if(position == 1){
				viewHolder.yn.setText("0KWh");
				tempStr = hv.nowHeat;
			}else{
				double jl = Double.parseDouble(tempStr.substring(0, tempStr.length()-3));
				tempStr = hv.nowHeat;
				double now = Double.parseDouble(tempStr.substring(0,tempStr.length()-3));
				if((now-jl)== 0){
					viewHolder.yn.setText("0KWh");
					
				}else{
					viewHolder.yn.setText(twoDf.format(now-jl)+"KWh");
				}
				
			}
			viewHolder.time.setText(hv.realTime);
			return convertView;
		}
		
		
	}

	private class ViewHolder{
		TextView id;
		TextView cb;
		TextView yn;
		TextView time;
	}
	
}
