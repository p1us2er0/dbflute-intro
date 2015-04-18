package org.dbflute.intro.mylasta.direction.sponsor;

import javax.servlet.http.HttpServletResponse;

import org.dbflute.lastaflute.web.api.ApiResultProvider;
import org.dbflute.lastaflute.web.callback.ActionRuntimeMeta;
import org.dbflute.lastaflute.web.response.ApiResponse;
import org.dbflute.lastaflute.web.response.JsonResponse;
import org.dbflute.lastaflute.web.ruts.message.ActionMessages;
import org.dbflute.optional.OptionalObject;
import org.dbflute.optional.OptionalThing;

public class DbfluteApiResultProvider implements ApiResultProvider {

    @Override
    public ApiResponse prepareLoginRequiredFailure(OptionalThing<ActionMessages> errors, ActionRuntimeMeta meta) {
        return JsonResponse.empty();
    }

    @Override
    public ApiResponse prepareValidationError(OptionalThing<ActionMessages> errors, ActionRuntimeMeta meta) {
        return JsonResponse.empty();
    }

    @Override
    public ApiResponse prepareApplicationException(OptionalThing<ActionMessages> errors, ActionRuntimeMeta meta,
            RuntimeException cause) {
        return JsonResponse.empty();
    }

    @Override
    public OptionalThing<ApiResponse> prepareSystemException(HttpServletResponse response, ActionRuntimeMeta meta,
            Throwable cause) {
        return OptionalObject.of(JsonResponse.empty());
    }
}
