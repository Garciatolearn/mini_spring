package sugar_spring_framework.factory.support;

import sugar_spring_framework.factory.config.SingletonRegistry;

import java.util.HashMap;
import java.util.Map;
public class DefaultSingletonRegistry implements SingletonRegistry {
    private Map<String,Object> singletonObjects = new HashMap<>();
    @Override
    public Object getSingleton(String name) {
        return singletonObjects.get(name);
    }
    protected void addSingletonBean(String name,Object singleTonObject){
        singletonObjects.put(name,singleTonObject);
    }
}
