package org.dbflute.intro.app.web.base.messages;

import org.dbflute.lastaflute.web.ruts.message.ActionMessages;

/**
 * The keys for message.
 * @author FreeGen
 */
public class DbfluteLabels extends ActionMessages {

    /** The serial version UID for object serialization. (Default) */
    private static final long serialVersionUID = 1L;

    /**
     * Assert the property is not null.
     * @param property The value of the property. (NotNull)
     */
    protected void assertPropertyNotNull(String property) {
        if (property == null) {
            String msg = "The argument 'property' for message should not be null.";
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * The definition of keys for labels.
     * @author FreeGen
     */
    public static interface LabelKey {
    }
}
