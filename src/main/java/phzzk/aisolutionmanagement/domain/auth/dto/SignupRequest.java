package phzzk.aisolutionmanagement.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import phzzk.aisolutionmanagement.common.constants.Role;

@Schema(description = "회원가입 요청 DTO")
public record SignupRequest(
        @Schema(description = "로그인 ID", example = "admin1")
        @NotBlank
        String username,

        @Schema(description = "비밀번호", example = "securePass123!")
        @NotBlank
        String password,

        @Schema(description = "권한", example = "ROLE_SYSTEM_ADMIN")
        @NotNull
        Role role,

        @Schema(description = "설명", example = "콘텐츠 관리자입니다.")
        String description
) {}