package sugar_spring_framework.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Garcia
 */
public interface BeanFactory {
    Object getBean(String name) throws BeanException;
}
