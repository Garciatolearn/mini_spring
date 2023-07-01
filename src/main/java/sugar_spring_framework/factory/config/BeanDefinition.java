package sugar_spring_framework.factory.config;

import java.util.GregorianCalendar;

/**
 * @author Garcia
 */
public class BeanDefinition {
    private Class beanClass;
    public BeanDefinition(Class beanClass){
        this.beanClass = beanClass;
    }

    public Class getBeanClass(){
        return this.beanClass;
    }
    public void setBeanClass(Class beanClass){
        this.beanClass = beanClass;
    }
}
