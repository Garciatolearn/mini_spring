package sugar.spring.framework.beans.factory.support;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.factory.DisposableBean;
import sugar.spring.framework.beans.factory.config.SingletonRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultSingletonRegistry implements SingletonRegistry {
    private final Map<String, Object> singletonObjects = new HashMap<>();

    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String name) {
        return singletonObjects.get(name);
    }

    protected void addSingletonBean(String name, Object singleTonObject) {
        singletonObjects.put(name, singleTonObject);
    }

    protected void addDisableBean(String name, DisposableBean disposableBean) {
        disposableBeans.put(name, disposableBean);
    }

    public void destroySingletons() {
        Set<String> keySet = this.disposableBeans.keySet();
        Object[] disposableBeanName = keySet.toArray();
        for (int i = disposableBeanName.length - 1; i >= 0; i--) {
            Object beanName = disposableBeanName[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try{
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeanException("Destroy method on bean with name '" + beanName + "' threw an exception");
            }
        }
    }
}
