package sugar.spring.framework.beans.factory.support;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.core.io.Resource;
import sugar.spring.framework.core.io.ResourceLoader;

import java.io.IOException;

public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(String location) throws BeanException;

    void loadBeanDefinitions(Resource resource) throws BeanException, IOException;

    void loadBeanDefinitions(Resource... resources) throws BeanException;

    void loadBeanDefinitions(String... locations) throws BeanException;
}
