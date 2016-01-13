package com.qlemon.test.bean;

import java.io.Serializable;

/**
 * 格口封装类
 * @author JeanSit added by 2016-01-12
 */
public class BoxBean implements Serializable {

	public BoxBean() {

	}

    public BoxBean(String board_no, String physic_no) {
        this.board_no = board_no;
        this.physic_no = physic_no;
    }

	/**
	 * 格口ID
	 */
	public String box_id;
	/**
	 * 逻辑编号
	 */
	public String logic_no;
	/**
	 * 物理编号
	 */
	public String physic_no;
	/**
	 * 板卡编号
	 */
	public String board_no;
	/**
	 * 类型 Control控制柜，Storage储物柜
	 */
	public String type;
	/**
	 * 状态   occupy占用，free空闲，pre_occupy预占，unavailable不可用
	 */
	public String status;

    @Override
    public String toString() {
        return "BoxBean{" +
                "box_id='" + box_id + '\'' +
                ", logic_no='" + logic_no + '\'' +
                ", physic_no='" + physic_no + '\'' +
                ", board_no='" + board_no + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
