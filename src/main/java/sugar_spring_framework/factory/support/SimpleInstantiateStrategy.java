package sugar_spring_framework.factory.support;

import sugar_spring_framework.factory.BeanException;
import sugar_spring_framework.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SimpleInstantiateStrategy implements InstantiationStrategy{
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor constructor, Object[] args) throws BeanException {
        Class clazz = beanDefinition.getBeanClass();
        try{
            if(constructor == null){
                return clazz.getDeclaredConstructor().newInstance();
            }else {
                return clazz.getDeclaredConstructor(constructor.getParameterTypes()).newInstance(args);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
