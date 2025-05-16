package phzzk.aisolutionmanagement.api.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import phzzk.aisolutionmanagement.common.constants.Role;

@Schema(description = "회원 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberClientDto {
        @Schema(description = "로그인 ID", example = "admin1")
        @NotNull
        private String username;

        @Schema(description = "권한", example = "ADMIN")
        @NotNull
        private Role role;
}