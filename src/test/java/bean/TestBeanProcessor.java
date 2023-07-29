package bean;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.factory.config.BeanPostProcessor;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/7/30
 **/
public class TestBeanProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeanException {
        if (beanName.equals("user")) {
            User user = (User)bean;
            user.setAge(13);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeanException {
        return bean;
    }
}
