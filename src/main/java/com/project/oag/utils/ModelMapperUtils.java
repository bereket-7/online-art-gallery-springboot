package com.project.oag.utils;

import org.springframework.stereotype.Component;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;

@Component
public class ModelMapperUtils {
    private final ModelMapper modelMapper;

    public ModelMapperUtils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <S, T> List<T> mapList(Collection<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .toList();
    }
}