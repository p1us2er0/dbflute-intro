package org.dbflute.intro.app.bean;

import org.dbflute.intro.app.definition.DatabaseInfoDef;

/**
 * @author p1us2er0
 */
public class DatabaseInfoDefBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private String databaseName;
    private String driverName;
    private String urlTemplate;
    private String defultSchema;
    private boolean needSchema;
    private boolean needJdbcDriverJar;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DatabaseInfoDefBean(DatabaseInfoDef databaseInfoDef) {
        this.databaseName = databaseInfoDef.databaseName();
        this.driverName = databaseInfoDef.driverName();
        this.urlTemplate = databaseInfoDef.getUrlTemplate();
        this.defultSchema = databaseInfoDef.getDefultSchema();
        this.needSchema = databaseInfoDef.needSchema();
        this.needJdbcDriverJar = databaseInfoDef.needJdbcDriverJar();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getDatabaseName() {
        return databaseName;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public String getDefultSchema() {
        return defultSchema;
    }

    public boolean isNeedJdbcDriverJar() {
        return needJdbcDriverJar;
    }

    public boolean isNeedSchema() {
        return needSchema;
    }
}
