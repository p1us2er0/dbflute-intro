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
