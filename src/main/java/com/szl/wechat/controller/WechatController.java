package com.szl.wechat.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.szl.wechat.util.WeixinMessageXmlBuilder;
import com.szl.wechat.util.XMLParse;
@RestController
@RequestMapping("/wechat")
public class WechatController {

	@RequestMapping(value = "/callback")
	public String hello(){
		return "~";
	}
	
	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public String doGet(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		System.out.println("zhixing");
//		if(!CheckUtil.checkSignature(signature, timestamp, nonce, null)){
//			return "";
//		}
		String data = IOUtils.toString(request.getInputStream());
		System.out.println(data);
		
		String xmlMsg = "";
		Map<String, String> msgMap = XMLParse.extractToMap(data);
		String msgType = msgMap.get("MsgType");
		if("event".equals(msgType)){
			String event = msgMap.get("Event");
			String eventKey = msgMap.get("EventKey");
			//关注回复
			if ("subscribe".equals(event)) {
				
			}
		}else if("text".equals(msgMap.get("MsgType"))){//消息回复
			String content = msgMap.get("Content");
			String value = "您发送的消息是：" + content;
			msgMap.put("content", value);
			xmlMsg = getXmlData(msgMap);
			return xmlMsg;
		}
		return xmlMsg;
	}
	
	private String getXmlData(Map<String, String> msgMap) {
		WeixinMessageXmlBuilder builder = new WeixinMessageXmlBuilder();
		builder.addFromUserName(msgMap.get("ToUserName")).addToUserName(msgMap.get("FromUserName"))
		.addCreateTime(msgMap.get("CreateTime")).addTextMsgType().addContent(msgMap.get("content"));
		return builder.builder();
	}
}
