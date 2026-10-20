package com.nmims.lms.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Populates SchoolContext per request.
 * Priority: request headers > application.properties defaults.
 * When auth/JWT is added later, extract school/campus from the token here.
 */
@Component
public class SchoolContextInterceptor implements HandlerInterceptor {

    @Value("${campusCode}")
    private String defaultCampusCode;

    @Value("${school}")
    private String defaultSchool;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String campusCode = request.getHeader("X-Campus-Code");
        String school = request.getHeader("X-School");

        SchoolContext ctx = SchoolContext.builder()
                .campusCode(campusCode != null ? campusCode : defaultCampusCode)
                .school(school != null ? school : defaultSchool)
                .build();

        SchoolContextHolder.set(ctx);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        SchoolContextHolder.clear();
    }
}
