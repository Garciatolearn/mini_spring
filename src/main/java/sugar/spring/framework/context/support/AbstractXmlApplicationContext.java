package sugar.spring.framework.context.support;

import sugar.spring.framework.beans.factory.support.DefaultListableBeanFactory;
import sugar.spring.framework.beans.factory.support.xml.XmlBeanDefinitionReader;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/7/28
 **/
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory factory) {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        String[] configLocations = getConfigLocations();
        if (configLocations != null){
            reader.loadBeanDefinitions(configLocations);
        }

    }
    protected abstract String[] getConfigLocations();
}
