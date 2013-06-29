package org.seasar.dbflute.emecha.eclipse.plugin.core.exception;

/**
 * @author jflute
 * @since 0.1.0 (2007/07/21 Saturday)
 */
public class EmExceptionHandler {

    public static void show(Throwable cause) {
        cause.printStackTrace();
    }
    
    public static void show(String msg, Throwable cause) {
        System.out.println(msg);
        cause.printStackTrace();
    }

    public static void throwAsPluginException(String msg, Throwable t) {
        throw new EmPluginException(msg, t);
    }
}
