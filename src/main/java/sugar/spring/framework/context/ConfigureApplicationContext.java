package sugar.spring.framework.context;

import sugar.spring.framework.beans.BeanException;

public interface ConfigureApplicationContext extends ApplicatonContext{
    void refresh() throws BeanException;
}
