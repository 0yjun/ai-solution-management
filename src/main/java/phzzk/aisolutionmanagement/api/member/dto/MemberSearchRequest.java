package phzzk.aisolutionmanagement.api.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import phzzk.aisolutionmanagement.common.constants.Role;

@Schema(description = "회원 조회 요청 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchRequest {
    @Schema(description = "유저 검색어", example = "admin1")
    @NotNull
    private String search;

    @Schema(description = "권한", example = "ADMIN")
    @NotNull
    private Role role;
}