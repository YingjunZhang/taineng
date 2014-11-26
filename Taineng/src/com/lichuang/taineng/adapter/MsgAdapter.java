package com.lichuang.taineng.adapter;

import java.util.List;

import com.lichuang.taineng.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MsgAdapter extends BaseAdapter{
	private LayoutInflater inflater;
    private List<String> src;

    public MsgAdapter(Context context,List<String> data){
        inflater = LayoutInflater.from(context);
        src = data;
    }
    public void SetData(List<String> data){
        src= data;
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return src.size();
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
		ViewHolder viewholder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.message_listitem,null);
            viewholder = new ViewHolder();
            viewholder.tv=(TextView)convertView.findViewById(R.id.msg_listitem_tv1);
            convertView.setTag(viewholder);
        }else{
            viewholder = (ViewHolder)convertView.getTag();
        }
        viewholder.tv.setText(src.get(position));
        return convertView;
	}
	
	 private class ViewHolder{
	        TextView tv;
	    }

}
