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
    private boolean upperSchema;
    private boolean assistInputUser;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DatabaseInfoDefBean(DatabaseInfoDef databaseInfoDef) {
        this.databaseName = databaseInfoDef.getDatabaseName();
        this.driverName = databaseInfoDef.getDriverName();
        this.urlTemplate = databaseInfoDef.getUrlTemplate();
        this.defultSchema = databaseInfoDef.getDefultSchema();
        this.needSchema = databaseInfoDef.isNeedSchema();
        this.needJdbcDriverJar = databaseInfoDef.isNeedJdbcDriverJar();
        this.upperSchema = databaseInfoDef.isUpperSchema();
        this.assistInputUser = databaseInfoDef.isAssistInputUser();
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

    public boolean isUpperSchema() {
        return upperSchema;
    }

    public boolean isAssistInputUser() {
        return assistInputUser;
    }
}
