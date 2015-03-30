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
package org.dbflute.intro.app.base;

import org.dbflute.intro.mylasta.action.DbfluteIntroUserBean;
import org.dbflute.intro.mylasta.action.DbfluteUserBean;
import org.dbflute.lastaflute.web.login.LoginHandlingResource;
import org.dbflute.lastaflute.web.login.TypicalLoginLogic;
import org.dbflute.lastaflute.web.login.option.LoginSpecifiedOption;

/**
 * @param <USER_BEAN> The type of user bean.
 * @param <USER_ENTITY> The type of user entity or model.
 * @author jflute
 */
public abstract class NoneLoginBaseLogic<USER_BEAN extends DbfluteUserBean, USER_ENTITY> extends TypicalLoginLogic<USER_BEAN, USER_ENTITY> {

    // ===================================================================================
    //                                                                                Find
    //                                                                                ====
    @Override
    protected boolean doCheckUserLoginable(String email, String cipheredPassword) {
        return false;
    }

    @Override
    protected USER_ENTITY doFindLoginUser(String email, String cipheredPassword) {
        return null;
    }

    @Override
    protected USER_ENTITY doFindLoginUser(Long userId) {
        return null;
    }

    // ===================================================================================
    //                                                                               Login
    //                                                                               =====
    @Override
    protected USER_BEAN createUserBean(USER_ENTITY userEntity) {
        return null;
    }

    @Override
    protected String getCookieAutoLoginKey() {
        return null;
    }

    @Override
    protected void saveLoginHistory(USER_ENTITY member, USER_BEAN userBean, LoginSpecifiedOption option) {
    }

    // ===================================================================================
    //                                                              Login Control Resource
    //                                                              ======================
    @Override
    protected Class<USER_BEAN> getUserBeanType() {
        // TODO
        return (Class<USER_BEAN>) DbfluteIntroUserBean.class;
    }

    @Override
    protected Class<?> getLoginActionType() {
        return Object.class; // dummy
    }

    @Override
    public boolean isLoginRequiredAction(LoginHandlingResource resource) {
        return false;
    }
}
