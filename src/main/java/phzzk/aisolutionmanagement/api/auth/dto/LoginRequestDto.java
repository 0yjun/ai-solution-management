package phzzk.aisolutionmanagement.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "로그인 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

        @Schema(description = "로그인 ID", example = "admin1")
        @NotBlank
        private String username;

        @Schema(description = "비밀번호", example = "securePass123!")
        @NotBlank
        private String password;

}
