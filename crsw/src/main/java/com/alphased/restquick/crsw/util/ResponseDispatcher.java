package com.alphased.restquick.crsw.util;

import com.alphased.restquick.crsw.model.Response;
import com.alphased.restquick.utils.JsonUtils;
import org.springframework.http.HttpStatus;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ResponseDispatcher {

    private static final Map<HttpStatus, Function<Object, Response>> statusStrategyMap = new HashMap<>();

    static {
        statusStrategyMap.put(HttpStatus.OK, o -> {
            if (!(o instanceof String)) {
                o = JsonUtils.serialize(o);
            }
            return Response.successResponseBuilder().body(o).build();
        });
        statusStrategyMap.put(HttpStatus.UNAUTHORIZED, o -> Response.failedResponseBuilder().failedMessage("Unauthorized").build());
    }

    public static Response doAction(HttpStatus httpStatus, @Nullable Object body) {
        return statusStrategyMap.get(httpStatus).apply(body);
    }
}
