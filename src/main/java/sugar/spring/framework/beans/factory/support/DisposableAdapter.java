package sugar.spring.framework.beans.factory.support;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.factory.DisposableBean;
import sugar.spring.framework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/8/24
 **/
public class DisposableAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;
    private String destroyMethodName;

    public DisposableAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }
        if (destroyMethodName != null && !"".equals(destroyMethodName) && !(bean instanceof DisposableBean)) {
            Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
            if (null == destroyMethod) {
                throw new BeanException("Couldn't find a destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
            }
            destroyMethod.invoke(bean);
        }
    }
}
