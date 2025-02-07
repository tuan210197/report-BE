package com.foxconn.EmployeeManagerment.service;






import com.foxconn.EmployeeManagerment.dto.request.UserInfoDTO;
import com.foxconn.EmployeeManagerment.dto.request.UserLoginDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * @author tuandv
 * JWTUserDetailsService implements the Spring Security UserDetailsService interface.
 * It overrides the loadUserByUsername for fetching user details from the database using the username.
 * The Spring Security Authentication Manager calls this method for getting the user details from the database
 * when authenticating the user details provided by the user.
 */

public interface JwtUserDetailsService extends UserDetailsService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    boolean registerUser(UserLoginDTO userLoginDTO) throws Exception;

    void forgetPassword(UserLoginDTO userLoginDTO);

    boolean updatePassword(UserLoginDTO userLoginDTO);

    boolean updateUser(String userId, UserLoginDTO userLoginDTO);

    boolean checkTokenForUser(UserLoginDTO userLoginDTO);

    boolean isLocked(String email);

    void resendOTP(UserLoginDTO userLoginDTO);

    void loginFailRetryCount(String email, boolean loginFailed);

    UserLoginDTO getBasicAuthByEmail(String email, boolean forceClear);

    UserInfoDTO getUserInfo(String uid);

    boolean logout(String token);

    boolean checkEmail(UserLoginDTO userLoginDTO);


    boolean banUser(UserLoginDTO userLoginDTO);

    boolean unlockUser(UserLoginDTO userLoginDTO);

    boolean checkPassword(UserLoginDTO userLoginDTO);

//	Users getCurrentUser();
}
