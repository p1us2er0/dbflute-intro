package org.dbflute.intro.app.bean;

/**
 * @author jflute
 * @author p1us2er0
 * @since 0.1.0 (2007/08/11 Saturday)
 */
public class DatabaseBean {

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
