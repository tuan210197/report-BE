package com.foxconn.EmployeeManagerment.security;



import com.foxconn.EmployeeManagerment.dto.request.UserInfoDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.io.Serializable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author tuandv
 * The JwtTokenUtil is responsible for performing JWT operations like creation and validation. It makes use of the io.jsonwebtoken.Jwts for achieving this.
 */
@Slf4j
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${jwt.expireToken}")
    private long JWT_TOKEN_VALIDITY;

    @Value("${jwt.secret}")
    private String secret;


    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    Boolean isTokenExpired(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
////        return expiration.before(new Date());
//
//        Instant now = Instant.now();  // Lấy thời gian hiện tại UTC
//        return expiration.toInstant().isBefore(now);  // So sánh expiration UTC với thời gian hiện tại UTC

        try {
            final Date expiration = getExpirationDateFromToken(token);
            Instant now = Instant.now();  // Lấy thời gian hiện tại UTC
            // Thêm clock skew vào thời gian hết hạn để kiểm tra
            Instant expirationTime = expiration.toInstant();
            return expirationTime.isBefore(now);  // Nếu hết hạn, trả về true
        } catch (Exception e) {
            return true;  // Nếu có lỗi, coi token đã hết hạn
        }
    }

    //generate token for user
    public String generateToken(UserInfoDTO users) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", users.getEmail());
        claims.put("uid", users.getUid());
        claims.put("role", users.getRoles());
        claims.put("dept", users.getDeptCode());

        return doGenerateToken(claims, users.getEmail());

    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
    //validate token
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
// Validate token (đơn giản hóa, không phụ thuộc vào UserDetails)

    public Boolean validateToken(String token) {
        try {
            // Kiểm tra xem token đã hết hạn hay chưa
            if (isTokenExpired(token)) {
                return false;
            }
            // Lấy username từ token (để kiểm tra token có hợp lệ không)
            String username = getUsernameFromToken(token);
            // Nếu có username và token không expired, token hợp lệ
            return username != null && !username.isEmpty();
        } catch (Exception e) {
            // Xử lý nếu token không hợp lệ hoặc gặp lỗi
            e.printStackTrace();
            return false;
        }
    }

//    public String extractJwtFromRequest(HttpServletRequest request) {
//        log.info("request: {}", request);
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
    public String extractJwtFromRequest(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    if(checkForRefreshToken(cookie.getValue())){

                        Claims claims = getAllClaimsFromToken(cookie.getValue());

                        UserInfoDTO userInfo = new UserInfoDTO();
                        userInfo.setEmail(claims.get("username", String.class));
                        userInfo.setUid(claims.get("uid", String.class));
                        userInfo.setRoles(claims.get("role", String.class)); // Nếu roles là List
                        userInfo.setDeptCode(claims.get("dept", String.class));
                        // Tạo Access Token mới
                        String newAccessToken = generateToken(userInfo);


                        Cookie accessTokenCookie = new Cookie("token", newAccessToken);
                        accessTokenCookie.setHttpOnly(true); // Chỉ có server có thể truy cập
                        accessTokenCookie.setSecure(true); // Yêu cầu HTTPS
                        accessTokenCookie.setPath("/"); // Cookie có hiệu lực trên toàn ứng dụng
                        accessTokenCookie.setMaxAge( 5 * 60); // Thời gian sống (15 phút)
                        response.addCookie(accessTokenCookie); // Gửi cookie về client
                        return newAccessToken;
                    }

                    return cookie.getValue();

                }
            }
        }
        return null; // Token không có trong cookie
    }
    Boolean checkForRefreshToken(String token) {

        try {
            final Date expiration = getExpirationDateFromToken(token);
            Instant now = Instant.now();  // Lấy thời gian hiện tại UTC
            // Thêm clock skew vào thời gian hết hạn để kiểm tra
            Instant expirationTime = expiration.toInstant();
            long diffInSeconds = ChronoUnit.SECONDS.between(now, expirationTime);
            return diffInSeconds <= 300;  // Nếu hết hạn, trả về true
        } catch (Exception e) {
            return true;  // Nếu có lỗi, coi token đã hết hạn
        }
    }
    public String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}