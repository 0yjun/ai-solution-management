package phzzk.aisolutionmanagement.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    CONTENT_MANAGER("콘텐츠 관리자"),
    CONTENT_ADMIN("콘텐츠 운영자"),
    SYSTEM_MANAGER("시스템 관리자"),
    SYSTEM_ADMIN("시스템 운영자");

    private final String description;
    public static final int MAX_COUNT = 4;
}