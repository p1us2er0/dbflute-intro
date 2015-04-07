package org.dbflute.dbFlute.mylasta.direction;

import org.dbflute.dbFlute.mylasta.direction.DbfluteConfig;

/**
 * @author FreeGen
 */
public interface DbFluteIntroConfig extends DbfluteConfig {

    /** The key of the configuration. e.g. DbfluteIntro */
    String DOMAIN_TITLE = "domain.title";

    /** The key of the configuration. e.g. DbfluteIntro */
    String COOKIE_AUTO_LOGIN_DOCKSIDE_KEY = "cookie.auto.login.dockside.key";

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
     * Get the value for the key 'domain.title'. <br />
     * The value is, e.g. DbfluteIntro <br />
     * comment: @Override The title of domain the application for logging
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String getDomainTitle();

    /**
     * Get the value for the key 'cookie.auto.login.dockside.key'. <br />
     * The value is, e.g. DbfluteIntro <br />
     * comment: The cookie key of auto-login for Dockside
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String getCookieAutoLoginDocksideKey();

    /**
     * The simple implementation for configuration.
     * @author FreeGen
     */
    public static class SimpleImpl extends DbfluteConfig.SimpleImpl implements DbFluteIntroConfig {

        /** The serial version UID for object serialization. (Default) */
        private static final long serialVersionUID = 1L;

        @Override
        public String getDomainTitle() {
            return get(DbFluteIntroConfig.DOMAIN_TITLE);
        }

        public String getCookieAutoLoginDocksideKey() {
            return get(DbFluteIntroConfig.COOKIE_AUTO_LOGIN_DOCKSIDE_KEY);
        }
    }
}
