package sugar.spring.framework.utils;


import cn.hutool.core.convert.Convert;

import java.lang.reflect.Field;
import java.net.URL;
public class Beanutil {
    public static void setFieldValue(Object bean, String fieldName, Object value) throws IllegalAccessException {
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field :
                fields) {
            Class<?> z =field.getType();
            if(field.getName().equals(fieldName)){
                if(!z.isAssignableFrom(value.getClass())){
                    value = Convert.convert(z,value);
                }
                field.setAccessible(true);
                field.set(bean,value);
            }
        }
    }
}
