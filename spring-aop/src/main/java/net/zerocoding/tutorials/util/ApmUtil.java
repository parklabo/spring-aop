package net.zerocoding.tutorials.util;

import java.time.Duration;
import java.time.Instant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Simple APM (Application Performance Monitoring) Utility
 *
 * AOP를 활용한 성능 측정 유틸리티입니다.
 * 메소드 실행 시간을 자동으로 측정하여 출력합니다.
 *
 * <h3>주요 기능:</h3>
 * <ul>
 *   <li>메소드 실행 시간 측정 (나노초 단위)</li>
 *   <li>@Around 어드바이스를 사용하여 메소드 전후에 동작</li>
 *   <li>PointCut 표현식으로 적용 대상 지정</li>
 * </ul>
 *
 * <h3>PointCut 표현식 설명:</h3>
 * <pre>
 * execution(* net.zerocoding.tutorials.SimpleCalculator.*(..))
 * - execution: 메소드 실행 시점을 가리킴
 * - *: 모든 반환 타입
 * - net.zerocoding.tutorials.SimpleCalculator: 적용할 클래스
 * - .*: 클래스의 모든 메소드
 * - (..): 모든 파라미터
 * </pre>
 *
 * @since 2016.07.11
 * @version ver0.2 (Updated 2025)
 * @author YoungSu Park
 */
@Aspect
public class ApmUtil {

	/**
	 * SimpleCalculator의 모든 메소드 실행 시간을 측정합니다.
	 *
	 * @Around 어드바이스는 메소드 실행 전후를 감싸서 제어할 수 있습니다.
	 * - 메소드 실행 전: 시작 시간 기록
	 * - 메소드 실행: joinPoint.proceed() 호출
	 * - 메소드 실행 후: 종료 시간 기록 및 경과 시간 계산
	 *
	 * @param joinPoint 실행되는 메소드의 정보를 담고 있는 객체
	 * @return 원본 메소드의 반환값 (현재는 void)
	 * @throws Throwable 원본 메소드에서 발생할 수 있는 예외
	 */
	@Around("execution(* net.zerocoding.tutorials.SimpleCalculator.*(..))")
	public Object elapsedTimeChecker(ProceedingJoinPoint joinPoint) throws Throwable {
		// 메소드 실행 전: 시작 시간 기록
		Instant start = Instant.now();
		String methodName = joinPoint.getSignature().getName();

		System.out.println("===== [APM] 메소드 실행 시작: " + methodName + " =====");

		// 실제 메소드 실행
		Object result = joinPoint.proceed();

		// 메소드 실행 후: 종료 시간 기록 및 경과 시간 계산
		Instant end = Instant.now();
		long nanoSeconds = Duration.between(start, end).toNanos();
		long milliSeconds = Duration.between(start, end).toMillis();

		System.out.println("===== [APM] 메소드 실행 종료: " + methodName + " =====");
		System.out.println("실행 시간: " + milliSeconds + "ms (" + nanoSeconds + " nano seconds)");
		System.out.println();

		return result;
	}
}
