package phzzk.aisolutionmanagement.api.menu.dto;

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
    private Integer id;

    @Schema(description = "메뉴명", example = "Dashboard")
    private String name;

    @Schema(description = "URL", example = "/dashboard")
    private String url;

    @Schema(description = "메뉴순서", example = "1")
    private Integer seq;

    @Schema(description = "아이콘명", example = "dashboard")
    private String icon;

    @Schema(description = "부모 메뉴 ID (null일 수 있음)", example = "1")
    private Integer parentId;

    @Schema(description = "하위 메뉴 목록")
    private List<MenuClientDto> children;

    @Schema(description = "이전 메뉴 ID", example = "1")
    private Integer prevMenuId;

    @Schema(description = "이전 메뉴 url", example = "/prev")
    private String prevMenuUrl;

    @Schema(description = "이전 메뉴 이름", example = "이전메뉴")
    private String prevMenuName;

    @Schema(description = "다음 메뉴 ID", example = "1")
    private Long nextMenuId;

    @Schema(description = "다음 메뉴 url", example = "/next")
    private String nextMenuUrl;

    @Schema(description = "다음 메뉴 이름", example = "다음 메뉴")
    private String nextMenuName;
}