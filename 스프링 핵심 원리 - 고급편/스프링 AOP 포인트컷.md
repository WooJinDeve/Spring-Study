# 스프링 AOP 포인트컷

## 포인트컷 지시자

- 애스펙트J는 포인트컷을 편리하게 표현하기 위한 특별한 표현식을 제공한다.
- 포인트컷 표현식은 `execution` 같은 `포인트컷 지시자(Pointcut Designator)`로 시작한다. 줄여서 `PCD`라 한다.
- **포인트컷 지시자의 종류**
    - `execution` : 메소드 실행 조인 포인트를 매칭한다. 스프링 AOP에서 가장 많이 사용하고, 기능도 복잡하다.
    - `within` : 특정 타입 내의 조인 포인트를 매칭한다.
    - `args` : 인자가 주어진 타입의 인스턴스인 조인 포인트
    - `this` : 스프링 빈 객체(스프링 AOP 프록시)를 대상으로 하는 조인 포인트
    - `target` : `Target` 객체(스프링 AOP 프록시가 가르키는 실제 대상)를 대상으로 하는 조인 포인트
    - `@target` : 실행 객체의 클래스에 주어진 타입의 애노테이션이 있는 조인 포인트
    - `@within` : 주어진 애노테이션이 있는 타입 내 조인 포인트
    - `@annotation` : 메서드가 주어진 애노테이션을 가지고 있는 조인 포인트를 매칭
    - `@args` : 전달된 실제 인수의 런타임 타입이 주어진 타입의 애노테이션을 갖는 조인 포인트
    - `bean` : 스프링 전용 포인트컷 지시자, 빈의 이름으로 포인트컷을 지정한다.
    

## execution 문법

- `execution(접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외?)`
    - 메소드 실행 조인 포인트를 매칭
    - `?`는 생략 가능
    - `*`같은 패턴을 지정할 수 있다.

```java
@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod(){
        log.info("helloMethod = {}", helloMethod);
    }

    @Test
    void exactMatch(){
        //public java.lang.Sring.hello.aop.hello.aop.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch(){
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar1(){
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar2() {
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchFalse(){
        pointcut.setExpression("execution(* *nono*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatch1(){
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch2(){
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactFalse(){
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageMatchSubPackage1(){
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchSubPackage2(){
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
```

- **매칭 조건**
    - **접근제어자?**: `public` , 생략
    - **반환타입**: `String`
    - **선언타입?**: `hello.aop.member.MemberServiceImpl` , 상략
    - **메서드이름**: `hello`
    - **파라미터**: `(String)`
    - **예외?**: 생략

- **타입 매칭 - 부모 타입 허용**

```java
@Slf4j
public class ExecutionTest {
    @Test
    void typeExactMatch() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }
}
```

- `execution` 에서는 부모 타입을 선언해도 그 자식 타입은 매칭된다. 다형성에서 `부모타입 = 자식타입` 이 할당 가능하다는 점을 떠올려보면 된다.
- 부모 타입을 표현식에 선언한 경우 부모 타입에서 선언한 메서드가 자식 타입에 있어야 매칭에 성공한다.

- **파라미터 매칭**

```java
@Slf4j
public class ExecutionTest {
    @Test
    void argsMatch(){
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchNoArgs(){
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
    void argsMatchStar(){
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchAll(){
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchComplex(){
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
```

- `**execution` 파라미터 매칭 규칙은 다음과 같다.**
    - `(String)` : 정확하게 `String` 타입 파라미터
    - `()` : 파라미터가 없어야 한다.
    - `(*)` : 정확히 하나의 파라미터, 단 모든 타입을 허용한다.
    - `(*, *)` : 정확히 두 개의 파라미터, 단 모든 타입을 허용한다
    - `(..)` : 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다.
    - `(String, ..)` : `String` 타입으로 시작해야 한다. 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다.

## within 문법

- `within` : 특정 타입 내의 조인 포인트에 대한 매칭을 제한한다.
- `within`은 표현식에 부모 타입을 지정하면 안된다.

