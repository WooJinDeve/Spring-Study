# 스프링 AOP

## AOP 소개 - 애스팩트

- 애스펙트는 우리말로 해석하면 관점이라는 뜻인데, 이름 그대로 애플리케이션을 바라보는 관점을 하나하나의 기능에서 `횡단 관심사(cross-cutting concerns) 관점`으로 달리 보는 것이다.
- 애스펙트를 사용한 프로그래밍 방식을 관점 지향 프로그래밍 `AOP(Aspect-Oriented Programming)`
- `AOP`는 `OOP`를 대체하기 위한 것이 아니라 횡단 관심사를 깔끔하게 처리하기 어려운 `OOP`의 부족한 부분을 보조하는 목적으로 개발
- **AspectJ 프레임워크**
    - AOP의 대표적인 구현으로 AspectJ 프레임워크가 있다.
    - **AspectJ 프레임워크의 기능**
        - 자바 프로그래밍 언어에 대한 완벽한 관점 지향 확장
        - 횡단 관심사의 깔끔한 모듈화
            - 오류 검사 및 처리
            - 동기화
            - 성능 최적화(캐싱)
            - 모니터링 및 로깅
            

## AOP 적용

- `AOP`를 사용하면 핵심 기능과 부가 기능이 코드상 완전히 분리되어서 관리된다.
- **핵심로직 추가 3가지 방법**
    - **컴파일 시점**
        - 실제 대상 코드에 애스팩트를 통한 부가 기능 호출 코드가 포함된다.  `AspectJ`를 직접 사용해야 한다.
    - **클래스 로딩 시점**
        - 실제 대상 코드에 애스팩트를 통한 부가 기능 호출 코드가 포함된다.  `AspectJ`를 직접 사용해야 한다
    - **런타임 시점(프록시)**
        - 실제 대상 코드는 그대로 유지된다. 대신에 프록시를 통해 부가 기능이 적용된다. 따라서 항상 프록시를 통해야 부가 기능을 사용할 수 있다. 스프링 AOP는 이 방식을 사용한다
- **AOP 적용 위치**
    - 적용 가능 지점(조인 포인트) : `생성자`, `필드 값 접근`, `static 메서드 접근`, `메서드 실행`
    - `ApsectJ`를 사용해서 컴파일 시점과 클래스 로딩 시점에 적용하는 `AOP`는 바이트 코드를 실제 조작하기 떄문에 모든 지점에 적용 가능
    - 프록시 방식을 사용하는 `스프링 AOP`는 메서드 실행 시점에만 적용
        - 프록시를 사용하는 `스프링 AOP`의 조인포인트는 메서드 실행으로 제한
    - 프록시 방식을 사용한느 `스프링 AOP`는 스프링 컨테이너가 관리할 수 있는 스프링 빈에만 적용 가능

## AOP 용어 정리

- **조인 포인트(Join point)**
    - 어드바이스가 적용될 수 있는 위치, 메소드 실행, 생성자 호출, 필드 값 접근, static 메서드 접근 같은 프로그램 실행 중 지점
    - 조인 포인트는 추상적인 개념이다. AOP를 적용할 수 있는 모든 지점이라 생각하면 된다.
    - 스프링 AOP는 프록시 방식을 사용하므로 조인 포인트는 항상 메소드 실행 지점으로 제한된다.
- **포인트컷(Pointcut)**
    - 조인 포인트 중에서 어드바이스가 적용될 위치를 선별하는 기능
    - 주로 `AspectJ` 표현식을 사용해서 지정
    - 프록시를 사용하는 `스프링 AOP`는 메서드 실행 지점만 포인트컷으로 선별 가능
- **타켓(Target)**
    - 어드바이스를 받는 객체, 포인트컷으로 결정
- **어드바이스(Advice)**
    - 부가 기능
    - 특정 조인 포인트에서 `Aspect`에 의해 취해지는 조치
    - `Around(주변), Before(전), After(후)`와 같은 다양한 종류의 어드바이스가 있음
