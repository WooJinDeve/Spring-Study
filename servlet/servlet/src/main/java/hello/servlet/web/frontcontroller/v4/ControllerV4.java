package hello.servlet.web.frontcontroller.v4;

import java.util.Map;

/**
 * @param paramMap
 * @param model
 * @return viewName
 */
public interface ControllerV4 {
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
