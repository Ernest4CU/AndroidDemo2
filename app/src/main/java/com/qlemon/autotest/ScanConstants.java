package com.qlemon.autotest;

/**
 * 常用指令项封装
 * @author JeanSit added at 2016-01-12
 */
public class ScanConstants {

	// ========================扫描枪==============================
	/**
	 * 串口指令，驱动扫描
	 * 指令格式：
     *      <STX><CMD_ID><CMD_LEN_H><CMD_LEN_L><DATA_BLOCK><ETX><CheckSum>
     * 指令格式项描述：
	 * STX      : Start Character (02h)
     * CMD_ID   :
     *            00h - 7Fh Command with Data
     *            80h - FFh Command without Data
     * CMD_LEN  : DATA_BLOCK LENGTH-1 DATA_BLOCK : DATA
     * ETX      : End Character (03h)
     * CheckSum : XOR from STX to ETX
	 */
	public static String SCAN_START = "02820383"; //82是trigger,83是异或检验码
	public static String SCAN_RSP_OK = "06";      //If OK : Response ACK (06h)
	public static String SCAN_RSP_FAIL = "15";    //If Error : Response NAK (15h)
	public static String SCAN_STOP = "0d0a";      //回车换行

	// ========================锁==============================
	/**
	 * 1.板地址查询0x80：
	 *		命令头 	命令头	 板地址	 状态 		校验码 (异或) 
	 *		0X80 	0X01 	0X00 	0X99 	0X18
	 *  返回: 
		       命令头     	固定      		 从机板地址        		 固定         校验位 
		  0X80      0X01       0X01到0X40      0X99    XXXX 
		  
	 *
	 *2、开锁命令如下0x8A： 
		命令          	板地址        			锁地址     	 状态      		校验码   (异或)
		0X8A    0X01-0XC8     0X01—18    0X11         xx 
	 *
	 *	命令     板地址      锁地址       状态      校验码 
		0X8A     0X01      0X01        0X11      0X9B     (锁为开) 
		0X8A     0X01      0X01        0X00      0X8A     (锁为关) 
	 *
	 *3.读锁状态命令 0X80（门开关状态反馈）
	 *	起始             板地址                      锁地址         命令       校验码   (异或
		0X80  0X01-0XC8  0X00—18 0X33  XX 
	 */
	/**
	 * 开锁或者关锁的头
	 */
	public static String LOCK_OPEN_CLOSE_HEADER = "8a";
	/**
	 * 开锁的状态
	 */
	public static String LOCK_OPEN_STUATS = "11";
	/**
	 * 关锁的状态
	 */
	public static String LOCK_CLOSE_STUATS = "00";
	/**
	 * 读取锁状态的头
	 */
	public static String LOCK_READ_HEADER = "80";
	/**
	 * 读取状态的命令
	 */
	public static String LOCK_READ_STUATS = "33";
	/**
	 * 检验码
	 */
	public static String LOCK_CHECK = "9b";
}
