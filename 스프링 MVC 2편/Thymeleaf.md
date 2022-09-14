# Thymeleaf

## 타임리프

- **타임리프 소개**
    - **서버 사이드 HTML 랜더링 (SSR)**
        - 타임리프는 백엔드 서버에서 HTML을 동적으로 렌더링 하는 용도로 사용한다.
    - **네츄럴 템플릿**
        - 타임 리프는 순수 HTML을 최대한 유지하는 특징이 있다
        - JSP를 포함한 다른 뷰 템플릿들은 해당 파일을 열면 파일 자체를 그대로 웹브라우저에서 열어보면 JSP 소스코드와 HTML이 뒤죽박죽 섞여서 웹 브라우저에서 정상적인 HTML 결과를 확인할 수 없다. 오직 서버를 통해서 JSP가 렌더링 되고 HTML 응답 결과를 받아야 화면을 확인할 수 있다.
        - 타임리프는 **순수 HTML을 그대로 유지하면서 뷰 템플릿도 사용할 수 있는 타임리프의 특징을 내츄럴 템플릿**이라 한다.
    - **스프링 융합 지원**
        - 타임리프는 스프링과 자연스럽게 통합되고, 스프링의 다양한 기능을 편리하게 사용할 수 있게 지원한다.
- **기본 표현식**

```
**•** **간단한 표현:
	◦ 변수 표현식: ${...}
	◦ 선택 변수 표현식: *{...}
	◦ 메시지 표현식: #{...}
	◦ 링크 URL 표현식: @{...}
	◦ 조각 표현식: ~{...}
• 리터럴
	◦ 텍스트: 'one text', 'Another one!',…
	◦ 숫자: 0, 34, 3.0, 12.3,…
	◦ 불린: true, false
	◦ 널: null
	◦ 리터럴 토큰: one, sometext, main,…
• 문자 연산:
	◦ 문자 합치기: +
	◦ 리터럴 대체: |The name is ${name}|
• 산술 연산:
	◦ Binary operators: +, -, *, /, %
	◦ Minus sign (unary operator): -
• 불린 연산:
	◦ Binary operators: and, or
	◦ Boolean negation (unary operator): !, not
• 비교와 동등:
	◦ 비교: >, <, >=, <= (gt, lt, ge, le)
	◦ 동등 연산: ==, != (eq, ne)
• 조건 연산:
	◦ If-then: (if) ? (then)
	◦ If-then-else: (if) ? (then) : (else)
	◦ Default: (value) ?: (defaultvalue)
• 특별한 토큰:
	◦ No-Operation: _**

```

## **텍스트 - text, utext**

- 타임리프는 기본적으로 HTML 태그와 속성에 기능을 정의해서 동작한다.
- `<span th:text=”${data}”>`

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1> 컨텐츠에 데이터 출력하기 </h1>
<ul>
    <li>th:text 사용 <span th:text="${data}"> </span></li>
    <li>컨텐츠 안에서 직접 출력하기 = [[${data}]]</li>
</ul>
</body>
</html>
```

- **HTML 엔티티**
    - HTML은 `<` 태그와  `>` 는 `&lt` , `&gt`로 변경해서 처리한다
    - `Thymeleaf`는 `[(..)]`를 통해 `Escape` 처리를 지원해준다.

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>text vs utext</h1>
<ul>
    <li>th:text = <span th:text="${data}"></span></li>
    <li>th:utext = <span th:utext="${data}"></span></li>
</ul>
<h1><span th:inline="none">[[...]] vs [(...)]</span></h1>
<ul>
    <li><span th:inline="none">[[...]] = </span>[[${data}]]</li>
    <li><span th:inline="none">[(...)] = </span>[(${data})]</li>
</ul>
</body>
</html>
```

- **변수**
    - 변수 표현식 :  `${...}`
    - Object
        - `user.username` : `user`의 `username`을 프로퍼티 접근 `user.getUsername()`
        - `user['username']` : 위와 같음 `user.getUsername()`
        - `user.getUsername()` : `user`의 `getUsername()` 을 직접 호출
    - List
        - `users[0].username` : `List`에서 첫 번째 회원을 찾고 `username` 프로퍼티 접근 `list.get(0).getUsername()`
        - `users[0]['username]` : 위와 같음
        - `users[0].getUsername()` : List에서 첫 번째 회원을 찾고 메서드 직접 호출
    - Map
        - `userMap['userA'].username` : `Map`에서 `userA`를 찾고, `username` 프로퍼티 접근 `map.get("userA").getUsername()`
        - `userMap['userA']['username']` : 위와 같음
        - `userMap['userA'].getUsername()` : `Map`에서 `userA`를 찾고 메서드 직접 호출
    - **지역변수**

