package cloud.cholewa.reactivetests.infrastrucure;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;

@Configuration
public class ExceptionHandler {

    @Bean
    @Order(-2)
    public GlobalExceptionHandler globalExceptionHandler(
            final ErrorAttributes errorAttributes,
            final WebProperties webProperties,
            final ApplicationContext applicationContext,
            final ServerCodecConfigurer serverCodecConfigurer
    ) {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(
                errorAttributes,
                webProperties.getResources(),
                applicationContext,
                serverCodecConfigurer
        );

        return globalExceptionHandler;
    }
}
