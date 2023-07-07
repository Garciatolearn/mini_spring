package sugar_spring_framework.beans.factory.config;

import sugar_spring_framework.beans.BeanException;
import sugar_spring_framework.beans.PropertyValues;

import java.util.GregorianCalendar;

/**
 * @author Garcia
 */
public class BeanDefinition {
    private Class beanClass;
    private PropertyValues propertyValues;
    public BeanDefinition(Class beanClass){
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }
    public BeanDefinition(Class beanClass,PropertyValues propertyValues){
        this.beanClass = beanClass;
        this.propertyValues = propertyValues == null ? new PropertyValues() : propertyValues;
    }

    public Class getBeanClass(){
        return this.beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public void setBeanClass(Class beanClass){
        this.beanClass = beanClass;
    }
}
