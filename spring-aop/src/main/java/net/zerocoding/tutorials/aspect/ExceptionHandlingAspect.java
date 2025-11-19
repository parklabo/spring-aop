package net.zerocoding.tutorials.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 예외 처리를 위한 AOP Aspect
 *
 * @Around Advice를 사용하여 메소드 실행 중 발생하는 예외를 처리합니다.
 * @AfterThrowing과 다르게 @Around는 예외를 catch하고 처리할 수 있습니다.
 *
 * <h3>주요 기능:</h3>
 * <ul>
 *   <li>예외 발생 시 로깅</li>
 *   <li>예외를 잡아서 기본값 반환 또는 다른 예외로 변환</li>
 *   <li>예외 발생 시에도 프로그램이 중단되지 않도록 처리</li>
 * </ul>
 *
 * <h3>@Around vs @AfterThrowing:</h3>
 * <ul>
 *   <li>@AfterThrowing: 예외를 로깅만 하고 다시 던짐</li>
 *   <li>@Around: 예외를 catch하고 처리할 수 있음 (try-catch와 유사)</li>
 * </ul>
 *
 * @since 2025
 * @version 1.0
 */
@Aspect
public class ExceptionHandlingAspect {

    /**
     * 메소드 실행 중 발생하는 예외를 처리합니다.
     *
     * 이 메소드는 다음과 같이 동작합니다:
     * 1. try 블록에서 원본 메소드를 실행 (joinPoint.proceed())
     * 2. 정상 실행 시: 반환값을 그대로 전달
     * 3. 예외 발생 시: 예외를 로깅하고 기본값 반환
     *
     * 실제 서비스에서는 다음과 같이 활용할 수 있습니다:
     * - 예외를 다른 타입의 예외로 변환 (예: SQLException -> ServiceException)
     * - 예외 발생 시 알림 발송 (이메일, SMS 등)
     * - 예외 발생 시 기본값 또는 캐시된 값 반환
     * - 재시도 로직 구현
     *
     * @param joinPoint 실행되는 메소드의 정보
     * @return 원본 메소드의 반환값 또는 예외 발생 시 기본값
     * @throws Throwable 처리하지 못한 예외
     */
    @Around("execution(* net.zerocoding.tutorials.SimpleCalculator.div(..))")
    public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        try {
            // 원본 메소드 실행
            return joinPoint.proceed();

        } catch (ArithmeticException e) {
            // ArithmeticException (예: 0으로 나누기) 처리
            System.err.println("╔════════════════════════════════════════╗");
            System.err.println("║  예외 처리 Aspect - 예외 감지 및 처리   ║");
            System.err.println("╚════════════════════════════════════════╝");
            System.err.println("[ExceptionHandler] 메소드: " + methodName);
            System.err.println("[ExceptionHandler] 인자: " + java.util.Arrays.toString(args));
            System.err.println("[ExceptionHandler] 예외 타입: " + e.getClass().getName());
            System.err.println("[ExceptionHandler] 예외 메시지: " + e.getMessage());
            System.err.println("[ExceptionHandler] 조치: 예외를 처리하고 기본값 반환");
            System.err.println();

            // 예외를 처리하고 프로그램을 계속 실행
            // 실제 서비스에서는 기본값, 캐시된 값, 또는 적절한 오류 응답을 반환할 수 있습니다.
            System.out.println("⚠️  0으로 나눌 수 없습니다. 연산을 건너뜁니다.");
            return null; // void 메소드이므로 null 반환

        } catch (Exception e) {
            // 기타 예외 처리
            System.err.println("╔════════════════════════════════════════╗");
            System.err.println("║  예외 처리 Aspect - 예상치 못한 예외   ║");
            System.err.println("╚════════════════════════════════════════╝");
            System.err.println("[ExceptionHandler] 예상치 못한 예외 발생");
            System.err.println("[ExceptionHandler] 예외: " + e.getClass().getName());
            System.err.println("[ExceptionHandler] 메시지: " + e.getMessage());
            System.err.println();

            // 예외를 다시 던지거나, 다른 타입의 예외로 변환할 수 있습니다.
            throw e;
        }
    }

    /**
     * 메소드 실행 재시도 예제
     *
     * 일시적인 오류가 발생할 수 있는 메소드에 대해 재시도 로직을 구현합니다.
     * 네트워크 호출, 데이터베이스 접속 등에 유용합니다.
     *
     * 참고: 이 예제는 주석 처리되어 있습니다.
     * 실제로 사용하려면 주석을 제거하고 적절한 pointcut을 지정하세요.
     *
     * @param joinPoint 실행되는 메소드의 정보
     * @return 원본 메소드의 반환값
     * @throws Throwable 재시도 후에도 실패한 경우
     */
    // @Around("execution(* net.zerocoding.tutorials.*.*(..))")
    public Object retryOnFailure(ProceedingJoinPoint joinPoint) throws Throwable {
        int maxRetries = 3;
        int retryCount = 0;
        Throwable lastException = null;

        while (retryCount < maxRetries) {
            try {
                // 메소드 실행 시도
                return joinPoint.proceed();

            } catch (Exception e) {
                lastException = e;
                retryCount++;

                if (retryCount < maxRetries) {
                    System.err.println("[Retry] 시도 " + retryCount + "/" + maxRetries +
                                     " 실패. 재시도 중...");
                    // 재시도 전 대기 (exponential backoff)
                    Thread.sleep(1000 * retryCount);
                }
            }
        }

        // 모든 재시도 실패
        System.err.println("[Retry] 모든 재시도 실패");
        throw lastException;
    }
}
