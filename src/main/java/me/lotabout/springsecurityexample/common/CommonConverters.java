package me.lotabout.springsecurityexample.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CommonConverters {

    public static abstract class CommonConverter<T> implements AttributeConverter<T, byte[]> {

        private static final ThreadLocal<ObjectMapper> MAPPER = ThreadLocal
                .withInitial(ObjectMapper::new);

        private final TypeReference type;

        public CommonConverter(TypeReference type) {
            this.type = type;
        }

        @Override
        public byte[] convertToDatabaseColumn(T data) {
            if (data == null) {
                return null;
            }
            try {
                return MAPPER.get().writeValueAsString(data).getBytes(Charsets.UTF_8);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException();
            }
        }

        @Override
        public T convertToEntityAttribute(byte[] column) {
            if (column == null) {
                return null;
            }
            try {
                return MAPPER.get().readValue(new String(column, Charsets.UTF_8), type);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static class ListStringConverter extends CommonConverter<List<String>> {

        public ListStringConverter() {
            super(new TypeReference<List<String>>() {});
        }
    }

    public static class MapStringStringConverter extends CommonConverter<Map<String, String>> {

        public MapStringStringConverter() {
            super(new TypeReference<Map<String, String>>() {});
        }
    }

    public static class StringListConvertor extends CommonConverter<List<String>> {

        public StringListConvertor() {
            super(new TypeReference<List<String>>() {});
        }
    }
}
