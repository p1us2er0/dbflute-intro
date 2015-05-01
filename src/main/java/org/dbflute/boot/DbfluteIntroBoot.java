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
import java.net.URI;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author p1us2er0
 */
public class DbfluteIntroBoot {

    public static void main(String[] args) {
        JettyBoot jettyBoot = new JettyBoot();
        URI uri = jettyBoot.start(getPort());

        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        jettyBoot.join();
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

        return 0;
    }
}
