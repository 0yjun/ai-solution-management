package phzzk.aisolutionmanagement.api.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import phzzk.aisolutionmanagement.common.constants.Role;

@Schema(description = "회원 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAdminDto {
        @Schema(description = "회원 ID", example = "1")
        @NotNull
        private Long id;

        @Schema(description = "로그인 ID", example = "admin1")
        @NotNull
        private String username;

        @Schema(description = "권한", example = "ADMIN")
        @NotNull
        private Role role;

        @Schema(description = "설명", example = "콘텐츠 관리자입니다.")
        private String description;
}