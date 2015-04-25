package org.dbflute.intro.app.bean;

/**
 * @author jflute
 * @author p1us2er0
 * @since 0.1.0 (2007/08/11 Saturday)
 */
public class OptionBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private boolean dbCommentOnAliasBasis = true;
    private String aliasDelimiterInDbComment = ":";
    private boolean checkColumnDefOrderDiff = true;
    private boolean checkDbCommentDiff = true;
    private boolean checkProcedureDiff = true;
    private boolean generateProcedureParameterBean = true;
    private String procedureSynonymHandlingType = "INCLUDE";

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public boolean isDbCommentOnAliasBasis() {
        return dbCommentOnAliasBasis;
    }

    public void setDbCommentOnAliasBasis(boolean isDbCommentOnAliasBasis) {
        this.dbCommentOnAliasBasis = isDbCommentOnAliasBasis;
    }

    public String getAliasDelimiterInDbComment() {
        return aliasDelimiterInDbComment;
    }

    public void setAliasDelimiterInDbComment(String aliasDelimiterInDbComment) {
        this.aliasDelimiterInDbComment = aliasDelimiterInDbComment;
    }

    public boolean isCheckColumnDefOrderDiff() {
        return checkColumnDefOrderDiff;
    }

    public void setCheckColumnDefOrderDiff(boolean isCheckColumnDefOrderDiff) {
        this.checkColumnDefOrderDiff = isCheckColumnDefOrderDiff;
    }

    public boolean isCheckDbCommentDiff() {
        return checkDbCommentDiff;
    }

    public void setCheckDbCommentDiff(boolean isCheckDbCommentDiff) {
        this.checkDbCommentDiff = isCheckDbCommentDiff;
    }

    public boolean isCheckProcedureDiff() {
        return checkProcedureDiff;
    }

    public void setCheckProcedureDiff(boolean isCheckProcedureDiff) {
        this.checkProcedureDiff = isCheckProcedureDiff;
    }

    public boolean isGenerateProcedureParameterBean() {
        return generateProcedureParameterBean;
    }

    public void setGenerateProcedureParameterBean(boolean isGenerateProcedureParameterBean) {
        this.generateProcedureParameterBean = isGenerateProcedureParameterBean;
    }

    public String getProcedureSynonymHandlingType() {
        return procedureSynonymHandlingType;
    }

    public void setProcedureSynonymHandlingType(String procedureSynonymHandlingType) {
        this.procedureSynonymHandlingType = procedureSynonymHandlingType;
    }
}
