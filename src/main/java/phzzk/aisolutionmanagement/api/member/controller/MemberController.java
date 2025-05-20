package phzzk.aisolutionmanagement.api.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import phzzk.aisolutionmanagement.api.member.dto.MemberAdminDto;
import phzzk.aisolutionmanagement.api.member.dto.MemberCreateRequestDto;
import phzzk.aisolutionmanagement.api.member.dto.MemberResetPasswordRequestDto;
import phzzk.aisolutionmanagement.api.member.dto.MemberUpdateRequestDto;
import phzzk.aisolutionmanagement.common.constants.Role;
import phzzk.aisolutionmanagement.config.security.JwtTokenProvider;
import phzzk.aisolutionmanagement.api.member.service.MemberService;
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
        Page<MemberAdminDto> members =  memberService.getMemberPage(search, role, pageable);

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

    @PostMapping
    @Operation(summary = "회원 생성", description = "새로운 관리자를 등록합니다.")
    public ResponseEntity<Map<String, Object>> createMember(
            @RequestBody @Valid MemberCreateRequestDto memberCreateRequestDto
    ) {
        MemberAdminDto memberAdminDto = memberService.createMemberByAdmin(memberCreateRequestDto);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "회원가입이 완료되었습니다.");
        result.put("data", memberAdminDto);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{memberId}")
    @Operation(summary = "회원 수정", description = "관리자를 수정합니다.")
    public ResponseEntity<Map<String, Object>> updateMember(
            @PathVariable Long memberId,
            @RequestBody @Valid MemberUpdateRequestDto memberUpdateRequestDto
            ) {
        MemberAdminDto memberAdminDto = memberService.updateMember(memberId,memberUpdateRequestDto);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "회원정보가 수정되었습니다.");
        result.put("data", memberAdminDto);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{memberId}/password/reset")
    @Operation(summary = "비밀번호 초기화", description = "비밀번호를 초기화 합니다.")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @PathVariable Long memberId,
            @RequestBody @Valid MemberResetPasswordRequestDto memberResetPasswordRequestDto
    ) {
        MemberAdminDto memberAdminDto = memberService.resetPassword(memberId,memberResetPasswordRequestDto);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "비밀번호가 초기화 되었습니다.");
        result.put("data", memberAdminDto);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "회원 삭제", description = "관리자를 삭제합니다.")
    public ResponseEntity<Map<String, Object>> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "회원이 삭제되었습니다.");
        result.put("data", null);

        return ResponseEntity.ok(result);
    }

}
