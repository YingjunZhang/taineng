package com.lichuang.taineng.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.lichuang.taineng.R;
import com.lichuang.taineng.adapter.MsgAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 消息管理页面
 * @author Administrator
 *
 */
public class MessageActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	private TextView message_title_txt; //消息标题
	private TextView message_info_txt;  //消息内容
	private TextView weidu_txt;
	private ImageButton weidu_zhankai_imbt;
	private ListView weidu_lv;
	private TextView yidu_txt;
	private ImageButton yidu_zhankai_imbt;
	private ListView yidu_lv;
	private ImageButton home_imbt;
	
	private List<String> data1 = new ArrayList<String>();
    private List<String> data2 = new ArrayList<String>();
    private MsgAdapter wdAdapter;
    private MsgAdapter ydAdapter;
    private String[] txtitle={
    		"供暖缴费",
    		"客户调查",
    		"供暖通知",
    		"供暖缴费"
    };
    private String[] txinfo = {
            "昨日供暖费用为10.31元，请您查收",
            "您觉得室内温度如何，如不合适可向我们反馈",
            "您已供暖30天",
            "您这一个月的供暖总费用为1200元"
    };
    private static int dataIndex=0; //静态数组索引，用于比较，以防止出现重复

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_layout);
		InitView();
	}
	
	private void InitView(){
		message_title_txt = (TextView)findViewById(R.id.message_title_txt);
		message_info_txt = (TextView)findViewById(R.id.message_info_txt);
		weidu_txt = (TextView)findViewById(R.id.weiyue_txt);
		weidu_txt.setText("未读消息");
		weidu_zhankai_imbt=(ImageButton)findViewById(R.id.weiyue_zhankai_imbt);
		weidu_zhankai_imbt.setOnClickListener(this);
		weidu_lv=(ListView)findViewById(R.id.message_weidu_lv);
		yidu_txt = (TextView)findViewById(R.id.yiyue_txt);
		yidu_txt.setText("已读消息");
		yidu_zhankai_imbt=(ImageButton)findViewById(R.id.yiyue_zhankai_imbt);
		yidu_zhankai_imbt.setOnClickListener(this);
		yidu_lv = (ListView)findViewById(R.id.message_yidu_lv);
		home_imbt=(ImageButton)findViewById(R.id.home_btn);
		home_imbt.setOnClickListener(this);
				
		data1.add("2014-09-04 未读通知");
        data1.add("2014-09-05 未读通知");
        data1.add("2014-09-06 未读通知");

        data2.add("2014-09-07 已读通知");
        data2.add("2014-09-08 已读通知");
        data2.add("2014-09-09 已读通知");
        ydAdapter= new MsgAdapter(MessageActivity.this,data2);
        yidu_lv.setAdapter(ydAdapter);
        yidu_lv.setOnItemClickListener(this);
        wdAdapter = new MsgAdapter(MessageActivity.this,data1);
        weidu_lv.setAdapter(wdAdapter);
        weidu_lv.setOnItemClickListener(this);
        
        Random random = new Random();
		int a = 0;
        do{
            a = random.nextInt(3);
        }while(a == dataIndex);
        message_info_txt.setText(txinfo[a]);
        message_title_txt.setText(txtitle[a]);
        dataIndex = a;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.weiyue_zhankai_imbt:
			if(weidu_lv.getVisibility() == View.GONE){
				weidu_lv.setVisibility(View.VISIBLE);
				weidu_zhankai_imbt.setImageResource(R.drawable.down_arrow);
			}else{
				weidu_lv.setVisibility(View.GONE);
				weidu_zhankai_imbt.setImageResource(R.drawable.right_arrow);
			}
			break;
		case R.id.yiyue_zhankai_imbt:
			if(yidu_lv.getVisibility() == View.GONE){
				yidu_lv.setVisibility(View.VISIBLE);
				yidu_zhankai_imbt.setImageResource(R.drawable.down_arrow);
			}else{
				yidu_lv.setVisibility(View.GONE);
				yidu_zhankai_imbt.setImageResource(R.drawable.right_arrow);
			}
			break;
			
		case R.id.home_btn:
			Intent intent = new Intent(MessageActivity.this,MainActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		// TODO Auto-generated method stub
		switch(adapterView.getId()){
		case R.id.message_weidu_lv:
			String str = data1.get(i);
            data1.remove(i);
            str=str.substring(0,10)+" 已读通知";
            data2.add(str);
            wdAdapter.SetData(data1);
            wdAdapter.notifyDataSetChanged();
            ydAdapter.SetData(data2);
            ydAdapter.notifyDataSetChanged();
			break;
		case R.id.message_yidu_lv:
			break;
		}
		Random random = new Random();
		int a = 0;
        do{
            a = random.nextInt(3);
        }while(a == dataIndex);
        message_info_txt.setText(txinfo[a]);
        message_title_txt.setText(txtitle[a]);
        dataIndex = a;
	}

	
}
