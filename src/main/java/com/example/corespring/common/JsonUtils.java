package com.example.corespring.common;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;


public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // Bỏ qua field không match
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Convert object thành một instance của class cụ thể
     */
    public static <T> T convertObject(Object source, Class<T> targetClass) {
        return mapper.convertValue(source, targetClass);
    }

    /**
     * Convert object (List<LinkedHashMap>) thành List<T>
     */
    public static <T> List<T> convertList(Object source, Class<T> targetClass) {
        return mapper.convertValue(
                source,
                mapper.getTypeFactory().constructCollectionType(List.class, targetClass)
        );
    }

    /**
     * Convert object thành Map
     */
    public static <K, V> Map<K, V> convertMap(Object source, Class<K> keyClass, Class<V> valueClass) {
        return mapper.convertValue(
                source,
                mapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass)
        );
    }

    /**
     * Chuyển object → JSON string
     */
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi chuyển object sang JSON", e);
        }
    }

    /**
     * Parse JSON string thành object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi parse JSON", e);
        }
    }

    /**
     * Parse JSON string thành List<T> hoặc Map<K, V> bằng TypeReference
     */
    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(json, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi parse JSON với TypeReference", e);
        }
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

}
