package ru.practicum.gateway.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import ru.practicum.mainsrv.exception.ItemNotFoundException;
import ru.practicum.mainsrv.exception.NotValidHeaderException;
import ru.practicum.mainsrv.exception.UserNotFoundException;

@Component
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof RuntimeException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(defineErrorType(ex))
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        } else {
            return null;
        }
    }

    private ErrorType defineErrorType(Throwable ex) {
        if (ex instanceof UserNotFoundException) {
            return ErrorType.NOT_FOUND;
        } else if (ex instanceof DataIntegrityViolationException || ex instanceof IllegalArgumentException) {
            return ErrorType.FORBIDDEN;
        } else if (ex instanceof ItemNotFoundException) {
            return ErrorType.NOT_FOUND;
        } else if (ex instanceof NotValidHeaderException) {
            return ErrorType.BAD_REQUEST;
        }
        return ErrorType.INTERNAL_ERROR;
    }

}
