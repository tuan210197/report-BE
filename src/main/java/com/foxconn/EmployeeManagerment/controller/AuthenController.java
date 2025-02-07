package com.foxconn.EmployeeManagerment.controller;

import com.foxconn.EmployeeManagerment.common.Const;
import com.foxconn.EmployeeManagerment.dto.request.UserInfoDTO;
import com.foxconn.EmployeeManagerment.dto.request.UserLoginDTO;
import com.foxconn.EmployeeManagerment.dto.request.JwtRequest;
import com.foxconn.EmployeeManagerment.dto.response.JwtResponse;

import com.foxconn.EmployeeManagerment.security.JwtTokenUtil;
import com.foxconn.EmployeeManagerment.service.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author tuandv
 */
//@Scope("session")
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/user")

public class AuthenController extends BaseController {

//    @Autowired
    private final AuthenticationManager authenticationManager;

//    @Autowired
    private final JwtTokenUtil jwtTokenUtil;

//    @Autowired
    private final JwtUserDetailsService userDetailsService;

//, ProjectRepository projectRepository
    public AuthenController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;

    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(HttpServletRequest request, @RequestBody JwtRequest authenticationRequest, HttpServletResponse response) {

        try {
            String requestId = request.getHeader("request-id");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));

            UserLoginDTO userLoginDTO = userDetailsService.getBasicAuthByEmail(authenticationRequest.getEmail(), true);

            if (Objects.isNull(userLoginDTO)) {
                return toExceptionResult("USER CHƯA XÁC THỰC, GỬI LẠI OTP", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
            String uid = userLoginDTO.getUserUid();
            UserInfoDTO userInfoDTO = userDetailsService.getUserInfo(uid);
            String token = jwtTokenUtil.generateToken(userInfoDTO);


            // Create HttpOnly cookie
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(false) // Chỉ bật nếu dùng HTTPS
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // Cookie tồn tại trong 7 ngày
//                    .sameSite("Strict") // Điều chỉnh nếu bạn muốn cho phép truy cập từ subdomain hoặc cross-origin
                    .sameSite("Lux")
                    .build();

            // Add the cookie to the response header
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            userDetailsService.loginFailRetryCount(authenticationRequest.getEmail(), false);
//            return toSuccessResult(new JwtResponse(token, userLoginDTO.getUserUid()), "");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(toSuccessResult(new JwtResponse("Token is set in cookie", userLoginDTO.getUserUid()), ""));

        } catch (IllegalArgumentException | InternalAuthenticationServiceException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (DisabledException e) {
            return toExceptionResult("USER BỊ KHÓA HÃY LIÊN HỆ VỚI QUẢN TRỊ VIÊN", Const.API_RESPONSE.RETURN_CODE_ERROR);

        } catch (BadCredentialsException e) {
            userDetailsService.loginFailRetryCount(authenticationRequest.getEmail(), true);
            return toExceptionResult("MẬT KHẨU KHÔNG ĐÚNG", Const.API_RESPONSE.RETURN_CODE_ERROR);


        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    @PostMapping(value = "/check-email")
    public ResponseEntity<?> checkEmailVerify(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            if (userDetailsService.checkEmail(userLoginDTO)) {
                return toSuccessResult(null, "EMAIL ĐÃ XÁC NHẬN");
            } else {
                return toExceptionResult("EMAIL KHÔNG TỒN TẠI HOẶC CHƯA XÁC THỰC", Const.API_RESPONSE.RETURN_CODE_SUCCESS);
            }
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        }

    }

    /**
     * Register an user
     *
     * @param userLoginDTO
     * @return Success or fail
     */
//
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO, Authentication authentication) throws Exception {
//        UserDetails auth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("GD"))) {
        try {
            String requestId = request.getHeader("request-id");
            if (userDetailsService.registerUser(userLoginDTO)) {
                return toSuccessResult(null, "ĐĂNG KÝ THÀNH CÔNG");
            } else {
                return toExceptionResult("ĐĂNG KÝ LỖI", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
//        } else {
//            return toExceptionResult("KHÔNG CÓ QUYỀN TRUY CẬP", Const.API_RESPONSE.RETURN_CODE_ERROR_NOTFOUND);
//        }
    }

    /**
     * User has forgotten their password. Request for changing password with email
     *
     * @param userLoginDTO
     * @return Success or fail
     */
    @RequestMapping(value = "/password/forget", method = RequestMethod.POST)
    public ResponseEntity<?> forgetPassword(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            userDetailsService.forgetPassword(userLoginDTO);
            return toSuccessResult(null, "GỬI OTP THÀNH CÔNG");
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    @PostMapping("/password/check")
    public int checkPassword(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
            String uid = this.getCurrentUser().getUid().trim();
            if (userDetailsService.checkPassword(userLoginDTO)) {
                return 200;
            } else {
                return 201;
            }

    }
    /**
     * Change password
     *
     * @param userLoginDTO
     * @return Success or fail
     */
    @RequestMapping(value = "/password/change", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            if (userDetailsService.updatePassword(userLoginDTO)) {
                return toSuccessResult(null, "THAY ĐỔI MẬT KHẨU THÀNH CÔNG");
            } else {
                return toExceptionResult("TOKEN_INVALID", Const.API_RESPONSE.RETURN_CODE_SUCCESS);
            }
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_SUCCESS);

        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_SUCCESS);
        }
    }

    @PostMapping("/ban-user")
    public ResponseEntity<?> banUser(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        UserDetails auth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("GD"))) {
            try {
                String requestId = request.getHeader("request-id");
                if (userDetailsService.banUser(userLoginDTO)) {
                    return toSuccessResult(null, "BAN_SUCCESS");
                } else {
                    return toExceptionResult("BAN_FAILED", Const.API_RESPONSE.RETURN_CODE_ERROR);
                }

            } catch (IllegalArgumentException e) {
                return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
            } catch (Exception e) {
                return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
            }

        } else {
            return toExceptionResult("KHÔNG PHẬN SỰ MIỄN VÀO", Const.API_RESPONSE.RETURN_CODE_ERROR_NOTFOUND);
        }
    }

