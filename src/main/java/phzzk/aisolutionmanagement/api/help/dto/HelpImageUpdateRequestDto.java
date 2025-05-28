package phzzk.aisolutionmanagement.api.help.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Schema(name = "HelpImageCreateRequestDto", description = "도움말 이미지 업로드 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelpImageUpdateRequestDto {
    @Schema(description = "도움말 이미지 설명", example = "이미지 1")
    private String imageDescription;

    @Schema(description = "도움말 이미지 파일 (최대 10MB, JPEG/PNG/GIF)", type = "string", format = "binary")
    @NotNull(message = "이미지 파일은 필수입니다.")
    private MultipartFile file;

    // 파일 크기 검증 (10MB 이하)
    @AssertTrue(message = "파일 크기는 100MB 이하여야 합니다.")
    private boolean isImageSizeValid() {
        return file != null && file.getSize() <= 100 * 1024 * 1024;
    }

    // 파일 타입 검증 (이미지 형식)
    @AssertTrue(message = "이미지 파일(JPEG/PNG/GIF)만 업로드 가능합니다.")
    private boolean isImageTypeValid() {
        if (file == null) {
            return true; // @NotNull에서 체크됨
        }
        String contentType = file.getContentType();
        List<String> allowed = Arrays.asList("image/jpeg", "image/png", "image/gif");
        return contentType != null && allowed.contains(contentType);
    }
}
