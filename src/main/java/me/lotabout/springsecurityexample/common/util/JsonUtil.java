package me.lotabout.springsecurityexample.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Reader;

@Slf4j
public class JsonUtil {

    private static final ObjectMapper OM = new ObjectMapper().findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /**
     * Convert an object to JSON string
     *
     * @param object the object to be converted
     * @return JSON string, or null if any error happens
     */
    public static String toJson(Object object) {
        try {
            return OM.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("error on serialize", e);
            return null;
        }
    }

    /**
     * Convert JSON string to {@code type}
     *
     * Note that if the type is List or Map, please check {@code fromJson} with TypeReference
     *
     * @param json json string
     * @param type the type to convert the json to
     * @param <T> the type to convert the json to
     * @return The object converted from json string, or null if any error happens.
     */
    public static <T> T fromJson(@NonNull String json, Class<T> type) {
        try {
            return OM.readValue(json, type);
        } catch (IOException e) {
            log.error("error on deserialize", e);
            return null;
        }
    }

    /**
     * It would be a bit trivial to convert JSON to List/Map of objects.
     *
     * For example:
     *
     * <pre>
     * List<SimpleClass> = JsonUtil.fromJson(json, List.class);
     * </pre>
     *
     * won't work because Jackson don't know what the exact type to convert to. You should however:
     *
     * <pre>
     * List<SimpleClass> simpleClass = JsonUtil.fromJson(
     *         simpleListJSON,
     *         new TypeReference<List<SimpleClass>>() {});
     * </pre>
     *
     * By giving TypeReference, JsonUtil know how to convert the types.
     *
     * @param json json string
     * @param type the type to convert the json to
     * @param <T> the type to convert the json to
     * @return The object converted from json string, or null if any error happens.
     */
    public static <T> T fromJson(@NonNull String json, TypeReference type) {
        try {
            return OM.readValue(json, type);
        } catch (IOException e) {
            log.error("error on deserialize", e);
            return null;
        }
    }


    public static <T> T fromJson(@NonNull Reader json, Class<T> type) {
        try {
            return OM.readValue(json, type);
        } catch (IOException e) {
            log.error("error on deserialize", e);
            return null;
        }
    }

    public static <T> T fromJson(@NonNull Reader json, TypeReference type) {
        try {
            return OM.readValue(json, type);
        } catch (IOException e) {
            log.error("error on deserialize", e);
            return null;
        }
    }

    public static ObjectMapper getObjectMapper() {
        return OM;
    }

    /**
     * Convert json to Jackson Tree Model
     *
     * @param json json string
     * @return the JSON Tree, or null if any error happens
     */
    public static JsonNode readTree(@NonNull String json) {
        try {
            return OM.readTree(json);
        } catch (IOException e) {
            log.error("error on deserialize", e);
            return null;
        }
    }
}