```java
public class WithinTest {
    @Test
    void withinExact() {
        pointcut.setExpression("within(hello.aop.member.MemberServiceImpl)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void withinStar() {
        pointcut.setExpression("within(hello.aop.member.*Service*)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void withinSubPackage() {
        pointcut.setExpression("within(hello.aop..*)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("타겟의 타입에만 직접 적용, 인터페이스를 선정하면 안된다.")
    void withinSuperTypeFalse() {
        pointcut.setExpression("within(hello.aop.member.MemberService)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
}
```

## Args 문법

- `args` : 인자가 주어진 타입의 인스턴스인 조인 포인트로 매칭
- **execution과 args의 차이점**
    - `execution` 은 파라미터 타입이 정확하게 매칭되어야 한다.
        - `execution` 은 클래스에 선언된 정보를 기반으로 판단한다.
    - `args` 는 부모 타입을 허용한다.
        - `args` 는 실제 넘어온 파라미터 객체 인스턴스를 보고 판단한다.

```java
public class ArgsTest {
    Method helloMethod;
    
    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }
    private AspectJExpressionPointcut pointcut(String expression) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        return pointcut;
    }
    @Test
    void args() {
        //hello(String)과 매칭
        assertThat(pointcut("args(String)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(Object)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args()")
                .matches(helloMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut("args(..)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(*)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(String,..)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
```

## @target, @within 문법

- `@target` : 실행 객체의 클래스에 주어진 타입의 애노테이션이 있는 조인 포인트
- `@within` : 주어진 애노테이션이 있는 타입 내 조인 포인트
- `@target(hello.aop.member.annotation.ClassAop)`
- `@within(hello.aop.member.annotation.ClassAop)`
- **@target vs @within**
    - `@target` 은 부모 클래스의 메서드까지 어드바이스를 다 적용하고, `@within` 은 자신의 클래스에 정의된 메서드에만 어드바이스를 적용한다.
- `args, @args, @target`는 단독으로 사용하면 안된다.


