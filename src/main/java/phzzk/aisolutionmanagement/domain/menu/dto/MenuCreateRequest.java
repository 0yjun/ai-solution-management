package phzzk.aisolutionmanagement.domain.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import phzzk.aisolutionmanagement.common.constants.Role;

import java.util.Set;

@Schema(name = "MenuCreateRequest", description = "메뉴 생성 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateRequest {

    @Schema(description = "메뉴명", example = "Dashboard", required = true)
    @NotBlank(message = "메뉴 이름을 입력해주세요.")
    private String name;

    @Schema(description = "설명", example = "관리자 대시보드 화면", required = false)
    @Size(max = 255, message = "설명은 최대 255자까지 입력 가능합니다.")
    private String description;

    @Schema(description = "부모 메뉴 ID (최상위일 경우 null)", example = "1", required = false)
    private Long parentId;

    @Schema(description = "URL (슬래시(`/`)로 시작)", example = "/dashboard", required = true)
    @NotBlank(message = "URL을 입력해주세요.")
    @Pattern(regexp = "^/.*", message = "URL은 '/'로 시작해야 합니다.")
    private String url;

    @Schema(description = "아이콘명 (예: material icon 이름)", example = "dashboard", required = true)
    @NotBlank(message = "아이콘을 입력해주세요.")
    private String icon;

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
}
