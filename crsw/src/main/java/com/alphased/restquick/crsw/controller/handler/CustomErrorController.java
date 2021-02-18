package com.alphased.restquick.crsw.controller.handler;

import com.alphased.restquick.crsw.exception.OperationNotFoundException;
import com.alphased.restquick.crsw.model.Response;
import com.alphased.restquick.crsw.util.ResponseDispatcher;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.NestedServletException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Log4j2
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class CustomErrorController extends AbstractErrorController {

    private final ErrorProperties properties;

    public CustomErrorController(ErrorAttributes attributes) {
        super(attributes);
        this.properties = new ErrorProperties();
    }

    @Override
    public String getErrorPath() {
        return this.properties.getPath();
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<Response> handleError(HttpServletRequest request) {
        log.trace("Catching Exception in REST API.");
        log.debug("Using {} for exception handling.", getClass().getSimpleName());
        int code = Integer.parseInt(String.valueOf(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)));
        Exception ex = ((Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
        if (code == 404) {
            ex = new OperationNotFoundException();
        }
        if (ex == null) {
            ex = new RuntimeException(String.valueOf(request.getAttribute(RequestDispatcher.ERROR_MESSAGE)));
        }
        if(ex instanceof NestedServletException && ex.getCause() instanceof Exception) {
            ex = (Exception) ex.getCause();
        }
        if (ex instanceof OperationNotFoundException && code != 404) {
            code = 404;
        }
        Response response = ResponseDispatcher.failureResponse(HttpStatus.valueOf(code), null, ex);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

}
