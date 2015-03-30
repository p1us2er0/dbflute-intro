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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.Entity;
import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.helper.HandyDate;
import org.dbflute.hook.AccessContext;
import org.dbflute.intro.mylasta.direction.DbfluteConfig;
import org.dbflute.intro.mylasta.paging.PagingNavi;
import org.dbflute.lastaflute.db.dbflute.accesscontext.AccessContextArranger;
import org.dbflute.lastaflute.db.dbflute.accesscontext.AccessContextResource;
import org.dbflute.lastaflute.web.TypicalAction;
import org.dbflute.lastaflute.web.callback.ActionRuntimeMeta;
import org.dbflute.lastaflute.web.login.LoginManager;
import org.dbflute.lastaflute.web.response.ActionResponse;
import org.dbflute.optional.OptionalObject;
import org.dbflute.optional.OptionalThing;

/**
 * @author jflute
 */
public abstract class DbfluteBaseAction extends TypicalAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    protected DbfluteConfig maihamaConfig;

    @Resource
    protected LoginManager loginManager;

    // ===================================================================================
    //                                                                      Login Handling
    //                                                                      ==============
    @Override
    protected OptionalThing<LoginManager> myLoginManager() {
        return OptionalObject.of(loginManager);
    }

    // ===================================================================================
    //                                                                              Paging
    //                                                                              ======
    // TODO jflute lastaflute: [B] paging
    /**
     * Create the paging navigation as empty.
     * @return The new-created instance of paging navigation as empty. (NotNull)
     */
    protected PagingNavi createPagingNavi() {
        return new PagingNavi();
    }

    /**
     * Prepare the paging navigation for page-range.
     * @param pagingNavi The paging navigation prepared for the paging data. (NotNull)
     * @param page The selected page as bean of paging result. (NotNull)
     * @param linkPaths The varying array of link paths. (NotNull, EmptyAllowed)
     */
    protected void preparePagingNavi(PagingNavi pagingNavi, PagingResultBean<? extends Entity> page, Object... linkPaths) {
        final Integer rangeSize = maihamaConfig.getPagingPageRangeSizeAsInteger();
        final boolean fillLimit = maihamaConfig.isPagingPageRangeFillLimit();
        pagingNavi.prepare(page, op -> {
            op.rangeSize(rangeSize);
            if (fillLimit) {
                op.fillLimit();
            }
        }, linkPaths);
    }

    /**
     * Get page size (record count of one page) for paging.
     * @return The integer as page size. (NotZero, NotMinus)
     */
    protected int getPagingPageSize() {
        return maihamaConfig.getPagingPageSizeAsInteger();
    }

    // ===================================================================================
    //                                                                            Callback
    //                                                                            ========
    // to suppress unexpected override by sub-class
    // you should remove the 'final' if you want to override this
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

    // ===================================================================================
    //                                                                      Access Context
    //                                                                      ==============
    @Override
    protected AccessContextArranger newAccessContextArranger() {
        return resource -> { /* #change_it common column settings */
            final AccessContext context = new AccessContext();
            context.setAccessLocalDateTimeProvider(() -> timeManager.getCurrentLocalDateTime());
            context.setAccessUserProvider(() -> buildAccessUserTrace(resource));
            return context;
        };
    }

    protected String buildAccessUserTrace(AccessContextResource resource) {
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
    //                                                                   Conversion Helper
    //                                                                   =================
    // -----------------------------------------------------
    //                                            Collectors
    //                                            ----------
    protected <T> Collector<T, ?, List<T>> toList() {
        return Collectors.toList();
    }

    protected <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends U> valueMapper) {
        return Collectors.toMap(keyMapper, valueMapper);
    }

    // -----------------------------------------------------
    //                                            Local Date
    //                                            ----------
    protected LocalDate toLocalDate(String dateExp) {
        if (dateExp == null || dateExp.isEmpty()) {
            return null;
        }
        return new HandyDate(dateExp, getConversionTimeZone()).getLocalDate();
    }

    protected LocalDateTime toLocalDateTime(String dateTimeExp) {
        if (dateTimeExp == null || dateTimeExp.isEmpty()) {
            return null;
        }
        return new HandyDate(dateTimeExp, getConversionTimeZone()).getLocalDateTime();
    }

    // -----------------------------------------------------
    //                                       to Display Date
    //                                       ---------------
    protected String toDispDate(LocalDate localDate) {
        return localDate != null ? doConvertToDispDate(localDate) : null;
    }

    protected String toDispDate(LocalDateTime localDateTime) {
        return localDateTime != null ? doConvertToDispDate(localDateTime) : null;
    }

    private String doConvertToDispDate(LocalDate localDate) {
        return new HandyDate(localDate, getConversionTimeZone()).toDisp(getDispDatePattern());
    }

    private String doConvertToDispDate(LocalDateTime localDateTime) {
        return new HandyDate(localDateTime, getConversionTimeZone()).toDisp(getDispDatePattern());
    }

    protected String toDispDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? doConvertToDispDateTime(localDateTime) : null;
    }

    private String doConvertToDispDateTime(LocalDateTime localDateTime) {
        return new HandyDate(localDateTime, getConversionTimeZone()).toDisp(getDispDateTimePattern());
    }

    // -----------------------------------------------------
    //                                   Conversion Resource
    //                                   -------------------
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
