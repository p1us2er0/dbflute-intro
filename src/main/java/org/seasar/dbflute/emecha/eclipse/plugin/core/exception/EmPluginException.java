package org.seasar.dbflute.emecha.eclipse.plugin.core.exception;

/**
 * @author jflute
 * @since 0.1.0 (2007/07/21 Saturday)
 */
public class EmPluginException extends RuntimeException {

    private static final long serialVersionUID = 6881734980455058322L;

    private Throwable coreException;

    public EmPluginException(String msg) {
        super(msg);
    }

    public EmPluginException(String msg, Throwable t) {
        super(msg, t);
        this.coreException = t;
    }

    public Throwable getCoreException() {
        return coreException;
    }

    public void setCoreException(Throwable coreException) {
        this.coreException = coreException;
    }
}
