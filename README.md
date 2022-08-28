# 개요

- [김영한의 스프링 완전 정복](https://www.inflearn.com/roadmaps/373)를 통해 배운것들을 정리한 Repository입니다.

# 스프링 시작

- **스프링 시작** 
  - [https://start.spring.io/](https://start.spring.io/)
  

- **Spring Package Setting**
  - `Java Version` : 11
  - `Project` : `Gradle Project`
  - `Packaging` 
    - `Servlet` : `War`
    - `Spring Boot` : `Jar`
  - `Dependencies`
    - `Spring Web`, `Thymeleaf`, `Lombok`, `Validation`, `H2 DATABASE`, `Spring DATA JPA`
  
    
- **IDE Setting**
  - **Lombok Setting**
    - `setting` -> `Bulid, Execution, Depolyment` -> `Annotation Processors` -> `Enable annotation processing ✔`
  - **Encoding Setting**
    - `setting` -> `File Encodings` -> `Global Encoding` -> `UTF-8`
    - `setting` -> `File Encodings` -> `Properties Files` -> `Default encoding for properties files` -> `UTF-8`
    
- **H2 DataBase**
  - **Download**
    - [H2 DataBase Download URL](https://www.h2database.com)
  - **Setting**
    - `JDBC URL` : `jdbc:h2:tcp://localhost/~/test`
    - `User` : `sa`

# 공부

<details>
<summary><h3>스프링 핵심 원리</h3></summary>

- [스프링 핵심 원리](https://github.com/WooJinDeve/Spring-Study/issues/1#issue-1346668714)

- 스프링 탄생
- 스프링 역사
- 스프링 이란?
- 스프링 부트
- 스프링의 핵심
- 다형성
- SOLID
- 스프링의 객체 지향
- IoC(Inversion of Control) : 제어의 역전
- DI(Dependency Injection) : 의존관계 주입
- IoC 컨테이너, DI 컨테이너

</details>

<details>
<summary><h3>스프링 컨테이너와 스프링 빈</h3></summary>

- [스프링 컨테이너와 스프링 빈](https://github.com/WooJinDeve/Spring-Study/issues/2#issue-1346684251)

- 스프링 컨테이너 생성
- 스프링 빈 출력
- 스프링 빈 조회
- 스프링 빈 조회 - 상속관계
- BeanFactory와 ApplicationContext
- 스프링 빈 설정 메타 정보 - BeanDefinition

</details>

<details>
<summary><h3>싱글톤 컨테이너</h3></summary>

- [싱글톤 컨테이너](https://github.com/WooJinDeve/Spring-Study/issues/3#issue-1346688236)

- 싱글톤 패턴
- 싱글톤 패턴의 문제점
- 싱글톤 컨테이너
- 싱글톤 방식의 주의점
- @Configuration과 싱글톤

</details>

<details>
<summary><h3>컴포넌트 스캔</h3></summary>

- [컴포넌트 스캔](https://github.com/WooJinDeve/Spring-Study/issues/3#issue-1346688236)

- 컴포넌트 스캔과 의존관계 자동 주입
- 컴포넌트 스캔 등록 과정
- 탐색 위치와 기본 
- 필터
- 중복 등록과 충돌

</details>

<details>
<summary><h3>의존관계 자동 주입</h3></summary>

- [의존관계 자동 주입](https://github.com/WooJinDeve/Spring-Study/issues/5#issue-1346690028)

- 다양한 의존관계 주입 방법
- 옵션 처리
- 롬복과 최신 트랜드 
- 조회 빈이 2개 이상 - 문제
- 애노테이션 생성법
- 조회한 빈이 모두 필요할 때, List, Map

</details>

<details>
<summary><h3>빈 생명주기 콜백</h3></summary>

- [빈 생명주기 콜백](https://github.com/WooJinDeve/Spring-Study/issues/6#issue-1346692365)

- 빈 생명주기 콜백
- 3가지 빈 생명주기 콜백

</details>

<details>
<summary><h3>빈 스코프</h3></summary>

- [빈 스코프](https://github.com/WooJinDeve/Spring-Study/issues/7#issue-1346692736)

- 프로토타입 스코프
- 프로토타입 스코프 - 싱글톤 빈과 함께 사용시 문제점 Provider로 해결
- 웹 스코프
- 스코프와 프록시

</details>

<details>
<summary><h3>스프링 MVC 원리</h3></summary>

- [스프링 MVC 원리](https://github.com/WooJinDeve/Spring-Study/issues/8#issue-1346693472)

- 웹 서버, 웹 애플리케이션 서버
- 웹 시스템 구성 - WAS, DB
- 웹 시스템 구성 - WEB, WAS, DB 
- 서블릿
- 동시 요청 - 멀티 쓰레드
- HTML, HTTP API, CSR, SSR

</details>

<details>
<summary><h3>서블릿</h3></summary>

- [서블릿](https://github.com/WooJinDeve/Spring-Study/issues/9#issue-1346697497)

- 서블릿 컨테이너 동작 방식
- HttpServletRequest 
- HttpServletResponse

</details>

<details>
<summary><h3>서블릿 + JSP MVC 패턴</h3></summary>

- [서블릿 + JSP MVC 패턴](https://github.com/WooJinDeve/Spring-Study/issues/10#issue-1346699364)

- MVC 패턴 - 개요
- MVC 패턴 한계

</details>

<details>
<summary><h3>스프링 MVC 전체 구조</h3></summary>

- [스프링 MVC 전체 구조](https://github.com/WooJinDeve/Spring-Study/issues/11#issue-1346700225)

- SpringMVC 구조
- 핸들러 매핑과 핸들러 어댑터
- 뷰 리졸버

</details>

<details>
<summary><h3>스프링 MVC</h3></summary>

- [스프링 MVC](https://github.com/WooJinDeve/Spring-Study/issues/12#issue-1346702690)

- 요청 매핑
- HTTP 요청 파라미터 
- HTTP 응답 
- HTTP 메시지 컨버터
- 요청 매핑 핸들러 어뎁터 구조

</details>

<details>
<summary><h3>Thymeleaf</h3></summary>

- [Thymeleaf](https://github.com/WooJinDeve/Spring-Study/issues/13#issue-1346703620)

- 타임리프
- 텍스트 - text, utext
- URL 링크
- 리터럴 
- 연산
- 속성 값 설정
- 반복
- 조건부 평가
- 주석
- 블록
- 자바스크립트 인라인
- 템플릿 조각
- 템플릿 레이아웃

</details>

<details>
<summary><h3>메시지 국제화</h3></summary>

- [메시지 국제화](https://github.com/WooJinDeve/Spring-Study/issues/14#issue-1346704152)

- 국제화
- 스프링 메시지 국제화
- 스프링 국제화 메시지 선택 

</details>

<details>
<summary><h3>검증</h3></summary>

- [검증](https://github.com/WooJinDeve/Spring-Study/issues/16#issue-1348155250)
- 클라이언트 검증, 서버 검증
- 오류처리
- 오류 코드와 메시지 처리


</details>


<details>
<summary><h3>Bean Validation</h3></summary>

- [Bean Validation](https://github.com/WooJinDeve/Spring-Study/issues/17#issue-1348155915)
- Bean Validation
- Bean Calidation - 에러 코드
- Bean Validation - 오브젝트 오류
- Bean Validation - groups

</details>

<details>
<summary><h3>Form 전송 객체</h3></summary>

- [Form 전송 객체](https://github.com/WooJinDeve/Spring-Study/issues/18#issue-1348156492)
- Form 전송 객체
- Bean Validation - HTTP 메시지 컨버터

</details>

<details>
<summary><h3>로그인 처리</h3></summary>

- [로그인 처리](https://github.com/WooJinDeve/Spring-Study/issues/19#issue-1348157256)
- 로그인 처리 - 쿠키
- 로그인 처리 - 세션
- 로그인 처리 - 서블릿 HTTP 세션
- 세션 정보와 타임아웃 설정

</details>


<details>
<summary><h3>서블릿 필터</h3></summary>

- [서블릿 필터](https://github.com/WooJinDeve/Spring-Study/issues/20#issue-1349605512)
- 공통 관심사
- 서블릿 필터
- 서블릿 필터 - 인증체크
- 스프링 인터셉터

</details>


<details>
<summary><h3>예외 처리와 오류페이지</h3></summary>

- [예외 처리와 오류페이지](https://github.com/WooJinDeve/Spring-Study/issues/21#issue-1349607031)
- 서블릿 예외 처리 - 서블릿
- 서블릿 예외 처리 - 필터
- 서블릿 예외 처리 - 인터셉터
- 스프링 부트 - 오류 페이지

</details>

<details>
<summary><h3>API 예외 처리</h3></summary>

- [API 예외 처리](https://github.com/WooJinDeve/Spring-Study/issues/22#issue-1351794341)
- API 예외 처리 - 서블릿
- API 예외 처리 - 스프링 부트 기본 예외 처리
- API 예외처리 - HandlerExceptionResolver (1)
- API 예외처리 - HandlerExceptionResolver (2)
- API 예외 처리 - 스프링이 제공하는 ExceptionResolver
- API 예외처리 - @ExceptionHandler
- API 예외처리 - @ControllerAdvice

</details>

<details>
<summary><h3>스프링 타입 컨버터</h3></summary>

- [스프링 타입 컨버터](https://github.com/WooJinDeve/Spring-Study/issues/23#issue-1351794746)
- 스프링 타입 컨버터 소개
- 타입 컨버터 - Converter
- 컨버전 서비스 - ConversionService
- 스프링에 Converter 적용하기
- 뷰 템플릿에 컨버터 적용하기
- 포맷터 - Formatter
- 포맷터를 지원하는 컨버전 서비스
- 스프링이 제공하는 기본 포맷터

</details>

<details>
<summary><h3>파일 업로드</h3></summary>

- [파일 업로드](https://github.com/WooJinDeve/Spring-Study/issues/24#issue-1351795152)
- 서블릿과 파일업로드 1
- 서블릿과 파일업로드 2
- 스프링과 파일 업로드

</details>
