package phzzk.aisolutionmanagement.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(name = "MenuResponse", description = "메뉴 응답 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuClientDto {

    @Schema(description = "메뉴 ID", example = "42")
    private Long id;

    @Schema(description = "메뉴명", example = "Dashboard")
    private String name;

    @Schema(description = "URL", example = "/dashboard")
    private String url;

    @Schema(description = "아이콘명", example = "dashboard")
    private String icon;

    @Schema(description = "부모 메뉴 ID (null일 수 있음)", example = "1")
    private Long parentId;

    @Schema(description = "하위 메뉴 목록")
    private List<MenuClientDto> children;
}