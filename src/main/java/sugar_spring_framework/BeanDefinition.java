package sugar_spring_framework;

/**
 * @author Garcia
 */
public class BeanDefinition {
    private Object bean;
    public BeanDefinition(Object bean){
        this.bean = bean;
    }
    public Object getBean(){
        return bean;
    }

}
