package com.foxconn.EmployeeManagerment.helper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
public class AuthHelper {

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName(); // lấy username từ token
    }

    public boolean hasRole(String role) {
        return getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(role));
    }

    public boolean hasAnyRole(String... roles) {
        for (String role : roles) {
            if (hasRole(role)) return true;
        }
        return false;
    }

    public boolean hasNoRole(String role) {
        return !hasRole(role);
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities();
    }
}
