package org.dbflute.jetty;

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

public class Main {

    public static void main(String[] args) throws Exception {
        WebAppContext war = new WebAppContext();
        war.setContextPath("/dbflute-intro");

        ProtectionDomain domain = Main.class.getProtectionDomain();
        URL warLocation = domain.getCodeSource().getLocation();
        war.setWar(warLocation.toExternalForm());

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

        Server server = new Server(8080);
        server.setHandler(war);
        server.start();

        Desktop desktop = Desktop.getDesktop();
        String uriString = "http://localhost:8080/dbflute-intro";
        try {
            URI uri = new URI(uriString);
            desktop.browse(uri);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        server.join();
    }
}
