package ru.practicum.gateway.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@Component
@Slf4j
public class RequestInterceptor implements WebGraphQlInterceptor {
    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        log.info("Intercepted");
        String value = request.getDocument();
        String operationName = request.getOperationName();
        log.info("operationName " + operationName);
        if (operationName != null && !operationName.isBlank()) {
            String correctOperationName = Pattern.compile("^.")
                    .matcher(operationName)
                    .replaceFirst(m -> m.group().toLowerCase());
            log.info("correctOperationName " + correctOperationName);
            request.configureExecutionInput((input, inputBuilder) ->
                    inputBuilder
                            .graphQLContext(contextBuilder -> contextBuilder.put("query", value))
                            .graphQLContext(contextBuilder -> contextBuilder.put("methodName", correctOperationName))
                            .build());
        } else {
            request.configureExecutionInput((input, inputBuilder) ->
                    inputBuilder
                            .graphQLContext(contextBuilder -> contextBuilder.put("query", value))
                            .build());
        }
        log.info(value);
        return chain.next(request);
    }
}