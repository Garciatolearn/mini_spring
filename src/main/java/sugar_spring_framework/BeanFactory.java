package sugar_spring_framework;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Garcia
 */
public class BeanFactory {
    Map<String,BeanDefinition> beanDefinitionMap = new HashMap<>();
    public Object getBean(String name){
        return beanDefinitionMap.get(name).getBean();
    }
    public void registerBeanDefinition(String name,BeanDefinition beanDefinition){
        beanDefinitionMap.put(name,beanDefinition);
    }
}
