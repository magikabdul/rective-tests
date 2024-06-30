package cloud.cholewa.reactivetests.infrastrucure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(
            ErrorAttributes errorAttributes,
            WebProperties.Resources resources,
            ApplicationContext applicationContext,
            ServerCodecConfigurer serverCodecConfigurer
    ) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected void logError(ServerRequest request, ServerResponse response, Throwable throwable) {
        log.error("Received {} error", throwable.getClass().getName());
    }


    Mono<ServerResponse> renderErrorResponse(final ServerRequest serverRequest) {
        Map<String, Object> errorAttributesMap = getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults());
        /* Map<String, Object> errorAttributesMap = getErrorAttributes(serverRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE)); */

        return ServerResponse
                .status((int) errorAttributesMap.get("status"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorAttributesMap));
    }
}
