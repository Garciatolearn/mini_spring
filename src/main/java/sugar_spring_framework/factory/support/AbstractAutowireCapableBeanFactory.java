package sugar_spring_framework.factory.support;

import sugar_spring_framework.factory.BeanException;
import sugar_spring_framework.factory.config.BeanDefinition;

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
        }catch (BeanException e){
            throw new BeanException("instantiation bean of failed : "+e);
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

}
