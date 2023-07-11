package sugar.spring.framework.beans.factory.support;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.PropertyValue;
import sugar.spring.framework.beans.PropertyValues;
import sugar.spring.framework.beans.factory.config.BeanDefinition;
import sugar.spring.framework.beans.factory.config.BeanReference;
import sugar.spring.framework.utils.Beanutil;

import java.lang.reflect.Constructor;

/**
 * @author Garcia
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiateStrategy();
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition,Object[] args)  throws BeanException {
        Object bean;
        try{
            bean = createBeanInstance(beanDefinition,beanName,args);
            //添加属性填充
            applyPropertyValues(beanName,bean,beanDefinition);
        }catch (BeanException e){
            throw new BeanException("instantiation bean of failed : "+beanName);
        }
        addSingletonBean(beanName,bean);
        return bean;
    }
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName , Object[] args)throws BeanException{
        Constructor<?> constructorToUse = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor<?> cor:
             declaredConstructors) {
            if(args != null && cor.getParameterTypes().length == args.length){
            //todo 添加参数的类型校验,防止实例化失败
                constructorToUse = cor;
                break;
            }

        }
        return  instantiationStrategy.instantiate(beanDefinition,beanName,constructorToUse,args);
    }

    //此时的bean已然被实例化,但是没有注入属性
    protected void applyPropertyValues(String beanName,Object bean ,BeanDefinition beanDefinition){
        try{
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue pv:
                 propertyValues.getPropertyValues()) {
                String name = pv.getName();
                Object value = pv.getValue();
                if(value instanceof BeanReference){
                    // A 依赖 B，获取 B 的实例化
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
                Beanutil.setFieldValue(bean,name,value);

            }

        } catch (IllegalAccessException e) {
            throw new BeanException("bean setting property failed: "+beanName);
        }

    }
    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

}
