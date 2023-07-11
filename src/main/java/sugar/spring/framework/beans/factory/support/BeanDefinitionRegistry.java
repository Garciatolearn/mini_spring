package sugar.spring.framework.beans.factory.support;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.factory.config.BeanDefinition;

/**
 * @author Garcia
 */
public interface BeanDefinitionRegistry {
    void registerDefinition(String beanName, BeanDefinition beanDefinition);
    boolean containsBeanDefinition(String beanName);
    String[] getBeanDefinitionNames() throws BeanException;

}
