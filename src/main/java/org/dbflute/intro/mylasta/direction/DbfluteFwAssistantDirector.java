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
package org.dbflute.intro.mylasta.direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.mylasta.direction.sponsor.DbfluteActionAdjustmentProvider;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteApiResultProvider;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteBootProcessCallback;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteCookieResourceProvider;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteSecurityResourceProvider;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteTimeResourceProvider;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteUserLocaleProcessProvider;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteUserTimeZoneProcessProvider;
import org.dbflute.lastaflute.core.direction.CachedFwAssistantDirector;
import org.dbflute.lastaflute.core.direction.OptionalAssistDirection;
import org.dbflute.lastaflute.core.direction.OptionalCoreDirection;
import org.dbflute.lastaflute.core.security.InvertibleCryptographer;
import org.dbflute.lastaflute.core.security.OneWayCryptographer;
import org.dbflute.lastaflute.db.direction.OptionalDbDirection;
import org.dbflute.lastaflute.web.api.ApiResultProvider;
import org.dbflute.lastaflute.web.direction.OptionalWebDirection;

/**
 * @author jflute
 */
public abstract class DbfluteFwAssistantDirector extends CachedFwAssistantDirector {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    public static final String DBFLUTE_CONFIG_FILE = "dbflute_config.properties";
    public static final String DBFLUTE_ENV_FILE = "dbflute_env.properties";
    public static final String DBFLUTE_LABEL_NAME = "dbflute_label";
    public static final String DBFLUTE_MESSAGE_NAME = "dbflute_message";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    protected DbfluteConfig dbfluteConfig;

    // ===================================================================================
    //                                                                              Assist
    //                                                                              ======
    @Override
    protected OptionalAssistDirection prepareOptionalAssistDirection() {
        final OptionalAssistDirection direction = new OptionalAssistDirection();
        prepareConfiguration(direction);
        return direction;
    }

    protected void prepareConfiguration(OptionalAssistDirection direction) {
        direction.directConfiguration(getDomainConfigFile(), getExtendsConfigFiles());
    }

    protected abstract String getDomainConfigFile();

    protected String[] getExtendsConfigFiles() {
        return new String[] { DBFLUTE_CONFIG_FILE, DBFLUTE_ENV_FILE };
    }

    // ===================================================================================
    //                                                                                Core
    //                                                                                ====
    @Override
    protected OptionalCoreDirection prepareOptionalCoreDirection() {
        final OptionalCoreDirection direction = new OptionalCoreDirection();
        prepareFramework(direction);
        prepareSecurity(direction);
        prepareTime(direction);
        return direction;
    }

    // -----------------------------------------------------
    //                                             Framework
    //                                             ---------
    protected void prepareFramework(OptionalCoreDirection direction) {
        // this configuration is on dbflute_env.properties
        // because this is true only when development
        direction.directDevelopmentHere(dbfluteConfig.isDevelopmentHere());

        // titles are from configurations
        direction.directLoggingTitle(dbfluteConfig.getDomainTitle(), dbfluteConfig.getEnvironmentTitle());

        // this configuration is on sea_env.properties
        // because it has no influence to production
        // even if you set true manually and forget to set false back
        direction.directFrameworkDebug(dbfluteConfig.isFrameworkDebug()); // basically false

        // you can add your own process when your application is booting
        direction.directBootProcessCallback(createBootProcessCallback());
    }

    protected DbfluteBootProcessCallback createBootProcessCallback() {
        return new DbfluteBootProcessCallback();
    }

    // -----------------------------------------------------
    //                                              Security
    //                                              --------
    protected void prepareSecurity(OptionalCoreDirection direction) {
        direction.directSecurity(createSecurityResourceProvider());
    }

    protected DbfluteSecurityResourceProvider createSecurityResourceProvider() { // #change_it AES, SHA1 for now
        final InvertibleCryptographer invertibleCryptographer = InvertibleCryptographer.createAesCipher(getPrimarySecurityWord());
        final OneWayCryptographer oneWayCryptographer = OneWayCryptographer.createSha1Cryptographer();
        return new DbfluteSecurityResourceProvider(invertibleCryptographer, oneWayCryptographer);
    }

    protected String getPrimarySecurityWord() {
        return "dbflute:dbfluteIntro"; // #change_it hard coding for now
    }

    // -----------------------------------------------------
    //                                                  Time
    //                                                  ----
    protected void prepareTime(OptionalCoreDirection direction) {
        direction.directTime(createTimeResourceProvider());
    }

    protected DbfluteTimeResourceProvider createTimeResourceProvider() {
        return new DbfluteTimeResourceProvider(dbfluteConfig);
    }

    // ===================================================================================
    //                                                                                 DB
    //                                                                                ====
    @Override
    protected OptionalDbDirection prepareOptionalDbDirection() {
        final OptionalDbDirection direction = new OptionalDbDirection();
        return direction;
    }

    // ===================================================================================
    //                                                                                Web
    //                                                                               =====
    @Override
    protected OptionalWebDirection prepareOptionalWebDirection() {
        final OptionalWebDirection direction = new OptionalWebDirection();
        prepareRequest(direction);
        prepareCookie(direction);
        prepareAdjustment(direction);
        prepareMessage(direction);
        prepareApiCall(direction);
        return direction;
    }

    // -----------------------------------------------------
    //                                               Servlet
    //                                               -------
    protected void prepareRequest(OptionalWebDirection direction) {
        direction.directRequest(createUserLocaleProcessProvider(), createUserTimeZoneProcessProvider());
    }

    protected DbfluteUserLocaleProcessProvider createUserLocaleProcessProvider() {
        return new DbfluteUserLocaleProcessProvider();
    }

    protected DbfluteUserTimeZoneProcessProvider createUserTimeZoneProcessProvider() {
        return new DbfluteUserTimeZoneProcessProvider();
    }

    protected void prepareCookie(OptionalWebDirection direction) {
        direction.directCookie(createCookieResourceProvider());
    }

    protected DbfluteCookieResourceProvider createCookieResourceProvider() { // #change_it AES for now
        final InvertibleCryptographer cryptographer = InvertibleCryptographer.createAesCipher(getCookieSecurityWord());
        return new DbfluteCookieResourceProvider(dbfluteConfig, cryptographer);
    }

    protected String getCookieSecurityWord() {
        return "dbfluteIntro:dbflute"; // #change_it hard coding for now
    }

    // -----------------------------------------------------
    //                                                Action
    //                                                ------
    protected void prepareAdjustment(OptionalWebDirection direction) {
        direction.directAdjustment(createActionAdjustmentProvider());
    }

    protected DbfluteActionAdjustmentProvider createActionAdjustmentProvider() {
        return new DbfluteActionAdjustmentProvider();
    }

    protected void prepareMessage(OptionalWebDirection direction) {
        final List<String> nameList = new ArrayList<String>();
        setupDomainMessage(nameList);
        nameList.addAll(Arrays.asList(getExtendsMessageNames()));
        direction.directMessage(nameList.remove(0), nameList.toArray(new String[0]));
    }

    protected abstract void setupDomainMessage(List<String> nameList);

    protected String[] getExtendsMessageNames() {
        return new String[] { DBFLUTE_LABEL_NAME, DBFLUTE_MESSAGE_NAME };
    }

    protected void prepareApiCall(OptionalWebDirection direction) {
        direction.directApiCall(createApiResultProvider());
    }

    protected ApiResultProvider createApiResultProvider() {
        return new DbfluteApiResultProvider();
    }
}
