package hello.servlet.web.frontcontroller.v5;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.web.frontcontroller.ModelView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MyHandlerAdapter {

    boolean supports(Object handler);

    ModelView handler(HttpServletRequest request, HttpServletResponse response, Object handler)throws ServletException, IOException;
}
