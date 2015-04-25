package org.dbflute.intro.app.web.client;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.intro.app.bean.ClientBean;
import org.dbflute.intro.app.bean.DatabaseBean;
import org.dbflute.lastaflute.web.api.JsonBody;

@JsonBody
public class ClientForm {

    @NotNull
    @Valid
    public ClientBean clientBean = new ClientBean();

    public Map<String, DatabaseBean> schemaSyncCheckMap = new LinkedHashMap<String, DatabaseBean>();
}