![Untitled](https://user-images.githubusercontent.com/106054507/190149352-349a644b-23e4-49ab-a66c-b9ed2b27cd7e.png)

```java
@Slf4j
@Import({AtTargetAtWithinTest.Config.class})
@SpringBootTest
class AtTargetAtWithinTest {
    @Slf4j
    @Aspect
    static class AtTargetAtWithinAspect {
        //@target: 인스턴스 기준으로 모든 메서드의 조인 포인트를 선정, 부모 타입의 메서드도 적용
        @Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
        public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@target] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        //@within: 선택된 클래스 내부에 있는 메서드만 조인 포인트로 선정, 부모 타입의 메서드는적용되지 않음
        @Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop)")
        public Object atWithin(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@within] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
```

## @annotation, @args 문법

- `@annotation` : 메서드가 주어진 애노테이션을 가지고 있는 조인 포인트를 매칭
- `@args` : 전달된 실제 인수의 런타입 타입이 주어진 타입의 애노테이션을 갖는 조인 포인트

```java
@Slf4j
@Import(AtAnnotationAspect.class)
@SpringBootTest
public class AtAnnotationTest {

    @Autowired
    MemberService memberService;

    @Test
    void success(){
        log.info("memberService Proxy = {}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect 
    static class AtAnnotationAspect {
        @Around("@annotation(hello.aop.member.annotation.MethodAop)")
        public Object doAtAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@annotation] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
```

## bean 문법

- `bean` : 스프링 전용 포인트컷 지시자, 빈의 이름으로 지정한다.
- 스프링 빈의 이름으로 AOP 적용 여부를 지정한다. 이것은 스프링에서만 사용할 수 있는 특별한 지시자이다.
    - `bean(orderService) || bean(*Repository)`
    - `*`과 같은 패턴을 사용할 수 있다

```java
@Slf4j
@Import(BeanTest.BeanAspect.class)
@SpringBootTest
public class BeanTest {

    @Autowired
    OrderService orderService;

    @Test
    void success(){
        orderService.orderItem("itemA");
    }

    @Aspect
    static class BeanAspect {

        @Around("bean(orderService) || bean(*Repository)")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[bean] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
```

## 매개변수 전달

- **`this, target, args, @target, @within, @annotation, @args` 표현식을 사용해서 어드바이스에 매개변수를 전달할 수 있다.**
- 포인트컷의 이름과 매개변수의 이름을 맞추어야한다.
- 타입이 메서드에 지정한 타입을 제한한다.

```java
@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberServiceProxy = {}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {
        }

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1]{}, arg={}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg, ..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{}, arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        @Before("allMember() && args(arg, ..)")
        public void logArgs3(String arg) {
            log.info("[logArg3] arg = {}", arg);
        }

        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[this] {}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[target] {}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && @target(annotation)")
        public void targetArgs(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[target] {}, annotation={}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[atWithin] {}, annotation={}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[atAnnotation] {}, annotationValue={}", joinPoint.getSignature(), annotation);
        }
    } 
}
```

- `this` : 프록시 객체를 전달 받는다.
- `target` : 실제 대상 객체를 전달 받는다.
- `@target , @within` : 타입의 애노테이션을 전달 받는다.
- `@annotation` : 메서드의 애노테이션을 전달 받는다. 여기서는 `annotation.value()` 로 값을 얻을 수 있다.

## this, target 문법

- `this` : 스프링 빈 객체를 대상으로 하는 조인 포인트
    - **스프링 빈 객체** : 스프링 AOP 프록시
- `target` : `Target 객체`를 대상으로 하는 조인 포인트
    - **Target** : 스프링 AOP 프록시가 가르키는 실제 대상
- **this vs target**
    - `this`와 `target`은 JDK 동적 프록시와 CGLIB 프록시에 따라 다르게 동작한다.
    - **JDK 동적 프록시 인터페이스로 지정할 경우**
        - `this(hello.aop.member.인터페이스)`
        - 이때는 `this, target` 모두 부모 타입을 허용하기 떄문에 AOP가 동작한다.
    - **JDK 동적 프록시 구체 클래스로 지정할 경우**
        - `this(hello.aop.member.구체 클래스)`
        - 이때 `this`는 인터페이스를 통해 만들어진 JDK 동적 프록시기 때문에 `AOP`가 동작하지 않는다
        - `target`은 구체 클래스로 접근하기 때문에 `AOP`가 동작한다.
    - **CGLIB 프록시 인터페이스 지정**
        - 부모타입을 허용하기 때문에 모두 AOP 동작
    - **CGLIB 프록시 구체 클래스 지정**
        - `this`의 경우 구체클래스로 만들어진 자식 프록시로 동작하기 때문에 `AOP`가 동작한다.
        - `target`의 경우에도 동작한다
    
    ```java
    /**
     * application.properties
     * spring.aop.proxy-target-class = true : CGLIB
     * spring.aop.proxy-target-class = false : JDK 동적 프록시 + CGLIB
     */
    @Slf4j
    @Import(ThisTargetTest.ThisTargetAspact.class)
    @SpringBootTest(properties = "spring.aop.proxy-target-class=false")
    public class ThisTargetTest {
        @Autowired
        MemberService memberService;
    
        @Test
        void success(){
            log.info("memberService Proxy = {}", memberService.getClass());
            memberService.hello("helloA");
        }
    
        @Slf4j
        @Aspect
        static class ThisTargetAspact {
            @Around("this(hello.aop.member.MemberService)")
            public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
                log.info("[this-interface] {}", joinPoint.getSignature());
                return joinPoint.proceed();
            }
    
            @Around("target(hello.aop.member.MemberService)")
            public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
                log.info("[target-interface] {}", joinPoint.getSignature());
                return joinPoint.proceed();
            }
    
            @Around("this(hello.aop.member.MemberServiceImpl)")
            public Object doThis(ProceedingJoinPoint joinPoint) throws Throwable {
                log.info("[this-impl] {}", joinPoint.getSignature());
                return joinPoint.proceed();
            }
    
            @Around("target(hello.aop.member.MemberServiceImpl)")
            public Object doTarget(ProceedingJoinPoint joinPoint) throws Throwable {
                log.info("[target-impl] {}", joinPoint.getSignature());
                return joinPoint.proceed();
            }
        }
    }
    ```
