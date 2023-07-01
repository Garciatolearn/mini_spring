package sugar_spring_framework.factory.support;

import sugar_spring_framework.factory.config.BeanDefinition;

/**
 * @author Garcia
 */
public interface BeanDefinitionRegistry {
    void registerDefinition(String beanName, BeanDefinition beanDefinition);
}
