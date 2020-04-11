package com.es.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Objects;

/**
 * es RestHighLevelClient 配置
 * es 7.x之后 只支持http 删除type
 */

@Configuration
@Slf4j
public class ElasticSearchConfig {
    private static final int ADDRESS_LENGTH = 2;
    private static final String HTTP_SCHEME = "http";
    public static int CONNECT_TIMEOUT_MILLIS = 1000;
    public static int SOCKET_TIMEOUT_MILLIS = 30000;
    public static int CONNECTION_REQUEST_TIMEOUT_MILLIS = 500;
    public static int MAX_CONN_PER_ROUTE = 10;
    public static int MAX_CONN_TOTAL = 30;

    //权限验证
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    /**
     * 使用冒号隔开ip和端口
     */
    @Value("${elasticsearch.host}")
    private String[] address;

    //@Value("${elasticsearch.username}")
    //private String username;

    //@Value("${elasticsearch.password}")
    //private String password;

    @Bean
    public RestClientBuilder restClientBuilder() {
        HttpHost[] hosts = Arrays.stream(address)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        log.info("hosts:{}", Arrays.toString(hosts));
        //配置权限验证
        //credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        RestClientBuilder restClientBuilder = RestClient.builder(hosts)
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        return restClientBuilder;
    }

    @Bean(name = "highLevelClient")
    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        setConnectTimeOutConfig(restClientBuilder);
        setConnectConfig(restClientBuilder);
        return new RestHighLevelClient(restClientBuilder);
    }

    // 处理请求地址
    private HttpHost makeHttpHost(String s) {
        assert StringUtils.isNotEmpty(s);
        String[] address = s.split(":");
        if (address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, HTTP_SCHEME);
        } else {
            return null;
        }
    }

    // 配置连接时间延时
    public void setConnectTimeOutConfig(RestClientBuilder builder) {
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
            return requestConfigBuilder;
        });
    }

    // 使用异步httpClient时设置并发连接数
    public void setConnectConfig(RestClientBuilder builder) {
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONN_TOTAL);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE);
            return httpClientBuilder;
        });
    }
}
