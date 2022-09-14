# 스프링 AOP 주의사항

## 프록시 내부 호출 문제

- 스프링은 `AOP`를 적용하려면 항상 프록시를 통해서 대상 `객체(Target)`을 호출해야 한다
- 프록시를 거치지 않고 대상 객체를 직접 호출하게 되면 `AOP`가 적용되지 않고, 어드바이스도 호출되지 않는다
- `AOP`를 적용하면 스프링은 대상 객체 대신에 프록시를 스프링 빈으로 등록한다
    - 스프링은 의존관계 주입시에 항상 프록시 객체를 주입한다
- 객체의 내부에서 메서드 호출이 발생하면 프록시를 거치지 않고
대상 객체를 직접 호출하는 문제가 발생한다.

```java
@Slf4j
@Component
public class CallServiceV0 {

    public void external() {
        log.info("call external");
        this.internal();
    }

    public void internal() {
        log.info("call internal");
    }
}

@Slf4j
@Aspect
public class CallLogAspect {

    @Before("execution(* hello.aop.internalcall..*.*(..))")
    public void deLog(JoinPoint joinPoint) {
        log.info("aop={}", joinPoint.getSignature());
    }
}
```

![Untitled](%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20AOP%20%E1%84%8C%E1%85%AE%E1%84%8B%E1%85%B4%E1%84%89%E1%85%A1%E1%84%92%E1%85%A1%E1%86%BC%20313f83430f11458bab6159a8b5752e62/Untitled.png)

- 자바 언어에서 메서드 앞에 별도의 참조가 없으면 `this` 라는 뜻으로 자기 자신의 인스턴스를 가리킨다.
- 결과적으로 자기 자신의 내부 메서드를 호출게 되는데, 여기서 `this` 는 실제 대상 객체(target)의 인스턴스를 뜻한다.
- 이러한 내부 호출은 프록시를 거치지 않는다.

## 프록시 내부 호출 - 자기 자신 주입

- `spring.main.allow-circular-references=true`
    - 스프링 부트 2.6부터는 순환 참조를 기본적으로 금지하도록 정책이 변경되었다

```java
@Slf4j
@Component
public class CallServiceV1 {
    private CallServiceV1 callServiceV1;
    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1) {
        this.callServiceV1 = callServiceV1;
    }
    public void external() {
        log.info("call external");
        callServiceV1.internal(); //외부 메서드 호출
    }
    public void internal() {
        log.info("call internal");
    }
}
```

![Untitled](%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20AOP%20%E1%84%8C%E1%85%AE%E1%84%8B%E1%85%B4%E1%84%89%E1%85%A1%E1%84%92%E1%85%A1%E1%86%BC%20313f83430f11458bab6159a8b5752e62/Untitled%201.png)

## 프록시 내부 호출 - 지연 조회

```java
@Slf4j
@Component
public class CallServiceV2 {

//    private final ApplicationContext applicationContext;

    private final ObjectProvider<CallServiceV2> callServiceProvider;

    public CallServiceV2(ObjectProvider<CallServiceV2> callServiceProvider) {
        this.callServiceProvider = callServiceProvider;
    }

    public void external() {
        log.info("call external");
        CallServiceV2 callServiceV2 = callServiceProvider.getObject();
//        CallServiceV2 callServiceV2 = applicationContext.getBean(CallServiceV2.class);
        callServiceV2.internal(); //외부 메서드 호출
    }

    public void internal() {
        log.info("call internal");
    }
}
```

- `ObjectProvider` 는 객체를 스프링 컨테이너에서 조회하는 것을 스프링 빈생성 시점이 아니라 실제 객체를 사용하는 시점으로 지연할 수 있다.
- `getObject()`를 호출하는 시점에 스프링 컨테이너에서 빈을 조회한다.

## 프록시 내부 호출 - 구조 변경

- 내부 호출이 발생하지 않도록 구조를 변경하는 것이다. 실제 이 방법을 가장 권장한다

```java
/**
 * 구조를 분리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV3 {
    
    private final InternalService internalService;

    public void external() {
        log.info("call external");
        internalService.internal();
    }
}

@Slf4j
@Component
public class InternalService {
    public void internal() {
        log.info("call internal");
    }
}
```

![Untitled](%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20AOP%20%E1%84%8C%E1%85%AE%E1%84%8B%E1%85%B4%E1%84%89%E1%85%A1%E1%84%92%E1%85%A1%E1%86%BC%20313f83430f11458bab6159a8b5752e62/Untitled%202.png)

## 프록시 한계 - 타입 캐스팅

- **JDK 동적 프록시 한계**
    - JDK 동적 프록시는 구체 클래스로 타입 캐스팅이 불가능한 한계가 있다.

```java
@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy(){
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false);

        //프록시를 인터페이스로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        Assertions.assertThrows(ClassCastException.class, () -> {
            MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
         });
    }

    @Test
    void cglibProxy(){
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);

        //프록시를 인터페이스로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        //CGLIB 프록시를 구현 클래스로 캐스팅 시도 성공
        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
    }
}
```

- `JDK 동적 프록시`는 인터페이스 기반으로 프록시를 생성하기 때문에 구체 클래스로 타입 캐스팅이 불가능하다
    - `ClassCastException.class`가 발생
- `CGLIB 프록시`는 구체 클래스를 기반으로 프록시를 생성한다.
    - `CGLIB 프록시`는 구체 클래스로 캐스팅, 부모 인터페이스도 캐스팅 가능하다.

## 프록시 한계 - 의존관계 주입

```java
@Slf4j
@Import(ProxyDIAspect.class)
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"})
@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"})
public class ProxyDITest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberServiceImpl memberServiceImpl;

    @Test
    void go(){
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }
}
```

- **JDK 동적 프록시에 구체 클래스 타입 주입**
    - `JDK Proxy`는 인터페이스를 기반으로 만들어진다. 따라서 구체클래스 타입이 뭔지 전혀 모른다. 그래서 해당 타입에 주입할 수 없다
- **CGLIB 프록시에 구체 클래스 타입 주입**
    - `CGLIB Proxy`는 구체 클래스를 기반으로 만들어진다. 따라서 구체 클래스 타입으로 캐스팅 할 수 있다

## 프록시 한계 - CGLIB

- **CGLIB 구체 클래스 기반 프록시 문제점**
    - 대상 클래스에 기본 생성자 필수
    - 생성자 2번 호출 문제
        - 실제 `target`의 객체를 생성할 때
        - 프록시 객체를 생성할 때 부모 클래스의 생성자 호출
    - `final` 키워드 클래스, 메서드 사용 불가
        - `final` 키워드가 클래스에 있으면 상송이 불가능하고, 메서드에 있으면 오버라이딩이 불가능하다.