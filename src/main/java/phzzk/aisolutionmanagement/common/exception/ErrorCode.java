package phzzk.aisolutionmanagement.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // JWT 관련
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED,
            "JWT001", "토큰이 만료되었습니다.", ErrorType.AUTH),
    JWT_INVALID(HttpStatus.UNAUTHORIZED,
            "JWT002", "유효하지 않은 토큰입니다.", ErrorType.AUTH),

    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,
            "USR001", "사용자를 찾을 수 없습니다.", ErrorType.BUSINESS),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED,
            "USR002", "비밀번호가 일치하지 않습니다.", ErrorType.BUSINESS),
    DUPLICATE_USERNAME(HttpStatus.UNAUTHORIZED,
            "USR003", "이미 존재하는 아이디입니다.",  ErrorType.BUSINESS),

    // 권한 관련
    AUTH_COMMON_ERROR(HttpStatus.FORBIDDEN,
            "auth_000", "권한이 없습니다.",   ErrorType.AUTH),
    AUTH_NOT_FOUND(HttpStatus.UNAUTHORIZED,
            "AUTH_001",
            "사용자 권한 정보를 찾을 수 없습니다.", ErrorType.AUTH),
    MULTIPLE_ROLES_FOUND(HttpStatus.UNAUTHORIZED, "AUTH_002",
            "사용자에게 여러 개의 권한이 할당되어 있습니다.", ErrorType.AUTH),
    INVALID_ROLE_FORMAT(HttpStatus.UNAUTHORIZED, "AUTH_003",
            "권한 형식이 올바르지 않습니다 (ROLE_ 접두사 필요).", ErrorType.AUTH),
    UNKNOWN_ROLE(HttpStatus.UNAUTHORIZED,      "AUTH_004",
            "정의되지 않은 권한입니다.",   ErrorType.AUTH),

    // 파라미터 관련
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST,
            "VLD001", "%s 필드 오류: %s",    ErrorType.VALIDATION),

    // 메뉴 관련
    MENU_PARENT_NOT_FOUND(HttpStatus.NOT_FOUND,    "MNU001",
            "부모 메뉴를 찾을 수 없습니다. id=%d", ErrorType.BUSINESS),
    MENU_ROLE_SUBSET_INVALID(HttpStatus.BAD_REQUEST,"MNU002",
            "자식 메뉴 권한은 부모 권한안에 속해야 합니다.", ErrorType.BUSINESS),
    MENU_EMPTY_ROLES(HttpStatus.BAD_REQUEST,       "MNU003",
            "메뉴에는 최소한 하나의 권한이 필요합니다.", ErrorType.BUSINESS),
    MENU_NOT_FOUND(HttpStatus.BAD_REQUEST,       "MNU004",
            "메뉴를 찾을수 없습니다.", ErrorType.BUSINESS),
    MENU_PARENT_CYCLE_INVALID(HttpStatus.BAD_REQUEST,       "MNU005",
            "부모를 자기 자신으로 설정 할 수 없습니다..", ErrorType.BUSINESS),

    // 권한 관련
    ROLE_NOT_FOUND(HttpStatus.BAD_REQUEST,       "ROL001",
            "해당 권한이 존재하지 않습니다.", ErrorType.BUSINESS),

    // 공통
    INTERNAL_COMMON_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"CMM001",
            "메뉴에는 최소한 하나의 권한이 필요합니다.", ErrorType.UNCATEGORIZED);

    private final HttpStatus httpStatus;
    private final String     code;
    private final String     message;
    private final ErrorType  type;
}