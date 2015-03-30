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

import javax.annotation.Resource;

import org.dbflute.intro.mylasta.direction.sponsor.DbfluteActionAdjustmentProvider;
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
import org.dbflute.lastaflute.web.direction.OptionalWebDirection;

/**
 * @author jflute
 */
public abstract class DbfluteFwAssistantDirector extends CachedFwAssistantDirector {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    public static final String MAIHAMA_CONFIG_FILE = "maihama_config.properties";
    public static final String MAIHAMA_ENV_FILE = "maihama_env.properties";
    public static final String MAIHAMA_MESSAGE_NAME = "maihama_message";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    protected DbfluteConfig maihamaConfig;

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
        return new String[] { MAIHAMA_CONFIG_FILE, MAIHAMA_ENV_FILE };
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
        // this configuration is on maihama_env.properties
        // because this is true only when development
        direction.directDevelopmentHere(maihamaConfig.isDevelopmentHere());

        // titles are from configurations
        direction.directLoggingTitle(maihamaConfig.getDomainTitle(), maihamaConfig.getEnvironmentTitle());

        // this configuration is on sea_env.properties
        // because it has no influence to production
        // even if you set true manually and forget to set false back
        direction.directFrameworkDebug(maihamaConfig.isFrameworkDebug()); // basically false

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
        return "maihama:dockside"; // #change_it hard coding for now
    }

    // -----------------------------------------------------
    //                                                  Time
    //                                                  ----
    protected void prepareTime(OptionalCoreDirection direction) {
        direction.directTime(createTimeResourceProvider());
    }

    protected DbfluteTimeResourceProvider createTimeResourceProvider() {
        return new DbfluteTimeResourceProvider(maihamaConfig);
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
        return new DbfluteCookieResourceProvider(maihamaConfig, cryptographer);
    }

    protected String getCookieSecurityWord() {
        return "dockside:maihama"; // #change_it hard coding for now
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
        direction.directMessage(getDomainMessageName(), getExtendsMessageNames());
    }

    protected abstract String getDomainMessageName();

    protected String[] getExtendsMessageNames() {
        return new String[] { MAIHAMA_MESSAGE_NAME };
    }
}
