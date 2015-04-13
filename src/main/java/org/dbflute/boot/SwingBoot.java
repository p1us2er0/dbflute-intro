package org.dbflute.boot;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;

public class SwingBoot {

    private static final String DBFLUTE_INTROF_RAME_CLASS = "org.dbflute.intro.app.swing.DbfluteIntroFrame";

    public static void main(String[] args) {
        // TODO クラスローダの整理。
        URL warLocation = SwingBoot.class.getProtectionDomain().getCodeSource().getLocation();
        String path;
        try {
            path = warLocation.toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ClassLoader classLoader = SwingBoot.class.getClassLoader();
        if (path.endsWith(".war")) {
            WebAppContext context = new WebAppContext();
            context.setWar(warLocation.toExternalForm());
            context.setConfigurations(new Configuration[] { new WebInfConfiguration() });
            try {
                context.preConfigure();
                context.configure();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            classLoader = context.getClassLoader();
        }

        try {
            Class<?> dbfluteIntroFrameClass = classLoader.loadClass(DBFLUTE_INTROF_RAME_CLASS);
            dbfluteIntroFrameClass.getMethod("main", String[].class).invoke(null, new Object[] {args});
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
