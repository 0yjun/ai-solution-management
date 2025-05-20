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
public class MemberResetPasswordRequestDto {
        @Schema(description = "회원 ID", example = "1")
        @NotNull
        private Long id;

        @Schema(description = "비밀번호", example = "q1w2e3r4")
        @NotBlank
        private String password;
}