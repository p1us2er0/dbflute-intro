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
package org.dbflute.intro.app.boot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.lastaflute.jetty.JettyBoot;

/**
 * @author p1us2er0
 */
public class DbfluteIntroBoot {

    protected static final int DEFAULT_PORT = 9000;

    public static void main(String[] args) {
        JettyBoot jettyBoot = new JettyBoot(getPort());
        jettyBoot.asDevelopment();
        if (!Boolean.getBoolean("browseOnDesktop")) {
            jettyBoot.suppressBrowseOnDesktop();
        }
        jettyBoot.boot();
    }

    private static int getPort() {
        String port = System.getProperty("port");

        if (port == null) {
            for (String app : Arrays.asList("dbfluteIntro", "dbflute")) {
                String conf = app + "_config.properties";
                try (InputStream inputStream = JettyBoot.class.getClassLoader().getResourceAsStream(conf)) {
                    if (inputStream != null) {
                        Properties properties = new Properties();
                        properties.load(inputStream);
                        port = properties.getProperty("server.port");
                    }
                    break;
                } catch (IOException ignore) {
                    continue;
                }
            }
        }

        if (port != null) {
            try {
                return Integer.parseInt(port);
            } catch (NumberFormatException ignore) {
            }
        }

        return DEFAULT_PORT;
    }
}
