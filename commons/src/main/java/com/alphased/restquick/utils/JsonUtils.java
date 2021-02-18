package com.alphased.restquick.utils;

import com.alphased.restquick.exception.JsonUtilsException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class JsonUtils {

    public static JsonNode jsonNodeCreator(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readTree(json);
    }

    public static <T> String sanitize(String json, Class<T> valueType) throws Exception {
        Assert.notNull(json, "json cannot be null.");
        Assert.notNull(valueType, "ValueType cannot be null.");
        return serialize(deserialize(json, valueType));
    }

    public static String mergedObjectSerialize(Pair<String, Object>... keyValues) throws Exception {
        HashMap<String, Object> mergedObject = new HashMap<>();
        Arrays.asList(keyValues).forEach(pair -> {
            Assert.notNull(pair.getKey(), "Key cannot be null.");
            Assert.notNull(pair.getValue(), "Value cannot be null.");
            mergedObject.put(pair.getKey(), pair.getValue());
        });
        return serialize(mergedObject);
    }

    public static String mergedObjectSerialize(Object... values) throws Exception {
        if (values.length > 1) {
            HashMap<String, Object> mergedObject = new HashMap<>();
            Arrays.asList(values).forEach(object -> {
                Assert.notNull(object, "Value cannot be null.");
                mergedObject.put(StringUtils.uncapitalize(object.getClass().getSimpleName()), object);
            });
            return serialize(mergedObject);
        } else {
            return serialize(values[0]);
        }
    }

    public static String serialize(Object value) throws Exception {
        Assert.notNull(value, "value cannot be null.");
        ObjectMapper mapper = new ObjectMapper();
        mapper.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String json = null;
        try {
            json = mapper.writeValueAsString(value);
        } catch (IOException e) {
            throw new JsonUtilsException(e.getMessage());
        }
        return json;
    }

    public static <T> T deserialize(String json, Class<T> valueType) throws Exception {
        Assert.notNull(json, "json cannot be null.");
        Assert.notNull(valueType, "ValueType cannot be null.");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        T object = null;
        try {
            object = mapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new JsonUtilsException(e.getMessage());
        }
        return object;
    }

    public static <T> T deserialize(String json, TypeReference<T> valueType) throws Exception {
        Assert.notNull(json, "json cannot be null.");
        Assert.notNull(valueType, "ValueType cannot be null.");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T object = null;
        try {
            object = mapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new JsonUtilsException(e.getMessage());
        }
        return object;
    }

    public static class HTMLCharacterEscapes extends CharacterEscapes {

        private final int[] asciiEscapes;

        public HTMLCharacterEscapes() {
            asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
            asciiEscapes['<'] = CharacterEscapes.ESCAPE_STANDARD;
            asciiEscapes['>'] = CharacterEscapes.ESCAPE_STANDARD;
            asciiEscapes['&'] = CharacterEscapes.ESCAPE_STANDARD;
            asciiEscapes['\''] = CharacterEscapes.ESCAPE_STANDARD;
            asciiEscapes['/'] = CharacterEscapes.ESCAPE_STANDARD;
            asciiEscapes['+'] = CharacterEscapes.ESCAPE_STANDARD;
        }

        @Override
        public int[] getEscapeCodesForAscii() {
            return asciiEscapes;
        }

        @Override
        public SerializableString getEscapeSequence(int ch) {
            return null;
        }
    }

}