- **애스펙트(Aspect)**
    - 어드바이스 + 포인트컷을 모듈화 한 것
    - `@Aspect` 를 생각하면 됨
    - 여러 어드바이스와 포인트 컷이 함께 존재
- **어드바이저(Advisor)**
    - 하나의 어드바이스와 하나의 포인트 컷으로 구성
    - `스프링 AOP`에서만 사용되는 특별한 용어
- **위빙(Weaving)**
    - 포인트컷으로 결정한 타겟의 조인 포인트에 어드바이스를 적용하는 것
    - 위빙을 통해 핵심 기능 코드에 영향을 주지 않고 부가 기능을 추가 할 수 있음
    - **AOP 적용을 위해 애스펙트를 객체에 연결한 상태**
        - 컴파일 타임(AspectJ compiler)
        - 로드 타임
        - 런타임, 스프링 AOP는 런타임, 프록시 방식
- **AOP 프록시**
    - `AOP` 기능을 구현하기 위해 만든 프록시 객체, 스프링에서 AOP 프록시는 `JDK 동적 프록시` 또는 `CGLIB 프록시`이다
    

## AOP 구현

```java
@Slf4j
@Aspect
public class AspectV1 {
    
    @Around("execution(* hello.aop.order..*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("[log] {}",joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
}
```

- `@Around` 애노테이션의 값인 `execution(* hello.aop.order..*(..))` 는 포인트컷이 된다.*
- *`@Around` 애노테이션의 메서드인 `doLog` 는 `어드바이스( Advice )`가 된다. `execution(* hello.aop.order..*(..))` 는 `hello.aop.order` 패키지와 그 하위 패키지( .. )를 지정하는 `AspectJ` 포인트컷 표현식이다.
- 프록시 방식의 `AOP`를 사용하므로 프록시를 통하는 메서드만 적용 대상이 된다

- **포인트컷 분리**

```java
@Slf4j
@Aspect
public class AspectV2 {

    //hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder(){} // pointcut signature

    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("[log] {}",joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
}
```

- **@Pointcut**
    - `@Pointcut` 에 포인트컷 표현식을 사용한다.
    - 메서드 이름과 파라미터를 합쳐서 `포인트컷 시그니처(signature)`라 한다.
    - 메서드의 반환 타입은 `void` 여야 한다.
    - `@Around 어드바이스`에서는 포인트컷을 직접 지정해도 되지만, 포인트컷 시그니처를 사용해도 된다.
- **포인트컷 모듈화**

```java
public class Pointcuts {

    //hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))")
    public void allOrder() {
    } // pointcut signature

    //클래스 이름 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {
    }

    @Pointcut("allOrder() && allService()")
    public void orderAndService(){}
}

@Slf4j
@Aspect
public class AspectV4PointCut {

    @Around("hello.aop.order.aop.Pointcuts.allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("[log] {}",joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
}
```

- **어드바이스 추가**

```java
@Slf4j
@Aspect
public class AspectV3 {

    //hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder(){} // pointcut signature

    //클래스 이름 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    private void allService(){}
    
    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("[log] {}",joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
    
    //hello.aop.order 패키와 하위 패키지 이면서 클래스 이름 패턴이 *Service
    @Around("allOrder() && allService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        } finally {
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }
}
```

- `allService()` 포인트컷은 타입 이름 패턴이 `*Service` 를 대상으로 한다.
    - 클래스, 인터페이스에 모두 적용된다.
- `@Around("allOrder() && allService()")` : 포인트컷은 이렇게 조합할 수 있다. `&& (AND)`, `|| (OR)`, `! (NOT)` 3가지 조합이 가능하다

- **어드바이스 순서**
    - 어드바이스는 기본적으로 순서를 보장하지 않는다
    - 순서를 지정하고 싶으면 `@Aspect` 적용 단위로 `org.springframework.core.annotation.@Order` 애노테이션을 적용해야 한다.
    - 어드바이스 단위가 아니라 클래스 단위로 적용할 수 있다

