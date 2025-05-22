package phzzk.aisolutionmanagement.api.help.dto;

import lombok.Data;

@Data
public class HelpImageDto {
    private Long id;
    private byte[] blob;
    private String imageDescription;
    private int seq;
}