package net.zerocoding.tutorials.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Arrays;

/**
 * 로깅을 위한 AOP Aspect
 *
 * 이 클래스는 다양한 Advice 타입을 보여주는 예제입니다.
 * 메소드 실행의 각 단계에서 로그를 출력하여 AOP의 동작을 이해할 수 있습니다.
 *
 * <h3>Advice 타입:</h3>
 * <ul>
 *   <li>@Before: 메소드 실행 전에 실행</li>
 *   <li>@After: 메소드 실행 후에 항상 실행 (finally와 유사)</li>
 *   <li>@AfterReturning: 메소드가 정상적으로 완료된 후 실행</li>
 *   <li>@AfterThrowing: 메소드 실행 중 예외가 발생한 후 실행</li>
 * </ul>
 *
 * @since 2025
 * @version 1.0
 */
@Aspect
public class LoggingAspect {

    /**
     * @Before Advice 예제
     *
     * 메소드 실행 전에 호출됩니다.
     * 주로 메소드 호출 전 검증, 로깅, 권한 체크 등에 사용됩니다.
     *
     * JoinPoint 객체를 통해 다음 정보를 얻을 수 있습니다:
     * - getSignature(): 메소드 시그니처 정보
     * - getArgs(): 메소드에 전달된 인자
     * - getTarget(): 실행되는 객체
     *
     * @param joinPoint 실행되는 메소드의 정보
     */
    @Before("execution(* net.zerocoding.tutorials.SimpleCalculator.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        System.out.println("────────────────────────────────────────");
        System.out.println("[Before] 메소드 호출: " + methodName);
        System.out.println("[Before] 전달 인자: " + Arrays.toString(args));
        System.out.println("────────────────────────────────────────");
    }

    /**
     * @After Advice 예제
     *
     * 메소드 실행 후에 항상 호출됩니다 (정상 완료 또는 예외 발생 모두).
     * Java의 finally 블록과 유사한 역할을 합니다.
     *
     * 주로 리소스 정리, 로깅 등에 사용됩니다.
     *
     * @param joinPoint 실행된 메소드의 정보
     */
    @After("execution(* net.zerocoding.tutorials.SimpleCalculator.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        System.out.println("────────────────────────────────────────");
        System.out.println("[After] 메소드 실행 완료: " + methodName);
        System.out.println("[After] 이 로그는 정상 완료/예외 발생 모두에서 출력됩니다");
        System.out.println("────────────────────────────────────────");
    }

    /**
     * @AfterReturning Advice 예제
     *
     * 메소드가 정상적으로 완료되고 값을 반환한 후에만 호출됩니다.
     * returning 속성을 사용하여 반환값을 받아올 수 있습니다.
     *
     * 주로 반환값 로깅, 캐싱, 결과 검증 등에 사용됩니다.
     *
     * 참고: 현재 SimpleCalculator의 메소드들은 void 반환이므로
     * returning 값은 null입니다.
     *
     * @param joinPoint 실행된 메소드의 정보
     * @param result 메소드의 반환값
     */
    @AfterReturning(
        pointcut = "execution(* net.zerocoding.tutorials.SimpleCalculator.*(..))",
        returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();

        System.out.println("────────────────────────────────────────");
        System.out.println("[AfterReturning] 메소드 정상 완료: " + methodName);
        System.out.println("[AfterReturning] 반환값: " + result);
        System.out.println("[AfterReturning] 이 로그는 정상 완료시만 출력됩니다");
        System.out.println("────────────────────────────────────────");
    }

    /**
     * @AfterThrowing Advice 예제
     *
     * 메소드 실행 중 예외가 발생한 후에 호출됩니다.
     * throwing 속성을 사용하여 발생한 예외를 받아올 수 있습니다.
     *
     * 주로 예외 로깅, 예외 변환, 알림 발송 등에 사용됩니다.
     *
     * 참고: 이 Advice는 예외를 처리하지 않고 다시 던집니다.
     * 예외를 처리하려면 @Around를 사용해야 합니다.
     *
     * @param joinPoint 실행된 메소드의 정보
     * @param error 발생한 예외
     */
    @AfterThrowing(
        pointcut = "execution(* net.zerocoding.tutorials.SimpleCalculator.*(..))",
        throwing = "error"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        String methodName = joinPoint.getSignature().getName();

        System.err.println("────────────────────────────────────────");
        System.err.println("[AfterThrowing] 메소드 실행 중 예외 발생: " + methodName);
        System.err.println("[AfterThrowing] 예외 타입: " + error.getClass().getName());
        System.err.println("[AfterThrowing] 예외 메시지: " + error.getMessage());
        System.err.println("────────────────────────────────────────");
    }
}
