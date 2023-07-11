package sugar.spring.framework.beans.factory.support;

import sugar.spring.framework.core.io.DefaultResourceLoader;
import sugar.spring.framework.core.io.ResourceLoader;

/**
 * @author Garcia
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
    private final BeanDefinitionRegistry registry;
    private final ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry){
        this(registry,new DefaultResourceLoader());
    }
    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry,ResourceLoader loader){
        this.registry =registry;
        this.resourceLoader = loader;
    }
    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
