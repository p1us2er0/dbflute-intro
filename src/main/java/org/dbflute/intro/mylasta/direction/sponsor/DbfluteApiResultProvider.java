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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.dbflute.intro.app.bean.ErrorBean;
import org.dbflute.lastaflute.web.api.ApiResultProvider;
import org.dbflute.lastaflute.web.api.ApiResultResource;
import org.dbflute.lastaflute.web.callback.ActionRuntimeMeta;
import org.dbflute.lastaflute.web.response.ApiResponse;
import org.dbflute.lastaflute.web.response.JsonResponse;
import org.dbflute.optional.OptionalObject;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfCollectionUtil;

/**
 * @author p1us2er0
 */
public class DbfluteApiResultProvider implements ApiResultProvider {

    @Override
    public ApiResponse prepareLoginRequiredFailure(ApiResultResource resource, ActionRuntimeMeta meta) {
        return new JsonResponse<ErrorBean>(createErrorBean(resource.getPropertyMessageMap())).httpStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public ApiResponse prepareValidationError(ApiResultResource resource, ActionRuntimeMeta meta) {
        return new JsonResponse<ErrorBean>(createErrorBean(resource.getPropertyMessageMap())).httpStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public ApiResponse prepareApplicationException(ApiResultResource resource, ActionRuntimeMeta meta,
            RuntimeException cause) {
        return new JsonResponse<ErrorBean>(createErrorBean(resource.getPropertyMessageMap())).httpStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public OptionalThing<ApiResponse> prepareSystemException(HttpServletResponse response, ActionRuntimeMeta meta,
            Throwable cause) {

        Map<String, List<String>> messages = DfCollectionUtil.newHashMap();
        messages.put("#", DfCollectionUtil.newArrayList(cause.getMessage()));
        return OptionalObject.of(new JsonResponse<ErrorBean>(createErrorBean(messages)).httpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
    }

    protected ErrorBean createErrorBean(Map<String, List<String>> messages) {
        ErrorBean errorBean = new ErrorBean();
        errorBean.setMessages(messages);
        return errorBean;
    }
}
