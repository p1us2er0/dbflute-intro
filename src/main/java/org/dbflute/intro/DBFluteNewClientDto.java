package org.dbflute.intro;

/**
 * @author jflute
 * @author ecode
 * @since 0.1.0 (2007/08/11 Saturday)
 */
public class DBFluteNewClientDto {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private String outputDirectory;
    private String project;
    private String database;
    private String targetContainer;
    private String packageBase;
    private String databaseInfoDriver;
    private String databaseInfoUrl;
    private String databaseInfoSchema;
    private String databaseInfoUser;
    private String databaseInfoPassword;
    private String jdbcDriverJarPath;
    private String versionInfoDBFlute;
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
    public String getDatabase() {
        return database;
    }

    public void setDatabase(String targetDatabase) {
        this.database = targetDatabase;
    }

    public String getTargetContainer() {
        return targetContainer;
    }

    public void setTargetContainer(String targetContainer) {
        this.targetContainer = targetContainer;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String containerName) {
        this.outputDirectory = containerName;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String projectName) {
        this.project = projectName;
    }

    public String getPackageBase() {
        return packageBase;
    }

    public void setPackageBase(String packageBase) {
        this.packageBase = packageBase;
    }

    public String getDatabaseInfoDriver() {
        return databaseInfoDriver;
    }

    public void setDatabaseInfoDriver(String databaseInfoDriver) {
        this.databaseInfoDriver = databaseInfoDriver;
    }

    public String getDatabaseInfoUrl() {
        return databaseInfoUrl;
    }

    public void setDatabaseInfoUrl(String databaseInfoUrl) {
        this.databaseInfoUrl = databaseInfoUrl;
    }

    public String getDatabaseInfoPassword() {
        return databaseInfoPassword;
    }

    public void setDatabaseInfoPassword(String databaseInfoPassword) {
        this.databaseInfoPassword = databaseInfoPassword;
    }

    public String getDatabaseInfoSchema() {
        return databaseInfoSchema;
    }

    public void setDatabaseInfoSchema(String databaseInfoSchema) {
        this.databaseInfoSchema = databaseInfoSchema;
    }

    public String getDatabaseInfoUser() {
        return databaseInfoUser;
    }

    public void setDatabaseInfoUser(String databaseInfoUser) {
        this.databaseInfoUser = databaseInfoUser;
    }

    public String getJdbcDriverJarPath() {
        return jdbcDriverJarPath;
    }

    public void setJdbcDriverJarPath(String jdbcDriverJarPath) {
        this.jdbcDriverJarPath = jdbcDriverJarPath;
    }

    public String getVersionInfoDBFlute() {
        return versionInfoDBFlute;
    }

    public void setVersionInfoDBFlute(String versionInfoDBFlute) {
        this.versionInfoDBFlute = versionInfoDBFlute;
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
