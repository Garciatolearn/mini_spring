package sugar_spring_framework.beans.factory.support;

import sugar_spring_framework.beans.factory.config.BeanDefinition;

/**
 * @author Garcia
 */
public interface BeanDefinitionRegistry {
    void registerDefinition(String beanName, BeanDefinition beanDefinition);
}
