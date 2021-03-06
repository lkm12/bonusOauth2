package com.fuzamei.bonuspoint.util;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: bonus-point-cloud
 * @description: SubMail 短信服务平台短信工具
 * @author: WangJie
 * @create: 2018-08-31 15:55
 **/
@Slf4j
@Component
public class SubmailSMSUtil {

    @Value("${sms_content}")
    private String content;
    /**
     * 时间戳接口配置
     */
    public static final String TIMESTAMP = "https://api.mysubmail.com/service/timestamp";
    /**
     * 备用接口
     * http://api.mysubmail.com/service/timestamp
     * https://api.submail.cn/service/timestamp
     * http://api.submail.cn/service/timestamp
     */

    public static final String TYPE_MD5 = "md5";
    public static final String TYPE_SHA1 = "sha1";
    /**
     * API 请求接口配置
     */
    private static final String URL = "https://api.mysubmail.com/message/send";

    /**
     * 备用接口
     *
     * @param mobile 手机号
     * @param code   短信验证码
     *               http://api.mysubmail.com/message/send
     *               https://api.submail.cn/message/send
     *               http://api.submail.cn/message/send
     */

    public boolean sendCaptcha(String mobile, String code) {
        TreeMap<String, Object> requestData = new TreeMap<String, Object>();
        /**
         * --------------------------------参数配置------------------------------------
         * 请仔细阅读参数配置说明
         *
         * 配置参数包括 appid, appkey ,to , signtype, 	content
         * 用户参数设置，其中 appid, appkey, content, to 为必须参数
         * 请访问 submail 官网创建并获取 appid 和 appkey，链接：https://www.mysubmail.com/chs/sms/apps
         * 请访问 submail 官网创建获取短信模板内容，链接：https://www.mysubmail.com/chs/sms/templates
         * content 参数
         *   |正文中必须提交有效的短信签名，且您的短信签名必须放在短信的最前端，e.g. 【SUBMAIL】您的短信验证码：4438，请在10分钟内输入。
         *   |content 参数将会与您账户中的短信模板进行匹配，如 API 返回 420 错误，请在您的账户中添加短信模板，并提交审核
         *   |请将 短信正文控制在 350 个字符以内
         * signtype 为可选参数: normal | md5 | sha1
         * 当 signtype 参数为空时，默认为 normal
         * --------------------------------------------------------------------------
         */
        String appid = "20431";
        String appkey = "ce1a9179d5a51ba2691f49a453e71636";

        String signtype = "md5";
        /**
         *  ---------------------------------------------------------------------------
         */


        /**
         *  签名验证方式
         *  详细说明可参考 SUBMAIL 官网，开发文档 → 开始 → API 授权与验证机制
         */
        String message = content.replace("code", code);
        requestData.put("appid", appid);
        requestData.put("content", message);
        requestData.put("to", mobile);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        @SuppressWarnings("deprecation")
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        for (Map.Entry<String, Object> entry : requestData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                builder.addTextBody(key, String.valueOf(value), contentType);
            }
        }
        if (signtype.equals(TYPE_MD5) || signtype.equals(TYPE_SHA1)) {
            String timestamp = getTimestamp();
            requestData.put("timestamp", timestamp);
            requestData.put("sign_type", signtype);
            String signStr = appid + appkey + RequestEncoder.formatRequest(requestData) + appid + appkey;
            builder.addTextBody("timestamp", timestamp);
            builder.addTextBody("sign_type", signtype);
            builder.addTextBody("signature", RequestEncoder.encode(signtype, signStr), contentType);
        } else {
            builder.addTextBody("signature", appkey, contentType);
        }
        /**
         * http post 请求接口
         * 成功返回 status: success,其中 fee 参数为短信费用 ，credits 参数为剩余短信余额
         * 详细的 API 错误日志请访问 SUBMAIL 官网 → 开发文档 → DEBUG → API 错误代码
         */
        HttpPost httpPost = new HttpPost(URL);
        httpPost.addHeader("charset", "UTF-8");
        httpPost.setEntity(builder.build());
        try {
            CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
            HttpResponse response = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                String jsonStr = EntityUtils.toString(httpEntity, "UTF-8");
                log.info("向手机{}发送验证码，返回信息{}", mobile, jsonStr);
                Map<String, Object> map = JSON.parseObject(jsonStr);
                if ("success".equals(map.get("status"))) {
                    return true;
                }
                log.error("向手机号：{}发送验证码失败", mobile);
                return false;

            }
        } catch (ClientProtocolException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public static boolean sendSMS(String mobile, String message) {
        TreeMap<String, Object> requestData = new TreeMap<String, Object>();
        /**
         * --------------------------------参数配置------------------------------------
         * 请仔细阅读参数配置说明
         *
         * 配置参数包括 appid, appkey ,to , signtype, 	content
         * 用户参数设置，其中 appid, appkey, content, to 为必须参数
         * 请访问 submail 官网创建并获取 appid 和 appkey，链接：https://www.mysubmail.com/chs/sms/apps
         * 请访问 submail 官网创建获取短信模板内容，链接：https://www.mysubmail.com/chs/sms/templates
         * content 参数
         *   |正文中必须提交有效的短信签名，且您的短信签名必须放在短信的最前端，e.g. 【SUBMAIL】您的短信验证码：4438，请在10分钟内输入。
         *   |content 参数将会与您账户中的短信模板进行匹配，如 API 返回 420 错误，请在您的账户中添加短信模板，并提交审核
         *   |请将 短信正文控制在 350 个字符以内
         * signtype 为可选参数: normal | md5 | sha1
         * 当 signtype 参数为空时，默认为 normal
         * --------------------------------------------------------------------------
         */
        String appid = "20431";
        String appkey = "ce1a9179d5a51ba2691f49a453e71636";

        String signtype = "md5";
        /**
         *  ---------------------------------------------------------------------------
         */


        /**
         *  签名验证方式
         *  详细说明可参考 SUBMAIL 官网，开发文档 → 开始 → API 授权与验证机制
         */

        requestData.put("appid", appid);
        requestData.put("content", message);
        requestData.put("to", mobile);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        @SuppressWarnings("deprecation")
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        for (Map.Entry<String, Object> entry : requestData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                builder.addTextBody(key, String.valueOf(value), contentType);
            }
        }
        if (signtype.equals(TYPE_MD5) || signtype.equals(TYPE_SHA1)) {
            String timestamp = getTimestamp();
            requestData.put("timestamp", timestamp);
            requestData.put("sign_type", signtype);
            String signStr = appid + appkey + RequestEncoder.formatRequest(requestData) + appid + appkey;
            builder.addTextBody("timestamp", timestamp);
            builder.addTextBody("sign_type", signtype);
            builder.addTextBody("signature", RequestEncoder.encode(signtype, signStr), contentType);
        } else {
            builder.addTextBody("signature", appkey, contentType);
        }
        /**
         * http post 请求接口
         * 成功返回 status: success,其中 fee 参数为短信费用 ，credits 参数为剩余短信余额
         * 详细的 API 错误日志请访问 SUBMAIL 官网 → 开发文档 → DEBUG → API 错误代码
         */
        HttpPost httpPost = new HttpPost(URL);
        httpPost.addHeader("charset", "UTF-8");
        httpPost.setEntity(builder.build());
        try {
            CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
            HttpResponse response = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                String jsonStr = EntityUtils.toString(httpEntity, "UTF-8");
                log.info("向手机{}发送短信，返回信息{}", mobile, jsonStr);
                Map<String, Object> map = JSON.parseObject(jsonStr);
                if ("success".equals(map.get("status"))) {
                    return true;
                }
                log.error("向手机号：{}发送短信失败", mobile);
                return false;

            }
        } catch (ClientProtocolException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    private static String getTimestamp() {
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(TIMESTAMP);
        try {
            HttpResponse response = closeableHttpClient.execute(httpget);
            HttpEntity httpEntity = response.getEntity();
            String jsonStr = EntityUtils.toString(httpEntity, "UTF-8");
            if (jsonStr != null) {
                JSONObject json = JSONObject.fromObject(jsonStr);
                return json.getString("timestamp");
            }
            closeableHttpClient.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
