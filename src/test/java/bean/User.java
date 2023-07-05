package bean;

public class User {
    int age;
    String name;
    public User(){

    }
    public User(String name,int age){
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "我是:" + name + ",年纪是:" + age;
    }
}
