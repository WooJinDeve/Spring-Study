# 서블릿 + JSP MVC 패턴

## MVC 패턴 - 개요

- **Model View Controller**
    - **컨트롤러** : HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행한다. 그리고 뷰에 전달할 결과 데이터를 조회해서 모델에 담는다.
    - **모델** : 뷰에 출력할 데이터를 담아둔다. 뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 뷰는 비즈니스 로직이나 데이터 접근을 몰라도 되고, 화면을 렌더링 하는 일에 집중할 수 있다.
    - **뷰** : 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다. 여기서는 HTML을 생성하는 부분을 말한다


![Untitled](https://user-images.githubusercontent.com/106054507/190147255-f30a4291-3981-4d91-b96f-c88f3fa9acd7.png)

## MVC 패턴 한계

- **MVC 컨트롤러의 단점**
    - **포워드 중복**
        - View로 이동하는 코드가 항상 중복 호출되어야 한다. 물론 이 부분을 메서드로 공통화해도 되지만, 해당 메서드로 항상 직적 호출해야 한다.
    - **ViewPath에 중복**
        - prefix : `/WEB-INF/views/`
        - suffix : `.jsp`
    - **사용하지 않는 코드**
    
    ```java
    HttpServletRequest request, HttpServletResponse response
    ```
    
    - **공통 처리가 어렵다.**
    

## 프론트 컨트롤러

![Untitled 1](https://user-images.githubusercontent.com/106054507/190147265-904732d5-3ee3-4a6e-9ecb-ce6b6d0cc7cf.png)


- **FrontController 패턴 특징**
    - 프론토 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
    - 프론트 컨트롤러가 요청에 맞는 컴트롤러를 찾아서 호출
    - 공통 처리 가능
    - 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서브릿을 사용하지 않아도 됨
- **스프링 MVC와 프론트 컨트롤러**
    - 스프링 웹 MVC의 핵심도 바로 `FronController`
    - 스프링 웹 MVC의 `DispatcherServlet`이 `FrontController` 패턴으로 구현되어 있음.
