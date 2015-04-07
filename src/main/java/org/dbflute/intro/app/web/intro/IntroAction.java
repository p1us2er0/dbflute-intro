package org.dbflute.intro.app.web.intro;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.DbFluteIntroLogic;
import org.dbflute.intro.app.web.base.DbfluteIntroBaseAction;
import org.dbflute.lastaflute.web.Execute;
import org.dbflute.lastaflute.web.response.JsonResponse;

public class IntroAction extends DbfluteIntroBaseAction {

    @Resource
    protected DbFluteIntroLogic dbFluteIntroLogic;

    @Execute
    public JsonResponse manifest() {
        return asJson(dbFluteIntroLogic.getManifestMap());
    }

    @Execute
    public JsonResponse publicProperties() {
        return asJson(dbFluteIntroLogic.getPublicProperties());
    }
}
