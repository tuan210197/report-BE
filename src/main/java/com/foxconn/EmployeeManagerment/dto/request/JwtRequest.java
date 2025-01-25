package com.foxconn.EmployeeManagerment.dto.request;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String email;
    private String password;

    public JwtRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }


}
