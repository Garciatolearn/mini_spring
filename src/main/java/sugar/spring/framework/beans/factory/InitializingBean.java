package sugar.spring.framework.beans.factory;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/8/23
 **/
public interface InitializingBean {
    /**
     * Bean 处理属性填充之后调用
     * */

    void afterPropertiesSet() throws Exception;
}
