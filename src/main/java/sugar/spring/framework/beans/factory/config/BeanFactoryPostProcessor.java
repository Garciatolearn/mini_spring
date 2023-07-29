package sugar.spring.framework.beans.factory.config;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.factory.ConfigurableListableBeanFactory;

public interface BeanFactoryPostProcessor {
    //在bean实例化之前,可进行对BeanDefinition进行修改
    void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeanException;
}
