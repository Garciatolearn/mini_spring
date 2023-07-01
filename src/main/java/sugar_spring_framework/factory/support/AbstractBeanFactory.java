package sugar_spring_framework.factory.support;

import sugar_spring_framework.factory.BeanException;
import sugar_spring_framework.factory.BeanFactory;
import sugar_spring_framework.factory.config.BeanDefinition;

public abstract class AbstractBeanFactory extends DefaultSingletonRegistry implements BeanFactory {
    @Override
    public Object getBean(String name) throws BeanException {
        Object bean = getSingleton(name);
        if(bean != null){
            return bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        return createBean(name,beanDefinition);
    }
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeanException;
    protected abstract Object createBean(String beanName,BeanDefinition beanDefinition) throws BeanException;
}
