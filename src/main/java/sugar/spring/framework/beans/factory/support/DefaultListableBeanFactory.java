package sugar.spring.framework.beans.factory.support;

import sugar.spring.framework.beans.BeanException;
import sugar.spring.framework.beans.factory.config.BeanDefinition;
import sugar.spring.framework.beans.factory.ConfigurableListableBeanFactory;
import sugar.spring.framework.beans.factory.config.BeanPostProcessor;

import java.util.HashMap;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeanException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition == null) {
            throw new BeanException("no bean named '"+beanName+"' is defined");
        }
        return beanDefinition;
    }

    @Override
    public void preInstantiateSingletons() throws BeanException {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }


    @Override
    public void registerDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName,beanDefinition);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }



    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeanException {
        Map<String,T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName,beanDefinition) ->{
            Class<?> clazz = beanDefinition.getBeanClass();
            if(type.isAssignableFrom(clazz)){
                result.put(beanName, (T)getBean(beanName));
            }
        });
        return result;
    }

    @Override
    public String[] getBeanDefinitionNames() throws BeanException {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }


}
