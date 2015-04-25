package org.dbflute.intro.app.bean;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author jflute
 * @author p1us2er0
 * @since 0.1.0 (2007/08/11 Saturday)
 */
public class ClientBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @NotBlank
    private String project;
    @NotBlank
    private String database;
    @NotBlank
    private String targetLanguage;
    @NotBlank
    private String targetContainer;
    @NotBlank
    private String packageBase;
    @NotBlank
    private String jdbcDriver;
    @NotNull
    @Valid
    private DatabaseBean databaseBean;
    @NotNull
    @Valid
    private DatabaseBean systemUserDatabaseBean;
    private String jdbcDriverJarPath;
    @NotBlank
    private String dbfluteVersion;
    @NotNull
    @Valid
    private OptionBean optionBean;
    @NotNull
    private Map<String, DatabaseBean> schemaSyncCheckMap;

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getProject() {
        return project;
    }

    public void setProject(String projectName) {
        this.project = projectName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getTargetContainer() {
        return targetContainer;
    }

    public void setTargetContainer(String targetContainer) {
        this.targetContainer = targetContainer;
    }

    public String getPackageBase() {
        return packageBase;
    }

    public void setPackageBase(String packageBase) {
        this.packageBase = packageBase;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public DatabaseBean getDatabaseBean() {
        if (databaseBean == null) {
            databaseBean = new DatabaseBean();
        }

        return databaseBean;
    }

    public void setDatabaseBean(DatabaseBean databaseBean) {
        this.databaseBean = databaseBean;
    }

    public DatabaseBean getSystemUserDatabaseBean() {
        if (systemUserDatabaseBean == null) {
            systemUserDatabaseBean = new DatabaseBean();
        }

        return systemUserDatabaseBean;
    }

    public void setSystemUserDatabaseBean(DatabaseBean systemUserDatabaseBean) {
        this.systemUserDatabaseBean = systemUserDatabaseBean;
    }

    public String getJdbcDriverJarPath() {
        return jdbcDriverJarPath;
    }

    public void setJdbcDriverJarPath(String jdbcDriverJarPath) {
        this.jdbcDriverJarPath = jdbcDriverJarPath;
    }

    public String getDbfluteVersion() {
        return dbfluteVersion;
    }

    public void setDbfluteVersion(String versionInfoDBFlute) {
        this.dbfluteVersion = versionInfoDBFlute;
    }

    public OptionBean getOptionBean() {
        if (optionBean == null) {
            optionBean = new OptionBean();
        }

        return optionBean;
    }

    public void setOptionBean(OptionBean optionBean) {
        this.optionBean = optionBean;
    }

    public Map<String, DatabaseBean> getSchemaSyncCheckMap() {
        if (schemaSyncCheckMap == null) {
            schemaSyncCheckMap = new LinkedHashMap<String, DatabaseBean>();
        }

        return schemaSyncCheckMap;
    }

    public void setSchemaSyncCheckMap(Map<String, DatabaseBean> schemaSyncCheckMap) {
        this.schemaSyncCheckMap = schemaSyncCheckMap;
    }
}
