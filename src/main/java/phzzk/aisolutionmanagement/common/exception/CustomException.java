package phzzk.aisolutionmanagement.common.exception;

public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    // 1) 기존: ErrorCode만
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 2) 기존: ErrorCode + 원인 예외
    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    // 3) 신규: ErrorCode + 오버라이드 메시지
    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    // 4) 신규: ErrorCode + 오버라이드 메시지 + 원인 예외
    public CustomException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
