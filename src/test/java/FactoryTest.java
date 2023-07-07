import bean.UserDao;
import bean.User;
import org.junit.jupiter.api.Test;
import sugar_spring_framework.beans.PropertyValue;
import sugar_spring_framework.beans.PropertyValues;
import sugar_spring_framework.beans.factory.config.BeanDefinition;
import sugar_spring_framework.beans.factory.config.BeanReference;
import sugar_spring_framework.beans.factory.support.DefaultListableBeanFactory;

public class FactoryTest {
    @Test
    public void factory(){
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.registerDefinition("userdao",new BeanDefinition(UserDao.class));
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("age",12));
        propertyValues.addPropertyValue(new PropertyValue("name","萨米"));
        propertyValues.addPropertyValue(new PropertyValue("dao",new BeanReference("userdao")));
        factory.registerDefinition("user",new BeanDefinition(User.class,propertyValues));
        User user =(User) factory.getBean("user");
        UserDao dao = user.getDao();
        System.out.println(dao.queryUserName("10001"));
    }
}
