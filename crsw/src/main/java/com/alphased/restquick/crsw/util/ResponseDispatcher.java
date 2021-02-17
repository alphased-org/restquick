package com.alphased.restquick.crsw.util;

import com.alphased.restquick.crsw.model.Response;
import com.alphased.restquick.utils.JsonUtils;
import org.springframework.http.HttpStatus;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ResponseDispatcher {

    private static final Map<HttpStatus.Series, BiFunction<Object, HttpStatus, Response>> statusStrategyMap = new HashMap<>();

    static {
        statusStrategyMap.put(HttpStatus.Series.SUCCESSFUL, (body, httpStatus) -> {
            if (body != null) {
                if (!(body instanceof String)) {
                    try {
                        body = JsonUtils.serialize(body);
                    } catch (Exception e) {
                        return Response.failedResponseBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).failedMessage("Operation success but serialize problem detected.").build();
                    }
                }
            }
            return Response.successResponseBuilder().body(body).build();
        });
        statusStrategyMap.put(HttpStatus.Series.CLIENT_ERROR, (body, httpStatus) -> Response.failedResponseBuilder().body(body).code(httpStatus.value()).failedMessage(httpStatus.getReasonPhrase()).build());
        statusStrategyMap.put(HttpStatus.Series.SERVER_ERROR, (body, httpStatus) -> Response.failedResponseBuilder().body(body).code(httpStatus.value()).failedMessage(httpStatus.getReasonPhrase()).build());
    }

    private static Response applyStrategy(HttpStatus httpStatus, Object body) {
        HttpStatus.Series series = HttpStatus.Series.resolve(httpStatus.value());
        return statusStrategyMap.getOrDefault(series, (_body, _httpStatus) -> Response.successResponseBuilder().body(body).build()).apply(body, httpStatus);
    }

    public static Response createResponse(HttpStatus httpStatus, @Nullable Object body) {
        return applyStrategy(httpStatus, body);
    }

    public static Response createResponse(HttpStatus httpStatus, @Nullable String jsonBody) {
        return applyStrategy(httpStatus, jsonBody);
    }
}
