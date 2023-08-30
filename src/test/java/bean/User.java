package bean;

import sugar.spring.framework.beans.factory.DisposableBean;
import sugar.spring.framework.beans.factory.InitializingBean;

public class User  {
     private int age;
     private String name;

    private UserDao dao;
    public User(){

    }
    public User(String name,int age){
        this.name = name;
        this.age = age;
    }

    public UserDao getDao() {
        return dao;
    }

    @Override
    public String toString() {
        return "我是:" + name + ",年纪是:" + age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public void destroy() throws Exception {
        System.out.println("我被删除了");
    }


    public void initMethod() throws Exception {
        System.out.println("我被初始化了");
    }
}
