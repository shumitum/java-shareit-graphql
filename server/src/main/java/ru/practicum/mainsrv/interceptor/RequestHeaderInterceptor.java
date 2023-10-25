package ru.practicum.mainsrv.interceptor;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.practicum.mainsrv.exception.NotValidHeaderException;

import static ru.practicum.mainsrv.item.ItemController.USER_ID_HEADER;

@Component
public class RequestHeaderInterceptor implements WebGraphQlInterceptor {
    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        if (request.getHeaders().containsKey(USER_ID_HEADER)) {
            String userIdHeader = request.getHeaders().getFirst(USER_ID_HEADER);
            if (userIdHeader == null || userIdHeader.isBlank()) {
                throw new NotValidHeaderException("Пустой user ID header");
            }
            Long userId = Long.parseLong(userIdHeader);

            request.configureExecutionInput((input, inputBuilder) ->
                    inputBuilder
                            .graphQLContext(contextBuilder -> contextBuilder.put("userId", userId))
                            .build()
            );
        }
        return chain.next(request);
    }
}
