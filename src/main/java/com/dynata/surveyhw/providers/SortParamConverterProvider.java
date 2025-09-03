package com.dynata.surveyhw.providers;

import com.dynata.surveyhw.utils.SortParamConverter;
import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

@Provider
public class SortParamConverterProvider implements ParamConverterProvider {

    private final SortParamConverter converter = new SortParamConverter();

    @SuppressWarnings("unchecked")
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (Map.class.isAssignableFrom(rawType)) {
            return (ParamConverter<T>) converter;
        }
        return null;
    }
}
