package com.lichuang.taineng.contentprovider;

import com.lichuang.taineng.sqlite.SqliteManager;
import com.lichuang.taineng.sqlite.model.HeatValue;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class MyContentProvider extends ContentProvider{
	private static final UriMatcher matcher;
	
	
	 static{
		 
		 matcher = new UriMatcher(UriMatcher.NO_MATCH);
			matcher.addURI(ProviderMeta.AUTHROTY, ProviderMeta.dbhrt, ProviderMeta.dbhrtCode);
			matcher.addURI(ProviderMeta.AUTHROTY, ProviderMeta.dbrc, ProviderMeta.dbrcCode);
			matcher.addURI(ProviderMeta.AUTHROTY, ProviderMeta.readWenkong, ProviderMeta.readWenkongCode);
	    }

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = getContext().getSharedPreferences(ProviderMeta.fileName, Context.MODE_MULTI_PROCESS+Context.MODE_PRIVATE).edit();  
		switch(matcher.match(uri)){
		
		case ProviderMeta.dbhrtCode: //实时处理
			
	        //将EditText中的文本内容添加到编辑器  
	        editor.putString(ProviderMeta.dbhrt,values.getAsString("heatValue") );
	        
	        //提交编辑器内容  
	        editor.commit();  
			getContext().getContentResolver().notifyChange(uri, null);
			break;
		case ProviderMeta.dbrcCode: //抄表处理
			
	        editor.putString(ProviderMeta.dbrc,values.getAsString("heatValue") );
	        
	        //提交编辑器内容  
	        editor.commit();  
			getContext().getContentResolver().notifyChange(uri, null);
			break;
		case ProviderMeta.readWenkongCode:
			break;
		}
//		if(matcher.match(uri) == ProviderMeta.dbrcCode){
//			HeatValue hv = new HeatValue();
//			hv.value = values.getAsString("heatValue");
//			hv.collectTime=values.getAsString("getDTime");
//			SqliteManager.getInstance(getContext()).addToHeatRealtime(hv);
//			getContext().getContentResolver().notifyChange(uri, null);
//		}
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return true ;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
