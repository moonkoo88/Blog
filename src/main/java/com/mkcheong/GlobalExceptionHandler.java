package com.mkcheong;

import lombok.extern.java.Log;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Date;

@ControllerAdvice
@Log
class GlobalExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "exceptionError";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;

        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("timestamp", new Date());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

    @ExceptionHandler({ SQLException.class, DataAccessException.class })
    public ModelAndView databaseError(HttpServletRequest req, Exception e) {
        log.info("Request raised " + e.getClass().getSimpleName());
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("timestamp", new Date());
        mav.setViewName("databaseError");
        return mav;
    }
}