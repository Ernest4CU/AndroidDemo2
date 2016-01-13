package com.qlemon.test.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 种族的工具类
 * @author JeanSit added at 2016-01-12
 */
public class NationUtils {

	/**
	 * 存储名族的集合
	 */
	private static Map<String,String> nationMap = null;//new HashMap<String,String>();
	
	/**
	 * 根据id获取名族的名称
	 * @param id
	 * @return
	 */
	public static String getNationNameById(String id){
		if(nationMap==null){
			setNations();
		}
		return nationMap.get(id);
	}
	
	/**
	 * 设置名族的集合
	 */
	public static void setNations(){
		nationMap = new HashMap<String,String>();
		nationMap.put("01","汉"); 
		nationMap.put("02","蒙古"); 
		nationMap.put("03","回"); 
		nationMap.put("04","藏"); 
		nationMap.put("05","维吾尔"); 
		nationMap.put("06","苗"); 
		nationMap.put("07","彝"); 
		nationMap.put("08","壮"); 
		nationMap.put("09","布依"); 
		nationMap.put("10","朝鲜"); 
		nationMap.put("11","满"); 
		nationMap.put("12","侗"); 
		nationMap.put("13","瑶"); 
		nationMap.put("14","白"); 
		nationMap.put("15","土家");
		nationMap.put("16","哈尼");
		nationMap.put("17","哈萨克");
		nationMap.put("18","傣"); 
		nationMap.put("19","黎"); 
		nationMap.put("20","傈僳");
		nationMap.put("21","佤"); 
		nationMap.put("22","畲"); 
		nationMap.put("23","高山");
		nationMap.put("24","拉祜");
		nationMap.put("25","水"); 
		nationMap.put("26","东乡");
		nationMap.put("27","纳西");
		nationMap.put("28","景颇");
		nationMap.put("29","柯尔克孜"); 
		nationMap.put("30","土"); 
		nationMap.put("31","达斡尔"); 
		nationMap.put("32","仫佬"); 
		nationMap.put("33","羌"); 
		nationMap.put("34","布朗"); 
		nationMap.put("35","撒拉"); 
		nationMap.put("36","毛南"); 
		nationMap.put("37","仡佬"); 
		nationMap.put("38","锡伯"); 
		nationMap.put("39","阿昌"); 
		nationMap.put("40","普米"); 
		nationMap.put("41","塔吉克"); 
		nationMap.put("42","怒"); 
		nationMap.put("43","乌孜别克"); 
		nationMap.put("44","俄罗斯");
		nationMap.put("45","鄂温克");
		nationMap.put("46","德昂");
		nationMap.put("47","保安");
		nationMap.put("48","裕固");
		nationMap.put("49","京"); 
		nationMap.put("50","塔塔尔");
		nationMap.put("51","独龙");
		nationMap.put("52","鄂伦春");
		nationMap.put("53","赫哲");
		nationMap.put("54","门巴");
		nationMap.put("55","珞巴");
		nationMap.put("56","基诺");
		nationMap.put("97","其他");   
		nationMap.put("98","外国血统中国籍人士");
	}
}
