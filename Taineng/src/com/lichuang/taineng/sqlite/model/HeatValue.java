package com.lichuang.taineng.sqlite.model;

/**
 * 热表采集值封装对象
 * Created by holy on 2014/11/6.
 * @version
 */
public class HeatValue {
    public int _id;
    public String nowHeat; //热表当前热量
    public String heatP; //热功率
    public String instanFlow; //瞬时流量
    public String leijiFlow; //累计流量
    public String gsTemperature; //供水温度
    public String hsTemperature; //回水温度
    public String realTime; //实时时间

    public HeatValue(){

    }

    /**
     * 设置当前热量
     * @param value
     */
    public void SetNh(String value){
    	nowHeat= value;
    }
    
    /**
     * 设置热功率
     * @param value
     */
    public void SetHp(String value){
    	heatP = value;
    }
    
    /**
     * 设置瞬时流量
     * @param value
     */
    public void SetIf(String value){
    	instanFlow = value;
    }
    
    /**
     * 设置累计流量
     * @param value
     */
    public void SetLf(String value){
    	leijiFlow = value;
    }
    
    /**
     * 设置供水温度
     * @param value
     */
    public void SetGt(String value){
    	gsTemperature = value;
    }
    
    /**
     * 设置回水温度
     * @param value
     */
    public void SetHt(String value){
    	hsTemperature = value;
    }
    
    /**
     * 设置实时时间
     * @param value
     */
    public void SetRt(String value){
    	realTime = value;
    }
    
}
