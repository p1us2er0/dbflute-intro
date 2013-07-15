package org.dbflute.intro;

/**
 * @author jflute
 * @author ecode
 * @since 0.1.0 (2007/08/11 Saturday)
 */
public class DatabaseDto {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private String url;
    private String schema;
    private String user;
    private String password;

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
