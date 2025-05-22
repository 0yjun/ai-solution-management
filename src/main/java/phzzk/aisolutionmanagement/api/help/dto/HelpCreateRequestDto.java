package phzzk.aisolutionmanagement.api.help.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(name = "HelpDto", description = "HelpAdminDto DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelpCreateRequestDto {
    @Schema(description = "메뉴 ID", example = "42")
    private Integer menuId;

    @Schema(description = "도움말설명", example = "Dashboard 도움말에 대한 설명")
    private String helpDescription;
}
