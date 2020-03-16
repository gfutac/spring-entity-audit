package com.gfutac.rest.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
public class EntityToDtoMapperFactory {

    private Map<Type, EntityToDtoMapper> mapperBeans;

    public EntityToDtoMapperFactory(@Autowired ApplicationContext applicationContext) {
        this.mapperBeans = new HashMap<>();

        var entityDtoMapperBeanNames = applicationContext.getBeanNamesForType(EntityToDtoMapper.class);
        for (var mapperName : entityDtoMapperBeanNames) {
            var bean = applicationContext.getBean(mapperName);
            var genericInterfaces = bean.getClass().getSuperclass().getGenericInterfaces();
            for (var gi : genericInterfaces) {
                if (gi instanceof ParameterizedType) {
                    var t = (ParameterizedType)gi;
                    if (t.getRawType() == EntityToDtoMapper.class) {
                        var entityKeyType = t.getActualTypeArguments()[0];
                        var dtoKeyType = t.getActualTypeArguments()[1];
                        this.mapperBeans.put(entityKeyType, (EntityToDtoMapper)bean);
                    }
                }
            }
        }
    }

    public EntityToDtoMapper getMapper(Class<?> entityType) {
        return this.mapperBeans.get(entityType);
    }
}