```html
<h1>지역 변수 - (th:with)</h1>
<div th:with="first=${users[0]}">
    <p>처음 사람의 이름은 <span th:text="${first.username}"></span></p>
</div>
```

- **기본 객체들**
    - `${#request}`
    - `${#response}`
    - `${#session}`
    - `${#servletContext}`
    - `${#locale}`
    - **HTTP 요청 파라미터 접근: param**
        - 예) `${param.paramData}`
    - **HTTP 세션 접근: session**
        - 예) `${session.sessionData}`
    - **스프링 빈 접근: @**
        - 예) `${@helloBean.hello('Spring!')}`

```html
<h1>식 기본 객체 (Expression Basic Objects)</h1>
<ul>
  <li>request = <span th:text="${#request}"></span></li>
  <li>response = <span th:text="${#response}"></span></li>
  <li>session = <span th:text="${#session}"></span></li>
  <li>servletContext = <span th:text="${#servletContext}"></span></li>
  <li>locale = <span th:text="${#locale}"></span></li>
</ul>
<h1>편의 객체</h1>
<ul>
  <li>Request Parameter = <span th:text="${param.paramData}"></span></li>
  <li>session = <span th:text="${session.sessionData}"></span></li>
  <li>spring bean = <span th:text="${@helloBean.hello('Spring!')}"></span></
  li>
</ul>
```

- **유틸리티 객체와 날짜**
    - `#message` : 메시지, 국제화 처리
    - `#uris` : URI 이스케이프 지원
    - `#dates` : java.util.Date 서식 지원
    - `#calendars` : java.util.Calendar 서식 지원
    - `#temporals` : 자바8 날짜 서식 지원
    - `#numbers` : 숫자 서식 지원
    - `#strings` : 문자 관련 편의 기능
    - `#objects` : 객체 관련 기능 제공
    - `#bools` : boolean 관련 기능 제공
    - `#arrays` : 배열 관련 기능 제공
    - `#lists` , `#sets` , `#maps` : 컬렉션 관련 기능 제공
    - `#ids` : 아이디 처리 관련 기능 제공, 뒤에서 설명
    - `**자바 8 날짜**`
        - `<span th:text="${#temporals.format(localDateTime, 'yyyy-MM-dd HH:mm:ss')}"></
        span>`

```html
<ul>
    <li>default = <span th:text="${localDateTime}"></span></li>
    <li>yyyy-MM-dd HH:mm:ss = <span th:text="${#temporals.format(localDateTime,'yyyy-MM-dd HH:mm:ss')}"></span></li>
</ul>
<h1>LocalDateTime - Utils</h1>
<ul>
    <li>${#temporals.day(localDateTime)} = <span th:text="${#temporals.day(localDateTime)}"></span></li>
    <li>${#temporals.month(localDateTime)} = <span th:text="${#temporals.month(localDateTime)}"></span></li>
    <li>${#temporals.monthName(localDateTime)} = <span th:text="${#temporals.monthName(localDateTime)}"></span></li>
    <li>${#temporals.monthNameShort(localDateTime)} = <span th:text="${#temporals.monthNameShort(localDateTime)}"></span></li>
    <li>${#temporals.year(localDateTime)} = <span th:text="${#temporals.year(localDateTime)}"></span></li>
    <li>${#temporals.dayOfWeek(localDateTime)} = <span th:text="${#temporals.dayOfWeek(localDateTime)}"></span></li>
    <li>${#temporals.dayOfWeekName(localDateTime)} = <span th:text="${#temporals.dayOfWeekName(localDateTime)}"></span></li>
    <li>${#temporals.dayOfWeekNameShort(localDateTime)} = <span th:text="${#temporals.dayOfWeekNameShort(localDateTime)}"></span></li>
    <li>${#temporals.hour(localDateTime)} = <span th:text="${#temporals.hour(localDateTime)}"></span></li>
    <li>${#temporals.minute(localDateTime)} = <span th:text="${#temporals.minute(localDateTime)}"></span></li>
    <li>${#temporals.second(localDateTime)} = <span th:text="${#temporals.second(localDateTime)}"></span></li>
    <li>${#temporals.nanosecond(localDateTime)} = <span th:text="${#temporals.nanosecond(localDateTime)}"></span></li>
</ul>
```

