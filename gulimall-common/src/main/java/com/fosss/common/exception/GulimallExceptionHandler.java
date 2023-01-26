package com.fosss.common.exception;

import com.fosss.common.exception.ExceptionResult;
import com.fosss.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fosss
 * @date 2023/1/25
 * @description： 统一异常处理
 */
@RestControllerAdvice
public class GulimallExceptionHandler {
    //数据校验异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R ValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> errors = bindingResult.getFieldErrors();
        Map<String, String> map = new HashMap<>();
        errors.forEach((item) -> {
            String field = item.getField();
            String message = item.getDefaultMessage();
            map.put(field, message);
        });
        return R.error(ExceptionResult.VALID_EXCEPTION.getCode(), ExceptionResult.VALID_EXCEPTION.getMessage()).put("data", map);
    }

    //未知异常
    @ExceptionHandler(value = Throwable.class)
    public R UnKnowException(Throwable t) {
        return R.error(ExceptionResult.UNKNOWN_EXCEPTION.getCode(), ExceptionResult.UNKNOWN_EXCEPTION.getMessage());
    }
}
















