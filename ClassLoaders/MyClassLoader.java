package ClassLoaders;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Cip on 19-May-16.
 */
public class MyClassLoader extends URLClassLoader {
    public MyClassLoader(URL[] urls) {
        super(urls);
    }
}
