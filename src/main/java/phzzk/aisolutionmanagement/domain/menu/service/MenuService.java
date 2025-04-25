package phzzk.aisolutionmanagement.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phzzk.aisolutionmanagement.common.constants.Role;
import phzzk.aisolutionmanagement.common.exception.CustomException;
import phzzk.aisolutionmanagement.common.exception.ErrorCode;
import phzzk.aisolutionmanagement.domain.menu.repository.MenuRepository;
import phzzk.aisolutionmanagement.domain.menu.dto.MenuCreateRequest;
import phzzk.aisolutionmanagement.domain.menu.dto.MenuClientDto;
import phzzk.aisolutionmanagement.domain.menu.entity.Menu;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final  ModelMapper modelMapper;

    public List<MenuClientDto> getAccessibleMenusByUserRole(Role userRole) {
        return menuRepository.findAllActiveWithChildren().stream()
                // (1) 부모 권한 필터
                .filter(parent -> parent.getRoles().contains(userRole))
                // (2) 자식 활성·권한 필터
                .peek(parent -> {
                    List<Menu> filteredChildren = parent.getChildren().stream()
                            .filter(child -> child.isActive())          // 자식 활성 여부
                            .filter(child -> child.getRoles().contains(userRole))  // 자식 권한
                            .collect(Collectors.toList());
                    parent.setChildren(filteredChildren);
                })
                // (3) DTO 매핑
                .map(menu -> modelMapper.map(menu, MenuClientDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuClientDto createMenu(MenuCreateRequest request) {
        // 1. 부모 메뉴 조회 (ID가 null이면 null 반환)
        Menu parent = findParentOrNull(request.getParentId());

        // 2. 역할 유효성 검증
        validateRoles(request.getRoles(),parent);

        // 3. 요청 DTO를 Menu 엔티티로 매핑
        Menu newMenu = modelMapper.map(request, Menu.class);
        newMenu.setParent(parent);

        // 4. 메뉴 엔티티 저장
        Menu savedMenu = menuRepository.save(newMenu);

        // 5. 저장된 엔티티를 응답 DTO로 변환하여 반환
        return modelMapper.map(savedMenu, MenuClientDto.class);
    }

    private Menu findParentOrNull(Long parentId) {
        if(parentId == null){
            return null;
        }
        return menuRepository
                .findById(parentId)
                .orElseThrow(()->new CustomException(ErrorCode.MENU_PARENT_NOT_FOUND));
    }

    private void validateRoles(Set<Role> role, Menu parent) {
        if (role.isEmpty()) {
            //생성할 메뉴의 권한이 없을떄,
            throw new CustomException(ErrorCode.MENU_EMPTY_ROLES);
        }
        if (parent != null && !parent.getRoles().containsAll(role)) {
            //부모가 존재하고 자식의 권한이 부모 권한에 포함되지 않을때
            throw new CustomException(ErrorCode.MENU_ROLE_SUBSET_INVALID);
        }
    }


}