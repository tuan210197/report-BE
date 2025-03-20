package com.foxconn.EmployeeManagerment.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.service.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Lazy
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException, IOException {


        String requestURL = request.getRequestURI();
        String token = request.getHeader("Authorization");

        System.out.println(requestURL);
        try {
            // JWT Token is in the form "Bearer token". Remove Bearer word and get
            // only the Token
            String jwtToken = jwtTokenUtil.extractJwtFromRequest(request,response);

            if (requestURL.startsWith("/api/user/refresh-token")) {
                Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        null, null, null);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                request.setAttribute("claims", claims);
            }
            else {
                if (StringUtils.hasText(jwtToken)) {
                    String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails =  jwtUserDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//                        }
                    }
                }
            }

        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            Map<String, Object> result = new HashMap<>();
            result.put("code", Const.API_RESPONSE.RETURN_CODE_ERROR);
            result.put("success", Const.API_RESPONSE.RESPONSE_STATUS_FALSE);
            result.put("description", null);
            result.put("message", e.getMessage());
            result.put("data", null);
            printWriter.print(new ObjectMapper().writeValueAsString(result));
            printWriter.close();
            return;
        } catch (ExpiredJwtException e) {

            String isRefreshToken = request.getHeader("isRefreshToken");
//            String isRefreshToken = request.getHeader("isRefreshToken");
            if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refresh-token")) {
                // Allow for Refresh Token creation if needed

                allowForRefreshToken(e, request);
            } else {
                // Nếu không phải là yêu cầu refresh token, trả về 401 Unauthorized

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
            }
        } catch (Exception e) {
            e.printStackTrace();
         }

        chain.doFilter(request, response);
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

        // create a UsernamePasswordAuthenticationToken with null values.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // Set the claims so that in controller we will be using it to create
        // new JWT
        request.setAttribute("claims", ex.getClaims());

    }
}
