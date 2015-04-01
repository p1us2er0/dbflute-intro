package org.dbflute.intro.app.web.engine;

import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.DBFluteIntroLogic;
import org.dbflute.intro.app.web.base.DbfluteIntroBaseAction;
import org.dbflute.lastaflute.web.Execute;
import org.dbflute.lastaflute.web.response.JsonResponse;
import org.dbflute.util.DfStringUtil;

public class EngineAction extends DbfluteIntroBaseAction {

    @Resource
    protected DBFluteIntroLogic dbFluteIntroLogic;

    @Execute
    public JsonResponse versions() {
        List<String> dbFluteVersionList = dbFluteIntroLogic.getExistedDBFluteVersionList();
        return asJson(dbFluteVersionList);
    }

    @Execute(urlPattern = "download/{version}")
    public JsonResponse download(String version) {
        // TODO urlPattern
        version = DfStringUtil.substringFirstRear(version, "/");
        dbFluteIntroLogic.downloadDBFlute(version);
        return asJson(true);
    }

    @Execute
    public JsonResponse publicProperties() {
        return asJson(dbFluteIntroLogic.getPublicProperties());
    }
}
