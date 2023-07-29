package sugar.spring.framework.context.support;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.factory.ConfigurableListableBeanFactory;
import sugar.spring.framework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/7/28
 **/
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
    private DefaultListableBeanFactory factory;

    @Override
    protected void refreshBeanFactory() throws BeanException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        factory = beanFactory;
    }

    private DefaultListableBeanFactory createBeanFactory(){
        return new DefaultListableBeanFactory();
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory factory);
    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return factory;
    }
}
