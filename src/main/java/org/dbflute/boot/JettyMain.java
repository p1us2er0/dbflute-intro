package org.dbflute.boot;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.ProtectionDomain;

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
        WebAppContext war = new WebAppContext();
        if (JettyMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().endsWith(".war")) {
            ProtectionDomain domain = JettyMain.class.getProtectionDomain();
            URL warLocation = domain.getCodeSource().getLocation();
            war.setWar(warLocation.toExternalForm());
        } else {
            war.setResourceBase("./src/main/webapp/");
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

        war.setConfigurations(configurations);

        Server server = new Server(PORT);
        server.setHandler(war);
        server.start();

        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI("http://localhost:" + PORT + war.getContextPath()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        server.join();
    }
}
