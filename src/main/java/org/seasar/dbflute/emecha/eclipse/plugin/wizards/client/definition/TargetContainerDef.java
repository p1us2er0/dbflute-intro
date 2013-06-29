package org.seasar.dbflute.emecha.eclipse.plugin.wizards.client.definition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jflute
 * @since 0.1.0 (2007/08/12 Sunday)
 */
public class TargetContainerDef {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    protected static java.util.List<TargetContainerDef> TARGET_CONTAINER_LIST = new ArrayList<TargetContainerDef>();
    static {
        TARGET_CONTAINER_LIST.add(new TargetContainerDef("seasar", "2.4"));
        TARGET_CONTAINER_LIST.add(new TargetContainerDef("spring", "2.5"));
        TARGET_CONTAINER_LIST.add(new TargetContainerDef("lucy", "0.5"));
        TARGET_CONTAINER_LIST.add(new TargetContainerDef("guice", "1.0"));
        TARGET_CONTAINER_LIST.add(new TargetContainerDef("slim3", "1.0"));
    }

    // ===================================================================================
    //                                                                 Definition Accessor
    //                                                                 ===================
    public static java.util.List<TargetContainerDef> extractTargetContainerInfoList() {
        return TARGET_CONTAINER_LIST;
    }

    public static java.util.List<String> extractTargetContainerList() {
        final List<TargetContainerDef> list = TARGET_CONTAINER_LIST;
        final ArrayList<String> resultList = new ArrayList<String>();
        for (TargetContainerDef info : list) {
            resultList.add(info.getTargetContainer());
        }
        return resultList;
    }

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String targetContainer;
    protected String targetContainerVersion;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public TargetContainerDef(String targetLanguage, String targetLanguageVersion) {
        this.targetContainer = targetLanguage;
        this.targetContainerVersion = targetLanguageVersion;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getTargetContainer() {
        return targetContainer;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetContainer = targetLanguage;
    }

    public String getTargetContainerVersion() {
        return targetContainerVersion;
    }

    public void setTargetContainerVersion(String targetContainerVersion) {
        this.targetContainerVersion = targetContainerVersion;
    }

}
