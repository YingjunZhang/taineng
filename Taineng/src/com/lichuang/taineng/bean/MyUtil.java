package com.lichuang.taineng.bean;

import java.util.LinkedList;
import java.util.List;

import com.lichuang.taineng.sqlite.model.HeatValue;



/**
 * Created by Administrator on 2014/11/6.
 */
public class MyUtil {
	
	public static String rebiao_address="11640958";
	public static String wenkongfa_address = "00000000";
	public static String qibiao_address="";
	public static String shuibiao_address="";
	public static String dianbiao_address="";
    
    public static int realt_space = 3000;//实时采集间隔
    public static int rebiaoModel= 1;//热表控制模式;1:直接挂载热表;2:温控一体化
    public static int if_Rebiao_Exist= 0; //0表示无热表，1表示有热表
    public static int if_Dianbiao_Exist= 0;//0表示无电表，1表示有电表
    public static int if_Qibiao_Exist = 0;//0表示无气表，1表示有气表
    public static int if_Shuibiao_Exist=0;//0表示无水表，1表示有水表
    
    public static String system_Config_File="systemconfig";//系统配置文件名
    public static String rebiao_Config_File="rebiaoconfig"; //热表配置文件名
    
    public static int heat_Caiji_Timespace=5; //热表采集间隔
    public static int heat_Cunchu_Timespace=5; //热表存储间隔
    public static int heat_Caiji_Timespace_Flag= 2; //1:秒	 2:分 	3:时
    public static int heat_Cunchu_Timespace_Flag= 2;
    public static int elec_Caiji_Timespace=5; //电表采集间隔
    public static int elec_Cunchu_Timespace=5; //电表存储间隔
    public static int elec_Caiji_Timespace_Flag= 2;
    public static int elec_Cunchu_Timespace_Flag= 2;
    public static int water_Caiji_Timespace=5; //水表采集间隔
    public static int water_Cunchu_Timespace=5; //水表存储间隔
    public static int water_Caiji_Timespace_Flag= 2;
    public static int water_Cunchu_Timespace_Flag= 2;
    public static int gas_Caiji_Timespace=5; //气表采集间隔
    public static int gas_Cunchu_Timespace=5; //气表存储间隔
    public static int gas_Caiji_Timespace_Flag= 2;
    public static int gas_Cunchu_Timespace_Flag= 2;
    
    //各表更改代码
    public final static int heat_address_chcode = 2011;
    public final static int heat_scheme_chcode = 2012;
    public final static int gas_address_chcode = 2021;
    public final static int gas_shceme_chcode = 2022;
    public final static int water_address_chcode = 2031;
    public final static int water_shceme_chcode = 2032;
    public final static int elec_address_chcode = 2041;
    public final static int elec_scheme_chcode = 2042;
    
    public final static int address_chcode= 2000;
    public final static int scheme_chcode = 2001;
    public final static int read_wenkongfa = 3001;
}
