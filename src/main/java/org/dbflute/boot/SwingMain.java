package org.dbflute.boot;

import java.net.URL;

import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;

public class SwingMain {

    private static final String DBFLUTE_INTROF_RAME_CLASS = "org.dbflute.intro.app.swing.DbfluteIntroFrame";

    public static void main(String[] args) throws Exception {
        // TODO クラスローダの整理。
        URL warLocation = SwingMain.class.getProtectionDomain().getCodeSource().getLocation();
        String path = warLocation.toURI().getPath();

        ClassLoader classLoader = SwingMain.class.getClassLoader();
        if (path.endsWith(".war")) {
            WebAppContext context = new WebAppContext();
            context.setWar(warLocation.toExternalForm());
            context.setConfigurations(new Configuration[] { new WebInfConfiguration() });
            context.preConfigure();
            context.configure();
            classLoader = context.getClassLoader();
        }

        Class<?> dbfluteIntroFrameClass = classLoader.loadClass(DBFLUTE_INTROF_RAME_CLASS);
        dbfluteIntroFrameClass.getMethod("main", String[].class).invoke(null, new Object[] {args});
    }
}
