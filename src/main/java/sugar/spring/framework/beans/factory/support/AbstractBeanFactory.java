package sugar.spring.framework.beans.factory.support;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.factory.BeanFactory;
import sugar.spring.framework.beans.factory.config.BeanDefinition;
import sugar.spring.framework.beans.factory.config.BeanPostProcessor;
import sugar.spring.framework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanFactory extends DefaultSingletonRegistry implements ConfigurableBeanFactory {
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String name) throws BeanException {
        return doGetBean(name,null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeanException {
        return doGetBean(name,args);
    }
    @Override
    public <T> T getBean(String name,Class<T> requireType) throws BeanException{
        return (T) getBean(name);
    }
    protected  <T> T doGetBean(String name,Object[] args){
        Object bean = getSingleton(name);
        if(bean != null){
            return (T) bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        return (T) createBean(name,beanDefinition,args);
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeanException;
    protected abstract Object createBean(String beanName,BeanDefinition beanDefinition,Object[] args) throws BeanException;

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors(){
        return this.beanPostProcessors;
    }
}
