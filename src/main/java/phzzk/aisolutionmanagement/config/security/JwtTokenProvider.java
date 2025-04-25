package phzzk.aisolutionmanagement.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import phzzk.aisolutionmanagement.common.constants.Role;
import phzzk.aisolutionmanagement.common.exception.CustomException;
import phzzk.aisolutionmanagement.common.exception.ErrorCode;

import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}") // milliseconds
    private long expirationTime;

    private Key signingKey;

    private static final String ROLE_PREFIX = "ROLE_";

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        signingKey = Keys.hmacShaKeyFor(keyBytes);
        log.info("init : ");
        log.info(secretKey);
        log.info(String.valueOf(expirationTime));
        log.info(keyBytes.toString());
        log.info(signingKey.toString());
    }
    public String createToken(String username, String role){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role",role);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String username = getusername(token);
        String role = getRole(token);

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX+role))
        );
    }

    public String getusername(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRole(String token) {
        return (String) parseClaims(token).get("role");
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.JWT_EXPIRED);
        } catch (MalformedJwtException | SecurityException | UnsupportedJwtException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.JWT_INVALID);
        }
    }

    private Claims parseClaims(String token) {
        //log.info(signingKey.toString());
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Role getRoleFromAuthentication(Authentication authentication) {
        // 1. Authentication 에서 GrantedAuthority 목록 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 2. 역할이 2개 이상이면 예외처리
        if (authorities.size() > 1) {
            throw new CustomException(ErrorCode.MULTIPLE_ROLES_FOUND);
        }

        // 3. 단일 권한 문자열을 가져오거나, 없으면 AUTH_NOT_FOUND 예외 발생
        String authority = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_NOT_FOUND));

        // 4. "ROLE_" 접두사가 올바르게 붙어 있는지 검증
        if (!authority.startsWith(ROLE_PREFIX)) {
            throw new CustomException(ErrorCode.INVALID_ROLE_FORMAT);
        }

        // 5. 접두사를 제거해 순수 권한명(roleName)을 추출
        String roleName = authority.substring(ROLE_PREFIX.length());

        // 6. Role enum으로 변환, 실패 시 UNKNOWN_ROLE 예외 발생
        try {
            return Role.valueOf(roleName);
        } catch (IllegalArgumentException ex) {
            throw new CustomException(ErrorCode.UNKNOWN_ROLE);
        }
    }
}
