# Form  전송 객체

- **폼 데이터 전달에 Item 도메인 객체 사용**
    - `HTML Form` → `Item` → `Controller` → `Item` → `Repository`
        - **장점:**
            - `Item` 도메인 객체를 컨트롤러, 리포지토리 까지 직접 전달해서 중간에 `Item`을 만드는 과정이 없어서 간단하다.
        - **단점**
            - 간단한 경우에만 적용할 수 있다. 수정시 검증이 중복될 수 있고, `groups`를 사용해야 한다. 폼 데이터 전달을 위한 별도의 객체 사용
    - `HTML Form` -> `ItemSaveForm` -> `Controller` -> `Item 생성` -> `Repository`
        - **장점**
            - 전송하는 폼 데이터가 복잡해도 거기에 맞춘 별도의 폼 객체를 사용해서 데이터를 전달 받을 수 있다. 보통 등록과, 수정용으로 별도의 폼 객체를 만들기 때문에 검증이 중복되지 않는다.
        - **단점**
            - 폼 데이터를 기반으로 컨트롤러에서 `Item` 객체를 생성하는 변환 과정이 추가된다.

```java
@Data
public class ItemSaveForm {

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(value = 9999)
    private Integer quantity;

}

@PostMapping("/add")
public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

    // 특정 필드가 아닌 복합 툴 검증
    if (form.getPrice() != null & form.getQuantity() != null) {
        int resultPrice = form.getQuantity() * form.getPrice();
        if (resultPrice < 10000) {
            indingResult.reject("totalPriceMin", new Object[]{10000, resultPrice},null);
        }
    }

    //검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
        log.info("errors = {}", bindingResult);
        return "validation/v4/addForm";
    }

    //성공 로직
    Item item = new Item();
    item.setItemName(form.getItemName());
    item.setPrice(form.getPrice());
    item.setQuantity(form.getQuantity());

    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v4/items/{itemId}";
}
```

## Bean Validation - HTTP 메시지 컨버터

- **API의 경우 3가지 경우를 나누어 생각해야 한다.**
    - 성공 요청: 성공
    - 실패 요청: JSON을 객체로 생성하는 것 자체가 실패함
    - 검증 오류 요청: JSON을 객체로 생성하는 것은 성공했고, 검증에서 실패함

```java
@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {

        log.info("API 컨트롤러 호출");

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 errors = {}", bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행");
        return form;
    }
}
```