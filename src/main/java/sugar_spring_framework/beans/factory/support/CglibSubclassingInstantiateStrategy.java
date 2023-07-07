package sugar_spring_framework.beans.factory.support;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import sugar_spring_framework.beans.BeanException;
import sugar_spring_framework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public class CglibSubclassingInstantiateStrategy implements InstantiationStrategy{
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor constructor, Object[] args) throws BeanException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        enhancer.setCallback(NoOp.INSTANCE);
        if(constructor == null){
            return enhancer.create();
        }
        return enhancer.create(constructor.getParameterTypes(),args);
    }
}
