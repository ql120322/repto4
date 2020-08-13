package com.lianfu.lf_scancode_accounts.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeixinOauth2Token {
    public static JSONObject getOauth2AccessToken(String appId, String appSecret, String code) {
        WeixinOauth2Token wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        requestUrl = requestUrl.replace("CODE", code);
        JSONObject demoJson = null;
        // 获取网页授权凭证
        try {
            URL urlGet = new URL(requestUrl);

            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();

            http.setRequestMethod("GET"); // 必须是get方式请求

            http.setRequestProperty("Content-Type",

                    "application/x-www-form-urlencoded");

            http.setDoOutput(true);

            http.setDoInput(true);

            http.connect();

            InputStream is = http.getInputStream();

            int size = is.available();

            byte[] jsonBytes = new byte[size];

            is.read(jsonBytes);

            String message = new String(jsonBytes, "UTF-8");
            demoJson = JSON.parseObject(message);

//	                wat = new WeixinOauth2Token();
//	                wat.setAccessToken(jsonObject.getString("access_token"));
//	                wat.setExpiresIn(jsonObject.getInt("expires_in"));
//	                wat.setRefreshToken(jsonObject.getString("refresh_token"));
//	                wat.setOpenId(jsonObject.getString("openid"));
//	                wat.setScope(jsonObject.getString("scope"));
        } catch (Exception e) {
            wat = null;
//	                int errorCode = jsonObject.getInt("errcode");
//	                String errorMsg = jsonObject.getString("errmsg");
            //log.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
        }

        return demoJson;
    }

    /**
     * URL编码（utf-8）
     *
     * @param source
     * @return
     */
    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
