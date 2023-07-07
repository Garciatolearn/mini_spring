package sugar_spring_framework.beans.factory.support;

import sugar_spring_framework.beans.BeanException;
import sugar_spring_framework.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry{
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    @Override
    protected BeanDefinition getBeanDefinition(String beanName) throws BeanException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition == null) {
            throw new BeanException("no bean named '"+beanName+"' is defined");
        }
        return beanDefinition;
    }

    @Override
    public void registerDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName,beanDefinition);
    }
}
