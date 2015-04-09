package org.dbflute.boot;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

public class JettyMain {

    private static final int PORT = 9000;

    public static void main(String[] args) throws Exception {

        URL warLocation = JettyMain.class.getProtectionDomain().getCodeSource().getLocation();
        String path = warLocation.toURI().getPath();

        WebAppContext context = new WebAppContext();
        if (path.endsWith(".war")) {
            context.setWar(warLocation.toExternalForm());
        } else {
            context.setResourceBase("./src/main/webapp/");
        }

        Configuration[] configurations = {
                new AnnotationConfiguration(),
                new WebInfConfiguration(),
                new WebXmlConfiguration(),
                new MetaInfConfiguration(),
                new FragmentConfiguration(),
                new EnvConfiguration(),
                new PlusConfiguration(),
                new JettyWebXmlConfiguration()
            };

        context.setConfigurations(configurations);

        Server server = new Server(PORT);
        server.setHandler(context);
        server.start();
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI("http://localhost:" + PORT + context.getContextPath()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        server.join();
    }
}
