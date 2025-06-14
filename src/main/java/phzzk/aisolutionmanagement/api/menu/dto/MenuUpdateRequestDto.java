package phzzk.aisolutionmanagement.api.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import phzzk.aisolutionmanagement.common.constants.Role;

import java.util.List;
import java.util.Set;

@Schema(name = "MenuCreateRequest", description = "메뉴 수정 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuUpdateRequestDto {
    @Schema(description = "메뉴아이디", example = "1")
    private Integer id;

    @Schema(description = "메뉴명", example = "Dashboard")
    private String name;

    @Schema(description = "메뉴설명", example = "Dashboard")
    private String description;

    @Schema(description = "URL", example = "/dashboard")
    private String url;

    @Schema(description = "메뉴순서", example = "1")
    private Integer seq;

    @Schema(description = "아이콘명", example = "dashboard")
    private String icon;

    @Schema(description = "활성화 여부", example = "true | false")
    private boolean isActive;

    @Schema(description = "부모 메뉴 ID (null일 수 있음)", example = "1")
    private Integer parentId;

    @Schema(
            description = "허용된 Role 목록 (최소 1개, 최대 4개)",
            implementation = Role.class,
            required = true
    )
    @NotEmpty(message = "권한을 최소 한 개 이상 선택해야 합니다.")
    @Size(
            max = Role.MAX_COUNT,
            message = "권한은 최대 {max}개까지 선택 가능합니다."
    )
    private Set<@NotNull Role> roles;

    @Schema(description = "이전 메뉴 ID", example = "1")
    private Integer prevMenuId;

    @Schema(description = "다음 메뉴 ID", example = "1")
    private Integer nextMenuId;
}
