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
    private String jdbcDriverJarPath;
    private String dbfluteVersion;
    private String aliasDelimiterInDbComment = ":";
    private boolean isDbCommentOnAliasBasis = true;
    private boolean isCheckColumnDefOrderDiff = true;
    private boolean isCheckDbCommentDiff = true;
    private boolean isCheckProcedureDiff = true;
    private boolean isGenerateProcedureParameterBean = true;
    private String procedureSynonymHandlingType = "INCLUDE";

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

    public String getAliasDelimiterInDbComment() {
        return aliasDelimiterInDbComment;
    }

    public void setAliasDelimiterInDbComment(String aliasDelimiterInDbComment) {
        this.aliasDelimiterInDbComment = aliasDelimiterInDbComment;
    }

    public boolean isDbCommentOnAliasBasis() {
        return isDbCommentOnAliasBasis;
    }

    public void setDbCommentOnAliasBasis(boolean isDbCommentOnAliasBasis) {
        this.isDbCommentOnAliasBasis = isDbCommentOnAliasBasis;
    }

    public boolean isCheckColumnDefOrderDiff() {
        return isCheckColumnDefOrderDiff;
    }

    public void setCheckColumnDefOrderDiff(boolean isCheckColumnDefOrderDiff) {
        this.isCheckColumnDefOrderDiff = isCheckColumnDefOrderDiff;
    }

    public boolean isCheckDbCommentDiff() {
        return isCheckDbCommentDiff;
    }

    public void setCheckDbCommentDiff(boolean isCheckDbCommentDiff) {
        this.isCheckDbCommentDiff = isCheckDbCommentDiff;
    }

    public boolean isCheckProcedureDiff() {
        return isCheckProcedureDiff;
    }

    public void setCheckProcedureDiff(boolean isCheckProcedureDiff) {
        this.isCheckProcedureDiff = isCheckProcedureDiff;
    }

    public boolean isGenerateProcedureParameterBean() {
        return isGenerateProcedureParameterBean;
    }

    public void setGenerateProcedureParameterBean(boolean isGenerateProcedureParameterBean) {
        this.isGenerateProcedureParameterBean = isGenerateProcedureParameterBean;
    }

    public String getProcedureSynonymHandlingType() {
        return procedureSynonymHandlingType;
    }

    public void setProcedureSynonymHandlingType(String procedureSynonymHandlingType) {
        this.procedureSynonymHandlingType = procedureSynonymHandlingType;
    }
}
