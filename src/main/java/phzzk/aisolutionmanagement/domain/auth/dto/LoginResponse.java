package phzzk.aisolutionmanagement.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import phzzk.aisolutionmanagement.common.constants.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "로그인 응답 DTO")
public class LoginResponse {
        @Schema(description = "유저이름", example = "user1234...")
        private String username; // private final 필드

        @Schema(description = "유저 권한", example = "ROLE_SYSTEM_ADMIN")
        private Role role; // private final 필드
}
