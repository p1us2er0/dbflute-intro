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

import org.dbflute.intro.mylasta.action.DbfluteIntroMessages;
import org.dbflute.optional.OptionalObject;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.web.callback.ActionRuntime;
import org.lastaflute.web.login.UserBean;
import org.lastaflute.web.validation.ActionValidator;
import org.lastaflute.web.validation.LaValidatableApi;

/**
 * @author p1us2er0
 */
public abstract class DbfluteIntroBaseAction extends DbfluteBaseAction implements LaValidatableApi<DbfluteIntroMessages> {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The application type for DbfluteIntro, e.g. used by access context. */
    protected static final String APP_TYPE = "DbfluteIntro";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Override
    public final void hookFinally(ActionRuntime runtime) {
        super.hookFinally(runtime);
    }

    // ===================================================================================
    //                                                                            Callback
    //                                                                            ========
    // #app_customize you can customize the god-hand callback

    // ===================================================================================
    //                                                                         My Resource
    //                                                                         ===========
    @Override
    protected String myAppType() { // for framework
        return APP_TYPE;
    }

    @Override
    protected OptionalThing<? extends UserBean> getUserBean() {
        return OptionalObject.empty(); // #app_customize return empty if login is unused
    }

    @Override
    protected OptionalThing<String> myUserType() { // for framework
        return OptionalObject.empty(); // #app_customize return empty if login is unused
    }

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    @SuppressWarnings("unchecked")
    @Override
    public ActionValidator<DbfluteIntroMessages> createValidator() {
        return super.createValidator();
    }

    @Override
    public DbfluteIntroMessages createMessages() { // application may call
        return new DbfluteIntroMessages(); // overriding to change return type to concrete-class
    }
}
