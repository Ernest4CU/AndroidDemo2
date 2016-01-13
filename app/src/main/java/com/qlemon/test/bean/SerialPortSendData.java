package com.qlemon.test.bean;

import java.io.Serializable;

/**
 * 窗口发送数据
 * @author JeanSit added at 2016-01-12
 */
public class SerialPortSendData implements Serializable {
	/**
	 * 设备的地址
	 */
	public String path;
	/**
	 * 波特率
	 */
	public int baudRate;
	/**
	 * 指令
	 */
	public String commandStr;
	/**
	 * 成功的标志
	 */
	public String okStr;
	/**
	 * 失败的标志
	 */
	public String failStr;
	/**
	 * stop
	 */
	public String stopStr;
	/**
	 * stop
	 */
	public String stopStr1 = "__#_#__";
	/**
	 * 独处数据时候的时候，按照长度读取，不需要其他的方式的时候，使用这个，比如读取身份证信息
	 */
	public boolean isOnlyLenght = false;
	/**
	 * 读取的数据的长度  和isOnlyLenght 配合使用
	 */
	public int readByteCount = 8;
	/**
	 * 是否需要成功和失败的标志
	 */
	public boolean isFlag = true;
	/**
	 * 这个是用于判断 判断过内部的读取用的
	 */
	public boolean isReadData = false;

	public int digitNum = 0; //输出流返回的位数
	public int special = -1;
	public enum Signal {
        OPEN_LOCK, LOCK_STATUS, ALL_LOCK_STATUS, SCAN, FIND_ID_CARD, SELECT_ID_CARD, READ_ID_CARD
    }

	/**
	 * 构造函数
	 */
	public SerialPortSendData(String path, int baudRate, String commandStr,
							  String okStr, String failStr, String stopStr, boolean isFlag) {
		this.path = path;
		this.baudRate = baudRate;
		this.commandStr = commandStr;
		this.okStr = okStr;
		this.failStr = failStr;
		this.stopStr = stopStr;
		this.isFlag = isFlag;
	}
	
	/**
	 * 构造函数
	 */
	public SerialPortSendData(String path, int baudRate, String commandStr, int readByteCount) {
		this.path = path;
		this.baudRate = baudRate;
		this.commandStr = commandStr;
		this.readByteCount = readByteCount;
		this.isOnlyLenght = true;
	}
}