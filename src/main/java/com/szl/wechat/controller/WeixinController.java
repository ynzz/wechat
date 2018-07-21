//package com.szl.wechat.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * @author szl
// * @data 2018年7月21日 下午7:08:17
// *
// */
//public class WeixinController {
//
//		private Logger logger = LoggerFactory.getLogger(WeixinController.class);
//
//		@Autowired
//		private ProductMainItf productMainItf;
//		@Autowired
//		private SubscriptionFansItf subscriptionFansItf;
//		@Autowired
//		private WechatAPIItf wechatAPIItf;
//		@Autowired
//		private WechatPushMsgItf wechatPushMsgItf;
//
//		@RequestMapping("/callback")
//		public String callback(HttpServletRequest request) {
//			
//			// add by chenrm 2018年4月19日, 上午11:02:30 将微信的回调转发到微擎服务器上 后续计划迁移掉下面的功能到微擎
//			String we7Resp = sendToWe7Server(request);
//			logger.info(we7Resp);
//			
//			String echostr = request.getParameter("echostr");
//			String timestamp = request.getParameter("timestamp");
//			String nonce = request.getParameter("nonce");
//			String encrypt_type = request.getParameter("encrypt_type");
//
//			String appId = PropertyUtils.get(Constant.WeixinConfKey.APPID);
//			String token = PropertyUtils.get(Constant.WeixinConfKey.TOKEN);
//			String encodingAesKey = PropertyUtils.get(Constant.WeixinConfKey.AESKEY);
//
//			WXBizMsgCrypt weixinCrypt = new WXBizMsgCrypt(token, encodingAesKey, appId);
//
//			boolean isEncrypt = true;
//			if (StringUtils.isBlank(encrypt_type) || "raw".equals(encrypt_type)) {
//				isEncrypt = false;
//			}
//
//			String data = null;
//			if (isEncrypt) {// 需走加解密流程
//				String msgSignature = request.getParameter("msg_signature");
//				logger.info(
//						"weixin param: echostr={}, encrypt_type={}, timestamp={}, nonce={}, signature={}, encrypt_type={}",
//						echostr, encrypt_type, timestamp, nonce, msgSignature, encrypt_type);
//
//				if (StringUtils.isNotBlank(echostr)) {
//					return weixinCrypt.verifyUrl(msgSignature, timestamp, nonce, echostr, true);
//				}
//
//				String encryptMsg = getPostData(request);
//				logger.info("解密前消息内容>>" + encryptMsg);
//				data = weixinCrypt.decryptMsg(msgSignature, timestamp, nonce, encryptMsg);
//			} else {// 不用加密
//				String signature = request.getParameter("signature");
//				logger.info(
//						"weixin param: echostr={}, encrypt_type={}, timestamp={}, nonce={}, signature={}, encrypt_type={}",
//						echostr, encrypt_type, timestamp, nonce, signature, encrypt_type);
//
//				if (StringUtils.isNotBlank(echostr)) {
//					return weixinCrypt.verifyUrl(signature, timestamp, nonce, echostr, false);
//				} else if (!weixinCrypt.verifySign(signature, timestamp, nonce)) {
//					// 验签不通过直接返回
//					return "";
//				}
//
//				data = getPostData(request);
//			}
//
//			logger.info("消息内容>>" + data);
//			Map<String, String> msgMap = XMLParse.extractToMap(data);
//
//			String xmlmsg = ""; // 回复的xml数据
//
//			String MsgType = msgMap.get("MsgType");
//			if ("event".equals(MsgType)) { // 事件消息
//				String Event = msgMap.get("Event");
//				String EventKey = msgMap.get("EventKey");
//
//				// 处理粉丝数据 by liujunliang
//				if ("subscribe".equals(Event) || "unsubscribe".equals(Event)) {
//					doFans(msgMap);
//				}
//
//				// 处理参数二维码 by dengjianjun
//				if (("subscribe".equals(Event) && StringUtils.isNotBlank(EventKey) && EventKey.startsWith("qrscene_"))
//						|| "SCAN".equals(Event)) {
//					xmlmsg = doScan(msgMap);
//				}
//
//				// 处理关注事件
//				if (StringUtils.isBlank(xmlmsg) && "subscribe".equals(Event)) {
//					//weixin.useroperation.enable值为1,关注回复功能调用运营平台
//					if (!Constant.UserCenter.ENABLED.equals(PropertyUtils.get(Constant.WeixinConfKey.WEIXIN_USEROPERATION_ENABLED))) {
//						xmlmsg = doSubscribe(msgMap);
//					}
//				}
//
//			} else if ("text".equals(MsgType)) { // 文本消息
//				//weixin.useroperation.enable值为1,自动回复功能调用运营平台
//				if (Constant.UserCenter.ENABLED.equals(PropertyUtils.get(Constant.WeixinConfKey.WEIXIN_USEROPERATION_ENABLED))) {
//					return "";
//				}
//				logger.info("接收到文本消息：{}", msgMap.toString());
//				String content = msgMap.get("Content");
//				String json = PropertyUtils.get(Constant.WeixinConfKey.REPLY_KEYWORD);
//				JSONObject jsonObj = JSONObject.parseObject(json);
//				if(jsonObj.containsKey(content)) {
//					String value = jsonObj.getString(content);
//					logger.info("匹配到关键字：{}, 返回信息：{}", content, value);
//					msgMap.put("content", value);
//					return getXmlData(msgMap);
//				} else {
//					// 其它关键词回复
//					String otherKeyWordsReply = PropertyUtils.get(Constant.WeixinConfKey.REPLY_KEYWORD_OTHER);
//					logger.info("otherKeyWordsReply={}, inputText={}", otherKeyWordsReply, content);
//					msgMap.put("content", otherKeyWordsReply);
//					return getXmlData(msgMap);
//				}
//			}
//
//			if (isEncrypt && StringUtils.isNotBlank(xmlmsg)) { // 需走加解密流程
//				return weixinCrypt.encryptMsg(xmlmsg, timestamp, nonce);
//			}
//
//			return xmlmsg;
//		}
//
//		private String sendToWe7Server(HttpServletRequest request) {
//			String we7response = "";
//			try {
//				String we7url = PropertyUtils.get(Constant.WeixinConfKey.WE7_SERVER_URL);
//				if (StringUtil.isEmpty(we7url)) {
//					//为空则不转发request到微擎服务器
//					return we7response;
//				}
//				BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
//				we7response = HttpUtil.sendPost(we7url, requestWrapper.getInputStream());
//			} catch (Exception e) {
//				logger.error("redirect whole request bytes to we7 server fail!", e);
//			}
//			return we7response;
//		}
//
//		/**
//		 * 处理粉丝数据
//		 * 
//		 * @param msgMap
//		 */
//		private void doFans(Map<String, String> msgMap) {
//			String Event = msgMap.get("Event");
//			if ("subscribe".equals(Event)) {
//				String openid = msgMap.get("FromUserName");
//				Long createTime = msgMap.get("CreateTime") == null ? null : Long.valueOf(msgMap.get("CreateTime"));
//				JSONObject user = JSONObject.parseObject(wechatAPIItf.userinfo(openid));
//				String unionid = user.getString("unionid");
//
//				SubscriptionFans param = new SubscriptionFans();
//				param.setUnionid(unionid);
//				//param.setOpenid(openid);
//				List<SubscriptionFans> fansList = subscriptionFansItf.selectList(param);
//				if (fansList.size() > 0) {
//					subscriptionFansItf.deleteSelective(param);
//				}
//				
//				SubscriptionFans entity = new SubscriptionFans();
//				entity.setUuid(StringUtil.getUUID());
//				entity.setOpenid(openid);
//				entity.setUnionid(unionid);
//				entity.setCreateTime(createTime);
//				subscriptionFansItf.insert(entity);
//
//				List<WechatPushMsg> msgList = wechatPushMsgItf.selectByUnionid(unionid);
//				for (WechatPushMsg msg : msgList) {
//					msg.setState(0);
//					msg.setPushTime(createTime + 600);
//					wechatPushMsgItf.updateSelectiveById(msg);
//				}
//			} else if ("unsubscribe".equals(Event)) {
//				SubscriptionFans entity = new SubscriptionFans();
//				String openid = msgMap.get("FromUserName");
//
//				entity.setOpenid(openid);
//				JSONObject user = JSONObject.parseObject(wechatAPIItf.userinfo(openid));
//				entity.setUnionid(user.getString("unionid"));
//				subscriptionFansItf.deleteSelective(entity);
//			}
//		}
//
//		/**
//		 * 处理二维码扫描事件
//		 * 
//		 * @param msgMap
//		 * @return
//		 */
//		private String doScan(Map<String, String> msgMap) {
//			String Event = msgMap.get("Event");
//			String EventKey = msgMap.get("EventKey");
//
//			if (StringUtils.isNotBlank(EventKey)) {
//				String scene_id = null;
//				if ("subscribe".equals(Event) && EventKey.startsWith("qrscene_")) {
//					scene_id = EventKey.substring("qrscene_".length());
//				} else if ("SCAN".equals(Event)) {
//					scene_id = EventKey;
//				}
//
//				logger.info("扫描参数二维码>>" + scene_id);
//
//				// 跳转wap的url
//				String wapUrl = PropertyUtils.get("wap.address") + scene_id;
//				ProductModel model = productMainItf.getByUuid(scene_id, ProductTerminal.PHONE.getValue());
//
//				String title = "【" + model.getProductMain().getProductName() + "】";
//				
//				String picUrl = model.getProductImage().getBigImageUrl();
//				if (StringUtils.isBlank(picUrl)) {
//					picUrl = model.getProductImage().getCenterImageUrl();
//				}
//				if (StringUtils.isBlank(picUrl)) {
//					picUrl = model.getProductImage().getMainImageUrl();
//				}
//				
//				String description = model.getProductMain().getAdviceNote();
//
//				// 组装回复消息，把商品信息返回给用户
//				WeixinMessageXmlBuilder builder = new WeixinMessageXmlBuilder();
//				builder.addFromUserName(msgMap.get("ToUserName")).addToUserName(msgMap.get("FromUserName"))
//						.addCreateTime(msgMap.get("CreateTime")).addNewsMsgType().addArticleCount("1")
//						.addArticleItem(title, description, picUrl, wapUrl);
//				return builder.builder();
//			}
//
//			return "";
//		}
//
//		/**
//		 * 关注事件回复
//		 * 
//		 * @param msgMap
//		 * @return
//		 */
//		private String doSubscribe(Map<String, String> msgMap) {
//			String content = PropertyUtils.get(Constant.WeixinConfKey.SUBSCRIBE_MSG_REPLY);
//			if (StringUtils.isBlank(content)) {
//				return "";
//			}
//
//			// 组装文本回复消息
//			WeixinMessageXmlBuilder builder = new WeixinMessageXmlBuilder();
//			builder.addFromUserName(msgMap.get("ToUserName")).addToUserName(msgMap.get("FromUserName"))
//					.addCreateTime(msgMap.get("CreateTime")).addTextMsgType().addContent(content);
//			return builder.builder();
//		}
//
//		private String getPostData(HttpServletRequest request) {
//			try {
//				return IOUtils.toString(request.getInputStream());
//			} catch (Exception e) {
//				logger.error("微信公众号>>获取请求体异常", e);
//				return "";
//			}
//		}
//
//		@RequestMapping("/initData")
//		public Object initData(String appid) throws Exception {
//			if (!PropertyUtils.get(Constant.WeixinConfKey.APPID).equals(appid)) {
//				return ResultVo.get(ResultVo.FAIL);
//			}
//			subscriptionFansItf.initData();
//
//			return ResultVo.get(ResultVo.SUCCESS);
//		}
//
//		private String getXmlData(Map<String, String> msgMap) {
//			WeixinMessageXmlBuilder builder = new WeixinMessageXmlBuilder();
//			builder.addFromUserName(msgMap.get("ToUserName")).addToUserName(msgMap.get("FromUserName"))
//			.addCreateTime(msgMap.get("CreateTime")).addTextMsgType().addContent(msgMap.get("content"));
//			return builder.builder();
//		}
//	}
//
//}
