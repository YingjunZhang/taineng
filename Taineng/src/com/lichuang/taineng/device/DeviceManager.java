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
    private Queue<byte[]> dataList; //数据队列
    private Queue<String> dataTimeList; //数据时间队列
    private Queue<CmdType> typeList; //标志队列;标志队列和数据队列保持同步，以便准确处理数据
    
    private boolean sFlag = true;
    private boolean aFlag= true;
    private static DeviceManager dm;
    private SerialPortOpt serialPort; //声明串口
    private LcRebiao lcRebiao;
    private LcWenkongfa lcWenkong;
    private Context mContext;
    private InputStream inS; //串口输入流
    private OutputStream outS;//串口输出流
    
    
    private DeviceManager(Context context){
        comdList= new LinkedList<CmdType>();
        cmdTimeList = new LinkedList<String>();
        cmdDataList = new LinkedList<byte[]>();
        dataList = new LinkedList<byte[]>();
        dataTimeList = new LinkedList<String>();
        typeList = new LinkedList<CmdType>();
        serialPort = new SerialPortOpt();
        serialPort.mDevNum = 0;//com0
        serialPort.mDataBits= 8 ;//数据位
        serialPort.mSpeed= 9600;// 波特率
        serialPort.mStopBits = 1;//停止位
        serialPort.mParity='n'; //校验位
        serialPort.openDev(serialPort.mDevNum); //打开串口
        serialPort.setSpeed(serialPort.mFd, serialPort.mSpeed);//设置波特率
        serialPort.setParity(serialPort.mFd, serialPort.mDataBits, serialPort.mStopBits, serialPort.mParity);//设置校验
        
        lcRebiao = new LcRebiao();
        lcWenkong = new LcWenkongfa();
        this.mContext= context;
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
    public synchronized void addCmd(CmdType ct){
    	comdList.offer(ct);
    }

    public synchronized void addCmd(CmdType ct ,String time){
    	comdList.offer(ct);        
        cmdTimeList.offer(time);
		
    }
    
    /**
     * 此函数只在写温控阀时使用
     * @param ct
     * @param data
     */
    public synchronized void addCmd(CmdType ct,byte[] data){
    	comdList.offer(ct);        
       
        cmdDataList.offer(data);
    }

    /**
     * 设置发送命令标志位，如果设置为false，发送命令即停止
     * @param b
     */
    public void setSFlag(boolean b){
         sFlag= b;
    }

    /**
     * 设置处理线程运行标志
     * @param b
     */
    public void setAFlag(boolean b){
         aFlag =b;

    }
    
    /**
     * 关闭串口
     */
    public void CloseSerialPort(){
    	if(serialPort.mFd != null){
    		serialPort.closeDev(serialPort.mFd);
    	}
    }
    

    @SuppressWarnings("static-access")
	public void sendComd() throws InterruptedException{
    	 MyLog.LogInfo("taineng", "开始发送命令");
        while(sFlag){
//        	 MyLog.LogInfo("taineng", "发送命令线程正在运行"+comdList.size());
            while(comdList.size()>0){
                CmdType ct = comdList.poll();
                String time = cmdTimeList.poll();
                switch(ct){
                    
                    case 采热表:
                    	
                    	MyLog.LogInfo("taineng send cmd", "采热表");
                    	byte[] cmd2 = lcRebiao.GetDataCommand(MyUtil.rebiao_address);
                    	WriteRebiao(cmd2);      	
                    	try{
                    	Thread.currentThread().sleep(300);
                    	MyLog.LogInfo("taineng send cmd", "开始接收数据");
                    	
                    	FileInputStream mInputStream = new FileInputStream(serialPort.mFd);
                    	byte[] buffer = ReadRebiao(mInputStream);
                    	MyLog.LogInfo("taineng send cmd", "数据接收完毕");
//                    	serialPort.readBytes(serialPort.mFd,buffer,61);
//                    	byte[] data2 = ReadRebiao();	
                    	putDt(buffer,ct,time);
                    	}catch(InterruptedException e){
                    		
                    		throw e;
                    	}
                    	
                        break;
                    
                    case 抄热表:
                    	MyLog.LogInfo("taineng send cmd", "抄热表");
                    	byte[] cmd = lcRebiao.GetDataCommand(MyUtil.rebiao_address);
                    	WriteRebiao(cmd);
                    	try{
                    	Thread.currentThread().sleep(300);
                    	FileInputStream mInputStream = new FileInputStream(serialPort.mFd);
                    	byte[] buffer = ReadRebiao(mInputStream);
                    	
                    	putDt(buffer,ct,time);
                    	}catch(InterruptedException e){
                    		
                    		throw e;
                    	}
                    	break;
                    case 读温控一体化:
                    	MyLog.LogInfo("taineng send cmd", "读温控一体化");
                    	byte[] cmd3 = lcWenkong.getTemperatureControlNuifyCommand(MyUtil.rebiao_address);
                    	WriteRebiao(cmd3);
                    	try{
                    		Thread.currentThread().sleep(300);
                        	FileInputStream mInputStream = new FileInputStream(serialPort.mFd);
                        	byte[] buffer = ReadRebiao(mInputStream);
                        	putDt(buffer,ct,time);
                    	}catch(InterruptedException e){
                    		throw e;
                    	}
                    	
                    	break;
                    case 抄温控一体化:
                    	MyLog.LogInfo("taineng send cmd", "抄温控一体化");
                    	byte[] cmd6 = lcWenkong.getTemperatureControlNuifyCommand(MyUtil.rebiao_address);
                    	WriteRebiao(cmd6);
                    	try{
                    		Thread.currentThread().sleep(300);
                        	FileInputStream mInputStream = new FileInputStream(serialPort.mFd);
                        	byte[] buffer = ReadRebiao(mInputStream);
                        	putDt(buffer,ct,time);
                    	}catch(InterruptedException e){
                    		throw e;
                    	}
                    	
                    	break;
                    case 读温控阀:
                    	MyLog.LogInfo("taineng send cmd", "读温控一体化");
                    	byte[] cmd4 = lcWenkong.getReadDataCommand(MyUtil.rebiao_address);
                    	WriteRebiao(cmd4);
                    	try{
                    		Thread.currentThread().sleep(300);
                        	FileInputStream mInputStream = new FileInputStream(serialPort.mFd);
                        	byte[] buffer = ReadRebiao(mInputStream);
                        	putDt(buffer,ct,time);
                    	}catch(InterruptedException e){
                    		throw e;
                    	}
                    	break;
                    case 写温控阀:
                    	MyLog.LogInfo("taineng send cmd", "写温控阀");
                    	byte[] cmdData = cmdDataList.poll();
                    	byte[] cmd5 = lcWenkong.getWriteDataCommand(MyUtil.rebiao_address, cmdData);
                    	WriteRebiao(cmd5);
                    	try{
                    		Thread.currentThread().sleep(300);
                        	FileInputStream mInputStream = new FileInputStream(serialPort.mFd);
                        	byte[] buffer = ReadRebiao(mInputStream);
                        	putDt(buffer,ct,time);
                    	}catch(InterruptedException e){
                    		throw e;
                    	}
                    	break;
                }
            }
        }
        
        comdList.clear();
    }
    
    private void putDt(byte[] d,CmdType ct,String time){
    	synchronized(dm){
    		typeList.offer(ct);
    		dataList.offer(d);
    		dataTimeList.offer(time);
    	}
    }
    
    private void WriteRebiao(byte[] cmd){
    		serialPort.writeBytes(cmd);	
    	
    }
    
    private byte[] ReadRebiao(InputStream inStream){
    	int bytesRead = 0;
		int bytesToRead= 61;
		
		byte[] data = new byte[bytesToRead];
		try {
			MyLog.LogInfo("taineng device", "可读字节数"+ inStream.available());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(bytesRead < bytesToRead){			
				try {
					int result = inStream.read(data, bytesRead, bytesToRead - bytesRead);
					if(result == -1) break;
					bytesRead += result;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}	
		return data;
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
            		MyLog.LogInfo("taineng analyse", "定时采");
            		byte[] data = dataList.poll();
            		String time = dataTimeList.poll();
    				Map<String,String> result = lcRebiao.GetData(data);
    				if(result != null){
    					HeatValue hv = new HeatValue();
    					hv.SetNh(result.get(LcRebiao.nowHeat));
    					hv.SetHp(result.get(LcRebiao.heatP));
    					hv.SetIf(result.get(LcRebiao.instanFlow));
    					hv.SetLf(result.get(LcRebiao.leijiFlow));
    					hv.SetHt(result.get(LcRebiao.hsTemperature));
    					hv.SetGt(result.get(LcRebiao.gsTemperature));
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
            		byte[] data2 = dataList.poll();
            		String time2 = dataTimeList.poll();
    				Map<String,String> result2 = lcRebiao.GetData(data2);
    				if(result2 != null){
    					HeatValue hv2 = new HeatValue();
    					hv2.SetNh(result2.get(LcRebiao.nowHeat));
    					hv2.SetHp(result2.get(LcRebiao.heatP));
    					hv2.SetIf(result2.get(LcRebiao.instanFlow));
    					hv2.SetLf(result2.get(LcRebiao.leijiFlow));
    					hv2.SetHt(result2.get(LcRebiao.hsTemperature));
    					hv2.SetGt(result2.get(LcRebiao.gsTemperature));
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
            	
            	case 读温控一体化: //实际也是读热表数据
            		byte[] data3 = dataList.poll();
            		String time3 = dataTimeList.poll();
            		Map<String,String> result3 = lcWenkong.analyseTemperatureControlNuifyRespond(data3);
            		if(result3 != null){
            			HeatValue hv = new HeatValue();
            			hv.SetNh(result3.get(LcWenkongfa.cueernt_heat));
            			hv.SetHp(LcWenkongfa.heatP);
            			hv.SetIf(result3.get(LcWenkongfa.instanFlow));
            			hv.SetLf(result3.get(LcWenkongfa.leijiFlow));
            			hv.SetHt(result3.get(LcWenkongfa.hsTemperature));
            			hv.SetGt(result3.get(LcWenkongfa.gsTemperature));
            			hv.SetRt(time3);
            			
            			switch(MyUtil.heat_Cunchu_Timespace_Flag){
            			case 1: //秒
							tempStr = time3.substring(17);
							number = Integer.valueOf(tempStr) % MyUtil.heat_Cunchu_Timespace;
							if( number == 0){
								SqliteManager.getInstance(mContext).addToHeatRealtime(hv);
							}
							break;
						case 2: //分
							tempStr = time3.substring(14, 15);
							number = Integer.valueOf(tempStr) % MyUtil.heat_Cunchu_Timespace;
							if (number == 0){
								SqliteManager.getInstance(mContext).addToHeatRealtime(hv);
							}
							break;
						case 3: //时
							tempStr = time3.substring(11, 12);
							number = Integer.valueOf(tempStr) % MyUtil.heat_Cunchu_Timespace;
							if (number == 0){
								SqliteManager.getInstance(mContext).addToHeatRealtime(hv);
							}
							break;
            			}
            			ContentValues values = new ContentValues();
    					tempStr = result3.get(LcRebiao.nowHeat);
    					values.put("heatValue", tempStr.substring(0,tempStr.length()-3 ));
    					values.put("getDTime",time3);
    					System.out.println("当前热量:"+result3.toString());
    					mContext.getContentResolver().insert(ProviderMeta.hrtUri, values);
            			
            		}
            		break;
            	case 抄温控一体化:
            		byte[] data4 = dataList.poll();
            		String time4 = dataTimeList.poll();
            		Map<String,String> result4 = lcWenkong.analyseTemperatureControlNuifyRespond(data4);
            		if(result4 != null){
            			HeatValue hv = new HeatValue();
            			hv.SetNh(result4.get(LcWenkongfa.cueernt_heat));
            			hv.SetHp(LcWenkongfa.heatP);
            			hv.SetIf(result4.get(LcWenkongfa.instanFlow));
            			hv.SetLf(result4.get(LcWenkongfa.leijiFlow));
            			hv.SetHt(result4.get(LcWenkongfa.hsTemperature));
            			hv.SetGt(result4.get(LcWenkongfa.gsTemperature));
            			hv.SetRt(time4);
            			SqliteManager.getInstance(mContext).addToHeatRealtime(hv);
            			ContentValues values4 = new ContentValues();
    					tempStr = result4.get(LcRebiao.nowHeat);
    					values4.put("heatValue", tempStr.substring(0,tempStr.length()-3));
    					values4.put("getDTime",time4);
    					System.out.println("当前热量:"+result4.toString());
    					mContext.getContentResolver().insert(ProviderMeta.hrcUri, values4);
            		}
            		break;
            	case 读温控阀:
            		byte[] data5 = dataList.poll();
            		
            		Map<String,String> result5 = lcWenkong.analyseReadDataCommandRespond(data5);
            		if (result5 != null){
            			
            		}
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
        读温控一体化,
        抄温控一体化,
        读温控阀,
        写温控阀
    }
}
