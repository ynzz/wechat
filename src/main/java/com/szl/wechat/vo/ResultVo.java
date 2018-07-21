package com.szl.wechat.vo;
import com.szl.wechat.common.ResultEnum;

/**
 * 接口返回类
 * @author szl
 * @data 2018年6月30日 下午8:07:49
 *
 */
public class ResultVo {

	/** 状态码*/
	private String code;
	/** 提示信息*/
	private String msg;
	/** 返回数据*/
	private Object data;
	
	public static ResultVo get(String code, String msg){
		ResultVo r = new ResultVo();
		r.setCode(code);
		r.setMsg(msg);
		return r;
	}
	
	public static ResultVo get(ResultEnum resultEnum){
		ResultVo r = new ResultVo();
		r.setCode(resultEnum.getCode());
		r.setMsg(resultEnum.getMsg());
		return r;
	}
	
	public static ResultVo getData(ResultEnum resultEnum, Object data){
		ResultVo r = new ResultVo();
		r.setCode(resultEnum.getCode());
		r.setMsg(resultEnum.getMsg());
		r.setData(data);
		return r;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