    @PostMapping("/unlock-user")
    public ResponseEntity<?> unlockUser(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
//        UserDetails auth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("GD"))) {
        try {
            String requestId = request.getHeader("request-id");
            if (userDetailsService.unlockUser(userLoginDTO)) {
                return toSuccessResult(null, "UNLOCK_SUCCESS");
            } else {
                return toExceptionResult("UNLOCK_FAILED", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }

//        } else {
//            return toExceptionResult("KHÔNG PHẬN SỰ MIỄN VÀO", Const.API_RESPONSE.RETURN_CODE_ERROR_NOTFOUND);
//        }
    }

    /**
     * Update profile in app_user table
     *
     * @param request
     * @param userLoginDTO
     * @return Success or fail
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateUser(HttpServletRequest request,
                                         @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String userId = this.getCurrentUser().getUid().trim();
            String requestId = request.getHeader("request-id");
            if (userDetailsService.updateUser(userId, userLoginDTO)) {
                return toSuccessResult(null, "CẬP NHẬT THÔNG TIN THÀNH CÔNG");
            } else {
                return toExceptionResult("CẬP NHẬT THÔNG TIN THẤT BẠI", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {

            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    /**
     * Get refresh token after token has expired
     *
     * @param request
     * @return new token
     */

//    @RequestMapping(value = "/refresh-token", method = RequestMethod.GET)
//    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
//
//        try {
//
//            String authorizationHeader = request.getHeader("Authorization");
//            System.out.println("Authorization Header: " + authorizationHeader); // Log token
//            DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");
//            Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
//            String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
//
//            return toSuccessResult(new JwtResponse(token, null), "Successfully");
//        } catch (IllegalArgumentException e) {
//            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
//        } catch (Exception e) {
//            return toExceptionResult("SYSTEM_ERROR", Const.API_RESPONSE.SYSTEM_CODE_ERROR);
//        }
//    }
//
//    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
//        return new HashMap<>(claims);
//    }
    @RequestMapping(value = "/refresh-token", method = RequestMethod.GET)
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Lấy Refresh Token từ cookie
            String refreshToken = jwtTokenUtil.getTokenFromCookie(request);

            if (refreshToken == null || !jwtTokenUtil.validateToken(refreshToken)) {
                return toExceptionResult("Invalid or expired refresh token", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }

            // Giải mã token để lấy thông tin claims
            Claims claims = jwtTokenUtil.getAllClaimsFromToken(refreshToken);

            UserInfoDTO userInfo = new UserInfoDTO();
            userInfo.setEmail(claims.get("username", String.class));
            userInfo.setUid(claims.get("uid", String.class));
            userInfo.setRoles(claims.get("role", String.class)); // Nếu roles là List
            userInfo.setDeptCode(claims.get("dept", String.class));
            // Tạo Access Token mới
            String newAccessToken = jwtTokenUtil.generateToken(userInfo);


            Cookie accessTokenCookie = new Cookie("token", newAccessToken);
            accessTokenCookie.setHttpOnly(true); // Chỉ có server có thể truy cập
            accessTokenCookie.setSecure(true); // Yêu cầu HTTPS
            accessTokenCookie.setPath("/"); // Cookie có hiệu lực trên toàn ứng dụng
            accessTokenCookie.setMaxAge( 5 * 60); // Thời gian sống (15 phút)
            response.addCookie(accessTokenCookie); // Gửi cookie về client


            return toSuccessResult(new JwtResponse(newAccessToken, null), "Successfully refreshed token");

        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult("SYSTEM_ERROR", Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    /**
     * Verify user with token from email
     *
     * @param userLoginDTO
     * @return Success or fail
     */
    @PostMapping("/verify")
    public ResponseEntity<?> checkOtpToken(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            if (userDetailsService.checkTokenForUser(userLoginDTO)) {
                return toSuccessResult(null, "XÁC THỰC THÀNH CÔNG");
            } else {
                return toExceptionResult("TOKEN_INVALID", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    /**
     * Resend token to User's email
     *
     * @param userLoginDTO
     * @return Success or fail
     */
    @PostMapping("/verify-code/resend")
    public ResponseEntity<?> resendOTP(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            userDetailsService.resendOTP(userLoginDTO);
            return toSuccessResult(null, "GỬI LẠI OTP THÀNH CÔNG");
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    /**
     * get user information
     *
     * @param request
     * @param userUid
     * @return information of user
     */

    @GetMapping("/{userUid}")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request, @PathVariable("userUid") String userUid) {

        try {
            String requestId = request.getHeader("request-id");
            UserInfoDTO userInfoDTO = userDetailsService.getUserInfo(userUid);
            return toSuccessResult(userInfoDTO, "COMPLETED");
        } catch (IllegalArgumentException e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }


//    @GetMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String requestId = request.getHeader("request-id");
//            String tokenCode = jwtTokenUtil.extractJwtFromRequest(request);
//            boolean result = userDetailsService.logout(tokenCode);
//            return toSuccessResult(result, "");
//        } catch (IllegalArgumentException e) {
//            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
//        } catch (Exception e) {
//            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
//        }
//    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout(HttpServletResponse response) {
        try {
            // Tạo một cookie cùng tên với cookie chứa token, giá trị rỗng và thời gian sống = 0
            Cookie accessTokenCookie = new Cookie("token", null);
            accessTokenCookie.setHttpOnly(true); // Chỉ có server truy cập
            accessTokenCookie.setSecure(true); // Yêu cầu HTTPS
            accessTokenCookie.setPath("/"); // Phạm vi hiệu lực của cookie
            accessTokenCookie.setMaxAge(0); // Xóa cookie ngay lập tức
            response.addCookie(accessTokenCookie); // Gửi cookie xóa về phía client

            // Trả về phản hồi thành công
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Logout failed");
        }
    }

    @RequestMapping(value = "/check-auth", method = RequestMethod.POST)
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        // Lấy token từ cookie
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token == null || !jwtTokenUtil.validateToken(token)) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Unauthorized");
        }

        return toSuccessResult(null, "Successfully");
    }

}