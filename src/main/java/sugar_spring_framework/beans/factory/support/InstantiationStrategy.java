package sugar_spring_framework.beans.factory.support;

import sugar_spring_framework.beans.BeanException;
import sugar_spring_framework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor constructor, Object[] args) throws BeanException;
}
