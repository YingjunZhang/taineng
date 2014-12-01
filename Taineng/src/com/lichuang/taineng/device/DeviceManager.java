package com.lichuang.taineng.device;

import android.content.ContentValues;
import android.content.Context;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.dwin.navy.serialportapi.SerialPortOpt;
import com.lichuang.taineng.bean.MyLog;
import com.lichuang.taineng.bean.MyUtil;
import com.lichuang.taineng.contentprovider.ProviderMeta;
import com.lichuang.taineng.sqlite.SqliteManager;
import com.lichuang.taineng.sqlite.model.HeatValue;

/**
 * Created by Holy on 2014/11/6.
 */
public class DeviceManager {
    private Queue<CmdType> comdList; //命令队列
    private Queue<String> cmdTimeList; //采集时间队列
    private Queue<byte[]> cmdDataList; //采集数据队列
    private Queue<Map<String,String>> dataList; //数据队列

    private Queue<CmdType> typeList; //标志队列;标志队列和数据队列保持同步，以便准确处理数据
    
    private boolean sFlag = true;
    private boolean aFlag= true;
    private static DeviceManager dm;
   
    private Context mContext;
    private ComZeroManager comZeroManager;
    
    
    private DeviceManager(Context context){
        comdList= new LinkedList<CmdType>();
        cmdTimeList = new LinkedList<String>();
        cmdDataList = new LinkedList<byte[]>();
        dataList = new LinkedList<Map<String,String>>();
       
        typeList = new LinkedList<CmdType>();
        
        this.mContext= context;
        comZeroManager = new ComZeroManager(context);
        comZeroManager.InitDevice();
    }

    /**
     * 获得设备管理类唯一实例
     *
     * @return
     */
    public synchronized static DeviceManager getInstance(Context context){
        if (dm == null) {
            dm = new DeviceManager(context);
        }
        return dm;
    }
    
    /**
     * 此函数只在读取温控阀时使用
     * @param ct
     */
    public void addCmd(CmdType ct){
    	synchronized(comdList){
    		comdList.offer(ct);
    	}
    	
    }

    public void addCmd(CmdType ct ,String time){
    	synchronized(comdList){
    		comdList.offer(ct);        
            cmdTimeList.offer(time);
    	}
    	
		
    }
    
    /**
     * 此函数只在写温控阀时使用
     * @param ct
     * @param data
     */
    public void addCmd(CmdType ct,byte[] data){
    	synchronized(comdList){
    		comdList.offer(ct);        
    	       
            cmdDataList.offer(data);
    	}
    	
    }

    /**
     * 设置发送命令标志位，如果设置为false，发送命令即停止
     * @param b
     */
    public void SetSFlag(boolean b){
         sFlag= b;
    }
    
    public void SetAFlag(boolean b){
    	aFlag=b;
    }