## URL 링크

- 타임리프에서 URL을 생성할 때는 `@{...}` 문법을 사용하면 된다

```html
<ul>
    <li><a th:href="@{/hello}">basic url</a></li>
    <li><a th:href="@{/hello(param1=${param1}, param2=${param2})}">hello query param</a></li>
    <li><a th:href="@{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})}">path variable</a></li>
    <li><a th:href="@{/hello/{param1}(param1=${param1}, param2=${param2})}">path variable + query parameter</a></li>
</ul>
```

## 리터럴

- 리터럴은 소스 코드상에 고정된 값을 말하는 용어이다.
- 타임리프의 리터럴
    - 문자: `'hello'`
    - 숫자: `10`
    - 불린: `true` , `false`
    - null: `null`

```html
<ul>
    <!--주의! 다음 주석을 풀면 예외가 발생함-->
    <!-- <li>"hello world!" = <span th:text="hello world!"></span></li>-->
    <li>'hello' + ' world!' = <span th:text="'hello' + ' world!'"></span></li>
    <li>'hello world!' = <span th:text="'hello world!'"></span></li>
    <li>'hello ' + ${data} = <span th:text="'hello ' + ${data}"></span></li>
    <li>리터럴 대체 |hello ${data}| = <span th:text="|hello ${data}|"></span></li>
</ul>
```

## 연산

- 비교연산: HTML 엔티티를 사용해야 하는 부분을 주의하자,
    - `>` (gt), `<` (lt), `>=` (ge), `<=` (le), `!` (not), `==` (eq), `!=` (neq, ne)
- 조건식: 자바의 조건식과 유사하다.
- Elvis 연산자: 조건식의 편의 버전
- No-Operation: _ 인 경우 마치 타임리프가 실행되지 않는 것 처럼 동작한다. 이것을 잘 사용하면 HTML 의 내용 그대로 활용할 수 있다. 마지막 예를 보면 데이터가 없습니다. 부분이 그대로 출력된다.

```html
<ul>
    <li>산술 연산
        <ul>
            <li>10 + 2 = <span th:text="10 + 2"></span></li>
            <li>10 % 2 == 0 = <span th:text="10 % 2 == 0"></span></li>
        </ul>
    </li>
    <li>비교 연산
        <ul>
            <li>1 > 10 = <span th:text="1 &gt; 10"></span></li>
            <li>1 gt 10 = <span th:text="1 gt 10"></span></li>
            <li>1 >= 10 = <span th:text="1 >= 10"></span></li>
            <li>1 ge 10 = <span th:text="1 ge 10"></span></li>
            <li>1 == 10 = <span th:text="1 == 10"></span></li>
            <li>1 != 10 = <span th:text="1 != 10"></span></li>
        </ul>
    </li>
    <li>조건식
        <ul>
            <li>(10 % 2 == 0)? '짝수':'홀수' = <span th:text="(10 % 2 == 0)?'짝수':'홀수'"></span></li>
        </ul>
    </li>
    <li>Elvis 연산자
        <ul>
            <li>${data}?: '데이터가 없습니다.' = <span th:text="${data}?: '데이터가없습니다.'"></span></li>
            <li>${nullData}?: '데이터가 없습니다.' = <span th:text="${nullData}?:'데이터가 없습니다.'"></span></li>
        </ul>
    </li>
    <li>No-Operation
        <ul>
            <li>${data}?: _ = <span th:text="${data}?: _">데이터가 없습니다.</span></li>
            <li>${nullData}?: _ = <span th:text="${nullData}?: _">데이터가없습니다.</span></li>
        </ul>
    </li>
</ul>
```

## 속성 값 설정

- 타임리프는 주로 HTML 태그에 `th:*` 속성을 지정하는 방식으로 동작한다. `th:*` 로 속성을 적용하면 기존 속성을 대체한다. 기존 속성이 없으면 새로 만든다.
- **속성 추가**
    - `th:attrappend` : 속성 값의 뒤에 값을 추가한다.
    - `th:attrprepend` : 속성 값의 앞에 값을 추가한다.
    - `th:classappend` : `class` 속성에 자연스럽게 추가한다.

