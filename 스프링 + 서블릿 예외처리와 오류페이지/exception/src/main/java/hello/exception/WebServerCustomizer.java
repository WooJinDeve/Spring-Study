package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage[] errorPages ={
                new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404"),
                new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/404"),
                new ErrorPage(RuntimeException.class, "/error-page/500"),
        };
        factory.addErrorPages(errorPages);
    }
}
