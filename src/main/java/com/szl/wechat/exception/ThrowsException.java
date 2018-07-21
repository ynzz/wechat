package com.szl.wechat.exception;

import com.szl.wechat.common.ResultEnum;

/**
 * 自定义异常
 * @author szl
 * @data 2018年6月30日 下午9:16:13
 *
 */
public class ThrowsException extends RuntimeException{

	private String code;
	
	public ThrowsException(ResultEnum resultEnum){
		super(resultEnum.getMsg());
		this.code = resultEnum.getCode();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
