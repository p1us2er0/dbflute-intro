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
import org.dbflute.optional.OptionalObject;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfCollectionUtil;
import org.lastaflute.web.api.ApiFailureHook;
import org.lastaflute.web.api.ApiFailureResource;
import org.lastaflute.web.callback.ActionRuntime;
import org.lastaflute.web.response.ApiResponse;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author p1us2er0
 */
public class DbfluteApiFailureHook implements ApiFailureHook {

    @Override
    public ApiResponse handleLoginRequiredFailure(ApiFailureResource resource, ActionRuntime runtime) {
        return createErrorResponse(resource.getPropertyMessageMap(), HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public ApiResponse handleValidationError(ApiFailureResource resource, ActionRuntime runtime) {
        return createErrorResponse(resource.getPropertyMessageMap(), HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public ApiResponse handleApplicationException(ApiFailureResource resource, ActionRuntime runtime, RuntimeException cause) {
        return createErrorResponse(resource.getPropertyMessageMap(), HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public OptionalThing<ApiResponse> handleClientException(ApiFailureResource resource, ActionRuntime runtime,
            RuntimeException cause) {
        Map<String, List<String>> messages = DfCollectionUtil.newHashMap();
        messages.put("#", DfCollectionUtil.newArrayList(cause.getMessage()));
        return OptionalObject.of(createErrorResponse(messages, HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
    }

    @Override
    public OptionalThing<ApiResponse> handleServerException(ApiFailureResource resource, ActionRuntime runtime,
            Throwable cause) {
        Map<String, List<String>> messages = DfCollectionUtil.newHashMap();
        messages.put("#", DfCollectionUtil.newArrayList(cause.getMessage()));
        return OptionalObject.of(createErrorResponse(messages, HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
    }

    protected ApiResponse createErrorResponse(Map<String, List<String>> messages, int httpStatus) {
        ErrorBean errorBean = new ErrorBean();
        errorBean.setMessages(messages);
        return new JsonResponse<ErrorBean>(errorBean).httpStatus(httpStatus);
    }
}
