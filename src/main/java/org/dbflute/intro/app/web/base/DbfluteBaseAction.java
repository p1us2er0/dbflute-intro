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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.dbflute.Entity;
import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.helper.HandyDate;
import org.dbflute.hook.AccessContext;
import org.dbflute.intro.mylasta.direction.DbfluteConfig;
import org.dbflute.intro.mylasta.paging.PagingNavi;
import org.dbflute.optional.OptionalObject;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.db.dbflute.accesscontext.AccessContextArranger;
import org.lastaflute.db.dbflute.accesscontext.AccessContextResource;
import org.lastaflute.web.TypicalAction;
import org.lastaflute.web.callback.ActionRuntimeMeta;
import org.lastaflute.web.login.LoginManager;
import org.lastaflute.web.response.ActionResponse;
import org.lastaflute.web.response.render.RenderData;
import org.lastaflute.web.servlet.request.RequestManager;

/**
 * @author p1us2er0
 */
public abstract class DbfluteBaseAction extends TypicalAction {

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[] {};

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** The manager of request. (NotNull) */
    @Resource
    private RequestManager requestManager;

    @Resource
    private DbfluteConfig dbfluteConfig;

    // ===================================================================================
    //                                                                            Callback
    //                                                                            ========
    // to suppress unexpected override by sub-class
    // you should remove the 'final' if you need to override this
    @Override
    public final ActionResponse godHandActionPrologue(ActionRuntimeMeta runtimeMeta) {
        return super.godHandActionPrologue(runtimeMeta);
    }

    @Override
    public final ActionResponse godHandExceptionMonologue(ActionRuntimeMeta runtimeMeta) {
        return super.godHandExceptionMonologue(runtimeMeta);
    }

    @Override
    public final void godHandActionEpilogue(ActionRuntimeMeta runtimeMeta) {
        super.godHandActionEpilogue(runtimeMeta);
    }

    // #app_customize you can customize the god-hand callback
    @Override
    public ActionResponse godHandBefore(ActionRuntimeMeta runtimeMeta) { // application's super class may override
        return super.godHandBefore(runtimeMeta);
    }

    @Override
    public void godHandFinally(ActionRuntimeMeta runtimeMeta) { // application's super class may override
        super.godHandFinally(runtimeMeta);
    }

    // ===================================================================================
    //                                                                         My Resource
    //                                                                         ===========
    @Override
    protected OptionalThing<LoginManager> myLoginManager() { // for framework
        return OptionalObject.empty(); // #app_customize if empty, login is unused
    }

    // ===================================================================================
    //                                                                      Access Context
    //                                                                      ==============
    @Override
    protected AccessContextArranger newAccessContextArranger() { // for framework
        return resource -> {
            final AccessContext context = new AccessContext();
            context.setAccessLocalDateTimeProvider(() -> getCurrentDateTime());
            context.setAccessUserProvider(() -> buildAccessUserTrace(resource));
            return context;
        };
    }

    // #app_customize you can customize the user trace style
    private String buildAccessUserTrace(AccessContextResource resource) {
        final StringBuilder sb = new StringBuilder();
        sb.append(myUserType()).append(":");
        sb.append(myUserBean().map(bean -> bean.getUserId()).orElseGet(() -> -1L));
        sb.append(",").append(myAppType()).append(",").append(resource.getModuleName());
        final String trace = sb.toString();
        final int traceColumnSize = 200;
        if (trace.length() > traceColumnSize) {
            return trace.substring(0, traceColumnSize);
        }
        return trace;
    }

    // ===================================================================================
    //                                                                              Paging
    //                                                                              ======
    // #app_customize you can customize the paging navigation logic
    /**
     * Register the paging navigation as page-range.
     * @param data The data object to render the HTML. (NotNull)
     * @param page The selected page as bean of paging result. (NotNull)
     * @param queryForm The form for query string added to link. (NullAllowed)
     */
    protected void registerPagingNavi(RenderData data, PagingResultBean<? extends Entity> page, Object queryForm) { // application may call
        data.register("pagingNavi", preparePagingNavi(page, queryForm));
    }

    /**
     * Prepare the paging navigation as page-range.
     * @param page The paging result bean for the paging data. (NotNull)
     * @return The paging navigation prepared for the paging data. (NotNull)
     * @param queryForm The form for query string added to link. (NullAllowed)
     */
    protected PagingNavi preparePagingNavi(PagingResultBean<? extends Entity> page, Object queryForm) { // application may call
        return new PagingNavi(page, op -> {
            op.rangeSize(dbfluteConfig.getPagingPageRangeSizeAsInteger());
            if (dbfluteConfig.isPagingPageRangeFillLimit()) {
                op.fillLimit();
            }
        }, myPagignLinkPaths(), queryForm);
    }

    protected Object[] myPagignLinkPaths() {
        return EMPTY_OBJECT_ARRAY;
    }

    /**
     * Get page size (record count of one page) for paging.
     * @return The integer as page size. (NotZero, NotMinus)
     */
    protected int getPagingPageSize() { // application may call
        return dbfluteConfig.getPagingPageSizeAsInteger();
    }

    // ===================================================================================
    //                                                                   Conversion Helper
    //                                                                   =================
    // -----------------------------------------------------
    //                                         to Local Date
    //                                         -------------
    protected LocalDate toLocalDate(String dateExp) { // application may call
        if (dateExp == null || dateExp.isEmpty()) {
            return null;
        }
        return new HandyDate(dateExp, getConversionTimeZone()).getLocalDate();
    }

    protected LocalDateTime toLocalDateTime(String dateTimeExp) { // application may call
        if (dateTimeExp == null || dateTimeExp.isEmpty()) {
            return null;
        }
        return new HandyDate(dateTimeExp, getConversionTimeZone()).getLocalDateTime();
    }

    // -----------------------------------------------------
    //                                       to Display Date
    //                                       ---------------
    protected String toDispDate(LocalDate localDate) { // application may call
        return localDate != null ? doConvertToDispDate(localDate) : null;
    }

    protected String toDispDate(LocalDateTime localDateTime) { // application may call
        return localDateTime != null ? doConvertToDispDate(localDateTime) : null;
    }

    private String doConvertToDispDate(LocalDate localDate) {
        return new HandyDate(localDate, getConversionTimeZone()).toDisp(getDispDatePattern());
    }

    private String doConvertToDispDate(LocalDateTime localDateTime) {
        return new HandyDate(localDateTime, getConversionTimeZone()).toDisp(getDispDatePattern());
    }

    protected String toDispDateTime(LocalDateTime localDateTime) { // application may call
        return localDateTime != null ? doConvertToDispDateTime(localDateTime) : null;
    }

    private String doConvertToDispDateTime(LocalDateTime localDateTime) {
        return new HandyDate(localDateTime, getConversionTimeZone()).toDisp(getDispDateTimePattern());
    }

    // -----------------------------------------------------
    //                                   Conversion Resource
    //                                   -------------------
    // #app_customize you can customize the date style
    protected String getDispDatePattern() {
        return "yyyy/MM/dd";
    }

    protected String getDispDateTimePattern() {
        return "yyyy/MM/dd HH:mm:ss";
    }

    protected TimeZone getConversionTimeZone() {
        return requestManager.getUserTimeZone();
    }
}
