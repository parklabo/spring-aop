# Spring AOP 학습 예제

Spring AOP(Aspect Oriented Programming)를 학습하기 위한 종합 예제 프로젝트입니다.
다양한 AOP 패턴과 Advice 타입을 실제 코드로 확인하고 학습할 수 있습니다.

## 📋 목차

- [개발 환경](#개발-환경)
- [AOP란?](#aop란)
- [프로젝트 구조](#프로젝트-구조)
- [실행 방법](#실행-방법)
- [예제 설명](#예제-설명)
- [AOP 핵심 개념](#aop-핵심-개념)
- [PointCut 표현식](#pointcut-표현식)
- [학습 가이드](#학습-가이드)

## 🛠 개발 환경

| 항목 | 버전 |
|------|------|
| Java | 11 |
| Spring Framework | 5.3.31 |
| AspectJ | 1.9.20.1 |
| Maven | 3.x |
| IDE | 자유 (IntelliJ IDEA, Eclipse, STS 권장) |

## 🎯 AOP란?

**AOP(Aspect Oriented Programming, 관점 지향 프로그래밍)**는 횡단 관심사(Cross-cutting Concerns)를 분리하여 모듈화하는 프로그래밍 패러다임입니다.

### 횡단 관심사란?

여러 클래스나 메소드에 공통으로 적용되는 기능을 말합니다:

- 로깅 (Logging)
- 보안 (Security)
- 트랜잭션 처리 (Transaction Management)
- 성능 측정 (Performance Monitoring)
- 예외 처리 (Exception Handling)
- 캐싱 (Caching)

### AOP를 사용하는 이유

**문제 상황:**
```java
public void businessMethod1() {
    System.out.println("메소드 시작"); // 로깅
    long start = System.currentTimeMillis(); // 성능 측정

    // 실제 비즈니스 로직
    performBusinessLogic();

    long end = System.currentTimeMillis(); // 성능 측정
    System.out.println("실행 시간: " + (end - start)); // 로깅
}

public void businessMethod2() {
    System.out.println("메소드 시작"); // 중복 코드!
    long start = System.currentTimeMillis(); // 중복 코드!

    // 실제 비즈니스 로직
    performAnotherBusinessLogic();

    long end = System.currentTimeMillis(); // 중복 코드!
    System.out.println("실행 시간: " + (end - start)); // 중복 코드!
}
```

**AOP 적용:**
```java
@Aspect
public class PerformanceAspect {
    @Around("execution(* com.example..*(..))")
    public Object measurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // 비즈니스 로직 실행
        long end = System.currentTimeMillis();
        System.out.println("실행 시간: " + (end - start));
        return result;
    }
}

// 비즈니스 로직은 깔끔하게!
public void businessMethod1() {
    performBusinessLogic();
}

public void businessMethod2() {
    performAnotherBusinessLogic();
}
```

### AOP의 장점

1. **관심사의 분리**: 핵심 비즈니스 로직과 부가 기능을 분리
2. **코드 재사용성**: 공통 기능을 한 곳에서 관리
3. **유지보수성 향상**: 공통 기능 수정 시 한 곳만 변경
4. **가독성 향상**: 비즈니스 로직에 집중 가능

## 📁 프로젝트 구조

```
spring-aop/
├── src/main/java/net/zerocoding/tutorials/
│   ├── Application.java                    # 메인 실행 클래스
│   ├── ICalculator.java                     # 계산기 인터페이스
│   ├── SimpleCalculator.java               # 계산기 구현체 (Target Object)
│   ├── aspect/
│   │   ├── LoggingAspect.java              # 로깅 Aspect (다양한 Advice 예제)
│   │   └── ExceptionHandlingAspect.java    # 예외 처리 Aspect
│   └── util/
│       └── ApmUtil.java                     # 성능 측정 Aspect
└── src/main/resources/
    └── application-context.xml              # Spring 설정 파일
```

## 🚀 실행 방법

### 1. 프로젝트 클론 또는 다운로드

```bash
git clone <repository-url>
cd spring-aop
```

### 2. Maven 빌드

```bash
cd spring-aop
mvn clean install
```

### 3. 실행

```bash
mvn exec:java -Dexec.mainClass="net.zerocoding.tutorials.Application"
```

또는 IDE에서 `Application.java`의 main 메소드를 직접 실행하세요.

### 4. 실행 결과 확인

프로그램을 실행하면 다양한 Aspect가 동작하는 것을 콘솔에서 확인할 수 있습니다:

- `[Before]`: 메소드 실행 전 로그
- `[APM]`: 성능 측정 로그
- `[After]`: 메소드 실행 후 로그
- `[AfterReturning]`: 정상 완료 후 로그
- `[AfterThrowing]`: 예외 발생 시 로그 (div 메소드에서 확인 가능)
- `[ExceptionHandler]`: 예외 처리 로그

## 📚 예제 설명

### 1. SimpleCalculator (Target Object)

AOP가 적용되는 대상 객체입니다. 간단한 계산기 기능을 제공합니다.

```java
public class SimpleCalculator implements ICalculator {
    public void add(int num1, int num2) { ... }
    public void sub(int num1, int num2) { ... }
    public void mul(int num1, int num2) { ... }
    public void div(int num1, int num2) { ... }
}
```

**학습 포인트:**
- 각 메소드는 의도적으로 1초의 지연(Thread.sleep)을 포함하여 성능 측정을 확인할 수 있습니다.
- `div` 메소드에서 0으로 나누면 예외 처리 Aspect를 확인할 수 있습니다.

### 2. ApmUtil (성능 측정 Aspect)

메소드 실행 시간을 자동으로 측정하는 Aspect입니다.

```java
@Aspect
public class ApmUtil {
    @Around("execution(* net.zerocoding.tutorials.SimpleCalculator.*(..))")
    public Object elapsedTimeChecker(ProceedingJoinPoint joinPoint) throws Throwable {
        // 실행 시간 측정 로직
    }
}
```

**학습 포인트:**
- `@Around` Advice: 메소드 실행 전후를 모두 제어 가능
- `ProceedingJoinPoint`: 실제 메소드 실행을 제어
- `joinPoint.proceed()`: 실제 메소드 호출

### 3. LoggingAspect (다양한 Advice 타입)

AOP의 다양한 Advice 타입을 보여주는 예제입니다.

```java
@Aspect
public class LoggingAspect {
    @Before(...)      // 메소드 실행 전
    @After(...)       // 메소드 실행 후 (항상)
    @AfterReturning(...) // 정상 완료 후
    @AfterThrowing(...)  // 예외 발생 후
}
```

**학습 포인트:**
- 각 Advice의 실행 시점 차이
- `JoinPoint`를 통한 메소드 정보 접근
- `@AfterReturning`의 반환값 접근
- `@AfterThrowing`의 예외 정보 접근

### 4. ExceptionHandlingAspect (예외 처리)

예외를 처리하고 프로그램의 중단을 방지하는 Aspect입니다.

```java
@Aspect
public class ExceptionHandlingAspect {
    @Around("execution(* net.zerocoding.tutorials.SimpleCalculator.div(..))")
    public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ArithmeticException e) {
            // 예외 처리 및 로깅
            return null;
        }
    }
}
```

**학습 포인트:**
- `@Around`를 사용한 예외 처리
- try-catch로 예외를 잡아서 처리 가능
- `@AfterThrowing`과의 차이점

## 🔑 AOP 핵심 개념

### Aspect의 구조

AOP는 다음 세 가지 요소로 구성됩니다:

#### 1. Advice (어드바이스)

**언제** 동작할지를 정의합니다.

| Advice 타입 | 설명 | 사용 예 |
|-------------|------|---------|
| `@Before` | 메소드 실행 전 | 권한 체크, 입력값 검증 |
| `@After` | 메소드 실행 후 (항상) | 리소스 정리 |
| `@AfterReturning` | 정상 완료 후 | 결과 로깅, 캐싱 |
| `@AfterThrowing` | 예외 발생 후 | 예외 로깅, 알림 |
| `@Around` | 실행 전후 모두 제어 | 성능 측정, 트랜잭션, 예외 처리 |

#### 2. JoinPoint (조인포인트)

Advice가 **적용될 수 있는 지점**입니다.

- 메소드 호출 시점
- 메소드 실행 시점
- 생성자 호출
- 필드 접근
- 등등...

Spring AOP는 **메소드 실행 시점**만 지원합니다.

#### 3. PointCut (포인트컷)

**어떤** JoinPoint에 Advice를 적용할지 선택하는 표현식입니다.

```java
@Before("execution(* com.example.service.*.*(..))")
//        ↑ PointCut 표현식
```

### Advice 실행 순서

여러 Aspect가 적용된 경우의 실행 순서:

```
1. @Around (시작 부분)
2. @Before
3. [실제 메소드 실행]
4. @AfterReturning (정상 완료 시) 또는 @AfterThrowing (예외 발생 시)
5. @After (finally와 유사)
6. @Around (종료 부분)
```

## 🎯 PointCut 표현식

PointCut 표현식은 어떤 메소드에 Aspect를 적용할지 결정합니다.

### 기본 문법

```
execution([접근제한자] 반환타입 [패키지.클래스.]메소드(파라미터))
```

### 예제

| 표현식 | 설명 |
|--------|------|
| `execution(* *(..))` | 모든 메소드 |
| `execution(public * *(..))` | 모든 public 메소드 |
| `execution(* com.example..*.*(..))` | com.example 패키지와 하위 패키지의 모든 메소드 |
| `execution(* com.example.*Service.*(..))` | com.example 패키지의 Service로 끝나는 클래스의 모든 메소드 |
| `execution(* get*(..))` | get으로 시작하는 모든 메소드 |
| `execution(* get*(*))` | get으로 시작하고 파라미터가 1개인 메소드 |
| `execution(* get*(*, *))` | get으로 시작하고 파라미터가 2개인 메소드 |
| `execution(String get*(..))` | get으로 시작하고 String을 반환하는 메소드 |

### 와일드카드

- `*`: 하나의 요소 (모든 값)
- `..`: 0개 이상 (패키지: 하위 패키지 포함, 파라미터: 개수 무관)
- `+`: 해당 타입의 서브타입 포함

### 조합

```java
// AND
@Before("execution(* com.example..*.*(..)) && @annotation(Transactional)")

// OR
@Before("execution(* get*(..)) || execution(* set*(..))")

// NOT
@Before("execution(* com.example..*.*(..)) && !execution(* get*(..))")
```

## 📖 학습 가이드

### 초급: AOP 기본 이해

1. **README를 끝까지 읽기** ✓
2. **프로젝트 실행해보기**
   ```bash
   mvn exec:java -Dexec.mainClass="net.zerocoding.tutorials.Application"
   ```
3. **콘솔 출력 관찰하기**
   - 어떤 순서로 로그가 출력되는지 확인
   - 각 Aspect가 언제 동작하는지 파악

### 중급: 코드 분석 및 수정

1. **ApmUtil.java 분석**
   - `@Around` Advice 이해
   - `ProceedingJoinPoint` 역할 파악
   - PointCut 표현식 수정해보기

2. **LoggingAspect.java 분석**
   - 각 Advice 타입의 차이점 이해
   - `JoinPoint`에서 얻을 수 있는 정보 확인

3. **실습: Application.java 수정**
   - 다른 연산 추가해보기
   - 0으로 나누기를 시도하여 예외 처리 확인

### 고급: 새로운 Aspect 작성

1. **캐싱 Aspect 만들기**
   - 같은 인자로 호출 시 캐시된 결과 반환
   - `@Around` 사용

2. **보안 Aspect 만들기**
   - 특정 메소드 실행 전 권한 체크
   - `@Before` 사용

3. **재시도 Aspect 만들기**
   - 실패 시 자동으로 재시도
   - `@Around` 사용
   - ExceptionHandlingAspect의 retryOnFailure 참고

### 실전: 실제 프로젝트 적용

1. **트랜잭션 관리**
   ```java
   @Around("@annotation(Transactional)")
   public Object manageTransaction(ProceedingJoinPoint joinPoint) { ... }
   ```

2. **API 호출 로깅**
   ```java
   @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
   public void logApiCall(JoinPoint joinPoint) { ... }
   ```

3. **성능 모니터링**
   ```java
   @Around("execution(* com.example.service..*(..))")
   public Object monitorPerformance(ProceedingJoinPoint joinPoint) { ... }
   ```

## 🤔 자주 묻는 질문

### Q1. @Around와 @Before + @After의 차이는?

**@Around:**
- 메소드 실행을 완전히 제어 가능
- 실행 여부 결정, 파라미터 변경, 반환값 변경 가능
- 예외 처리 가능

**@Before + @After:**
- 메소드 실행 전후에 추가 로직만 실행
- 메소드 실행 자체는 제어 불가
- 더 간단하고 명확한 의도 표현

### Q2. Spring AOP vs AspectJ의 차이는?

| 특징 | Spring AOP | AspectJ |
|------|-----------|---------|
| 프록시 방식 | 런타임 프록시 (JDK Dynamic Proxy, CGLIB) | 컴파일 타임 Weaving |
| JoinPoint | 메소드 실행만 지원 | 메소드, 생성자, 필드 등 모두 지원 |
| 성능 | 약간 느림 (프록시 오버헤드) | 빠름 (컴파일 타임에 처리) |
| 설정 | 간단 (Spring 설정만) | 복잡 (별도 컴파일러 필요) |

### Q3. 같은 클래스 내부에서 메소드 호출 시 AOP가 안 먹히는 이유?

Spring AOP는 **프록시 기반**이기 때문입니다.

```java
public class MyService {
    public void method1() {
        method2(); // AOP 적용 안 됨!
    }

    public void method2() {
        // ...
    }
}
```

외부에서 호출해야 프록시를 거치므로 AOP가 적용됩니다:

```java
myService.method2(); // AOP 적용됨!
```

해결 방법:
1. 다른 Bean으로 분리
2. `self-injection` 사용
3. AspectJ 사용

## 📝 참고 자료

- [Spring AOP 공식 문서](https://docs.spring.io/spring-framework/reference/core/aop.html)
- [AspectJ 문서](https://www.eclipse.org/aspectj/doc/released/progguide/index.html)
- [Baeldung - Spring AOP Tutorial](https://www.baeldung.com/spring-aop)

## 📄 라이센스

이 프로젝트는 학습 목적으로 자유롭게 사용할 수 있습니다.

## 👤 Author

- Original: YoungSu Park (2016)
- Updated: 2025 (Spring 5.x, Java 11, 종합 예제 및 문서화)

---

**Happy Learning! 🎉**

궁금한 점이 있다면 이슈를 등록해주세요.
