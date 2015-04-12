package org.dbflute.boot;

public class DbfluteIntroMain {

    public static void main(String[] args) throws Exception {
        String mode = System.getProperty("mode");
        if ("swing".equals(mode)) {
            SwingMain.main(args);
        } else {
            JettyMain.main(args);
        }
    }
}
