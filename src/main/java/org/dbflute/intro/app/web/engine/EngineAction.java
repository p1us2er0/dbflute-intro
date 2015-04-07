package org.dbflute.intro.app.web.engine;

import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.DbFluteEngineLogic;
import org.dbflute.intro.app.web.base.DbfluteIntroBaseAction;
import org.dbflute.lastaflute.web.Execute;
import org.dbflute.lastaflute.web.response.JsonResponse;

public class EngineAction extends DbfluteIntroBaseAction {

    @Resource
    protected DbFluteEngineLogic dbFluteEngineLogic;

    @Execute
    public JsonResponse versions() {
        List<String> dbFluteVersionList = dbFluteEngineLogic.getExistedVersionList();
        return asJson(dbFluteVersionList);
    }

    @Execute
    public JsonResponse download(String version) {
        dbFluteEngineLogic.download(version);
        return asJson(true);
    }
}
