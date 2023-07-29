import bean.UserDao;
import bean.User;
import org.junit.jupiter.api.Test;
import sugar.spring.framework.beans.PropertyValue;
import sugar.spring.framework.beans.PropertyValues;
import sugar.spring.framework.beans.factory.config.BeanDefinition;
import sugar.spring.framework.beans.factory.config.BeanReference;
import sugar.spring.framework.beans.factory.support.DefaultListableBeanFactory;
import sugar.spring.framework.beans.factory.support.xml.XmlBeanDefinitionReader;
import sugar.spring.framework.context.support.ClassPathXmlApplicationContext;

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
    @Test
    public void reader(){
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions("E:\\杂七杂八的\\spring.xml");
        User user =(User) factory.getBean("user");
        System.out.println(user);

    }

    @Test
    public void applicationContext(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        User user =(User) context.getBean("user");
        System.out.println(user);
    }
}
