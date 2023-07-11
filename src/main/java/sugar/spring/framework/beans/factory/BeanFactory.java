package sugar.spring.framework.beans.factory;

import sugar.spring.framework.beans.BeanException;

/**
 * @author Garcia
 */
public interface BeanFactory {
    Object getBean(String name) throws BeanException;
    Object getBean(String name,Object... args) throws BeanException;
    <T> T getBean(String name,Class<T> requireType) throws BeanException;


}
