# @Aspect AOP

## @Aspect 프록시

- 스프링은 `@Aspect 애노테이션`으로 매우 편리하게 포인트컷과 어드바이스로 구성되어 있는 어드바이저 생성 기능을 지원한다

```java
@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* hello.proxy.app..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus statue = null;
        try {
            String message = joinPoint.getSignature().toShortString();
            statue = logTrace.begin(message);

            // Logic 호출
            Object result = joinPoint.proceed();

            logTrace.end(statue);
            return result;
        } catch (Exception e) {
            logTrace.exception(statue,e);
            throw e;
        }
    }
}
```

- `@Aspect` : 애노테이션 기반 프록시를 적용할 때 필요하다.\
- `@Around("execution(* hello.proxy.app..*(..))")`
    - `@Around` 의 값에 포인트컷 표현식을 넣는다. 표현식은 `AspectJ` 표현식을 사용한다.
    - `@Around` 의 메서드는 어드바이스가 된다.
- `ProceedingJoinPoint joinPoint` : 내부에 실제 호출 대상, 전달 인자, 그리고 어떤 객체와 어떤 메서드가 호출되었는지 정보가 포함되어 있다.
- `joinPoint.proceed()` : 실제 호출 `대상( target )`을 호출한다.

## @Aspect 프록시 - 설명

- 자동 프록시 생성기는 여기에 추가로 하나의 역할을 더 하는데, 바로
`@Aspect` 를 찾아서 이것을 `Advisor` 로 만들어준다.
- **자동 프록시 생성기는 2가지**
    1. `@Aspect` 를 보고 어드바이저로 변환해서 저장한다.
    2. 어드바이저를 기반으로 프록시를 생성한다

![Untitled](@Aspect%20AOP%209963627882d148c4b5a7c525e8f6e5dd/Untitled.png)

- **실행**: 스프링 애플리케이션 로딩 시점에 자동 프록시 생성기를 호출한다.
- **모든 @Aspect 빈 조회**: 자동 프록시 생성기는 스프링 컨테이너에서 `@Aspect 애노테이션`이 붙은 스프링 빈을 모두 조회한다.
- **어드바이저 생성**: `@Aspect` 어드바이저 빌더를 통해 `@Aspect 애노테이션` 정보를 기반으로 어드바이저를 생성한다.
- **@Aspect 기반 어드바이저 저장**: 생성한 어드바이저를 `@Aspect 어드바이저 빌더` 내부에 저장한다

![Untitled](@Aspect%20AOP%209963627882d148c4b5a7c525e8f6e5dd/Untitled%201.png)

- **생성**: 스프링 빈 대상이 되는 객체를 생성한다.
- **전달**: 생성된 객체를 빈 저장소에 등록하기 직전에 빈 후처리기에 전달한다.
- **Advisor 빈 조회**: 스프링 컨테이너에서 `Advisor 빈`을 모두 조회한다.
- **@Aspect Advisor 조회**: `@Aspect 어드바이저 빌더` 내부에 저장된 `Advisor` 를 모두 조회한다.
- **프록시 적용 대상 체크**: 조회한 `Advisor` 에 포함되어 있는 포인트컷을 사용해서 해당 객체가 프록시를 적용할 대상인지 아닌지 판단한다.
- **프록시 생성**: 프록시 적용 대상이면 프록시를 생성하고 프록시를 반환한다.
- **빈 등록**: 반환된 객체는 스프링 빈으로 등록된다.