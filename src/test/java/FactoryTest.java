import bean.User;
import org.junit.jupiter.api.Test;
import sugar_spring_framework.BeanDefinition;
import sugar_spring_framework.BeanFactory;

public class FactoryTest {
    @Test
    public void factory(){
        BeanFactory factory = new BeanFactory();
        factory.registerBeanDefinition("user",new BeanDefinition(new User()));
        User user = (User)factory.getBean("user");
        System.out.println(user);
    }
}
