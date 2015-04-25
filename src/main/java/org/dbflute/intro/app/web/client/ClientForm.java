package org.dbflute.intro.app.web.client;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.intro.app.bean.ClientBean;
import org.dbflute.lastaflute.web.api.JsonBody;

@JsonBody
public class ClientForm {

    @NotNull
    @Valid
    public ClientBean clientBean;

    public boolean testConnection = true;
}
