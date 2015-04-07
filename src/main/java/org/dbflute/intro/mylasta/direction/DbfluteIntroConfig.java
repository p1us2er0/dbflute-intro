package org.dbflute.intro.mylasta.direction;


/**
 * @author FreeGen
 */
public interface DbfluteIntroConfig extends DbfluteConfig {

    /** The key of the configuration. e.g. DbfluteIntro */
    String DOMAIN_TITLE = "domain.title";

    /** The key of the configuration. e.g. DCK */
    String COOKIE_AUTO_LOGIN_DBFLUTE_INTRO_KEY = "cookie.auto.login.dbflute.intro.key";

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
     * The value is, e.g. DBFluteIntro <br />
     * comment: @Override The title of domain the application for logging
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String getDomainTitle();

    /**
     * Get the value for the key 'cookie.auto.login.dBFluteIntro.key'. <br />
     * The value is, e.g. DCK <br />
     * comment: The cookie key of auto-login for DBFluteIntro
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String getCookieAutoLoginDBFluteIntroKey();

    /**
     * The simple implementation for configuration.
     * @author FreeGen
     */
    public static class SimpleImpl extends DbfluteConfig.SimpleImpl implements DbfluteIntroConfig {

        /** The serial version UID for object serialization. (Default) */
        private static final long serialVersionUID = 1L;

        @Override
        public String getDomainTitle() {
            return get(DbfluteIntroConfig.DOMAIN_TITLE);
        }

        @Override
        public String getCookieAutoLoginDBFluteIntroKey() {
            return get(DbfluteIntroConfig.COOKIE_AUTO_LOGIN_DBFLUTE_INTRO_KEY);
        }
    }
}
