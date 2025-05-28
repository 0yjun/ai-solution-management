package phzzk.aisolutionmanagement.api.menu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import phzzk.aisolutionmanagement.api.menu.dto.MenuAdminDto;
import phzzk.aisolutionmanagement.api.menu.dto.MenuCreateRequestDto;
import phzzk.aisolutionmanagement.api.menu.dto.MenuUpdateRequestDto;
import phzzk.aisolutionmanagement.common.constants.Role;
import phzzk.aisolutionmanagement.common.exception.CustomException;
import phzzk.aisolutionmanagement.common.exception.ErrorCode;
import phzzk.aisolutionmanagement.config.security.JwtTokenProvider;
import phzzk.aisolutionmanagement.api.menu.dto.MenuClientDto;
import phzzk.aisolutionmanagement.api.menu.service.MenuService;

import java.util.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Slf4j
public class MenuController {
    private final MenuService menuService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 사용자 권한별 메뉴 조회
     *
     * @return 200
     */
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
     * 관리자 권한별 메뉴 조회
     * @url /members/menus?role=all
     * @param roleParam 메뉴 권한
     * @return 200
     */
    @GetMapping(params = "role")
    public ResponseEntity<Map<String, Object>> getMenuByRole(
            @RequestParam(name="role", defaultValue="all") String roleParam
    ) {
        // 1) 유효성 검사: "all" 이 아니면서 enum 에도 없으면 400 에러
        if (!"all".equalsIgnoreCase(roleParam) && !Role.contains(roleParam)) {
            throw new CustomException(ErrorCode.ROLE_NOT_FOUND);
        }

        // 2) 서비스 호출 (DTO 변환은 service 내부에서)
        List<MenuAdminDto> menus;
        if ("all".equalsIgnoreCase(roleParam)) {
            menus = menuService.findAdminMenuAll();
        } else {
            menus = menuService.findAdminMenuByRoleParam(roleParam);
        }

        // 3) 공통 응답 포맷
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code",    "SUCCESS");
        body.put("message", "메뉴 조회 성공");
        body.put("data",    menus);

        return ResponseEntity.ok(body);
    }

    /**
     * 관리자 권한별 메뉴 조회
     * @url /members/menus/1234
     * @pathvariable roleParam 메뉴 권한
     * @return 200
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<Map<String, Object>> getMenuById(
            @PathVariable Integer menuId,
            Authentication authentication
    ) {
        Role userRole = jwtTokenProvider.getRoleFromAuthentication(authentication);
        log.info(userRole.toString());

        MenuAdminDto result =  menuService.getMenuAdminDtoById(menuId);

        // 3) 공통 응답 포맷
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code",    "SUCCESS");
        body.put("message", "메뉴 조회 성공");
        body.put("data",    result);

        return ResponseEntity.ok(body);
    }

    /**
     * 메뉴 생성
     *
     * @param request 메뉴 생성 정보
     * @return 201 Created
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createMenu(@Valid @RequestBody MenuCreateRequestDto request) {
        // 1) 서비스에서 저장하고, 생성된 메뉴 ID를 반환
        MenuClientDto menuClientDto = menuService.createMenu(request);

        // 2) 응답 포맷에 맞춰 Map 생성
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "메뉴 생성이 완료되었습니다.");

        result.put("data", menuClientDto);

        return ResponseEntity.ok(result);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteMenu(@PathVariable Integer id) {
        // 1) 서비스에서 저장하고, 생성된 메뉴 ID를 반환
        menuService.deleteMenu(id);

        // 2) 응답 포맷에 맞춰 Map 생성
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "메뉴 수정이 완료되었습니다.");
        result.put("data", null);

        return ResponseEntity.ok(result);
    }
}

