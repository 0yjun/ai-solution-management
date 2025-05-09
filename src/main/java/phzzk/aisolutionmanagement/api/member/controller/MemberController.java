package phzzk.aisolutionmanagement.api.member.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import phzzk.aisolutionmanagement.api.member.dto.MemberSearchRequest;
import phzzk.aisolutionmanagement.common.constants.Role;
import phzzk.aisolutionmanagement.config.security.JwtTokenProvider;
import phzzk.aisolutionmanagement.api.member.service.MemberService;
import phzzk.aisolutionmanagement.api.member.dto.MemberDto;
import phzzk.aisolutionmanagement.api.member.entity.Member;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity<Map<String, Object>> searchPage(
            @RequestParam(name="search", defaultValue="") String search,
            @RequestParam(name="role", required = false) Role role,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<MemberDto> members =  memberService.getMemberPage(search, role, pageable);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "유저 조회 성공");
        result.put("data", members);

        return ResponseEntity.ok(result);
    }


    @GetMapping("/{id}")
    public Member detail(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @GetMapping("me")
    public ResponseEntity<Map<String, Object>> getMyInfo(Authentication authentication) {
        // 1) Authentication에서 username, ROLE 추출
        String username = authentication.getName();
        Role role = jwtTokenProvider.getRoleFromAuthentication(authentication);

        // 2) 응답 데이터 조립
        Map<String, Object> userData = new LinkedHashMap<>();
        userData.put("username", username);
        userData.put("role", role);

        // 3) 응답 맵 구성
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "유저 조회 성공");
        result.put("data", userData);

        return ResponseEntity.ok(result);
    }
}
