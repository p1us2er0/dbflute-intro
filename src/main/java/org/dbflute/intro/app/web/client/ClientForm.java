package org.dbflute.intro.app.web.client;

import java.util.LinkedHashMap;
import java.util.Map;

import org.dbflute.intro.app.bean.ClientBean;
import org.dbflute.intro.app.bean.DatabaseBean;
import org.dbflute.lastaflute.web.api.JsonParameter;

public class ClientForm {

    @JsonParameter
    public ClientBean clientBean = new ClientBean();

    @JsonParameter
    public Map<String, DatabaseBean> schemaSyncCheckMap = new LinkedHashMap<String, DatabaseBean>();
}
