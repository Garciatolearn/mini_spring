package bean;

public class User {
    int age;
    String name;


    @Override
    public String toString() {
        return "我是:" + name + ",年纪是:" + age;
    }
}
