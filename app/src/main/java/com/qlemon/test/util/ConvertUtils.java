package com.qlemon.test.util;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串工具
 * @author JeanSit added at 2016-01-12
 */
public class ConvertUtils {

	/**
	 * byte数组转为对应的16进制字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * byte数组转为对应的16进制字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src, int length) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || length <= 0) {
			return null;
		}
		for (int i = 0; i < length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 十六进制编码字符串转为对应的二进制数组
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] hexStringToBytes(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {

				baKeyword[i] = (byte) (Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return baKeyword;
	}

    /**
     * 十六进制字符串转换成字节数组
     * @param hex 十六进制字符串
     * @param reverse 值true是从尾开始读取字符串，值false是从头开始读取字符串
     * @return 字节数组
     */
    public static byte[] hexStringToByte(String hex, boolean reverse) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<hex.length(); i++) {
            sb.append(StringUtils.leftPad(Integer.toBinaryString(Integer.parseInt(hex.substring(i, i+1), 16)), 4, '0'));
        }
        if (reverse) {
            return sb.reverse().toString().getBytes();
        }
        return sb.toString().getBytes();
    }

	/**
	 * 十六进制转ascii
	 * 
	 * @param hex
	 * @return
	 */
	public static String convertHexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);
		}

		return sb.toString();
	}

	/**
	 * 10进制字符串 转为16进制字符串
	 * @param s
	 * @return
	 */
	public static String convertDecToHexString(String s) {
		String str = Integer.toHexString(Integer.parseInt(s));
		if(str.length()%2==1){
			str = "0"+str;
		}
		return str;
	}
	
	/**
	 * 通过做异或运算,求出校验码
	 * @param cmd
	 * @return
	 */
	public static String xor(String cmd)
	{
		if(cmd.length()%2!=0){
			cmd = "0"+cmd;
		}
		int result = 0;
		for (int i = 0; i < cmd.length()-1; i=i+2) {
			//System.out.println(cmd.substring(i,i+2));
			result ^= Integer.valueOf(cmd.substring(i,i+2),16);
			System.out.println("16-->"+ Integer.valueOf(cmd.substring(i,i+2),16));
			System.out.println("result:"+result);
		}
		return Integer.toHexString(result);
	}
	
	/**
	 * 以"-"拆分字符串
	 * @param str
	 * @return
	 */
	public static String[] splitString(String str){
		return str.split("-");
	}
	
	public static String takeCity(String str){
		String nstr = null;
		if(str!=null){
			nstr=str.substring(0, str.length()-1);
		}
		return nstr;
	}
	
	/**
	 * 时间戳转为日期
	 * @param datestr
	 * @return
	 */
	public static String getSimpDate(String datestr){
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		String date = sdf.format(new Date(Long.parseLong( datestr) ));
		return date;
	}
	/**
	 * 时间戳转为日期
	 * @param smdateint
	 * @return
	 */
	public static String getSimpDate(long smdateint){
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		String date = sdf.format(new Date(smdateint));
		System.out.println(date);
		return date;
	}

	public static boolean isBlank(String text) {
		if (text == null || "".equals(text.trim())) {
			return true;
		}
		return false;
	}
}
