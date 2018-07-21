package com.szl.wechat.common;

/**
 * 返回信息统一管理
 * @author szl
 * @data 2018年6月30日 下午9:28:27
 *
 */
public enum ResultEnum {
	UNKNOWN_ERROR("-1", "系统错误"),
	SUCCESS("0", "成功"),
	FAIL("1", "失败"),
	;
	
	private String code;
	
	private String msg;
	
	ResultEnum(String code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
	
	
}