```html
<h1>속성 설정</h1>
<input type="text" name="mock" th:name="userA" />
<h1>속성 추가</h1>
- th:attrappend = <input type="text" class="text" th:attrappend="class='large'" /><br/>
- th:attrprepend = <input type="text" class="text" th:attrprepend="class='large'" /><br/>
- th:classappend = <input type="text" class="text" th:classappend="large"/></br/>
<h1>checked 처리</h1>
- checked o <input type="checkbox" name="active" th:checked="true" /><br/>
- checked x <input type="checkbox" name="active" th:checked="false" /><br/>
- checked=false <input type="checkbox" name="active" checked="false" /><br/>
```

## 반복

- 타임리프에서 반복은 `th:each` 를 사용한다. 추가로 반복에서 사용할 수 있는 여러 상태 값을 지원한다.
- 반복 기능
    - `<tr th:each="user : ${users}">`
    - 반복시 오른쪽 컬렉션 `( ${users} )`의 값을 하나씩 꺼내서 왼쪽 변수 `( user )`에 담아서 태그를 반복 실행합니다.
    - `th:each` 는 `List` 뿐만 아니라 배열, `java.util.Iterable` , `java.util.Enumeration` 을 구현한 모든 객체를 반복에 사용할 수 있습니다. `Map` 도 사용할 수 있는데 이 경우 변수에 담기는 값은 `Map.Entry` 입니다.

```html
<table border="1">
  <tr>
    <th>count</th>
    <th>username</th>
    <th>age</th>
    <th>etc</th>
  </tr>
  <tr th:each="user, userStat : ${users}">
    <td th:text="${userStat.count}">username</td>
    <td th:text="${user.username}">username</td>
    <td th:text="${user.age}">0</td>
    <td>
      index = <span th:text="${userStat.index}"></span>
      count = <span th:text="${userStat.count}"></span>
      size = <span th:text="${userStat.size}"></span>
      even? = <span th:text="${userStat.even}"></span>
      odd? = <span th:text="${userStat.odd}"></span>
      first? = <span th:text="${userStat.first}"></span>
      last? = <span th:text="${userStat.last}"></span>
      current = <span th:text="${userStat.current}"></span>
    </td>
  </tr>
</table>
```

## 조건부 평가

- **if, unless**
    - 타임리프는 해당 조건이 맞지 않으면 태그 자체를 렌더링하지 않는다.
    - 만약 다음 조건이 `false` 인 경우 `<span>...<span>` 부분 자체가 렌더링 되지 않고 사라진다.
    `<span th:text="'**미성년자**'" th:if="${user.age lt 20}"></span>`
- `switch`은 만족하는 조건이 없을 때 사용하는 디폴트이다.

```html
<table border="1">
  <tr>
    <th>count</th>
    <th>username</th>
    <th>age</th>
  </tr>
  <tr th:each="user, userStat : ${users}">
    <td th:text="${userStat.count}">1</td>
    <td th:text="${user.username}">username</td>
    <td>
      <span th:text="${user.age}">0</span>
      <span th:text="'미성년자'" th:if="${user.age lt 20}"></span>
      <span th:text="'미성년자'" th:unless="${user.age ge 20}"></span>
    </td>
  </tr>
</table>
<h1>switch</h1>
<table border="1">
  <tr>
    <th>count</th>
    <th>username</th>
    <th>age</th>
  </tr>
  <tr th:each="user, userStat : ${users}">
    <td th:text="${userStat.count}">1</td>
    <td th:text="${user.username}">username</td>
    <td th:switch="${user.age}">
      <span th:case="10">10살</span>
      <span th:case="20">20살</span>
      <span th:case="*">기타</span>
    </td>
  </tr>
</table>
```

## 주석

- **표준 HTML 주석**
- **타임리프 파서 주석**
    - 타임리프 파서 주석은 렌더링에서 주석 부분을 제거한다
- **타임리프 프로토타입 주석**
    - `HTML **파일**`을 웹 브라우저에서 열어보면 웹 브라우저가 렌더링 하지 않는다 `**타임리프 렌더링**`을 거치면 이 부분이 정상 렌더링 된다.

```html
<span th:text="${data}">html data</span>
<h1>1. 표준 HTML 주석</h1>
<!--
<span th:text="${data}">html data</span>
-->
<h1>2. 타임리프 파서 주석</h1>
<!--/* [[${data}]] */-->
<!--/*-->
<span th:text="${data}">html data</span>
<!--*/-->
<h1>3. 타임리프 프로토타입 주석</h1>
<!--/*/
<span th:text="${data}">html data</span>
/*/-->
```

## 블록

