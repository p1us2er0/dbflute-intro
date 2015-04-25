package org.dbflute.intro.app.web.client;

import org.dbflute.intro.app.bean.ClientBean;

/**
 * @author jflute
 * @author p1us2er0
 * @since 0.1.0 (2007/08/11 Saturday)
 */
public class ClientResponseBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========

    private ClientBean clientBean;
    private boolean schemahtml;
    private boolean historyhtml;
    private boolean replaceSchema;

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public ClientBean getClientBean() {
        return clientBean;
    }

    public void setClientBean(ClientBean clientBean) {
        this.clientBean = clientBean;
    }

    public boolean isSchemahtml() {
        return schemahtml;
    }

    public void setSchemahtml(boolean schemahtml) {
        this.schemahtml = schemahtml;
    }

    public boolean isHistoryhtml() {
        return historyhtml;
    }

    public void setHistoryhtml(boolean historyhtml) {
        this.historyhtml = historyhtml;
    }

    public boolean isReplaceSchema() {
        return replaceSchema;
    }

    public void setReplaceSchema(boolean replaceSchema) {
        this.replaceSchema = replaceSchema;
    }
}
