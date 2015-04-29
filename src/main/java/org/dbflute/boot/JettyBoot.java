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

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

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

public class JettyBoot {

    /**
     * Jettyを起動します。
     * @param args プログラム引数
     */
    public static void main(String[] args) {

        URL warLocation = JettyBoot.class.getProtectionDomain().getCodeSource().getLocation();
        String path;
        try {
            path = warLocation.toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
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

        String port = System.getProperty("port");

        if (port == null) {
            for (String app : Arrays.asList("dbfluteIntro", "dbflute")) {
                String conf = app + "_config.properties";
                try (InputStream inputStream = JettyBoot.class.getClassLoader().getResourceAsStream(conf)) {
                    if (inputStream == null) {
                        continue;
                    }
                    Properties properties = new Properties();
                    properties.load(inputStream);
                    port = properties.getProperty("server.port");
                    break;
                } catch (IOException ignore) {
                    continue;
                }
            }
        }

        Server server = new Server(new InetSocketAddress("localhost", toInt(port, 0)));
        server.setHandler(context);
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(server.getURI());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 数字に変換します。
     * @param str 文字列
     * @param defaultValue デフォルト値
     * @return 数字
     */
    private static int toInt(final String str, final int defaultValue) {
        if (str == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
