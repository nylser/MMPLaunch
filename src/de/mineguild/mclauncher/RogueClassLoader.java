package de.mineguild.mclauncher;


import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.nio.ByteBuffer;
import java.security.CodeSource;
import java.security.cert.Certificate;
import java.util.jar.Manifest;
import java.util.logging.Logger;

/**
 * This class loader disables sealed package checking and certificate
 * checking. It requires access to two restricted sun.* packages however.
 *
 * @author sk89q
 */
@SuppressWarnings("restriction")
public class RogueClassLoader extends URLClassLoader {

    private static final Logger logger = Logger.getLogger(RogueClassLoader.class.getCanonicalName());
    private URLClassPath urlClassPath;

    public RogueClassLoader(URL[] urls, ClassLoader parent,
                            URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
        install();
    }

    public RogueClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        install();
    }

    public RogueClassLoader(URL[] urls) {
        super(urls);
        install();
    }

    private void install() {
        try {
            Field field = URLClassLoader.class.getDeclaredField("ucp");
            field.setAccessible(true);
            this.urlClassPath = (URLClassPath) field.get(this);
            //field.set(this, new RogueURLClassPath(urlClassPath.getURLs()));
        } catch (SecurityException e) {
            throw new RuntimeException("Failed to find 'ucp'", e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to find 'ucp'", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to find 'ucp'", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to find 'ucp'", e);
        }
    }

    @Override
    protected Class<?> findClass(final String name)
            throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
        Resource res = urlClassPath.getResource(path, false);
        if (res != null) {
            try {
                return defineClass(name, res, true);
            } catch (IOException e) {
                throw new ClassNotFoundException(name, e);
            }
        } else {
            if (name.equals("BaseModMp")) {
                logger.info("(!!) Something requested ModLoaderMP, but you don't have it.");
            } else if (name.equals("BaseMod")) {
                logger.info("(!!) Something requested ModLoader, but you don't have it.");
            }
            throw new ClassNotFoundException(name);
        }
    }

    private Class<?> defineClass(String name, Resource res, boolean verify)
            throws IOException {
        int i = name.lastIndexOf('.');
        URL url = res.getCodeSourceURL();
        if (i != -1) {
            String pkgName = name.substring(0, i);
            Package pkg = getPackage(pkgName);
            Manifest manifest = res.getManifest();
            if (pkg == null) {
                if (manifest != null) {
                    definePackage(pkgName, manifest, url);
                } else {
                    definePackage(pkgName, null, null, null, null, null, null,
                            null);
                }
            }
        }
        ByteBuffer buffer = res.getByteBuffer();
        byte[] bytes = (buffer == null) ? res.getBytes() : null;
        CodeSource cs = new CodeSource(url, (Certificate[]) null);
        return (buffer != null ? defineClass(name, buffer, cs) : defineClass(name,
                bytes, 0, bytes.length, cs));
    }
    /*
private class RogueURLClassPath extends URLClassPath {

public RogueURLClassPath(URL[] urls, URLStreamHandlerFactory urlStreamHandlerFactory) {
super(urls, urlStreamHandlerFactory);
}

public RogueURLClassPath(URL[] urls) {
super(urls);
}
public Manifest getManifest() {
return null;
}
}*/

}