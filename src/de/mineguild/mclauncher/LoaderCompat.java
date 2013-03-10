package de.mineguild.mclauncher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Various hacks to get things to jive.
 *
 * @author sk89q
 */
public class LoaderCompat {

    private static final Logger logger = Logger.getLogger(LoaderCompat.class.getCanonicalName());
    private GameLauncher gameLauncher;
    private boolean modLoaderInited = false;

    public LoaderCompat(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
    }

    private void initModLoader() {
        if (modLoaderInited) return;
        modLoaderInited = true;

        try {
            Class<?> modLoaderCls = gameLauncher.getClassLoader().loadClass("ModLoader");
            Method method = modLoaderCls.getDeclaredMethod("readFromClassPath", new Class<?>[]{File.class});
            method.setAccessible(true);
            for (String addonPath : gameLauncher.getAddonPaths()) {
                File f = new File(addonPath);
                if (f.exists()) {
                    logger.info("Trying to force ModLoader to load " + f.getAbsolutePath());
                    method.invoke(null, f);
                }
            }
        } catch (ClassNotFoundException e) {
            logger.info("initModLoader(): No ModLoader found");
        } catch (SecurityException e) {
            logger.log(Level.WARNING, "initModLoader(): error", e);
        } catch (NoSuchMethodException e) {
            logger.log(Level.WARNING, "initModLoader(): error", e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "initModLoader(): error", e);
        } catch (IllegalAccessException e) {
            logger.log(Level.WARNING, "initModLoader(): error", e);
        } catch (InvocationTargetException e) {
            logger.log(Level.WARNING, "initModLoader(): error", e);
        }
    }

    public void afterStart() {
    }

    public void installHooks() {
        System.setOut(new PrintStream(new StdOutCatcher(System.out), true));
    }

    private class StdOutCatcher extends ByteArrayOutputStream {
        private PrintStream orig;

        public StdOutCatcher(PrintStream out) {
            this.orig = out;
        }

        @Override
        public void flush() {
            String message = toString();
            orig.print(message);

            if (message.startsWith("ModLoader ")) {
                initModLoader();
            }

            reset();
        }
    }

}