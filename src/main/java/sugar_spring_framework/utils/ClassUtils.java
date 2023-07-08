package sugar_spring_framework.utils;

public class ClassUtils {
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl;
        cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }
}