    /*
     * 
     * 关闭串口 
     */
    public void CloseSerialPort(){
    	comZeroManager.ColoseComdev();
    }
    

    
	public void sendComd() throws InterruptedException{
    	 MyLog.LogInfo("taineng", "开始发送命令");
        while(sFlag){
//        	 MyLog.LogInfo("taineng", "发送命令线程正在运行"+comdList.size());
            while(comdList.size()>0){
                CmdType ct = comdList.poll();
               
                switch(ct){
                    
                    case 采热表:
                    	 String time = cmdTimeList.poll();
                    	 
                    	 comZeroManager.SendReadDataCommand();
                    	 Map<String,String> result = comZeroManager.MultiHeatResult();
                    	 result.put(LcRebiao.realTime, time);
                    	 dataList.add(result);
                    	 typeList.add(ct);
                        break;
                    
                    case 抄热表:
                    	 String time2 = cmdTimeList.poll();
                    	 
                    	 comZeroManager.SendReadDataCommand();
                    	 Map<String,String> result2 = comZeroManager.MultiHeatResult();
                    	 result2.put(LcRebiao.realTime, time2); 
                    	 dataList.add(result2);
                    	 typeList.add(ct);
                    	break;
                  
                    case 读温控阀:
                    
                    	break;
                    case 写温控阀:
                    	
                    	break;
                }
            }
        }
        
        comdList.clear();
    }
    
    
	 //处理收到的Byte数组
    public void AnalyseData(){
          while(aFlag){
        	  
            while (dataList.size()>0){
            	CmdType ct = typeList.poll();
            	String tempStr = "";
            	int number = 0;
            	switch(ct){
            	case 采热表: //
            		MyLog.LogInfo("taineng analyse", "采热表");
            		Map<String,String> result = dataList.poll();
    				if(result != null){
    					HeatValue hv = new HeatValue();
    					hv.SetNh(result.get(LcRebiao.nowHeat));
    					hv.SetHp(result.get(LcRebiao.heatP));
    					hv.SetIf(result.get(LcRebiao.instanFlow));
    					hv.SetLf(result.get(LcRebiao.leijiFlow));
    					hv.SetHt(result.get(LcRebiao.hsTemperature));
    					hv.SetGt(result.get(LcRebiao.gsTemperature));
    					String time = result.get(LcRebiao.realTime);
    					hv.SetRt(time);
    					switch(MyUtil.heat_Cunchu_Timespace_Flag){
    						case 1: //秒
    							tempStr = time.substring(17);
    							number = Integer.valueOf(tempStr) % MyUtil.heat_Cunchu_Timespace;
    							if( number == 0){
    								SqliteManager.getInstance(mContext).addToHeatRealtime(hv);
    							}
    							break;
    						case 2: //分
    							tempStr = time.substring(14, 15);
    							number = Integer.valueOf(tempStr) % MyUtil.heat_Cunchu_Timespace;
    							if (number == 0){
    								SqliteManager.getInstance(mContext).addToHeatRealtime(hv);
    							}
    							break;
    						case 3: //时
    							tempStr = time.substring(11, 12);
    							number = Integer.valueOf(tempStr) % MyUtil.heat_Cunchu_Timespace;
    							if (number == 0){
    								SqliteManager.getInstance(mContext).addToHeatRealtime(hv);
    							}
    							break;
    					}
    					ContentValues values = new ContentValues();
    					tempStr = result.get(LcRebiao.nowHeat);
    					values.put("heatValue", tempStr.substring(0,tempStr.length()-3 ));
    					values.put("getDTime",time);
    					System.out.println("当前热量:"+result.toString());
    					mContext.getContentResolver().insert(ProviderMeta.hrtUri, values);
    					
    				}
            		break;
            	
            	case 抄热表:
            		Map<String,String> result2 = dataList.poll();		
    				if(result2 != null){
    					HeatValue hv2 = new HeatValue();
    					hv2.SetNh(result2.get(LcRebiao.nowHeat));
    					hv2.SetHp(result2.get(LcRebiao.heatP));
    					hv2.SetIf(result2.get(LcRebiao.instanFlow));
    					hv2.SetLf(result2.get(LcRebiao.leijiFlow));
    					hv2.SetHt(result2.get(LcRebiao.hsTemperature));
    					hv2.SetGt(result2.get(LcRebiao.gsTemperature));
    					String time2 = result2.get(LcRebiao.realTime);
    					hv2.SetRt(time2);
    					SqliteManager.getInstance(mContext).addToHeatChaobiao(hv2);
    					ContentValues values2 = new ContentValues();
    					tempStr = result2.get(LcRebiao.nowHeat);
    					values2.put("heatValue", tempStr.substring(0,tempStr.length()-3));
    					values2.put("getDTime",time2);
    					System.out.println("当前热量:"+result2.toString());
    					mContext.getContentResolver().insert(ProviderMeta.hrcUri, values2);
    				}
            		break;
            	
            	
            	case 读温控阀:
            		
            		break;
            	case 写温控阀:
            		break;
            		
            	
            	}
            	
            }
          }
        dataList.clear();
        typeList.clear();
    }

    
  


    //命令类型枚举
    public static enum CmdType{
        采热表,
        采气表,
        抄热表,
        抄气表,
        读温控阀,
        写温控阀
    }
}
