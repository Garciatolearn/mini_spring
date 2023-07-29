package sugar.spring.framework.beans.factory.config;

import sugar.spring.framework.beans.BeanException;

public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeanException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeanException;


}
