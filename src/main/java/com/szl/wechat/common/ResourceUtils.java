package com.szl.wechat.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author szl
 * @data 2018年7月21日 下午6:59:00
 *
 */
@Configuration
@ConfigurationProperties(prefix="wechat")
public class ResourceUtils {

	private String appid;
	private String secret;
	private String token;
	private String aesKey;
	private String apiurl;
	private String subscribeContent;
	private String replyKeyword;
	private String otherKeyWordsReply;
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAesKey() {
		return aesKey;
	}
	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}
	public String getApiurl() {
		return apiurl;
	}
	public void setApiurl(String apiurl) {
		this.apiurl = apiurl;
	}
	public String getSubscribeContent() {
		return subscribeContent;
	}
	public void setSubscribeContent(String subscribeContent) {
		this.subscribeContent = subscribeContent;
	}
	public String getReplyKeyword() {
		return replyKeyword;
	}
	public void setReplyKeyword(String replyKeyword) {
		this.replyKeyword = replyKeyword;
	}
	public String getOtherKeyWordsReply() {
		return otherKeyWordsReply;
	}
	public void setOtherKeyWordsReply(String otherKeyWordsReply) {
		this.otherKeyWordsReply = otherKeyWordsReply;
	}
	
}
