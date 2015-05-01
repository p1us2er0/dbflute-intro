/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.boot;

import java.net.InetSocketAddress;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author p1us2er0
 */
public class JettyBoot {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Logger LOG = LoggerFactory.getLogger(JettyBoot.class);

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private Server server;

    /**
     * Jettyを起動する。
     * @param port ポート
     * @return 起動したJettyのURI
     */
    public URI start(int port) {

        URL warLocation = JettyBoot.class.getProtectionDomain().getCodeSource().getLocation();
        String path;
        try {
            path = warLocation.toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("server start failed.", e);
        }

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

        server = new Server(new InetSocketAddress("localhost", port));
        server.setHandler(context);
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException("server start failed.", e);
        }

        URI uri = server.getURI();
        LOG.debug("uri={}", uri);

        return uri;
    }

    /**
     * Jettyにジョインする。
     */
    public void join() {
        if (server == null) {
            throw new RuntimeException("server has not been started.");
        }

        try {
            server.join();
        } catch (Exception e) {
            throw new RuntimeException("server join failed.", e);
        }
    }

    /**
     * Jettyを停止する。
     */
    public void stop() {
        if (server == null) {
            throw new RuntimeException("server has not been started.");
        }

        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException("server stop failed.", e);
        }
    }
}
