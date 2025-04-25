package phzzk.aisolutionmanagement.domain.menu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import phzzk.aisolutionmanagement.common.constants.Role;
import phzzk.aisolutionmanagement.config.security.JwtTokenProvider;
import phzzk.aisolutionmanagement.domain.menu.dto.MenuClientDto;
import phzzk.aisolutionmanagement.domain.menu.service.MenuService;
import phzzk.aisolutionmanagement.domain.menu.dto.MenuCreateRequest;

import java.util.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Slf4j
public class MenuController {
    private final MenuService menuService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getMenu(Authentication authentication) {
        // 1) authentication 으로 부터 권한 추출
        Role userRole = jwtTokenProvider.getRoleFromAuthentication(authentication);

        // 3) 서비스 호출 (단일 Role 전달)
        List<MenuClientDto> menus = menuService.getAccessibleMenusByUserRole(userRole);
        // 4) 응답 맵 구성
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "메뉴 조회 성공");

        result.put("data", menus);

        return ResponseEntity.ok(result);
    }

    /**
     * 메뉴 생성
     *
     * @param request 메뉴 생성 정보
     * @return 201 Created
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createMenu(@Valid @RequestBody MenuCreateRequest request) {
        // 1) 서비스에서 저장하고, 생성된 메뉴 ID를 반환
        MenuClientDto menuClientDto = menuService.createMenu(request);

        // 2) 응답 포맷에 맞춰 Map 생성
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "메뉴 생성이 완료되었습니다.");

        result.put("data", menuClientDto);

        return ResponseEntity.ok(result);
    }
}

