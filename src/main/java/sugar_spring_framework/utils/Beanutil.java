package sugar_spring_framework.utils;


import java.lang.reflect.Field;

public class Beanutil {
    public static void setFieldValue(Object bean, String fieldName, Object value) throws IllegalAccessException {
        Class beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field :
                fields) {
            Class z =field.getType();

            if(field.getName().equals(fieldName)){
                field.setAccessible(true);
                field.set(bean,value);
            }
        }
    }
}
