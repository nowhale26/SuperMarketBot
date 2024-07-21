package com.github.supermarket.entity.enumconverter;

import com.github.supermarket.enums.ExternalType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter()
public class ExternalTypeConverter implements AttributeConverter<ExternalType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ExternalType externalType) {
        if (externalType == null) {
            return null;
        }
        return externalType.getValue();
    }

    @Override
    public ExternalType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return Stream.of(ExternalType.values())
                .filter(type -> type.getValue().equals(value))
                .findFirst().orElse(null);
    }
}
