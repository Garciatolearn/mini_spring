package sugar.spring.framework.beans.factory.config;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.factory.BeanFactory;

/**
 * 自动化处理Bean工厂配置的接口
 * */
public interface AutowireCapableBeanFactory extends BeanFactory {
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeanException;

    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeanException;

}
