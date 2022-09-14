# Bean Validation

## Bean Validation

- 먼저 `Bean Validation`은 특정한 구현체가 아니라 `Bean Validation 2.0(JSR-380)`이라는 기술 표준이다.
- `Bean Validation`을 구현한 기술중에 일반적으로 사용하는 구현체는 하이버네이트 `Validator`이다. 이름이 하이버네이트가 붙어서 그렇지 `ORM`과는 관련이 없다.

```java
@Data
public class Item {

    private Long id;
    
    @NotBlank
    private String itemName;
    
    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;
    
    @NotNull
    @Max(9999)
    private Integer quantity;
}
```

- 스프링 부트는 자동으로 **글로벌 Validator로 등록**한다
    - `LocalValidatorFactoryBean` 을 글로벌 `Validator`로 등록한다. 이 `Validator`는 `@NotNull` 같은 애노테이션을 보고 검증을 수행한다. 이렇게 글로벌 `Validator`가 적용되어 있기 때문에, `@Valid` , `@Validated` 만 적용하면 된다.
    - 검증 오류가 발생하면, `FieldError` , `ObjectError` 를 생성해서 `BindingResult` 에 담아준다
- **검증 순서**
    - `@ModelAttribute` 각각의 필드에 타입 변환 시도
        1. 성공하면 다음으로
        2. 실패하면 `typeMismatch` 로 `FieldError` 추가
        3. `Validator` 적용

## Bean Calidation - 에러 코드

- **@NotBlank**
    - `NotBlank.item.itemName`
    - `NotBlank.itemName`
    - `NotBlank.java.lang.String`
    - `NotBlank`
- **@Range**
    - `Range.item.price`
    - `Range.price`
    - `Range.java.lang.Integer`
    - `Range`

```java
#Bean Validation 추가
NotBlank={0} 공백X 
Range={0}, {2} ~ {1} 허용
Max={0}, 최대 {1}
```

- **BeanValidation 메시지 찾는 순서**
    1. 생성된 메시지 코드 순서대로 `messageSource` 에서 메시지 찾기
    2. 애노테이션의 `message` 속성 사용 `@NotBlank(message = "공백! {0}")`
    3. 라이브러리가 제공하는 기본 값 사용 공백일 수 없습니다.
    

## Bean Validation - 오브젝트 오류

- **메시지 코드**
    - `ScriptAssert.item`
    - `ScriptAssert`

```java
@Data
@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000"
		, message = "총합이 10000원을 넘겨주세요.")
public class Item {
 //...
}
```

## Bean Validation - groups

- `BeanValidation groups` 기능 사용
    - 이런 문제를 해결하기 위해 `Bean Validation`은 `groups`라는 기능을 제공한다.
    - 예를 들어서 등록시에 검증할 기능과 수정시에 검증할 기능을 각각 그룹으로 나누어 적용할 수 있다.

```java
@Data
public class Item {

    @NotNull(groups = UpdateCheck.class)
    private Long id;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = {SaveCheck.class})
    private Integer quantity;
}

@PostMapping("/add")
public String addItem2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		....
}
```