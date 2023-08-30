package sugar.spring.framework.beans.factory;

public interface DisposableBean {

    void destroy() throws Exception;
}
