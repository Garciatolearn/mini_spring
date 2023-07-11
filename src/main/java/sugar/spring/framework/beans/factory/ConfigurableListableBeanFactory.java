package sugar.spring.framework.beans.factory;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.factory.config.AutowireCapableBeanFactory;
import sugar.spring.framework.beans.factory.config.BeanDefinition;
import sugar.spring.framework.beans.factory.support.ListableBeanFactory;
import sugar.spring.framework.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory, AutowireCapableBeanFactory {
    BeanDefinition getBeanDefinition(String beanName) throws BeanException;
}
