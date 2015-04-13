package org.dbflute.boot;

public class DbfluteIntroBoot {

    public static void main(String[] args) {
        String mode = System.getProperty("mode");
        if ("swing".equals(mode)) {
            SwingBoot.main(args);
        } else {
            JettyBoot.main(args);
        }
    }
}
