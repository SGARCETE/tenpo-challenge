package com.tenpo.challenge.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

/**
 * Mapping helper to convert business objects to dto and vice versa
 */
public class MappingHelper {
    private static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    private MappingHelper() {
    }

    /**
     * Maps an object T to an object D. For example, a DTO to a business class.
     * @param entity the entity T that we need to map to an object of type D
     * @param outClass the class of the object D
     */
    public static <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

}
