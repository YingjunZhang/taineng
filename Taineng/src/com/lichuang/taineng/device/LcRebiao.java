package com.lichuang.taineng.device;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lichuang.taineng.bean.MyLog;

/**
 * 力创热表驱动
 * Created by holy on 2014/11/6.
 */
public class LcRebiao {
    public static String lastHeat="上次抄表热量";
    public static String nowHeat="当前热量";
    public static String heatP="热功率";
    public static String instanFlow="瞬时流量";
    public static String leijiFlow = "累计流量";
    public static String gsTemperature= "供水温度";
    public static String hsTemperature="回水温度";
    public static String leijiTime = "累计工作时间";
    public static String realTime = "实时时间";


    public LcRebiao(){

    }

    /**
     * 得到读数据命令
     * @return
     */
    public byte[] GetDataCommand(String address){
    	
        byte send[] = new byte[18];
        send[0]=(byte) 254;//0xfe;
        send[1]=(byte) 254;//0xfe;
        send[2]=(byte) 104;//0x68;
        send[3]=(byte) 32;//0x20;
        send[4]=(byte) (Integer.parseInt(address.substring(6), 16));//0x78;
        send[5]=(byte) (Integer.parseInt(address.substring(4, 6), 16));//0x56;
        send[6]=(byte) (Integer.parseInt(address.substring(2, 4), 16));//0x34;
        send[7]=(byte) (Integer.parseInt(address.substring(0, 2), 16));//0x12;
        send[8]=(byte) 0;//0x00;
        send[9]=(byte) 89;//0x59;
        send[10]=(byte) 66;//0x42;
        send[11]=(byte) 1;//0x01;
        send[12]=(byte) 3;//0x03;
        send[13]=(byte) 144;//0x90;
        send[14]=(byte) 31;//0x1f;
        send[15]=(byte) 0;//0x00;
        int num = 0;
        for (int i =2;i<16;i++){
            num = num+(int)send[i];
        }
        num = num%256;
        send[16]=(byte) num;
        send[17]=(byte) 22;//0x16
        
        return send;
    }

    public Map<String,String> GetData(byte[] data){
        Map<String,String> result= new HashMap<String,String>(); //结果以map的方式返回
        StringBuilder sb = new StringBuilder();
        List<String> tempList = new ArrayList<String>(); //将byte数据都转换成16进制字符串并暂存在list里面
        String address=""; //表地址
        String tempStr="";
        String tempStr2 = "";
        int tempValue=0;
        DecimalFormat df = new DecimalFormat("#.00");
        DecimalFormat dfThree = new DecimalFormat("#.000");
        for (int i =2;i< data.length;i++ ){
            tempStr = Integer.toHexString(data[i]&0xff);
            if (tempStr.length() < 2){
                tempStr= "0"+tempStr ;
            }
            tempStr2 = tempStr2 + tempStr;
            tempList.add(tempStr);
        }
        MyLog.LogInfo("taineng rebiao", tempStr2);

        if(tempList.get(0).equals("68") && tempList.get(1).equals("20") ){ //&& tempList.get(tempList.size()-1).equals("16")
            int num = 0;
            for (int i=2;i< data.length -2;i++){
                num = num+(int)(data[i]); //16进制字符串转成十进制并进行累加

            }
            num = num%256;
            tempStr = Integer.toHexString(num);
            if (tempStr.length()<2){
                tempStr= "0"+tempStr ;
            }
//            System.out.println(tempStr);
            MyLog.LogInfo("taineng rebiao", tempStr);
//            if (tempList.get(tempList.size()-2).equals(tempStr) ){ //验证校验和是否正确
                //截取地址
                for(int i=6;i>1;i--){
                    address =address+tempList.get(i);
                }

                //读取上次抄表热量
                tempStr="";
                for(int i =15;i<19;i++){
                    tempStr = tempList.get(i)+tempStr;
                }
                tempValue=Integer.valueOf(tempStr);
                result.put(lastHeat, df.format(tempValue/100.0));

                //读取当前热量
                tempStr="";
                for(int i =20;i<24;i++){
                    tempStr = tempList.get(i)+tempStr;
                }
                tempValue=Integer.valueOf(tempStr);
                if(tempValue==0){
                	result.put(nowHeat, "0");
                }else{
                	result.put(nowHeat, df.format(tempValue/100.0));
                }
                

                //读取热功率
                tempStr="";
                for(int i =25;i<29;i++){
                    tempStr = tempList.get(i)+tempStr;
                }
                tempValue=Integer.valueOf(tempStr);
                result.put(heatP, df.format(tempValue/100.0));

                //读取瞬时流量
                tempStr="";
                for(int i =30;i<34;i++){
                    tempStr = tempList.get(i)+tempStr;
                }
                tempValue=Integer.valueOf(tempStr);
                result.put(instanFlow, dfThree.format(tempValue/10000.0));

                //读取累计流量
                tempStr="";
                for(int i =35;i<39;i++){
                    tempStr = tempList.get(i)+tempStr;
                }
                tempValue=Integer.valueOf(tempStr);
                result.put(leijiFlow, df.format(tempValue/100.0));

                //读取供水温度
                tempStr="";
                for(int i =39;i<42;i++){
                    tempStr = tempList.get(i)+tempStr;
                }
                tempValue=Integer.valueOf(tempStr);
                result.put(gsTemperature, df.format(tempValue/100.0));

                //读取回水温度
                tempStr="";
                for(int i =42;i<45;i++){
                    tempStr = tempList.get(i)+tempStr;
                }
                tempValue=Integer.valueOf(tempStr);
                result.put(hsTemperature, df.format(tempValue/100.0));

                //读取累计工作时间
                tempStr="";
                for(int i =45;i<48;i++){
                    tempStr = tempList.get(i)+tempStr;
                }

                tempValue = Integer.valueOf(tempStr);
                result.put(leijiTime, String.valueOf(tempValue));

                //读取实时时间
                tempStr="";
                for(int i =48;i<55;i++){
                    tempStr = tempList.get(i)+tempStr;
                }

                result.put(realTime, tempStr);

                return result;
//            }else{
//                return null;
//            }

        }
        else{
            return null;
        }


    }

    //根据字符串得到Int数
    private int GetIntData(String str){
        int index = 0;
        String tempStr = "";
        int result=0;
        for(int i=0;i<str.length();i++){
            char c = str.charAt(i);
            if (c != '0'){
                index = i;
                break;
            }
        }
        tempStr = str.substring(index);
        result= Integer.valueOf(tempStr);
        return result;
    }

}
