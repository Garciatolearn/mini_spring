package sugar.spring.framework.core.io;

import sugar.spring.framework.utils.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource {
    private String path;
    private ClassLoader classLoader;

    public  ClassPathResource(String path){
        this(path,null);
    }
    public ClassPathResource(String path,ClassLoader classLoader){
        this.path = path;
        this.classLoader = classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader();
    }
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream in = classLoader.getResourceAsStream(path);
        if(in == null){
            throw new FileNotFoundException(this.path + "cannot be opened because it does not exist");
        }
        return in;
    }
}
