package phzzk.aisolutionmanagement.common.exception;

public enum ErrorType {
    AUTH,           // 인증/인가 관련
    VALIDATION,     // 입력값 검증 관련
    BUSINESS,       // 비즈니스 로직 관련
    EXTERNAL_API,   // 외부 API 연동 관련
    DATABASE,       // 데이터베이스 관련
    UNCATEGORIZED   // 분류되지 않음
}
