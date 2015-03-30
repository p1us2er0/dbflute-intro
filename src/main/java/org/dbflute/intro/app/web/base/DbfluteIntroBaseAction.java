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

import javax.annotation.Resource;

import org.dbflute.intro.app.base.DbfluteBaseAction;
import org.dbflute.intro.app.logic.MemberLoginLogic;
import org.dbflute.intro.mylasta.action.DbfluteIntroMessages;
import org.dbflute.intro.mylasta.action.DbfluteIntroUserBean;
import org.dbflute.lastaflute.web.callback.ActionRuntimeMeta;
import org.dbflute.lastaflute.web.login.UserBean;
import org.dbflute.lastaflute.web.login.exception.LoginTimeoutException;
import org.dbflute.optional.OptionalObject;
import org.dbflute.optional.OptionalThing;

/**
 * @author jflute
 */
public abstract class DbfluteIntroBaseAction extends DbfluteBaseAction {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The application type for Dockside, e.g. used by access context. */
    protected static final String APP_TYPE = "DCK";

    /** The user type for Member, e.g. used by access context. */
    protected static final String USER_TYPE = "M";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    protected MemberLoginLogic memberLoginLogic;

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
    /**
     * Get the bean of login user on session. (for application)
     * @return The optional thing of found user bean. (NotNull, EmptyAllowed: when not login)
     */
    protected OptionalThing<DbfluteIntroUserBean> getUserBean() { // user interface
        return wrapUserBeanAsOptional(memberLoginLogic.getSessionUserBean());
    }

    @Override
    protected OptionalThing<UserBean> myUserBean() { // for interface handling
        return wrapUserBeanAsOptional(getUserBean().orElse(null));
    }

    @SuppressWarnings("unchecked")
    protected <USER_BEAN> OptionalThing<USER_BEAN> wrapUserBeanAsOptional(final DbfluteIntroUserBean userBean) {
        return (OptionalThing<USER_BEAN>) OptionalObject.ofNullable(userBean, () -> {
            throw new LoginTimeoutException("Not found the user in session."); /* to login action */
        });
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
