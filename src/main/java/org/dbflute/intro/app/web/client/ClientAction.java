package org.dbflute.intro.app.web.client;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dbflute.intro.app.bean.ClientBean;
import org.dbflute.intro.app.bean.DatabaseBean;
import org.dbflute.intro.app.logic.DbFluteClientLogic;
import org.dbflute.intro.app.web.base.DbfluteIntroBaseAction;
import org.dbflute.lastaflute.web.Execute;
import org.dbflute.lastaflute.web.response.JsonResponse;
import org.dbflute.util.DfStringUtil;

/**
 * @author p1us2er0
 */
public class ClientAction extends DbfluteIntroBaseAction {

    @Resource
    protected DbFluteClientLogic dbFluteClientLogic;

    @Execute
    public JsonResponse list(ClientSearchForm clientSearchForm) {
        List<String> projectList = dbFluteClientLogic.getProjectList();
        return asJson(projectList);
    }

    @Execute(urlPattern = "detail/{project}")
    public JsonResponse detail(String project) {
        // TODO urlPattern
        project = DfStringUtil.substringFirstRear(project, "/");
        ClientBean clientBean = dbFluteClientLogic.convClientBeanFromDfprop(project);
        return asJson(clientBean);
    }

    @Execute
    public JsonResponse create(ClientCreateForm clientCreateForm) {
        ClientBean clientBean = new ClientBean(); // TODO convert
        Map<String, DatabaseBean> schemaSyncCheckMap = new LinkedHashMap<String, DatabaseBean>();
        dbFluteClientLogic.createNewClient(clientBean, schemaSyncCheckMap);
        return asJson(clientBean);
    }
}
