package com.alphased.restquick.crsw.processor;

import com.alphased.restquick.crsw.model.Response;
import com.alphased.restquick.crsw.util.ResponseDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class EndPointProcessor {

    public Response apply() throws Exception {
        // TODO: DoAction for operation. Response Type will be String and Json Format.
//        Map body = Map.of("asd", "asd");
        Object body = "asdasd";
        return ResponseDispatcher.successResponse(body);
    }

}
