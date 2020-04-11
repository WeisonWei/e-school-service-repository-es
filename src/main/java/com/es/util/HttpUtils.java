package com.es.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    public static String getCliectIp() {
        return getCliectIp(getHttpRequest());
    }

    public static String getCliectIp(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }
        return ip;
    }

    public static HttpServletRequest getHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes)requestAttributes).getRequest();
    }

    public static HttpServletResponse getHttpResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes)requestAttributes).getResponse();
    }


    /**
     * post请求，发送json数据
     *
     * @param url
     * @param json
     * @return
     */
    public static String doPost(String url, String json) {
        String result = null;
        HttpPost post = new HttpPost(url);
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(50000).build();// requestConfig - post请求配置类，设置超时时间
            post.setConfig(requestConfig);
            StringEntity s = new StringEntity(json, "UTF-8");// 中文乱码在此解决
            s.setContentType("application/json");
            post.setEntity(s);
            HttpResponse req = HttpClients.createDefault().execute(post);
            if (req.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(req.getEntity(), "UTF-8");// 返回json格式：
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * post请求，发送map数据
     *
     * @param url
     * @param map
     * @return
     */
    public static String doPost(String url, Map<String, Object> map) {
        String result = null;
        HttpPost post = new HttpPost(url);
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(50000).build();// requestConfig - post请求配置类，设置超时时间
            post.setConfig(requestConfig);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!StringUtils.isEmpty(entry.getValue())) {
                    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue() + ""));// 用basicNameValuePair来封装数据
                }
            }
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));// 中文乱码在此解决
            HttpResponse req = HttpClients.createDefault().execute(post);
            if (req.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(req.getEntity(), "UTF-8");// 取出应答字符串
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
