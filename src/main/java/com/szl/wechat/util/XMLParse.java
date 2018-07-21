/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package com.szl.wechat.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * XMLParse class
 *
 * 提供提取消息格式中的密文及生成回复消息格式的接口.
 */
public class XMLParse {

	@SuppressWarnings("unchecked")
	public static Map<String, String> extractToMap(String xmltext) throws AesException {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Document document = DocumentHelper.parseText(xmltext);
			Element root = document.getRootElement();
			List<Element> elementList = root.elements();
			for (Element e : elementList) {
				map.put(e.getName(), e.getText());
			}
		} catch (Exception e) {
			throw new AesException(AesException.ParseXmlError);
		}

		return map;
	}

	/**
	 * 提取出xml数据包中的加密消息
	 * 
	 * @param xmltext
	 *            待提取的xml字符串
	 * @return 提取出的加密消息字符串
	 * @throws AesException
	 */
	public static Object[] extract(String xmltext) throws AesException {
		Object[] result = new Object[3];
		try {
			Document document = DocumentHelper.parseText(xmltext);

			Element root = document.getRootElement();
			Element nodelist1 = root.element("Encrypt");
			Element nodelist2 = root.element("ToUserName");
			result[0] = 0;
			result[1] = nodelist1.getText();
			result[2] = nodelist2.getText();
			return result;
		} catch (Exception e) {
			throw new AesException(AesException.ParseXmlError);
		}
	}

	/**
	 * 生成xml消息
	 * 
	 * @param encrypt
	 *            加密后的消息密文
	 * @param signature
	 *            安全签名
	 * @param timestamp
	 *            时间戳
	 * @param nonce
	 *            随机字符串
	 * @return 生成的xml字符串
	 */
	public static String generate(String encrypt, String signature, String timestamp, String nonce) {

		String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
				+ "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n" + "<TimeStamp>%3$s</TimeStamp>\n"
				+ "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
		return String.format(format, encrypt, signature, timestamp, nonce);

	}
}
