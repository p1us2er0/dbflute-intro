package org.dbflute.intro;

/**
 * @author jflute
 * @author ecode
 * @since 0.1.0 (2007/08/11 Saturday)
 */
public class OptionDto {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private boolean isDbCommentOnAliasBasis = true;
    private String aliasDelimiterInDbComment = ":";
    private boolean isCheckColumnDefOrderDiff = true;
    private boolean isCheckDbCommentDiff = true;
    private boolean isCheckProcedureDiff = true;
    private boolean isGenerateProcedureParameterBean = true;
    private String procedureSynonymHandlingType = "INCLUDE";

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public boolean isDbCommentOnAliasBasis() {
        return isDbCommentOnAliasBasis;
    }

    public void setDbCommentOnAliasBasis(boolean isDbCommentOnAliasBasis) {
        this.isDbCommentOnAliasBasis = isDbCommentOnAliasBasis;
    }

    public String getAliasDelimiterInDbComment() {
        return aliasDelimiterInDbComment;
    }

    public void setAliasDelimiterInDbComment(String aliasDelimiterInDbComment) {
        this.aliasDelimiterInDbComment = aliasDelimiterInDbComment;
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
