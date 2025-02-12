package com.foxconn.EmployeeManagerment.security;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CookieCleanupFilter implements Filter {

    private final JwtTokenUtil jwtTokenUtil;

    public CookieCleanupFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (jwtTokenUtil.isTokenExpired(token)) {
                        Cookie expiredCookie = new Cookie("token", null);
                        expiredCookie.setHttpOnly(true);
                        expiredCookie.setSecure(false);
                        expiredCookie.setPath("/");
                        expiredCookie.setMaxAge(0);
                        response.addCookie(expiredCookie);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
