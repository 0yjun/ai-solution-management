package phzzk.aisolutionmanagement.api.help.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import phzzk.aisolutionmanagement.api.menu.dto.MenuAdminDto;
import phzzk.aisolutionmanagement.common.constants.Role;

import java.util.List;
import java.util.Set;

@Schema(name = "HelpDto", description = "HelpAdminDto DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelpDto {

    @Schema(description = "도움말 ID", example = "42")
    private Long helpId;

    @Schema(description = "메뉴 ID", example = "42")
    private Integer menuId;

    @Schema(description = "도움말설명", example = "Dashboard 도움말에 대한 설명")
    private String helpDescription;

    @Schema(description = "도움말 이미지를 저장하는 blob 배열", example = "['asdfdasasdf....', 'asdfdasasdsd....',...]")
    private List<HelpImageDto> images;
}
