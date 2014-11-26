package com.lichuang.taineng.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by holy on 2014/11/6.
 */
public class MeterSqliteHelper extends SQLiteOpenHelper{
    private static  String DATABASE_NAME = "tainengdb";
    private static  int DATABASE_VERSION = 1;

    public MeterSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库
    	sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS HeatRealtime(id integer   primary key autoincrement,当前热量  varchar(15),热功率  varchar(15),"
    			+ "瞬时流量  varchar(15),累计流量  varchar(15),供水温度 varchar(15),回水温度  varchar(15),实时时间  DATETIME) ");//建立抄表记录表
    	sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS HeatChaobiao(id integer   primary key autoincrement,当前热量  varchar(15),热功率  varchar(15),"
    			+ "瞬时流量  varchar(15),累计流量  varchar(15),供水温度 varchar(15),回水温度  varchar(15),实时时间  DATETIME) ");//建立抄表记录表

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
