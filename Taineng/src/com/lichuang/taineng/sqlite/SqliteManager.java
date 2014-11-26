package com.lichuang.taineng.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.lichuang.taineng.bean.MyLog;
import com.lichuang.taineng.sqlite.model.HeatValue;


/**
 * Created by holy on 2014/11/6.
 */
public class SqliteManager {
    private  MeterSqliteHelper sqliteHelper;
    private  SQLiteDatabase db;
    private static SqliteManager sm;

    private  SqliteManager(Context mContext){
        sqliteHelper = new MeterSqliteHelper(mContext);
//        db = sqliteHelper.getWritableDatabase();
    }

    /**
     * 获得数据库帮助类唯一实例
     * @param context
     * @return
     */
    public synchronized static SqliteManager getInstance(Context context){
        if (sm == null) {
            sm = new SqliteManager(context);
        }
        return sm;
    }

    /**
     * 向实时记录表单条插入数据
     * @param hv 单条数据
     */
    public void addToHeatRealtime(HeatValue hv){
    	db = sqliteHelper.getWritableDatabase();
        db.execSQL("INSERT INTO HeatRealtime VALUES(null, ?, ?,?,?,?,?,?)", new Object[]{hv.nowHeat,hv.heatP,hv.instanFlow,hv.leijiFlow,hv.gsTemperature,hv.hsTemperature,hv.realTime});
        db.close();
    }

    /**
     * 向实时记录表批量添加数据
     * @param heatValues 装有多条数据的list
     */
    public void addToHeatRealtimeTs(List<HeatValue> heatValues) {
    	db = sqliteHelper.getWritableDatabase();
        db.beginTransaction();  //开始事务
        try {
            for (HeatValue hv : heatValues) {
            	db.execSQL("INSERT INTO HeatRealtime VALUES(null, ?, ?,?,?,?,?,?)", new Object[]{hv.nowHeat,hv.heatP,hv.instanFlow,hv.leijiFlow,hv.gsTemperature,hv.hsTemperature,hv.realTime});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
        db.close();
    }

    
    /**
     * 向抄表记录表插入数据
     * @param hv 单条数据
     */
    public void addToHeatChaobiao(HeatValue hv){
    	db = sqliteHelper.getWritableDatabase();
        db.execSQL("INSERT INTO HeatChaobiao VALUES(null, ?, ?,?,?,?,?,?)", new Object[]{hv.nowHeat,hv.heatP,hv.instanFlow,hv.leijiFlow,hv.gsTemperature,hv.hsTemperature,hv.realTime});
        db.close();
    }



    /**
     * 根据查询语句查询结果,查询热表相关数据库
     * @param sql 查询语句
     * @return 返回包含查询结果的List
     */
    public List<HeatValue> QueryHeat(String sql){
    	db = sqliteHelper.getReadableDatabase();
        List<HeatValue> queryResult = new ArrayList<HeatValue>();
        Cursor c = db.rawQuery(sql, null);
        while(c.moveToNext()){
            HeatValue hv = new HeatValue();
            hv._id=c.getInt(c.getColumnIndex("id"));
            hv.nowHeat=c.getString(c.getColumnIndex("当前热量"));
            hv.heatP = c.getString(c.getColumnIndex("热功率"));
            hv.instanFlow = c.getString(c.getColumnIndex("瞬时流量"));
            hv.leijiFlow = c.getString(c.getColumnIndex("累计流量"));
            hv.gsTemperature = c.getString(c.getColumnIndex("供水温度"));
            hv.hsTemperature = c.getString(c.getColumnIndex("回水温度"));
            hv.realTime = c.getString(c.getColumnIndex("实时时间"));
            MyLog.LogInfo("taineng sqlitemanager", "realtime:"+hv.realTime);
            queryResult.add(hv);
        }
        db.close();
        return queryResult;
        
    }

    /**
     * 使用完毕后关闭数据库连接
     */
    public void closeDB() {
        db.close();
        sqliteHelper.close();
    }
}
