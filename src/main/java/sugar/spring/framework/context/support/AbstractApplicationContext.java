package sugar.spring.framework.context.support;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.factory.ConfigurableListableBeanFactory;
import sugar.spring.framework.beans.factory.config.BeanFactoryPostProcessor;
import sugar.spring.framework.beans.factory.config.BeanPostProcessor;
import sugar.spring.framework.context.ConfigureApplicationContext;
import sugar.spring.framework.core.io.DefaultResourceLoader;

import java.util.Map;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/7/28
 **/
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigureApplicationContext {
    @Override
    public Object getBean(String name) throws BeanException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeanException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requireType) throws BeanException {
        return getBeanFactory().getBean(name, requireType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeanException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() throws BeanException {
        return getBeanFactory().getBeanDefinitionNames();
    }

    protected abstract void refreshBeanFactory() throws BeanException;

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    @Override
    public void refresh() throws BeanException {
        refreshBeanFactory();
        ConfigurableListableBeanFactory factory = getBeanFactory();
        invokeBeanFactoryPostProcessors(factory);
        registerBeanPostProcessor(factory);
        factory.preInstantiateSingletons();
    }

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory factory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = factory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor postProcessor :
                beanFactoryPostProcessorMap.values()) {
            postProcessor.postProcessBeanFactory(factory);
        }
    }

    private void registerBeanPostProcessor(ConfigurableListableBeanFactory factory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = factory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor :
                beanPostProcessorMap.values()) {
            factory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    @Override
    public void registerShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        getBeanFactory().destroySingletons();
    }
}