- `<th:blick>` 은 HTML 태그가 아닌 타임리프 유일한 자체 태그이다.

```html
<th:block th:each="user : ${users}">
    <div>
        사용자 이름1 <span th:text="${user.username}"></span>
        사용자 나이1 <span th:text="${user.age}"></span>
    </div>
    <div>
        요약 <span th:text="${user.username} + ' / ' + ${user.age}"></span>
    </div>
</th:block>
```

## 자바스크립트 인라인

- `<script th:inline=”javasript>`
- **텍스트 렌더링**
    - `var username = [[${user.username}]];`
    - 인라인 사용 전 `var username = userA;`
    - 인라인 사용 후 `var username = "userA"`
- **자바스크립트 내추럴템플릿**
    - `var username2 = /*[[${user.username}]]*/ "test username";`
    - 인라인 사용 전 `var username2 = /*userA*/ "test username";`
    - 인라인 사용 후 `var username2 = "userA";`
- **객체**
    - 타임리프의 자바스크립트 인라인 기능을 사용하면 객체를 `JSON`으로 자동 변역해준다.
    - `var user = [[${user}]];`
    - 인라인 사용 전 `var user = BasicController.User(username=userA, age=10);`
    - 인라인 사용 후 `var user = {"username":"userA","age":10}`

```jsx
<script>
	 var username = [[${user.username}]];
	 var age = [[${user.age}]];
	 //자바스크립트 내추럴 템플릿
	 var username2 = /*[[${user.username}]]*/ "test username";
	 //객체
	 var user = [[${user}]];
</script>
<!-- 자바스크립트 인라인 사용 후 -->
<script th:inline="javascript">
	 var username = [[${user.username}]];
	 var age = [[${user.age}]];
	 //자바스크립트 내추럴 템플릿
	 var username2 = /*[[${user.username}]]*/ "test username";
	 //객체
	 var user = [[${user}]];
</script>
<!-- 자바스크립트 인라인 each -->
<script th:inline="javascript">
	 [# th:each="user, stat : ${users}"]
	 var user[[${stat.count}]] = [[${user}]];
	 [/]
</script>
```

## 템플릿 조각

- `template/fragment/footer :: copy : template/fragment/footer.html` 템플릿에 있는
`th:fragment="copy"` 라는 부분을 템플릿 조각으로 가져와서 사용한다는 의미이다.
- **부분 포함 insert**
    - `<div th:insert="~{template/fragment/footer :: copy}"></div>`
- **부분 포함 replace**
    - `<div th:replace="~{template/fragment/footer :: copy}"></div>`
    - 부분 포함 단순 표현식
        - `<div th:replace="template/fragment/footer :: copy"></div>`
- **파라미터 사용**
    - `<div th:replace="~{template/fragment/footer :: copyParam ('데이터1', '데이터2')}"></
    div>`

```jsx
<h1>부분 포함</h1>
<h2>부분 포함 insert</h2>
<div th:insert="~{template/fragment/footer :: copy}"></div>

<h2>부분 포함 replace</h2>
<div th:replace="~{template/fragment/footer :: copy}"></div>

<h2>부분 포함 단순 표현식</h2>
<div th:replace="template/fragment/footer :: copy"></div>

<h1>파라미터 사용</h1>
<div th:replace="~{template/fragment/footer :: copyParam ('데이터1', '데이터2')}"></div>
```

## 템플릿 레이아웃

- `common_header(~{::title},~{::link})` 이 부분이 핵심이다.
    - `::title` 은 현재 페이지의 `title **태그**`들을 전달한다.
    - `::link` 는 현재 페이지의 `link **태그**`들을 전달한다.

```jsx
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:fragment="common_header(title,links)">
    <title th:replace="${title}">레이아웃 타이틀</title>
    <!-- 공통 -->
    <link rel="stylesheet" type="text/css" media="all" th:href="@{/css/awesomeapp.css}">
    <link rel="shortcut icon" th:href="@{/images/favicon.ico}">
    <script type="text/javascript" th:src="@{/sh/scripts/codebase.js}"></script>
    <!-- 추가 -->
    <th:block th:replace="${links}" />
</head>

<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="template/layout/base :: common_header(~{::title},~{::link})">
  <title>메인 타이틀</title>
  <link rel="stylesheet" th:href="@{/css/bootstrap.min.css}">
  <link rel="stylesheet" th:href="@{/themes/smoothness/jquery-ui.css}">
</head>
<body>
  메인 컨텐츠
</body>
</html>
```