package org.seasar.dbflute.intro.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Monitor {

    public static void output(String message) {
        System.out.println(message);
    }

    public static String input() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            final String line = br.readLine();
            return line;
        } catch (IOException e) {
            String msg = "Failed to read system input.";
            throw new IllegalStateException(msg, e);
        }
    }
}
