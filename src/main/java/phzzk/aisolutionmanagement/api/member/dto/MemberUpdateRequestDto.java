package phzzk.aisolutionmanagement.api.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import phzzk.aisolutionmanagement.common.constants.Role;

@Schema(description = "회원 수정 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateRequestDto {
        @Schema(description = "회원 ID", example = "1")
        @NotNull
        private Long id;

        @Schema(description = "로그인 ID", example = "admin1")
        @NotBlank
        private String username;

        @Schema(description = "비밀번호", example = "securePass123!")
        @NotBlank
        private String password;

        @Schema(description = "권한", example = "ROLE_SYSTEM_ADMIN")
        @NotNull
        private Role role;

        @Schema(description = "설명", example = "유저에대한 설명")
        @NotNull
        private String description;
}