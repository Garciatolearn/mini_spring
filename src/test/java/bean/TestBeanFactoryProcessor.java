package bean;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.PropertyValue;
import sugar.spring.framework.beans.PropertyValues;
import sugar.spring.framework.beans.factory.ConfigurableListableBeanFactory;
import sugar.spring.framework.beans.factory.config.BeanDefinition;
import sugar.spring.framework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/7/30
 **/
public class TestBeanFactoryProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeanException {
        BeanDefinition definition = factory.getBeanDefinition("user");
        PropertyValues propertyValues = definition.getPropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name","燃烧你的梦"));
    }
}
