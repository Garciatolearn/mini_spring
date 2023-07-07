package bean;

public class User {
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
}
