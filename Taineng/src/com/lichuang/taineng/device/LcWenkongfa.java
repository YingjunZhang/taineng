package com.lichuang.taineng.device;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LcWenkongfa {

	// 温控一体化系统数据
		public static String panal_data = "面板数据";
		public static String cueernt_heat = "当前热量";
		public static String heatP = "热功率";
		public static String instanFlow = "瞬时流量";
		public static String leijiFlow = "累计流量";
		public static String gsTemperature = "供水温度";
		public static String hsTemperature = "回水温度";
		public static String leijiTime = "累计工作时间";
		public static String realTime = "实时时间";
		// 读数据命令，采集器与温控阀数据交互
		public static String panal_display_heat = "面板显示热量";
		public static String temperature_indoor = "室内温度";
		public static String temperature_setted = "设定温度";
		public static String valve_opening = "阀门开度";
		public static String set_temperature_limit = "设定温度限值";
		public static String temperature_correct_limit = "温度修正限值";
		public static String valve_up_limit="阀门开度上限";
		public static String valve_down_limit="阀门开度下限";

		/**
		 * 获取读温控一体化系统数据的命令
		 * 
		 * @Description:
		 * 
		 * @date 2014年11月7日
		 * @return
		 */
		public byte[] getTemperatureControlNuifyCommand(String address) {
			byte[] dataCommand = new byte[18];
			dataCommand[0] = (byte) 254;// 0xfe
			dataCommand[1] = (byte) 254;// 0xfe
			dataCommand[2] = (byte) 104;// 0x68
			dataCommand[3] = (byte) 81;// 0x51

			// 获取地址---高位在前 低位在后
			dataCommand[4]=(byte) (Integer.parseInt(address.substring(6), 16));//0x78;
	        dataCommand[5]=(byte) (Integer.parseInt(address.substring(4, 6), 16));//0x56;
	        dataCommand[6]=(byte) (Integer.parseInt(address.substring(2, 4), 16));//0x34;
	        dataCommand[7]=(byte) (Integer.parseInt(address.substring(0, 2), 16));//0x12;
			dataCommand[8]= (byte) 0;
			dataCommand[9] = (byte) 89;// 0x59;
			dataCommand[10] = (byte) 66;// 0x42;
			dataCommand[11] = (byte) 1;// 0x01
			dataCommand[12] = (byte) 3;// 0x03
			dataCommand[13] = (byte) 144;// 0x90
			dataCommand[14] = (byte) 31;// 0x1f
			dataCommand[15] = (byte) 0;// 0x00
			int num = 0;
			for (int i = 2; i < dataCommand.length - 2; i++) {
				num = num + (int) dataCommand[i];
			}
			num = num % 256;
			dataCommand[16] = (byte) num;

			dataCommand[17] = (byte) 22;// 0x16

			return dataCommand;
		}

		/**
		 * 解析温控一体化系统数据的响应
		 * 
		 * @Description:
		 * 
		 * @date 2014年11月7日
		 * @param data
		 * @return
		 */
		public Map<String, String> analyseTemperatureControlNuifyRespond(byte[] data) {
			String tempStr = "";

			ArrayList<String> dataList = new ArrayList<String>();
			for (int i = 2; i < data.length; i++) {
				tempStr = Integer.toHexString(data[i] & 0xff);

				if (tempStr.length() < 2) {
					tempStr = "0" + tempStr;
				}
				dataList.add(tempStr);
			}

			if ("68".equals(dataList.get(0)) && "51".equals(dataList.get(1)) && "16".equals(dataList.get(dataList.size() - 1))) {
				int sum = 0;
				for (int i = 2; i < data.length - 2; i++) {
					sum = sum + (int) (data[i]);
				}
				tempStr = Integer.toHexString(sum % 256 );

				if (tempStr.length() < 2) {
					tempStr = "0" + tempStr;
				}

				Map<String, String> result = new HashMap<String, String>();
				DecimalFormat df_2 = new DecimalFormat("#.00");
				DecimalFormat df_3 = new DecimalFormat("#.000");
				if (tempStr.equals(dataList.get(dataList.size() - 2))) {

					// 截取地址
					String address = "";
					for (int i = 6; i > 1; i--) {
						address = address + dataList.get(i);
					}
					System.out.println("address:" + address);

					// 面板数据
					tempStr = "";
					for (int i = 14; i < 19; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(panal_data, tempStr);

					// 当前热量
					tempStr = "";
					for (int i = 20; i < 24; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(cueernt_heat, df_2.format(Integer.parseInt(tempStr) / 100.0));

					// 热功率
					tempStr = "";
					for (int i = 25; i < 29; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(heatP, df_2.format(Integer.parseInt(tempStr) / 100.0) );

					// 瞬时流量
					tempStr = "";
					for (int i = 30; i < 34; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(instanFlow, df_3.format(Integer.parseInt(tempStr) / 10000.0) );

					// 累计流量
					tempStr = "";
					for (int i = 35; i < 39; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(leijiFlow, df_2.format(Integer.parseInt(tempStr) / 100.0) );

					// 供水温度
					tempStr = "";
					for (int i = 39; i < 42; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(gsTemperature, df_2.format(Integer.parseInt(tempStr) / 100.0) );

					// 回水温度
					tempStr = "";
					for (int i = 42; i < 45; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(hsTemperature, df_2.format(Integer.parseInt(tempStr) / 100.0) );

					// 累计工作时间
					tempStr = "";
					for (int i = 45; i < 48; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(leijiTime, tempStr);

					// 实时时间
					tempStr = "";
					for (int i = 48; i < 55; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(realTime, tempStr);

					return result;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		/**
		 * 获取读取数据命令
		 * 
		 * @Description:
		 * @author 秦昌建
		 * @date 2014年11月7日
		 * @return
		 */
		public byte[] getReadDataCommand(String address) {
			byte[] dataCommand = new byte[18];
			dataCommand[0] = (byte) 254;// 0xfe
			dataCommand[1] = (byte) 254;// 0xfe
			dataCommand[2] = (byte) 104;// 0x68
			dataCommand[3] = (byte) 80;// 0x50

			// 获取地址---高位在前 低位在后
			dataCommand[4]=(byte) (Integer.parseInt(address.substring(6), 16));//0x78;
	        dataCommand[5]=(byte) (Integer.parseInt(address.substring(4, 6), 16));//0x56;
	        dataCommand[6]=(byte) (Integer.parseInt(address.substring(2, 4), 16));//0x34;
	        dataCommand[7]=(byte) (Integer.parseInt(address.substring(0, 2), 16));//0x12;
	        dataCommand[8] = (byte)0; //00
			dataCommand[9] = (byte) 89;// 0x59;
			dataCommand[10] = (byte) 66;// 0x42;
			dataCommand[11] = (byte) 1;// 0x01
			dataCommand[12] = (byte) 3;// 0x03
			dataCommand[13] = (byte) 144;// 0x90
			dataCommand[14] = (byte) 31;// 0x1f
			dataCommand[15] = (byte) 0;// 0x00
			int num = 0;
			for (int i = 2; i < dataCommand.length - 2; i++) {
				num = num + (int) dataCommand[i];
			}
			num = num % 256;
			dataCommand[16] = (byte) num;

			dataCommand[17] = (byte) 22;// 0x16

			return dataCommand;
		}

		/**
		 * 解析读数据命令响应
		 * 
		 * @Description:
		 * @author 秦昌建
		 * @date 2014年11月7日
		 * @param data
		 * @return
		 */
		public Map<String, String> analyseReadDataCommandRespond(byte[] data) {
			String tempStr = "";

			ArrayList<String> dataList = new ArrayList<String>();
			for (int i = 2; i < data.length; i++) {
				tempStr = Integer.toHexString(data[i] & 0xff);

				if (tempStr.length() < 2) {
					tempStr = "0" + tempStr;
				}
				dataList.add(tempStr);
			}

			if ("68".equals(dataList.get(0)) && "50".equals(dataList.get(1)) && "16".equals(dataList.get(dataList.size() - 1))) {
				int sum = 0;
				for (int i = 2; i < data.length - 2; i++) {
					sum = sum + (int) (data[i]);
				}
				tempStr = Integer.toHexString((sum % 256));

				if (tempStr.length() < 2) {
					tempStr = "0" + tempStr;
				}

				Map<String, String> result = new HashMap<String, String>();
				DecimalFormat df_2 = new DecimalFormat("#.00");
				if (tempStr.equals(dataList.get(dataList.size() - 2))) {

					// 截取地址
					String address = "";
					for (int i = 6; i > 1; i--) {
						address = address + dataList.get(i);
					}
					System.out.println("address:" + address);

					// 面板显示热量
					tempStr = "";
					for (int i = 14; i < 18; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					tempStr=df_2.format(Integer.parseInt(tempStr) / 100.0);
					result.put(panal_display_heat, tempStr);

					// 室内温度
					tempStr = "";
					for (int i = 18; i < 21; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(temperature_indoor, df_2.format(Integer.parseInt(tempStr) / 100.0) );

					// 设定温度
					tempStr = "";
					for (int i = 21; i < 24; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(temperature_setted, df_2.format(Integer.parseInt(tempStr) / 100.0) );

					// 阀门开度
					tempStr = dataList.get(24);
					result.put(valve_opening, tempStr);

					// 设定温度限值
					tempStr = "";
					for (int i = 25; i < 28; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(set_temperature_limit, df_2.format(Integer.parseInt(tempStr) / 100.0) );

					// 温度修正限值
					tempStr = "";
					for (int i = 28; i < 31; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(temperature_correct_limit, df_2.format(Integer.parseInt(tempStr) / 100.0) );

					// 实时时间
					tempStr = "";
					for (int i = 33; i < 40; i++) {
						tempStr = dataList.get(i) + tempStr;
					}
					result.put(realTime, tempStr);

					//阀门开度上限
					tempStr="";
					tempStr = dataList.get(42);
					result.put(valve_up_limit,tempStr);

					//阀门开度下限
					tempStr="";
					tempStr=dataList.get(43);
					result.put(valve_down_limit,tempStr);

					return result;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		/**
		 * 获取写数据命令
		 * 
		 * @Description:
		 * @author 秦昌建
		 * @date 2014年11月8日
		 * @param address
		 * @param data
		 * @return
		 */
		public byte[] getWriteDataCommand(String address, byte[] data) {
			int data_length = data.length;
			System.out.println("data长度："+data_length);
			
			byte[] dataCommand = new byte[18 + data_length];
			dataCommand[0] = (byte) 254;// 0xfe
			dataCommand[1] = (byte) 254;// 0xfe
			dataCommand[2] = (byte) 104;// 0x68
			dataCommand[3] = (byte) 80;// 0x50

			// 获取地址---高位在前 低位在后--begin
			dataCommand[4]=(byte) (Integer.parseInt(address.substring(6), 16));//0x78;
			dataCommand[5]=(byte) (Integer.parseInt(address.substring(4, 6), 16));//0x56;
			dataCommand[6]=(byte) (Integer.parseInt(address.substring(2, 4), 16));//0x34;
			dataCommand[7]=(byte) (Integer.parseInt(address.substring(0, 2), 16));//0x12;
			dataCommand[8] = (byte)0; //00
			dataCommand[9] = (byte) 89;// 0x59;
			dataCommand[10] = (byte) 66;// 0x42;
			// --end
			dataCommand[11] = (byte) 4;// 0x04
			dataCommand[12] = (byte) (3 + data_length);// 0x0X//X=3+data_length
			dataCommand[13] = (byte) 241;// 0xf1
			dataCommand[14] = (byte) 9;// 0x09
			dataCommand[15] = (byte) 0;// 0x00
			// --获取数据--begin
			int j=0;
			for(int i=data_length-1;i>=0;i--){
				dataCommand[16+j]=(byte)data[i];
				j++;
			}
			// --end
			int num = 0;
			for (int i = 2; i < dataCommand.length - 2; i++) {
				num = num + (int) dataCommand[i];
			}
			num = num % 256;
			dataCommand[16 + data_length] = (byte) num;

			dataCommand[17 + data_length] = (byte) 22;// 0x16

			return dataCommand;
		}

		/**
		 * 解析写数据命令响应，判断响应是否正确
		 * 
		 * @Description:
		 * @author 秦昌建
		 * @date 2014年11月8日
		 * @param data
		 * @return
		 */
		public boolean analyseWriteDataRespond(byte[] data) {
			String tempStr = "";

			ArrayList<String> dataList = new ArrayList<String>();
			for (int i = 2; i < data.length; i++) {
				tempStr = Integer.toHexString(data[i] & 0xff);

				if (tempStr.length() < 2) {
					tempStr = "0" + tempStr;
				}
				dataList.add(tempStr);
			}

			if ("68".equals(dataList.get(0)) && "50".equals(dataList.get(2)) && "16".equals(dataList.get(dataList.size() - 1))) {
				int sum = 0;
				for (int i = 2; i < data.length - 2; i++) {
					sum = sum + (int) (data[i]);
				}
				tempStr = Integer.toHexString((sum % 256) & 0xff);

				if (tempStr.length() < 2) {
					tempStr = "0" + tempStr;
				}

				if (tempStr.equals(dataList.get(dataList.size() - 2))) {
					// 截取地址
					String address = "";
					for (int i = 6; i > 1; i--) {
						address = address + dataList.get(i);
					}
					System.out.println("address:" + address);

					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	
}
