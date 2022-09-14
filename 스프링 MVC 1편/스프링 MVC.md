# 스프링 MVC

## 요청 매핑

- **MappingController**

```java
@Slf4j
@RestController
public class MappingController {

    @RequestMapping("/hello-basic")
    public String helloBasic(){
        log.info("helloBasic");
        return "ok";
    }
}
```

- `@RestController`
    - `@Controller` 는 반환 값이 `String` 이면 뷰 이름으로 인식된다. 그래서 뷰를 찾고 뷰가 랜더링 된다.
    - `@RestController` 는 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력한다. 따라서 실행 결과로 ok 메세지를 받을 수 있다.
- `@RequestMapping("/hello-basic")`
    - `/hello-basic` URL 호출이 오면 이 메서드가 실행되도록 매핑한다.
    - 대부분의 속성을 배열[] 로 제공하므로 다중 설정이 가능하다. `{"/hello-basic", "/hello-go"}`
- **HTTP 메서드 매핑 축약**

```java
@Slf4j
@RestController
public class MappingController {
    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "ok";
    }

    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }
}
```

- **PathVariable(경로 변수 사용)**

```java
@GetMapping("mapping/{userId}")
public String mappingPath(@PathVariable("userId") String data) {
    log.info("userid = {}", data);
    return "ok";
}

@GetMapping("/mapping/users/{userId}/orders/{orderId}")
public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
    log.info("mappingPath userId={}, orderId={}", userId, orderId);
    return "ok";
}
```

## HTTP 요청 파라미터

- **쿼리 파라미터**

```java
@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username = {}, age = {}", username, age);

        response.getWriter().write("ok");

    }
}
```

- **HTTP Form**

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<form action="/request-param-v1" method="post">
  username: <input type="text" name="username" />
  age: <input type="text" name="age" />
  <button type="submit">전송</button>
</form>
</body>
</html>
```

- **@RequestParam**
    - `@RequestParam` : 파라미터 이름으로 바인딩
    - `@ResponseBody` : `View` 조회를 무시하고, `HTTP message body`에 직접 해당 내용 출력.
    - `String` , `int` , `Integer` 등의 단순 타입이면 `@RequestParam` 도 생략 가능
    - `@RequestParam.required`
        - 파라미터 필수 여부
        - 기본값이 파라미터 필수 `(true)`이다
    - 파라미터를 `Map`, `MultiValueMap`으로 조회 할 수 있다.

```java
@Slf4j
@Controller
public class RequestParamController {
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(@RequestParam("username") String memberName,
                                 @RequestParam("age") int memberAge) {
        log.info("username = {}, age = {}", memberName, memberAge);
        return "ok";
    }

		@ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV2(@RequestParam String username,
                                 @RequestParam int age) {
        log.info("username = {}, age = {}", username, age);
        return "ok";
	  }

		@ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

		@ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(@RequestParam(required = true) String username,
                                       @RequestParam(required = false) Integer age) {
        log.info("username = {}, age = {}", username, age);
        return "ok";
	  }

