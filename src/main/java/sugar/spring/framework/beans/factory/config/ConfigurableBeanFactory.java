package sugar.spring.framework.beans.factory.config;

import sugar.spring.framework.beans.factory.HierarchialBeanFactory;

public interface ConfigurableBeanFactory extends HierarchialBeanFactory {
    String SCOPE_PROTOTYPE = "prototype";
    String SCOPE_SINGLETON = "singleton";
}
