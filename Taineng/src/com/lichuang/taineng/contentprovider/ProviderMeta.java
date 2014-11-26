package com.lichuang.taineng.contentprovider;

import android.net.Uri;

public class ProviderMeta {
	 public static final String AUTHROTY = "com.lichuang.taineng.authority";
	 public static final String dbhrt = "HeatRealtime";
	 public static final String dbrc = "HeatChaobiao";
	 public static final String readWenkong="ReadWenkongfa";
	 public static final int dbhrtCode = 100001;//实时特征码
	 public static final int dbrcCode = 100002;//超标特征码
	 public static final int readWenkongCode = 100003;
	 public static final Uri hrtUri = Uri.parse("content://"+ AUTHROTY+"/"+dbhrt); //实时
	 public static final Uri hrcUri = Uri.parse("content://"+ AUTHROTY+"/"+dbrc); //抄表
	 public static final Uri read_wenkong_uri = Uri.parse("content://"+ AUTHROTY+"/"+readWenkong);
	 public static final String fileName="表数据";//表数据文件名称
}
