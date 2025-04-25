package phzzk.aisolutionmanagement.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 요청 DTO")
public record LoginRequest(
        @Schema(description = "로그인 ID", example = "admin1")
        @NotBlank
        String username,

        @Schema(description = "비밀번호", example = "securePass123!")
        @NotBlank
        String password
) {}
