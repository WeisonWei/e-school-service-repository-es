package com.es.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUtil {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    public static String safeToJson(Object object) {
        try {
            return toJson(object);
        } catch (IOException ignore) {
            throw new RuntimeException(ignore);
        }
    }

    public static <T> T read(InputStream in, Class<T> type) throws IOException {
        return objectMapper.readValue(in, type);
    }

    public static <T> T read(String json, Class<T> type) throws IOException {
        return objectMapper.readValue(json.getBytes(Charset.defaultCharset()), type);
    }

    public static <T> T safeRead(String json, Class<T> type) {
        try {
            return read(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> safeReadList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T safeReadByJavaType(String json, Type type) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructType(type);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }


    public static Map<String, Object> toMap(String jsondata) {
        JSONObject obj = JSON.parseObject(jsondata);
        Map<String, Object> data = new HashMap<>();
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            data.put(entry.getKey(), entry.getValue());
        }
        return data;
    }

}
