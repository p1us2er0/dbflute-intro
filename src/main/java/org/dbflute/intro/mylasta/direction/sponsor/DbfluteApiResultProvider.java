package org.dbflute.intro.mylasta.direction.sponsor;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.dbflute.lastaflute.web.api.ApiResultProvider;
import org.dbflute.lastaflute.web.api.ApiResultResource;
import org.dbflute.lastaflute.web.callback.ActionRuntimeMeta;
import org.dbflute.lastaflute.web.response.ApiResponse;
import org.dbflute.lastaflute.web.response.JsonResponse;
import org.dbflute.optional.OptionalObject;
import org.dbflute.optional.OptionalThing;

public class DbfluteApiResultProvider implements ApiResultProvider {

    @Override
    public ApiResponse prepareLoginRequiredFailure(ApiResultResource resource, ActionRuntimeMeta meta) {
        return new JsonResponse<List<String>>(resource.getMessageList()).httpStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public ApiResponse prepareValidationError(ApiResultResource resource, ActionRuntimeMeta meta) {
        return new JsonResponse<List<String>>(resource.getMessageList()).httpStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public ApiResponse prepareApplicationException(ApiResultResource resource, ActionRuntimeMeta meta,
            RuntimeException cause) {
        return new JsonResponse<List<String>>(resource.getMessageList()).httpStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public OptionalThing<ApiResponse> prepareSystemException(HttpServletResponse response, ActionRuntimeMeta meta,
            Throwable cause) {
        return OptionalObject.of(JsonResponse.empty().httpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
    }
}
