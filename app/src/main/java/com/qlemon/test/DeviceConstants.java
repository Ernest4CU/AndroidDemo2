package com.qlemon.test;

/**
 * 常用指令项封装
 * @author JeanSit added at 2016-01-12
 */
public class DeviceConstants {

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

	// ========================身份证信息==============================
    /* UART 数据输入传输帧格式:
     * 	Preamble Len1 Len2 CMD Para Data CHK_SUM
     *
     * UART 数据输出传输帧格式:
     * 	Preamble ￼en1 Len2 SW1 SW2 SW3 Data CHK_SUM
     *
     * 1、Preamble:本帧数据的帧头,5 字节,为 0xAA、0xAA、0xAA、0x96、0x69。
     * 2、Len1、Len2:数据帧的有效数据长度,各为 1 字节。Len1 为数据长度高字节;Len2为数据长度低字节。
     *     输入数据长度为:CMD、Para、Data、CHK_SUM 字 段字节数 之和;
     *     输出数据长度为:SW1、SW2、SW3、Data、CHK_SUM 字段字节数之和。
     * 3、CHK_SUM:校验和,1 字节。
     * 数据帧中除帧头和校验和之外 的数据逐字节按位异或的结果。
     *
     * 寻卡命令:AA AA AA 96 69 00 03 20 01 22
     * 返 回 值:AA AA AA 96 69 00 08 00 00 9F 00 00 00 00 97
     *
     * 选卡命令:AA AA AA 96 69 00 03 20 02 21
     * 返 回 值:AA AA AA 96 69 00 0C 00 00 90 00 00 00 00 00 00 00 00 9C
     *
     * 读卡命令:AA AA AA 96 69 00 03 30 01 32
     * 返 回 值:1295 字节数据身份证信息
     * AAAAAA9669050800009001000400(14字节)+(256 字节文字信息)+(1024 字节 照片信息)+(1 字节 CRC)
     *
     */
    public static String FIND_ID_CARD = "AAAAAA96690003200122";

    public static String SELECT_ID_CARD = "AAAAAA96690003200221";

    public static String READ_ID_CARD = "AAAAAA96690003300132";

}
