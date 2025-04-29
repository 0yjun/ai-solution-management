package phzzk.aisolutionmanagement.api.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import phzzk.aisolutionmanagement.common.constants.Role;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/constants")
@Slf4j
public class ConstantsController {
    @AllArgsConstructor
    @Getter
    public static class RoleInfo{
        private final String value;
        private final String label;
    }
    @GetMapping("roles")
    public ResponseEntity<Map<String, Object>> getRoles(Authentication authentication) {
        List<RoleInfo> roleInfos = Arrays.stream(Role.values())
                .map(r->new RoleInfo(r.name(),r.getDescription()))
                .collect(Collectors.toList());


        // 3) 응답 맵 구성
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "성공");
        result.put("data", roleInfos);

        return ResponseEntity.ok(result);
    }
}
