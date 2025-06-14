package phzzk.aisolutionmanagement.api.help.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import phzzk.aisolutionmanagement.api.help.dto.*;
import phzzk.aisolutionmanagement.api.help.service.HelpService;
import phzzk.aisolutionmanagement.api.menu.dto.MenuAdminDto;
import phzzk.aisolutionmanagement.api.menu.dto.MenuUpdateRequestDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/helps")
@RequiredArgsConstructor
@Slf4j
public class HelpController {
    private final HelpService helpService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getHelpAll() {
        List<HelpDto> data = helpService.getHelpDtoAll();

        // 3) 공통 응답 포맷
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code",    "SUCCESS");
        body.put("message", "메뉴 조회 성공");
        body.put("data",    data);

        return ResponseEntity.ok(body);
    }

    @GetMapping(params = "menuId")
    public ResponseEntity<Map<String, Object>> getHelpByMenuId(
            @RequestParam(name="menuId", required = true) Long menuId
    ) {
        HelpDto data = helpService.getHelpByMenuId(menuId);

        // 3) 공통 응답 포맷
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code",    "SUCCESS");
        body.put("message", "메뉴 조회 성공");
        body.put("data",    data);

        return ResponseEntity.ok(body);
    }

    /**
     * 도움말 이미지 스트리밍 제공
     *
     * @param helpId  도움말 ID
     * @param imageId 이미지 ID
     * @return 이미지 바이트 배열과 적절한 HTTP 헤더
     */
    @GetMapping(value = "/{helpId}/images/{imageId}")
    public ResponseEntity<byte[]> serveHelpImage(
            @PathVariable Long helpId,
            @PathVariable Long imageId) {

        // 서비스에서 blob + contentType + length 등을 얻어오는 DTO
        HelpImageResourceDto imageData = helpService.getHelpImageResource(helpId, imageId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(imageData.getContentType()));
        headers.setContentLength(imageData.getLength());
        // 필요하다면 캐시 제어 헤더
        headers.setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));

        return new ResponseEntity<>(
                imageData.getBlob(),
                headers,
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createHelp(
            @Validated @RequestBody HelpCreateRequestDto helpCreateRequestDto
            ) {
        HelpDto data = helpService.create(helpCreateRequestDto);

        // 3) 공통 응답 포맷
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code",    "SUCCESS");
        body.put("message", "도움말 생성 성공");
        body.put("data",    data);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/{helpId}")
    public ResponseEntity<Map<String, Object>> createHelpImage(
            @PathVariable Long helpId,
            @Validated @ModelAttribute HelpImageCreateRequestDto helpImageCreateRequestDto
    ) {
        HelpDto data = helpService.createHelpImage(helpId, helpImageCreateRequestDto);

        // 3) 공통 응답 포맷
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code",    "SUCCESS");
        body.put("message", "도움말 생성 성공");
        body.put("data",    data);

        return ResponseEntity.ok(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateHelp(
            @PathVariable Long id,
            @Valid @RequestBody HelpUpdateRequestDto request) {
        // 1) 서비스에서 저장하고, 생성된 메뉴 ID를 반환
        HelpDto helpDto = helpService.update(id, request);

        // 2) 응답 포맷에 맞춰 Map 생성
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "메뉴 수정이 완료되었습니다.");
        result.put("data", helpDto);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{helpId}/images/{helpImageId}")
    public ResponseEntity<Map<String, Object>> updateHelpImage(
            @PathVariable Long helpId,
            @PathVariable Long helpImageId,
            @Valid @ModelAttribute HelpImageUpdateRequestDto request) {
        // 1) 서비스에서 저장하고, 생성된 메뉴 ID를 반환
        HelpImageDto helpImageDto = helpService.updateHelpImage(helpId, helpImageId, request);

        // 2) 응답 포맷에 맞춰 Map 생성
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "메뉴 수정이 완료되었습니다.");
        result.put("data", helpImageDto);

        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteHelp(@PathVariable Long id) {
        // 1) 서비스에서 저장하고, 생성된 메뉴 ID를 반환
        helpService.delete(id);

        // 2) 응답 포맷에 맞춰 Map 생성
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "도움말 삭제가 완료 되었습니다.");
        result.put("data", null);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{helpId}/images/{imageId}")
    public ResponseEntity<Map<String, Object>> deleteHelpImage(
            @PathVariable Long helpId,
            @PathVariable Long imageId
    ) {
        // 1) 서비스에서 저장하고, 생성된 메뉴 ID를 반환
        helpService.deleteHelpImage(helpId, imageId);

        // 2) 응답 포맷에 맞춰 Map 생성
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "도움말 삭제가 완료 되었습니다.");
        result.put("data", null);

        return ResponseEntity.ok(result);
    }
}

