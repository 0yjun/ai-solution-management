package phzzk.aisolutionmanagement.api.help.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phzzk.aisolutionmanagement.api.help.dto.HelpCreateRequestDto;
import phzzk.aisolutionmanagement.api.help.dto.HelpDto;
import phzzk.aisolutionmanagement.api.help.dto.HelpImageCreateRequestDto;
import phzzk.aisolutionmanagement.api.help.dto.HelpUpdateRequestDto;
import phzzk.aisolutionmanagement.api.help.entity.Help;
import phzzk.aisolutionmanagement.api.help.entity.HelpImage;
import phzzk.aisolutionmanagement.api.help.repository.HelpRepository;
import phzzk.aisolutionmanagement.api.menu.dto.MenuAdminDto;
import phzzk.aisolutionmanagement.api.menu.dto.MenuClientDto;
import phzzk.aisolutionmanagement.api.menu.dto.MenuCreateRequestDto;
import phzzk.aisolutionmanagement.api.menu.dto.MenuUpdateRequestDto;
import phzzk.aisolutionmanagement.api.menu.entity.Menu;
import phzzk.aisolutionmanagement.api.menu.repository.MenuRepository;
import phzzk.aisolutionmanagement.api.menu.service.MenuService;
import phzzk.aisolutionmanagement.common.constants.Role;
import phzzk.aisolutionmanagement.common.exception.CustomException;
import phzzk.aisolutionmanagement.common.exception.ErrorCode;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelpService {
    private final MenuService menuService;
    private final HelpRepository helpRepository;
    private final  ModelMapper modelMapper;

    public List<HelpDto> getHelpDtoAll(){
        return helpRepository.findAll()
                .stream().map(item->modelMapper.map(item,HelpDto.class))
                .collect(Collectors.toList());
    }

    public HelpDto getHelpByMenuId(Long menuId){
        Help help = helpRepository.findByMenu_Id(menuId)
                .orElseThrow(()-> new CustomException(ErrorCode.MENU_NOT_FOUND));
        return modelMapper.map(help,HelpDto.class);
    }

    public HelpDto create(HelpCreateRequestDto helpCreateRequestDto){

        Menu menu = menuService.findMenuById(helpCreateRequestDto.getMenuId());

        Help help = Help.builder()
                .menu(menu)
                .helpDescription(helpCreateRequestDto.getHelpDescription())
                .build();
        log.info(help.toString());
        helpRepository.save(help);
        return modelMapper.map(help,HelpDto.class);
    }

    @Transactional
    public HelpDto update(Long helpId, HelpUpdateRequestDto helpUpdateRequestDto) {
        // 1. 수정 대상 메뉴 조회
        Help existing = helpRepository.findById(helpId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        // 2. 매개변수로 받은 ID와 dto의 아이디가 다른경우
        if(!existing.getHelpId().equals(helpId)){
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        // 3. 요청 DTO의 변경값을 기존 엔티티에 매핑
        existing.setHelpDescription(helpUpdateRequestDto.getHelpDescription());

        // 5. 엔티티 저장 및 응답 DTO 변환
        Help updated = helpRepository.save(existing);
        return modelMapper.map(updated, HelpDto.class);
    }

    @Transactional
    public void delete(Long helpId) {
        if (!helpRepository.existsById(helpId)) {
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }
        helpRepository.deleteById(helpId);
    }

    @Transactional
    public void deleteHelpImage(Long helpId, Long imageId) {
        Help help = helpRepository.findById(helpId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 1) 해당 이미지가 정말 이 도움말에 속해 있는지 검증
        HelpImage target = help.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 2) 엔티티 편의 메서드로 제거 (orphanRemoval로 DB에서도 삭제)
        help.removeImage(target);
        // 별도 save() 호출 불필요 (영속성 컨텍스트에서 변경 감지)
    }

    @Transactional
    public HelpDto createHelpImage(Long helpId, HelpImageCreateRequestDto dto) {
        // 1) 도움말 조회
        Help help = helpRepository.findById(helpId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Help not found: " + helpId));

        // 2) 파일 → byte[] 변환 & 엔티티 생성
        byte[] imageBytes;
        try {
            imageBytes = dto.getFile().getBytes();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_COMMON_ERROR, "Failed to read image", e);
        }
        HelpImage image = new HelpImage();
        image.setBlob(imageBytes);
        image.setImageDescription(dto.getImageDescription());

        // 3) 연관관계 설정 및 영속화
        help.addImage(image);
        // (helpRepository.save(help) 불필요: 영속성 컨텍스트 감지)

        // 4) 변경 후 DTO 반환
        return modelMapper.map(help, HelpDto.class);
    }
}