package com.ead.authuser.adapter.inbound.exceptionhandler;

import com.ead.authuser.adapter.response.FieldErrorDetailsResponse;
import com.ead.authuser.adapter.response.ProblemResponse;
import com.ead.authuser.core.exception.AuthUserException;
import com.ead.authuser.core.exception.DomainNotFoundException;
import com.ead.authuser.core.exception.DuplicatedDataException;
import com.ead.authuser.core.exception.IncorrectPasswordException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Log4j2
@ControllerAdvice
@AllArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers,
                                                                  final HttpStatusCode status, final WebRequest request) {
        var rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException invalidFormatException){
            return handleInvalidFormatException(invalidFormatException, headers, status, request);
        }else if (rootCause instanceof PropertyBindingException propertyBindingException){
            return handlePropertyBindingException(propertyBindingException, headers, status, request);
        }
        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message("O corpo da requisição está inválido. Verifique erro de sintaxe.")
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers,
                                                                   final HttpStatusCode status, final WebRequest request) {
        var detail = String.format("O recurso '%s', que você tentou acessar, é inexistente", ex.getRequestURL());
        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message(detail)
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
                                                        final HttpStatusCode status, final WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(methodArgumentTypeMismatchException,
                    headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final HttpHeaders headers,
                                                                   final HttpStatusCode status, final WebRequest request) {
        var detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', que é de um tipo inválido. " +
                        "Corrija e informe um valor compatível com o tipo %s",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message(detail)
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(final PropertyBindingException ex, final HttpHeaders headers,
                                                                  final HttpStatusCode status, final WebRequest request){
        String detail = String.format("A propriedade '%s' é inválida para o objeto '%s'",
                ex.getPropertyName(),
                ex.getReferringClass().getSimpleName());
        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message(detail)
                .build();
        return handleExceptionInternal(ex, response, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(final InvalidFormatException ex, final HttpHeaders headers,
                                                                final HttpStatusCode status, final WebRequest request) {
        String path = ex.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s",
                path, ex.getValue(), ex.getTargetType().getSimpleName());
        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message(detail)
                .build();
        return handleExceptionInternal(ex, response, headers, status, request);
    }

    @ExceptionHandler(DomainNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(final DomainNotFoundException ex, final WebRequest request){
        HttpStatusCode status = NOT_FOUND;
        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(AuthUserException.class)
    public ResponseEntity<?> handleBusinessException(final AuthUserException ex, final WebRequest request){
        HttpStatusCode status = BAD_REQUEST;
        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUncaught(final Exception ex, final WebRequest request){
        HttpStatusCode status = INTERNAL_SERVER_ERROR;
        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(final AccessDeniedException ex, final WebRequest request){
        HttpStatusCode status = FORBIDDEN;
        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(DuplicatedDataException.class)
    public ResponseEntity<?> handleDuplicatedData(final DuplicatedDataException ex, final WebRequest request){
        HttpStatusCode status = CONFLICT;
        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<?> handleIncorrectPassword(final IncorrectPasswordException ex, final WebRequest request){
        HttpStatusCode status = CONFLICT;
        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(final BadCredentialsException ex, final WebRequest request){
        HttpStatusCode status = BAD_REQUEST;
        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(final HttpMediaTypeNotAcceptableException ex,
                                                                      final HttpHeaders headers, final HttpStatusCode status,
                                                                      final WebRequest request) {
        return ResponseEntity.status(status).headers(headers).build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers, final HttpStatusCode status,
                                                                  final WebRequest request) {
        return buildResponseBodyWithValidateErrors(ex.getBindingResult(), status, request, ex);
    }

    private ResponseEntity<Object> buildResponseBodyWithValidateErrors(final BindingResult bindingResult, final HttpStatusCode status,
                                                                       final WebRequest request, final Exception ex){
        var detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";
        var problemFields = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
                    String name = objectError.getObjectName();
                    if (objectError instanceof FieldError fieldError){
                        name = fieldError.getField();
                    }
                    return FieldErrorDetailsResponse.builder()
                            .name(name)
                            .message(message)
                            .build();
                })
                .collect(Collectors.toList());

        var response = ProblemResponse.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .message(detail)
                .fields(problemFields)
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

}