		@ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(@RequestParam(defaultValue = "guest") String username,
                                       @RequestParam(required = false, defaultValue = "-1") Integer age) {
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

		@ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"),
                paramMap.get("age"));
        return "ok";
    }
}
```

- **@ModelAttribute**
    - 스프링MVC는 `@ModelAttribute` 가 있으면 다음을 실행한다.
        - `HelloData` 객체를 생성한다.
        - 요청 파라미터의 이름으로 `HelloData` 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 `setter`를 호출해서 파라미터의 값을 입력(바인딩) 한다.
        - 예) 파라미터 이름이 `username` 이면 `setUsername()` 메서드를 찾아서 호출하면서 값을 입력한다
    - **바인딩 오류**
        - 쿼리 파라미터에  `Int = String`  등으로 처리 할 경우 `BindException error` 가 발생한다.
    - **스프링 규칙**
        - `String`, `int`, `Integer` 같은 단순 타입 = `@RequestParam`
        - `ETC` = `@ModelAttribute`
        - `argument resolver` 로 지정한 타입 제외.

```java
@Slf4j
@Controller
public class RequestParamController {
		@ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
        log.info("helloData = {}", helloData);
        return "ok";
    }

		@ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
        log.info("helloData = {}", helloData);
        return "ok";
    }
}
```

- **단순 텍스트**
    - **HTTP message body**에 데이터를 담아서 요청
        - HTTP API에서 주로 사용 : `JSON`, `XML`, `TEXT`
        - 요청 파라미터와 다르게 HTTP 메시지 바디를 통해 데이터가 직접 데이터로 넘어오는 경우 `@RequestParam`, `@ModelAttribute` 사용 불가
    - **스프링 MVC는 파라미터를 지원**
        - `InputStream(Reader)`: HTTP 요청 메시지 바디의 내용을 직접 조회
        - `OutputStream(Writer)`: HTTP 응답 메시지의 바디에 직접 결과 출력
        - `HttpEntity`: `HTTP header`, `body` 정보를 편리하게 조회
            - 메시지 바디 정보를 직접 조회
            - 요청 파라미터를 조회하는 기능과 관계 없음 `@RequestParam X` , `@ModelAttribute X`
        - `HttpEntity`는 응답에도 사용 가능
            - 메시지 바디 정보 직접 반환
            - 헤더 정보 포함 가능
            - `view` 조회 X

```java
@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);
        response.getWriter().write("ok");
    }

		@PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);
        responseWriter.write("ok");
    }

		@PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {

        String messageBody = httpEntity.getBody();
        log.info("messageBody = {}", messageBody);
        return new HttpEntity<>("ok");
    }

		@PostMapping("/request-body-string-v3-1")
    public HttpEntity<String> requestBodyStringV3_1(RequestEntity<String> httpEntity) throws IOException {

        String messageBody = httpEntity.getBody();
        log.info("messageBody = {}", messageBody);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody){

        log.info("messageBody = {}", messageBody);
        return "ok";
    }
}
```

- **JSON**
    - **@RequestBody 객체 파라미터**
        - `@RequestBody HelloData data`
        - `@RequestBody` 에 직접 만든 객체를 지정할 수 있다.
        - `HttpEntity` , `@RequestBody` 를 사용하면 HTTP 메시지 컨버터가 HTTP 메시지 바디의 내용을 우리가 원하는 문자나 객체 등으로 변환해준다.

```java
@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }

    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

		@ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }

		@ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(HttpEntity<HelloData> httpEntity) {
        HelloData data = httpEntity.getBody();
        log.info("username = {}, age = {}", data.getUsername(), data.getAge());
        return "ok";
    }

		@ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) {
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
        return helloData;
    }
}
```

## HTTP 응답

- **정적 리소스**
    - 스트링 부트는 클래스패스의 다음 디렉토리에 있는 정적 리소스를 제공한다
        - `/static`, `/public`, `/resouces`, `/META-INF/resources`
    - [`http://localhost:8080/basic/hello-form.html`](http://localhost:8080/basic/hello-form.html)

- **뷰 템플릿**
    - 뷰 템플릿을 거쳐서 HTML이 생성되고, 뷰가 응답을 만들어서 전달한다.
    - 스프링 부트는 기본 뷰 템플릿 경로를 제공한다.
        - `src/main/resources/templates`
    - `@ResponseBody` 가 없으면 `response/hello` 로 뷰 리졸버가 실행되어서 뷰를 찾고, 렌더링 한다.
    - `@ResponseBody` 가 있으면 뷰 리졸버를 실행하지 않고, HTTP 메시지 바디에 직접 `response/hello` 라는 문자가 입력된다.

```java

@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1(){
        ModelAndView mav = new ModelAndView("response/hello")
                .addObject("data", "hello!");
        return mav;
    }

		@RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "Hello!");
        return "response/hello";
    }

		@RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        model.addAttribute("data", "Hello!");
    }
}
```

- **HTTP API, 메시지 바디**

```java
@Slf4j
@RestController
public class ReponseBodyController {
    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }

    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyV2() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping("/response-body-string-v3")
    public String responseBodyV3() {
        return "ok";
    }

    @GetMapping("response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1(){
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("response-body-json-v2")
    public HelloData responseBodyJsonV2(){
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return helloData;
    }
}
```

