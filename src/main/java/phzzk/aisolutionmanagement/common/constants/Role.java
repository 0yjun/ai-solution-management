package phzzk.aisolutionmanagement.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import phzzk.aisolutionmanagement.common.exception.CustomException;
import phzzk.aisolutionmanagement.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum Role {
    CONTENT_MANAGER("콘텐츠 관리자"),
    CONTENT_ADMIN("콘텐츠 운영자"),
    SYSTEM_MANAGER("시스템 관리자"),
    SYSTEM_ADMIN("시스템 운영자");

    private final String description;
    public static final int MAX_COUNT = 4;

    /**
     * 전달된 name이 이 Enum에 정의되어 있으면 true, 아니면 false
     */
    public static boolean contains(String name) {
        for (Role r : values()) {
            if (r.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 전달된 name을 실제 Role 객체로 변환.
     * 없으면 CustomException 발생.
     */
    public static Role from(String name) {
        if (!contains(name)) {
            throw new CustomException(ErrorCode.ROLE_NOT_FOUND);
        }
        return Role.valueOf(name);
    }
}