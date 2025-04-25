package phzzk.aisolutionmanagement.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import phzzk.aisolutionmanagement.common.exception.CustomException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtSecurityFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if(token !=null){
            try {
                if(jwtTokenProvider.validateToken(token)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch (CustomException e) {
                // JWT 관련 예외 처리
                e.printStackTrace();
                SecurityContextHolder.clearContext();
                response.setStatus(e.getErrorCode().getHttpStatus().value());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                        "{\"code\":\"" + e.getErrorCode().getCode() + "\"," +
                        " \"message\":\"" + e.getErrorCode().getMessage() + "\"}"
                );
                return;
            }catch (Exception e){
                e.printStackTrace();
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // 1) Bearer 에서 추출
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        // 2) 쿠키에서 access_token 찾기 
        Cookie cookie = WebUtils.getCookie(request, "access_token");
        return (cookie != null ? cookie.getValue() : null);

//        Cookie[] cookies = request.getCookies();
//        String authToken = null;
//        if (cookies == null) {
//            log.info("1) request.getCookies() returned null → 쿠키가 없습니다.");
//        } else {
//            log.info("1) request.getCookies() → {} 개의 쿠키 발견", cookies.length);
//
//            // 2) 모든 쿠키 이름·값 출력
//            for (Cookie c : cookies) {
//                log.info("   ▶ cookie name='{}', value='{}'", c.getName(), c.getValue());
//            }
//
//            // 3) auth_token 쿠키 필터링
//            Optional<Cookie> authCookie = Arrays.stream(cookies)
//                    .filter(c -> "access_token".equals(c.getName()))
//                    .peek(c -> log.info("2) auth_token 쿠키 필터링 → 해당 쿠키 발견"))
//                    .findFirst();
//
//            if (authCookie.isPresent()) {
//                log.info("3) auth_token 쿠키가 존재합니다.");
//                // 4) 값 추출
//                authToken = authCookie.get().getValue();
//                log.info("4) auth_token 값 → {}", authToken);
//                // 이후 authToken 활용 로직…
//            } else {
//                log.info("2) auth_token 쿠키가 없습니다.");
//            }
//        }
//        return authToken;
    }
}