## HTTP 메시지 컨버터

- **스프링 MVC의 HTTP 컨버터 적용**
    - HTTP 요청: `@RequestBody` , `HttpEntity(RequestEntity)`
    - HTTP 응답: `@ResponseBody` , `HttpEntity(ResponseEntity)`
- **HTTP 메시지 컨버터 인터페이스**
    - `org.springframwork.http.converter.HttpMessageConverter`
- **스프링 부트 기본 메시지 컨버터**

```
0 = ByteArrayHttpMessageConverter
1 = StringHttpMessageConverter 
2 = MappingJackson2HttpMessageConverter
```

- **ByteArrayHttpMessageConverter**
    - `byte[]` 데이터를 처리한다
    - 클래스 타입 : `byte[]`, 미디어 타입 : `“**/*”*`
- **StringHttpMessageConverter**
    - String 문자로 데이터를 처리한다
    - 클래스 타입 : `String` , 미디어 타입 : `“**/*”*`
- **MappingJackson2HttpMessageConverter**
    - 클래스타입 : 객체 또는 `HashMap`, 미디어 타입 : `application/json`
    
- **HTTP 요청 데이터 읽기**
    - HTTP 요청이 오고, 컨트롤러에서 `@RequestBody`, `HttpEntity` 파라미터를 사용한다
    - 메시지 컨버터가 메시지를 읽을 수 있는지 확인하기 위해 `canRead()` 를 호출한다.
        - 대상 클래스 타입을 지원하는가
        - HTTP 요청의 `Content-Type` 미디어 타입을 지원하는가
    - `canRead()` 조건을 만족하면 `read()`를 호출해서 객체 생성하고 반환한다.
- **HTTP 응답 데이터 생성**
    - 컨트롤러에서 `@ResponseBody`, `HttpEntity` 파라미터를 사용한다.
    - 메시지 컨버터가 메시지를 쓸 수 있는지 확인하기 위해 `canWrite()` 를 호출한다.
        - 대상 클래스 타입을 지원하는가
        - HTTP 요청의 Accept 미디어 타입을 지원하는가
    - `canWrite()` 조건을 만족하면 `write()`를 호출해서  HTTP 응답 메시지 바디에 데이터를 생성한다
    

## 요청 매핑 핸들러 어뎁터 구조

- **SpringMVC 구조**

![Untitled](%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20MVC%20fc03e30eb28d4b38802278fbf3b387a2/Untitled.png)

- **RequestMappingHandlerAdapter 동작방식**

![Untitled](%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20MVC%20fc03e30eb28d4b38802278fbf3b387a2/Untitled%201.png)

- **ArgumentResolver**
    - 애노테이션 기반 컨트롤러를 처리하는 `RequestMappingHandlerAdapter` 는 바로 이
    `ArgumentResolver` 를 호출해서 컨트롤러(핸들러)가 필요로 하는 다양한 파라미터의 값(객체)을 생성한다.
    - 그리고 이렇게 파리미터의 값이 모두 준비되면 컨트롤러를 호출하면서 값을 넘겨준다
    - **동작 방식**
        - `ArgumentResolver` 의 `supportsParameter()` 를 호출해서 해당 파라미터를 지원하는지 체크한다.
        - 지원하면 `resolveArgument()` 를 호출해서 실제 객체를 생성한다. 그리고 이렇게 생성된 객체가 컨트롤러호출시 넘어가는 것이다.
- **ReturnValueHandler**
    - `HandlerMethodReturnValueHandler` 를 줄여서 `ReturnValueHandler` 라 부른다.
    - `ArgumentResolver` 와 비슷한데, 이것은 응답 값을 변환하고 처리한다.
    - 컨트롤러에서 `String`으로 뷰 이름을 반환해도, 동작하는 이유가 바로 `ReturnValueHandler` 덕분이다.

- **HTTP 메시지 컨버터**

![Untitled](%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20MVC%20fc03e30eb28d4b38802278fbf3b387a2/Untitled%202.png)