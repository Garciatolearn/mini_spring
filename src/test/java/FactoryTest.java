import bean.User;
import org.junit.jupiter.api.Test;
import sugar_spring_framework.factory.config.BeanDefinition;
import sugar_spring_framework.factory.support.DefaultListableBeanFactory;

public class FactoryTest {
    @Test
    public void factory(){
        Class userClass = User.class;
        BeanDefinition definition = new BeanDefinition(userClass);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerDefinition("user",definition);
        User user = (User) beanFactory.getBean("user");
        System.out.println(user);
        User user2 = (User) beanFactory.getSingleton("user");
        System.out.println(user2);
    }
}
