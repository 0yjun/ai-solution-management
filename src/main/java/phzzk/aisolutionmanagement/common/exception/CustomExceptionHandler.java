package phzzk.aisolutionmanagement.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.awt.event.FocusEvent;


@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
        // 1. 원본 에러 정보 추출
        ErrorCode originalErrorCode = ex.getErrorCode();
        ErrorType errorType = originalErrorCode.getType();

        // 2. 서버 로그 기록: 상세 정보 기록
        log.warn(
                "CustomException occurred: type={}, code={}, msg='{}', uri={}",
                errorType,
                originalErrorCode.getCode(),
                ex.getMessage(),
                request.getRequestURI(),
                ex  // 스택트레이스
        );

        // 3. 클라이언트에게 반환할 ErrorCode 결정
        ErrorCode clientErrorCode; // 최종적으로 클라이언트에게 보여줄 ErrorCode
        switch (errorType) {
            case AUTH:
                // JWT 토튼 만료를 제외한 인증/인가 관련 오류는 보안을 위해 공통 코드로 대체
                if(!originalErrorCode.equals(ErrorCode.JWT_EXPIRED)){
                    clientErrorCode = ErrorCode.AUTH_COMMON_ERROR;
                }else{
                    clientErrorCode = originalErrorCode;
                }
                break;
            case VALIDATION:
            case BUSINESS:
                // 입력값 검증 오류 및 예측 가능한 비즈니스 규칙 오류는 사용자에게 전달
                clientErrorCode = originalErrorCode;
                break;
            default: // EXTERNAL_API, DATABASE, UNCATEGORIZED, CONFIGURATION, SYSTEM 등 나머지 모든 타입
                clientErrorCode = ErrorCode.INTERNAL_COMMON_ERROR;
                log.error("common error: Type={}, OriginalCode={}", errorType, originalErrorCode.getCode(), ex);
                break;
        }

        // 4. 최종 응답 생성 및 반환
        return ResponseEntity
                .status(clientErrorCode.getHttpStatus())
                .body(new ErrorResponse(
                        clientErrorCode.getCode(),
                        clientErrorCode.getMessage())
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String field = ex.getBindingResult().getFieldError().getField();
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();

        // 예: VALIDATION_ERROR("VAL001", "%s 필드 오류: %s")
        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        errorCode.getCode(),
                        String.format(errorCode.getMessage(), field, message)
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        ex.printStackTrace();
        ErrorCode resultErrorCode = ErrorCode.VALIDATION_ERROR;
        return ResponseEntity.status(resultErrorCode.getHttpStatus())
                .body(new ErrorResponse(
                        resultErrorCode.getCode(),
                        String.format(resultErrorCode.getMessage(), "", "")
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorCode code = ErrorCode.VALIDATION_ERROR;
        String msg = String.format(code.getMessage(), ex.getName(), "잘못된 값: " + ex.getValue());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(code.getCode(), msg));
    }

    // 데이터 접근 에러
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDbError(DataAccessException ex) {
        ErrorCode code = ErrorCode.INTERNAL_COMMON_ERROR;
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(code.getCode(), code.getMessage()));
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }
}