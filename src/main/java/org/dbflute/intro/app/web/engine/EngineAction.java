package org.dbflute.intro.app.web.engine;

import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.DbFluteEngineLogic;
import org.dbflute.intro.app.web.base.DbfluteIntroBaseAction;
import org.dbflute.lastaflute.web.Execute;
import org.dbflute.lastaflute.web.response.JsonResponse;
import org.dbflute.util.DfStringUtil;

public class EngineAction extends DbfluteIntroBaseAction {

    @Resource
    protected DbFluteEngineLogic dbFluteEngineLogic;

    @Execute
    public JsonResponse versions() {
        List<String> dbFluteVersionList = dbFluteEngineLogic.getExistedVersionList();
        return asJson(dbFluteVersionList);
    }

    @Execute(urlPattern = "download/{version}")
    public JsonResponse download(String version) {
        // TODO urlPattern
        version = DfStringUtil.substringFirstRear(version, "/");
        dbFluteEngineLogic.download(version);
        return asJson(true);
    }
}
