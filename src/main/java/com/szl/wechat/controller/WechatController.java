package com.szl.wechat.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.szl.wechat.common.Constant;
import com.szl.wechat.common.PropertiesUtils;
import com.szl.wechat.util.CheckUtil;
import com.szl.wechat.util.StringUtils;
import com.szl.wechat.util.WeixinMessageXmlBuilder;
import com.szl.wechat.util.XMLParse;
@RestController
@RequestMapping("/wechat")
public class WechatController {
	
	private static Logger log = LoggerFactory.getLogger(WechatController.class);
	
	@RequestMapping(value = "/hello")
	public String hello(){
		return "hello";
	}
	
	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public String doGet(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		//验证失败，直接返回
		if(!CheckUtil.checkSignature(signature, timestamp, nonce, null)){
			return "";
		}
		String data = getData(request);
		String xmlMsg = "";
		Map<String, String> msgMap = XMLParse.extractToMap(data);
		String msgType = msgMap.get("MsgType");
		if("event".equals(msgType)){//关注回复
			String event = msgMap.get("Event");
			String eventKey = msgMap.get("EventKey");
			if ("subscribe".equals(event)) {
				xmlMsg = doSubscribe(msgMap);
			}
		}else if("text".equals(msgMap.get("MsgType"))){//消息回复
			xmlMsg = keyWordReply(msgMap);
		}
		return xmlMsg;
	}
	
	//获取结收信息
	private String getData(HttpServletRequest request){
		String data = "";
		try {
			data = IOUtils.toString(request.getInputStream());
			log.info("结收到的信息： " + data);
		} catch (Exception e) {
			log.error("解析出错！", e);
		}
		return data;
	}
	
	//处理关注事件
	private String doSubscribe(Map<String, String> msgType){
		String content = PropertiesUtils.getProperty(Constant.Wechat.WECHAT_SUBSCRIBE_CONTENT);
		log.info("自动回复内容为：{} ", content);
		if(StringUtils.isEmpty(content)){
			return "";
		}
		msgType.put("content", content);
		return getXmlData(msgType);
	}
	
	//关键词回复
	private String keyWordReply(Map<String, String> msgMap){
		String xmlMsg = "";
		String content = msgMap.get("Content");
		log.info("用户发送的关键词是：{}", content);
		String replyKeyword = PropertiesUtils.getProperty(Constant.Wechat.WECHAT_REPLY_KEYWORD);
		JSONObject jsonObj = JSONObject.parseObject(replyKeyword);
		if(jsonObj.containsKey(content)){
			String value = jsonObj.getString(content);
			log.info("匹配到关键字：{}, 返回信息：{}", content, value);
			msgMap.put("content", value);
			xmlMsg = getXmlData(msgMap);
		}else{
			String otherKeyWordsReply = PropertiesUtils.getProperty(Constant.Wechat.WECHAT_OTHER_KEYWORDSREPLY);
			log.info("关键词：{},回复：{}", content, otherKeyWordsReply);
			msgMap.put("content", otherKeyWordsReply);
			xmlMsg = getXmlData(msgMap);
		}
		return xmlMsg;
	}
	//组装数据
	private String getXmlData(Map<String, String> msgMap) {
		WeixinMessageXmlBuilder builder = new WeixinMessageXmlBuilder();
		builder.addFromUserName(msgMap.get("ToUserName")).addToUserName(msgMap.get("FromUserName"))
		.addCreateTime(msgMap.get("CreateTime")).addTextMsgType().addContent(msgMap.get("content"));
		return builder.builder();
	}
}
