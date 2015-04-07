package org.dbflute.dbFlute.mylasta.direction;

import org.dbflute.dbFlute.mylasta.direction.DbfluteEnv;

/**
 * @author FreeGen
 */
public interface DbfluteConfig extends DbfluteEnv {

    /** The key of the configuration. e.g. should be overridden by each domain */
    String DOMAIN_TITLE = "domain.title";

    /** The key of the configuration. e.g. / */
    String COOKIE_DEFAULT_PATH = "cookie.default.path";

    /** The key of the configuration. e.g. 31556926 */
    String COOKIE_DEFAULT_EXPIRE = "cookie.default.expire";

    /** The key of the configuration. e.g. 315360000 */
    String COOKIE_ETERNAL_EXPIRE = "cookie.eternal.expire";

    /** The key of the configuration. e.g. 4 */
    String PAGING_PAGE_SIZE = "paging.page.size";

    /** The key of the configuration. e.g. 3 */
    String PAGING_PAGE_RANGE_SIZE = "paging.page.range.size";

    /** The key of the configuration. e.g. true */
    String PAGING_PAGE_RANGE_FILL_LIMIT = "paging.page.range.fill.limit";

    /** The key of the configuration. e.g. 1930 */
    String MINIMUM_YEAR = "minimum.year";

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
     * The value is, e.g. should be overridden by each domain <br />
     * comment: The title of domain the application for logging
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String getDomainTitle();

    /**
     * Get the value for the key 'cookie.default.path'. <br />
     * The value is, e.g. / <br />
     * comment: The default path of cookie (basically '/' if no context path)
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String getCookieDefaultPath();

    /**
     * Get the value for the key 'cookie.default.expire'. <br />
     * The value is, e.g. 31556926 <br />
     * comment: The default expire of cookie in seconds e.g. 31556926: one year, 86400: one day
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String getCookieDefaultExpire();

    /**
     * Get the value for the key 'cookie.default.expire' as {@link Integer}. <br />
     * The value is, e.g. 31556926 <br />
     * comment: The default expire of cookie in seconds e.g. 31556926: one year, 86400: one day
     * @return The value of found property. (NullAllowed: if null, not found)
     * @throws NumberFormatException When the property is not integer.
     */
    Integer getCookieDefaultExpireAsInteger();

    /**
     * Get the value for the key 'cookie.eternal.expire'. <br />
     * The value is, e.g. 315360000 <br />
     * comment: The eternal expire of cookie in seconds e.g. 315360000: ten year, 86400: one day
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String getCookieEternalExpire();

    /**
     * Get the value for the key 'cookie.eternal.expire' as {@link Integer}. <br />
     * The value is, e.g. 315360000 <br />
     * comment: The eternal expire of cookie in seconds e.g. 315360000: ten year, 86400: one day
     * @return The value of found property. (NullAllowed: if null, not found)
     * @throws NumberFormatException When the property is not integer.
     */
    Integer getCookieEternalExpireAsInteger();

    /**
     * Get the value for the key 'paging.page.size'. <br />
     * The value is, e.g. 4 <br />
     * comment: The size of one page for paging
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String getPagingPageSize();

    /**
     * Get the value for the key 'paging.page.size' as {@link Integer}. <br />
     * The value is, e.g. 4 <br />
     * comment: The size of one page for paging
     * @return The value of found property. (NullAllowed: if null, not found)
     * @throws NumberFormatException When the property is not integer.
     */
    Integer getPagingPageSizeAsInteger();

    /**
     * Get the value for the key 'paging.page.range.size'. <br />
     * The value is, e.g. 3 <br />
     * comment: The size of page range for paging
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String getPagingPageRangeSize();

    /**
     * Get the value for the key 'paging.page.range.size' as {@link Integer}. <br />
     * The value is, e.g. 3 <br />
     * comment: The size of page range for paging
     * @return The value of found property. (NullAllowed: if null, not found)
     * @throws NumberFormatException When the property is not integer.
     */
    Integer getPagingPageRangeSizeAsInteger();

    /**
     * Get the value for the key 'paging.page.range.fill.limit'. <br />
     * The value is, e.g. true <br />
     * comment: The option 'fillLimit' of page range for paging
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String getPagingPageRangeFillLimit();

    /**
     * Is the property for the key 'paging.page.range.fill.limit' true? <br />
     * The value is, e.g. true <br />
     * comment: The option 'fillLimit' of page range for paging
     * @return The determination, true or false. (if the property can be true, returns true)
     */
    boolean isPagingPageRangeFillLimit();

    /**
     * Get the value for the key 'minimum.year'. <br />
     * The value is, e.g. 1930 <br />
     * comment: The minimum year of this project
     * @return The value of found property. (NullAllowed: if null, not found)
     */
    String getMinimumYear();

    /**
     * Get the value for the key 'minimum.year' as {@link Integer}. <br />
     * The value is, e.g. 1930 <br />
     * comment: The minimum year of this project
     * @return The value of found property. (NullAllowed: if null, not found)
     * @throws NumberFormatException When the property is not integer.
     */
    Integer getMinimumYearAsInteger();

    /**
     * The simple implementation for configuration.
     * @author FreeGen
     */
    public static class SimpleImpl extends DbfluteEnv.SimpleImpl implements DbfluteConfig {

        /** The serial version UID for object serialization. (Default) */
        private static final long serialVersionUID = 1L;

        public String getDomainTitle() {
            return get(DbfluteConfig.DOMAIN_TITLE);
        }

        public String getCookieDefaultPath() {
            return get(DbfluteConfig.COOKIE_DEFAULT_PATH);
        }

        public String getCookieDefaultExpire() {
            return get(DbfluteConfig.COOKIE_DEFAULT_EXPIRE);
        }

        public Integer getCookieDefaultExpireAsInteger() {
            return getAsInteger(DbfluteConfig.COOKIE_DEFAULT_EXPIRE);
        }

        public String getCookieEternalExpire() {
            return get(DbfluteConfig.COOKIE_ETERNAL_EXPIRE);
        }

        public Integer getCookieEternalExpireAsInteger() {
            return getAsInteger(DbfluteConfig.COOKIE_ETERNAL_EXPIRE);
        }

        public String getPagingPageSize() {
            return get(DbfluteConfig.PAGING_PAGE_SIZE);
        }

        public Integer getPagingPageSizeAsInteger() {
            return getAsInteger(DbfluteConfig.PAGING_PAGE_SIZE);
        }

        public String getPagingPageRangeSize() {
            return get(DbfluteConfig.PAGING_PAGE_RANGE_SIZE);
        }

        public Integer getPagingPageRangeSizeAsInteger() {
            return getAsInteger(DbfluteConfig.PAGING_PAGE_RANGE_SIZE);
        }

        public String getPagingPageRangeFillLimit() {
            return get(DbfluteConfig.PAGING_PAGE_RANGE_FILL_LIMIT);
        }

        public boolean isPagingPageRangeFillLimit() {
            return is(DbfluteConfig.PAGING_PAGE_RANGE_FILL_LIMIT);
        }

        public String getMinimumYear() {
            return get(DbfluteConfig.MINIMUM_YEAR);
        }

        public Integer getMinimumYearAsInteger() {
            return getAsInteger(DbfluteConfig.MINIMUM_YEAR);
        }
    }
}
