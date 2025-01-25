package com.foxconn.EmployeeManagerment.security;

import com.foxconn.EmployeeManagerment.entity.BasicLogin;
import com.foxconn.EmployeeManagerment.repository.BasicLoginRepository;
import com.foxconn.EmployeeManagerment.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomUserDetailsService implements UserDetailsService {

    private BasicLoginRepository basicLoginRepository;


    private RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        BasicLogin user = basicLoginRepository.findByEmail(email);

        return User.withUsername(
                user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
