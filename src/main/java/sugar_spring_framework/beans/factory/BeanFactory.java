package sugar_spring_framework.beans.factory;

import sugar_spring_framework.beans.BeanException;

/**
 * @author Garcia
 */
public interface BeanFactory {
    Object getBean(String name) throws BeanException;
    Object getBean(String name,Object... args) throws BeanException;
}
