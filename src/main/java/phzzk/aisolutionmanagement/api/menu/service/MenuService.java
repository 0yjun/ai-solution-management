package phzzk.aisolutionmanagement.api.menu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phzzk.aisolutionmanagement.api.menu.dto.MenuAdminDto;
import phzzk.aisolutionmanagement.api.menu.dto.MenuCreateRequestDto;
import phzzk.aisolutionmanagement.api.menu.dto.MenuUpdateRequestDto;
import phzzk.aisolutionmanagement.common.constants.Role;
import phzzk.aisolutionmanagement.common.exception.CustomException;
import phzzk.aisolutionmanagement.common.exception.ErrorCode;
import phzzk.aisolutionmanagement.api.menu.repository.MenuRepository;
import phzzk.aisolutionmanagement.api.menu.dto.MenuClientDto;
import phzzk.aisolutionmanagement.api.menu.entity.Menu;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {
    private final MenuRepository menuRepository;
    private final  ModelMapper modelMapper;

    /**
     * 사용자 권한별 메뉴 조회
     * @return 권한에 맞는 사용자 메뉴 트리
     */
    public List<MenuClientDto> getAccessibleMenusByUserRole(Role userRole) {
        return menuRepository.findByParentIsNullAndIsActiveTrueOrderBySeq().stream()
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
                .map(this::toClientMenuDto)   // 재귀 호출
                .collect(Collectors.toList());
    }

    /**
     * 주어진 Role에 맞는 최상위 메뉴와 자식 메뉴를 트리 형태로 조회
     *
     * @param menu 엔티티
     * @return 권한에 맞는 메뉴 트리
     */
    private MenuClientDto toClientMenuDto(Menu menu) {
        // 1) 기본 필드 매핑
        MenuClientDto dto = modelMapper.map(menu, MenuClientDto.class);

        // 2) prev/next 매핑
        if (menu.getPrevMenuId() != null && menu.getPrevMenu() != null) {
            dto.setPrevMenuName(menu.getPrevMenu().getName());
            dto.setPrevMenuUrl( menu.getPrevMenu().getUrl());
        }
        if (menu.getNextMenuId() != null && menu.getNextMenu() != null) {
            dto.setNextMenuName(menu.getNextMenu().getName());
            dto.setNextMenuUrl( menu.getNextMenu().getUrl());
        }

        // 3) children 재귀 매핑
        List<MenuClientDto> childDtos = menu.getChildren().stream()
                .map(this::toClientMenuDto)
                .collect(Collectors.toList());
        dto.setChildren(childDtos);

        return dto;
    }

    /**
     * 주어진 Role에 맞는 최상위 메뉴와 자식 메뉴를 트리 형태로 조회
     *
     * @param rolename 사용자 권한
     * @return 권한에 맞는 메뉴 트리
     */
    public List<MenuAdminDto> findAdminMenuByRoleParam(String rolename) {
        return menuRepository.findDistinctByParentIsNullAndRolesContainingOrderBySeq(rolename).stream()
                .map(this::toAdminMenuDto)
                .collect(Collectors.toList());
    }

    public List<MenuAdminDto> findAdminMenuAll() {
        return menuRepository.findByParentIsNullOrderBySeq().stream()
                .map(this::toAdminMenuDto)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 Role에 맞는 최상위 메뉴와 자식 메뉴를 트리 형태로 조회
     *
     * @param menu 엔티티
     * @return 권한에 맞는 메뉴 트리
     */
    private MenuAdminDto toAdminMenuDto(Menu menu) {
        log.info(menu.toString());
        // 1) 기본 필드 매핑
        MenuAdminDto dto = modelMapper.map(menu, MenuAdminDto.class);

        // 2) prev/next 매핑
        if (menu.getPrevMenuId() != null && menu.getPrevMenu() != null) {
            dto.setPrevMenuName(menu.getPrevMenu().getName());
            dto.setPrevMenuUrl( menu.getPrevMenu().getUrl());
        }
        if (menu.getNextMenuId() != null && menu.getNextMenu() != null) {
            dto.setNextMenuName(menu.getNextMenu().getName());
            dto.setNextMenuUrl( menu.getNextMenu().getUrl());
        }

        // 3) children 재귀 매핑
        List<MenuAdminDto> childDtos = menu.getChildren().stream()
                .map(this::toAdminMenuDto)
                .collect(Collectors.toList());
        dto.setChildren(childDtos);

        return dto;
    }

    @Transactional
    public MenuClientDto createMenu(MenuCreateRequestDto request) {
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

    private Menu findParentOrNull(Integer parentId) {
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

    public MenuClientDto updateMenu(Integer menuId, MenuUpdateRequestDto request) {
        // 1. 수정 대상 메뉴 조회
        Menu existing = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        // 2. 부모 메뉴 조회 (ID가 null이면 null 반환)
        Menu parent = findParentOrNull(request.getParentId());

        // 2-1. 자기 자신을 부모로 지정할 수 없도록 방지
        if (request.getParentId() != null && request.getParentId().equals(menuId)) {
            throw new CustomException(ErrorCode.MENU_PARENT_CYCLE_INVALID);
        }

        // 3. 역할 유효성 검증 (빈 집합, 부모 권한 포함 여부)
        validateRoles(request.getRoles(), parent);

        // 4. 요청 DTO의 변경값을 기존 엔티티에 매핑
        modelMapper.map(request, existing);
        existing.setParent(parent);

        // 5. 엔티티 저장 및 응답 DTO 변환
        Menu updated = menuRepository.save(existing);
        return modelMapper.map(updated, MenuClientDto.class);
    }
}