package phzzk.aisolutionmanagement.api.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import phzzk.aisolutionmanagement.api.auth.dto.LoginRequestDto;
import phzzk.aisolutionmanagement.api.auth.dto.LoginResponseDto;
import phzzk.aisolutionmanagement.api.auth.dto.SignupRequestDto;
import phzzk.aisolutionmanagement.api.auth.dto.SignupResponseDto;
import phzzk.aisolutionmanagement.api.auth.service.AuthService;
import phzzk.aisolutionmanagement.api.member.dto.MemberClientDto;
import phzzk.aisolutionmanagement.config.security.JwtTokenProvider;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 및 사용자 관리 API")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    @Operation(summary = "회원 등록", description = "새로운 관리자를 등록합니다.")
    public ResponseEntity<Map<String, Object>> createMember(@RequestBody @Valid SignupRequestDto request) {

        MemberClientDto memberClientDto = authService.signup(request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "회원가입이 완료되었습니다.");
        result.put("data", memberClientDto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "아이디/비밀번호로 로그인하고 JWT 토큰을 발급받습니다.")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginRequestDto request) {
        LoginResponseDto LoginResponseDto = authService.login(request);

        String token = jwtTokenProvider.createToken(LoginResponseDto.getUsername(), LoginResponseDto.getRole().name());
        ResponseCookie cookie = ResponseCookie.from("access_token",token)
                .httpOnly(true)
                .secure(true)
                .sameSite("none")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();
        log.info(cookie.getValue());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "성공");
        result.put("data", LoginResponseDto);
//
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result);
    }
}