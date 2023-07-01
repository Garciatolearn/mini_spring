package sugar_spring_framework.factory.support;

import sugar_spring_framework.factory.BeanException;
import sugar_spring_framework.factory.config.BeanDefinition;

/**
 * @author Garcia
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition)  throws BeanException {
        Object bean;
        try{
            bean = beanDefinition.getBeanClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanException("instantiation of bean failed",e);
        }
        addSingletonBean(beanName,bean);
        return bean;
    }
}
