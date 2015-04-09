package org.dbflute.boot;

public class DbfluteIntroMain {

    public static void main(String[] args) throws Exception {
        String mode = System.getProperty("mode");
        if ("server".equals(mode)) {
            JettyMain.main(args);
        } else {
            SwingMain.main(args);
        }
    }
}
