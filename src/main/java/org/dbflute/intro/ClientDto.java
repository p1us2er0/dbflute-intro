package org.dbflute.intro;

/**
 * @author jflute
 * @author ecode
 * @since 0.1.0 (2007/08/11 Saturday)
 */
public class ClientDto {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private String project;
    private String database;
    private String targetLanguage;
    private String targetContainer;
    private String packageBase;
    private String jdbcDriver;
    private DatabaseDto databaseDto;
    private DatabaseDto systemUserDatabaseDto;
    private String jdbcDriverJarPath;
    private String dbfluteVersion;
    private OptionDto optionDto;

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

    public DatabaseDto getDatabaseDto() {
        if (databaseDto == null) {
            databaseDto = new DatabaseDto();
        }

        return databaseDto;
    }

    public void setDatabaseDto(DatabaseDto databaseDto) {
        this.databaseDto = databaseDto;
    }

    public DatabaseDto getSystemUserDatabaseDto() {
        if (systemUserDatabaseDto == null) {
            systemUserDatabaseDto = new DatabaseDto();
        }

        return systemUserDatabaseDto;
    }

    public void setSystemUserDatabaseDto(DatabaseDto systemUserDatabaseDto) {
        this.systemUserDatabaseDto = systemUserDatabaseDto;
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

    public OptionDto getOptionDto() {
        if (optionDto == null) {
            optionDto = new OptionDto();
        }

        return optionDto;
    }

    public void setOptionDto(OptionDto optionDto) {
        this.optionDto = optionDto;
    }
}
