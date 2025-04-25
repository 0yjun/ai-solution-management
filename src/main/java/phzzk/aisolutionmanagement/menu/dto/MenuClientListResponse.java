package phzzk.aisolutionmanagement.menu.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuClientListResponse {
    private List<MenuClientDto> menus;
}
