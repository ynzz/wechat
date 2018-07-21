package com.szl.wechat.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 微信回复XML消息构建工具
 * 
 * @author dengjianjun
 *
 */
public class WeixinMessageXmlBuilder {

	private Document document;
	private Element root;

	public WeixinMessageXmlBuilder() {
		document = DocumentHelper.createDocument();
		root = document.addElement("xml");
	}

	public WeixinMessageXmlBuilder addToUserName(String toUser) {
		Element ele = root.addElement("ToUserName");
		ele.addCDATA(toUser);
		return this;
	}

	public WeixinMessageXmlBuilder addFromUserName(String fromUser) {
		Element ele = root.addElement("FromUserName");
		ele.addCDATA(fromUser);
		return this;
	}

	public WeixinMessageXmlBuilder addCreateTime(String createTime) {
		Element ele = root.addElement("CreateTime");
		ele.addText(createTime);
		return this;
	}

	public WeixinMessageXmlBuilder addNowCreateTime() {
		return addCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
	}

	public WeixinMessageXmlBuilder addMsgType(String msgType) {
		Element ele = root.addElement("MsgType");
		ele.addCDATA(msgType);
		return this;
	}

	public WeixinMessageXmlBuilder addNewsMsgType() {
		return addMsgType("news");
	}

	public WeixinMessageXmlBuilder addImageMsgType() {
		return addMsgType("image");
	}

	public WeixinMessageXmlBuilder addTextMsgType() {
		return addMsgType("text");
	}

	public WeixinMessageXmlBuilder addArticleCount(String articleCount) {
		Element ele = root.addElement("ArticleCount");
		ele.addText(articleCount);
		return this;
	}

	public WeixinMessageXmlBuilder addArticleCount(int articleCount) {
		return addArticleCount(String.valueOf(articleCount));
	}

	public WeixinMessageXmlBuilder addArticleItem(String title, String description, String picUrl, String url) {
		Element articles = root.element("Articles");
		if (articles == null) {
			articles = root.addElement("Articles");
		}

		Element item = articles.addElement("item");

		if (title == null) {
			title = "";
		}
		if (description == null) {
			description = "";
		}

		Element eleTitle = item.addElement("Title");
		eleTitle.addCDATA(title.replaceAll("<([^>]*)>", ""));

		Element eleDescription = item.addElement("Description");
		eleDescription.addCDATA(description.replaceAll("<([^>]*)>", ""));

		Element elePicUrl = item.addElement("PicUrl");
		elePicUrl.addCDATA(picUrl);

		Element eleUrl = item.addElement("Url");
		eleUrl.addCDATA(url);

		return this;
	}

	public WeixinMessageXmlBuilder addContent(String content) {
		Element ele = root.addElement("Content");
		ele.addCDATA(content);
		return this;
	}

	public String builder() {
		return document.asXML();
	}

}
