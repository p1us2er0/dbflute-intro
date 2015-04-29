/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
