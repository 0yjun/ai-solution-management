package phzzk.aisolutionmanagement.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import phzzk.aisolutionmanagement.common.constants.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원가입 응답 DTO")
public class SignupResponseDto {
        @Schema(description = "로그인 ID", example = "admin1")
        @NotBlank
        private Long id;

        @Schema(description = "권한", example = "ADMIN")
        @NotNull
        private String username;

        @Schema(description = "설명", example = "콘텐츠 관리자입니다.")
        private Role role;
}