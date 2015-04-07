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
package org.dbflute.intro.app.web.base;

import org.dbflute.intro.app.base.DbfluteBaseAction;
import org.dbflute.intro.mylasta.action.DbfluteIntroMessages;
import org.dbflute.lastaflute.web.callback.ActionRuntimeMeta;
import org.dbflute.lastaflute.web.login.UserBean;
import org.dbflute.optional.OptionalObject;
import org.dbflute.optional.OptionalThing;

/**
 * @author jflute
 */
public abstract class DbfluteIntroBaseAction extends DbfluteBaseAction {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The application type for DbfluteIntro, e.g. used by access context. */
    protected static final String APP_TYPE = "DbfluteIntro";

    /** The user type for Member, e.g. used by access context. */
    protected static final String USER_TYPE = "M";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------

    // -----------------------------------------------------
    //                                          Display Data
    //                                          ------------
    public DbfluteIntroUserWebBean userWebBean;

    // ===================================================================================
    //                                                                    Application Info
    //                                                                    ================
    @Override
    protected String myAppType() {
        return APP_TYPE;
    }

    // ===================================================================================
    //                                                                      Login Handling
    //                                                                      ==============
    @Override
    protected OptionalThing<UserBean> myUserBean() { // for interface handling
        return OptionalObject.empty();
    }

    @Override
    protected OptionalThing<String> myUserType() {
        return OptionalObject.of(USER_TYPE);
    }

    // ===================================================================================
    //                                                                             Message
    //                                                                             =======
    @Override
    protected DbfluteIntroMessages createMessages() {
        return new DbfluteIntroMessages();
    }

    // ===================================================================================
    //                                                                            Callback
    //                                                                            ========
    // -----------------------------------------------------
    //                                      God Hand Finally
    //                                      ----------------
    @Override
    public void godHandFinally(ActionRuntimeMeta runtimeMeta) {
        super.godHandFinally(runtimeMeta);
    }
}
