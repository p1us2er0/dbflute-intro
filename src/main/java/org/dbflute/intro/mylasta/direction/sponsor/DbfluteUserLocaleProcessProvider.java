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
package org.dbflute.intro.mylasta.direction.sponsor;

import java.util.Locale;

import org.dbflute.lastaflute.web.callback.ActionRuntimeMeta;
import org.dbflute.lastaflute.web.servlet.request.RequestManager;
import org.dbflute.lastaflute.web.servlet.request.UserLocaleProcessProvider;
import org.dbflute.optional.OptionalObject;
import org.dbflute.optional.OptionalThing;

/**
 * @author jflute
 */
public class DbfluteUserLocaleProcessProvider implements UserLocaleProcessProvider {

    @Override
    public boolean isAcceptCookieLocale() {
        return false;
    }

    @Override
    public OptionalThing<Locale> findBusinessLocale(ActionRuntimeMeta runtimeMeta, RequestManager requestManager) {
        return OptionalObject.empty(); // to next determination
    }

    @Override
    public OptionalThing<Locale> getRequestedLocale(RequestManager requestManager) {
        return OptionalObject.empty(); // means browser default
    }

    @Override
    public String toString() {
        return "{acceptCookieLocale=" + isAcceptCookieLocale() + "}";
    }
}
