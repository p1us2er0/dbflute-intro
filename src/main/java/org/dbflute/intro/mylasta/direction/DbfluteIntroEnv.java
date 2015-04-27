package org.dbflute.intro.mylasta.direction;

import org.dbflute.intro.mylasta.direction.DbfluteConfig;

/**
 * @author FreeGen
 */
public interface DbfluteIntroEnv extends DbfluteConfig {

    /**
     * Get the value of property as {@link String}.
     * @param propertyKey The key of the property. (NotNull)
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String get(String propertyKey);

    /**
     * Is the property true?
     * @param propertyKey The key of the property which is boolean type. (NotNull)
     * @return The determination, true or false. (if the property can be true, returns true)
     */
    boolean is(String propertyKey);

    /**
     * The simple implementation for configuration.
     * @author FreeGen
     */
    public static class SimpleImpl extends DbfluteConfig.SimpleImpl implements DbfluteIntroEnv {

        /** The serial version UID for object serialization. (Default) */
        private static final long serialVersionUID = 1L;
    }
}
