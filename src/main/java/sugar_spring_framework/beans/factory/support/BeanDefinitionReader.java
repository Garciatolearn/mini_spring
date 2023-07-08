package sugar_spring_framework.beans.factory.support;

import sugar_spring_framework.beans.BeanException;
import sugar_spring_framework.core.io.Resource;
import sugar_spring_framework.core.io.ResourceLoader;

public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(String location) throws BeanException;

    void loadBeanDefinitions(Resource resource) throws BeanException;

    void loadBeanDefinitions(Resource... resources) throws BeanException;
}
