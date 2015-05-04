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
package org.lastaflute.jetty;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

/**
 * @author p1us2er0
 * @author jflute
 */
public class JettyBoot {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    protected static final String DEFAULT_MARK_DIR = "/tmp/lastaflute/jettyboot";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final int port;
    protected final String contextPath;
    protected boolean development;
    protected boolean suppressShutdownHook;
    protected boolean suppressBrowseOnDesktop;

    protected Server server;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public JettyBoot(int port) {
        this(port, null);
    }

    public JettyBoot(int port, String contextPath) {
        super();
        this.port = port;
        this.contextPath = contextPath;
    }

    public JettyBoot asDevelopment() {
        development = true;
        return this;
    }

    public JettyBoot suppressShutdownHook() {
        suppressShutdownHook = true;
        return this;
    }

    public JettyBoot suppressBrowseOnDesktop() {
        suppressBrowseOnDesktop = true;
        return this;
    }

    // ===================================================================================
    //                                                                               Boot
    //                                                                              ======
    public URI boot() {
        System.out.println("...Booting the Jetty: port=" + port + " contextPath=" + contextPath);
        if (development) {
            registerShutdownHook();
        }
        prepareServer();
        final URI uri = startServer();
        System.out.println("Boot successful" + (development ? " as development" : "") + ": uri=" + uri);
        if (development) {
            browseOnDesktop(uri);
        }
        join();
        return uri;
    }

    protected void prepareServer() {
        final WebAppContext context = prepareWebAppContext();
        server = new Server(new InetSocketAddress("localhost", port));
        server.setHandler(context);
    }

    protected URI startServer() {
        try {
            server.start();
        } catch (Exception e) {
            throw new IllegalStateException("server start failed.", e);
        }
        return server.getURI();
    }

    protected WebAppContext prepareWebAppContext() {
        final URL warLocation = JettyBoot.class.getProtectionDomain().getCodeSource().getLocation();
        final String path;
        try {
            path = warLocation.toURI().getPath();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("server start failed.", e);
        }
        final WebAppContext context = new WebAppContext();
        if (path.endsWith(".war")) {
            context.setWar(warLocation.toExternalForm());
        } else {
            context.setResourceBase("./src/main/webapp/");
        }
        final Configuration[] configurations = { new WebInfConfiguration(), new WebXmlConfiguration(),
                new MetaInfConfiguration(), new JettyWebXmlConfiguration() };
        context.setConfigurations(configurations);
        if (contextPath != null) {
            context.setContextPath(contextPath);
        }
        return context;
    }

    protected void join() {
        try {
            server.join();
        } catch (Exception e) {
            throw new IllegalStateException("server join failed.", e);
        }
    }

    // ===================================================================================
    //                                                                         Development
    //                                                                         ===========
    // -----------------------------------------------------
    //                                         Shutdown Hook
    //                                         -------------
    protected void registerShutdownHook() {
        if (suppressShutdownHook) {
            return;
        }
        final File markFile = prepareMarkFile();
        final long lastModified = markFile.lastModified();
        final String exp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date(lastModified));
        System.out.println("...Registering the shutdown hook for the Jetty: lastModified=" + exp);
        new Thread(() -> {
            while (true) {
                if (needsShutdown(markFile, lastModified)) {
                    shutdownForcedly();
                    break;
                }
                waitForNextShuwdownHook();
            }
        }).start();
    }

    protected File prepareMarkFile() {
        final File markFile = new File(buildMarkFilePath());
        if (markFile.exists()) {
            markFile.setLastModified(System.currentTimeMillis());
        } else {
            markFile.mkdirs();
            try {
                markFile.createNewFile();
            } catch (IOException e) {
                throw new IllegalStateException("Failed to create new file: " + markFile, e);
            }
        }
        return markFile;
    }

    protected String buildMarkFilePath() {
        return getMarkDir() + "/boot" + port + ".dfmark";
    }

    protected String getMarkDir() {
        return DEFAULT_MARK_DIR;
    }

    protected boolean needsShutdown(File markFile, long lastModified) {
        return !markFile.exists() || lastModified != markFile.lastModified();
    }

    protected void shutdownForcedly() {
        System.out.println("...Shuting down the Jetty forcedly: port=" + port);
        close();
    }

    protected void waitForNextShuwdownHook() {
        try {
            Thread.sleep(getShuwdownHookWaitMillis());
        } catch (InterruptedException e) {
            throw new IllegalStateException("Failed to sleep the thread.", e);
        }
    }

    protected long getShuwdownHookWaitMillis() {
        return 2000L;
    }

    // -----------------------------------------------------
    //                                                Browse
    //                                                ------
    protected void browseOnDesktop(final URI uri) {
        if (suppressBrowseOnDesktop) {
            return;
        }
        final java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            desktop.browse(uri);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to browse the URI: " + uri, e);
        }
    }

    // ===================================================================================
    //                                                                               Close
    //                                                                               =====
    public void close() {
        if (server == null) {
            throw new IllegalStateException("server has not been started.");
        }
        try {
            server.stop();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to stop the Jetty.", e);
        } finally {
            try {
                server.destroy();
            } catch (RuntimeException e) {
                throw new IllegalStateException("Failed to destroy the Jetty.", e);
            }
        }
    }
}
