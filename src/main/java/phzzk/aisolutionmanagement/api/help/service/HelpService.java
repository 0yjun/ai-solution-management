package phzzk.aisolutionmanagement.api.help.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phzzk.aisolutionmanagement.api.help.dto.*;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelpService {
    private final MenuService menuService;
    private final HelpRepository helpRepository;
    private final  ModelMapper modelMapper;

    @Value("${domain}")
    private String domain;

    public List<HelpDto> getHelpDtoAll() {
        return helpRepository.findAll().stream()
                .map(help -> {
                    HelpDto dto = modelMapper.map(help, HelpDto.class);
                    fillImageUrls(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public HelpDto getHelpByMenuId(Long menuId) {
        Help help = helpRepository.findByMenu_Id(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
        HelpDto dto = modelMapper.map(help, HelpDto.class);
        fillImageUrls(dto);
        return dto;
    }

    private void fillImageUrls(HelpDto dto) {
        dto.getImages().forEach(imgDto -> {
            StringBuilder stringBuilder = new StringBuilder();
            String url =  stringBuilder
                    //.append(domain)
                    .append("/api/helps/")
                    .append(dto.getHelpId())
                    .append("/images/")
                    .append(imgDto.getId())
                            .toString();
            imgDto.setUrl(url);
        });
    }

    public HelpImageResourceDto getHelpImageResource(Long helpId, Long helpImageId){
        Help help = helpRepository.findById(helpId)
                .orElseThrow(()->new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        HelpImage helpImage = help.getImages().stream()
                .filter(img -> img.getId().equals(helpImageId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        Map<String, String> mimeMap = Map.of(
                "image/jpeg", MediaType.IMAGE_JPEG_VALUE,
                "jpeg",       MediaType.IMAGE_JPEG_VALUE,
                "image/png",  MediaType.IMAGE_PNG_VALUE,
                "png",        MediaType.IMAGE_PNG_VALUE,
                "image/gif",  MediaType.IMAGE_GIF_VALUE,
                "gif",        MediaType.IMAGE_GIF_VALUE
        );
        String contentType = Optional.ofNullable(helpImage.getContentType())
                .map(String::trim)
                .map(String::toLowerCase)
                .flatMap(key -> Optional.ofNullable(mimeMap.get(key)))
                .orElse(MediaType.IMAGE_JPEG_VALUE);

        HelpImageResourceDto helpImageResourceDto = new HelpImageResourceDto();
        helpImageResourceDto.setBlob(helpImage.getBlob());
        helpImageResourceDto.setContentType(contentType);
        helpImageResourceDto.setLength(helpImage.getBlob().length);

        return helpImageResourceDto;
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
    public HelpImageDto updateHelpImage(Long helpId, Long imageId, HelpImageUpdateRequestDto dto) {
        Help help = helpRepository.findById(helpId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 1) 해당 이미지가 정말 이 도움말에 속해 있는지 검증
        HelpImage target = help.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 2) 엔티티 편의 메서드로 제거 (orphanRemoval로 DB에서도 삭제)
        byte[] imageBytes;
        String imageType;
        try {
            imageBytes = dto.getFile().getBytes();
            imageType = dto.getFile().getContentType().toString();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_COMMON_ERROR, "Failed to read image", e);
        }
        target.setBlob(imageBytes);
        target.setImageDescription(dto.getImageDescription());
        target.setContentType(imageType);
        // 별도 save() 호출 불필요 (영속성 컨텍스트에서 변경 감지)

        return modelMapper.map(target, HelpImageDto.class);
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
        String imageType;
        try {
            imageBytes = dto.getFile().getBytes();
            imageType = dto.getFile().getContentType().toString();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_COMMON_ERROR, "Failed to read image", e);
        }
        HelpImage image = new HelpImage();
        image.setBlob(imageBytes);
        image.setImageDescription(dto.getImageDescription());
        image.setContentType(imageType);

        // 3) 연관관계 설정 및 영속화
        help.addImage(image);
        // (helpRepository.save(help) 불필요: 영속성 컨텍스트 감지)

        // 4) 변경 후 DTO 반환
        return modelMapper.map(help, HelpDto.class);
    }
}