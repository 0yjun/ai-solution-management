package phzzk.aisolutionmanagement.api.help.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@Schema(name = "HelpImageResourceDto", description = "도움말 이미지 데이터를 제공하는 dto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelpImageResourceDto {
    @Schema(description = "도움말 이미지 blob", example = "AFADAFDFAFDFF....")
    private byte[] blob;
    @Schema(description = "도움말 이미지 blob 길이", example = "100")
    private long length;
    @Schema(description = "도움말 이미지 타입", example = "img/jpeg")
    private String contentType;
}