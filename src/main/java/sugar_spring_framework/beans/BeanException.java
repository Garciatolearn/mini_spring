package sugar_spring_framework.beans;

public class BeanException extends RuntimeException{


    public BeanException(String instantiation_of_bean_failed, ReflectiveOperationException e) {
        super(instantiation_of_bean_failed,e);
    }
    public BeanException(String failed){
        super(failed);
    }

}
