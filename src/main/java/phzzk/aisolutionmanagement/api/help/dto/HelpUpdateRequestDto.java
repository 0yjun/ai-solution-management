package phzzk.aisolutionmanagement.api.help.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "HelpDto", description = "HelpAdminDto DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelpUpdateRequestDto {
    @Schema(description = "도움말 ID", example = "42")
    private Long helpId;

    @Schema(description = "도움말설명", example = "Dashboard 도움말에 대한 설명")
    private String helpDescription;
}
