package com.doranco.site.util;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute != null ? String.join(",", attribute) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return dbData != null && !dbData.isEmpty() ? Arrays.asList(dbData.split(",")) : List.of();
    }
}
