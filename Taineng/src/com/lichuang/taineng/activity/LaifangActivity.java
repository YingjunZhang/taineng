package com.lichuang.taineng.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import com.lichuang.taineng.R;
import com.lichuang.taineng.adapter.MsgAdapter;
import com.lichuang.taineng.camera.CBCamera;
import com.lichuang.taineng.camera.Preview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 来访界面
 * @author Administrator
 *
 */
public class LaifangActivity extends BaseActivity implements OnItemClickListener,OnClickListener{

	private TextView weidu_txt;
	private ImageButton weidu_zhankai_imbt;
	private ListView weidu_lv;
	private TextView yidu_txt;
	private ImageButton yidu_zhankai_imbt;
	private ListView yidu_lv;
	private ImageButton home_imbt;
	private FrameLayout preview_frame;
	private Preview preview;
	
	
	private List<String> data1 = new ArrayList<String>();
	private List<String> data2 = new ArrayList<String>();
	private int[] pic = {R.drawable.people2,R.drawable.people3,R.drawable.people4,R.drawable.people5,R.drawable.people6};
	private Random random;
	private static int dataIndex= 0;
	private MsgAdapter wjAdapter;
	private MsgAdapter yjAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.laifang_layout);
		random = new Random();
        InitView();
	}
	
	private void InitView(){
		data1.add("2014-09-04 未接来访");
        data1.add("2014-09-05 未接来访");
        data1.add("2014-09-06 未接来访");

        data2.add("2014-09-07 已接来访");
        data2.add("2014-09-08 已接来访");
        data2.add("2014-09-09 已接来访");
        yidu_lv = (ListView)findViewById(R.id.laifang_yidu_lv);
        yjAdapter = new MsgAdapter(LaifangActivity.this,data2);
        yidu_lv.setAdapter(yjAdapter);
        yidu_zhankai_imbt=(ImageButton)findViewById(R.id.yiyue_zhankai_imbt);
		yidu_zhankai_imbt.setOnClickListener(this);
        weidu_lv = (ListView)findViewById(R.id.laifang_weidu_lv);
        wjAdapter=new MsgAdapter(LaifangActivity.this,data1);
        weidu_lv.setAdapter(wjAdapter);
        weidu_zhankai_imbt=(ImageButton)findViewById(R.id.weiyue_zhankai_imbt);
		weidu_zhankai_imbt.setOnClickListener(this);
        
        yidu_lv.setOnItemClickListener(this);
        weidu_lv.setOnItemClickListener(this);
        home_imbt=(ImageButton)findViewById(R.id.home_btn);
        home_imbt.setOnClickListener(this);
        
        preview_frame=(FrameLayout)findViewById(R.id.laifang_act_preview_fram);
        CBCamera.instance().setWindowManager((WindowManager)getSystemService(WINDOW_SERVICE));
		CBCamera.instance().setContentResolver(getContentResolver());
		preview = new Preview(this);
		preview_frame.addView(preview);
         
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		// TODO Auto-generated method stub
		switch(adapterView.getId()){
		case R.id.laifang_weidu_lv:
			String str = data1.get(i);
            data1.remove(i);
            str=str.substring(0,10)+" 已接来访";
            data2.add(str);
            wjAdapter.SetData(data1);
            wjAdapter.notifyDataSetChanged();
            yjAdapter.SetData(data2);
            yjAdapter.notifyDataSetChanged();
			break;
		case R.id.message_yidu_lv:
			break;
		}
		Random random = new Random();
		int a = 0;
        do{
            a = random.nextInt(3);
        }while(a == dataIndex);
        
       
        dataIndex = a;
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.weiyue_zhankai_imbt:
			CBCamera.instance().takePicture();
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
			Intent intent = new Intent(LaifangActivity.this,MainActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	

}