```java
@Slf4j
@Aspect
public class AspectV5Order {
    @Aspect
    @Order(2)
    public static class LogAspect{
        @Around("hello.aop.order.aop.Pointcuts.allOrder()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
            log.info("[log] {}",joinPoint.getSignature()); // join point 시그니처
            return joinPoint.proceed();
        }

    }
    @Aspect
    @Order(1)
    public static class txAspect{
        //hello.aop.order 패키와 하위 패키지 이면서 클래스 이름 패턴이 *Service
        @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
            try {
                log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
                Object result = joinPoint.proceed();
                log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
                return result;
            } catch (Exception e) {
                log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
                throw e;
            } finally {
                log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
            }
        }
    }
}
```

## **어드바이스 종류**

- **@Around** : 메서드 호출 전후에 수행, 가장 강력한 어드바이스, 조인 포인트 실행 여부 선택, 반환 값 변환, 예외 변환 등이 가능
- **@Before** : 조인 포인트 실행 이전에 실행
- **@AfterReturning** : 조인 포인트가 정상 완료후 실행
- **@AfterThrowing** : 메서드가 예외를 던지는 경우 실행
- **@After** : 조인 포인트가 정상 또는 예외에 관계없이 실행(finally)

```java
@Slf4j
@Aspect
public class AspectV6Advice {

    //hello.aop.order 패키와 하위 패키지 이면서 클래스 이름 패턴이 *Service
    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            //@Before

            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();

            //@AfterReturning
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            //@AfterThrowing
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        } finally {
            //@After
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }

    //joinPoint가 실행되기 전에 실행되는 advice
    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {} return = {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {} message ={}", joinPoint.getSignature(), ex);
    }

    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }
}
```

- **JoinPoint 인터페이스의 주요 기능**
    - `getArgs()` : 메서드 인수를 반환합니다.
    - `getThis()` : 프록시 객체를 반환합니다.
    - `getTarget()` : 대상 객체를 반환합니다.
    - `getSignature()` : 조언되는 메서드에 대한 설명을 반환합니다.
    - `toString()` : 조언되는 방법에 대한 유용한 설명을 인쇄합니다.
- **ProceedingJoinPoint 인터페이스의 주요 기능**
    - `proceed()` : 다음 어드바이스나 타켓을 호출한다.
    
- **@Before : 조인 포인트 실행 전**
    - @Around 와 다르게 작업 흐름을 변경할 수는 없다.
    - @Before 는 ProceedingJoinPoint.proceed() 자체를 사용하지 않는다. 메서드 종료시 자동으로 다음 타켓이 호출된다.
- **@AfterReturning : 메서드를 정상적으로 반환될 때 실행**
    - `returning` 속성에 사용된 이름은 어드바이스 메서드의 매개변수 이름과 일치해야 한다.
    - `returning` 절에 지정된 타입의 값을 반환하는 메서드만 대상으로 실행한다.
    - 반환 객체를 조작할 수 는 있다.
- **@AfterThrowing : 메서드 실행이 예외를 던져서 종료될 때 실행**
    - `throwing` 속성에 사용된 이름은 어드바이스 메서드의 매개변수 이름과 일치해야 한다.
    - `throwing` 절에 지정된 타입과 맞은 예외를 대상으로 실행한다
- **@After**
    - 메서드 실행이 종료되면 실행된다.
    - 정상 및 예외 반환 조건을 모두 처리한다.
    - 일반적으로 리소스를 해제하는 데 사용한다
- **@Around** : 메서드의 실행의 주변에서 실행된다. 메서드 실행 전후에 작업을 수행한다.
    - 조인 포인트 실행 여부 선택 `joinPoint.proceed()` 호출 여부 선택
    - 전달 값 변환: `joinPoint.proceed(args[])`
    - 반환 값 변환
    - 예외 변환
    - `proceed()` 를 통해 대상을 실행한다.
    - `proceed()` 를 여러번 실행할 수도 있음