package org.example.clipflow.exception;

import org.example.clipflow.utils.R;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 *
 * @author hugoxg
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R ex(Exception e) {
        e.printStackTrace();
        String msg = ObjectUtils.isEmpty(e.getMessage()) ? e.toString() : e.getMessage();
        return R.error().setMessage(msg);
    }

    @ExceptionHandler(BaseException.class)
    public R bex(BaseException e) {
        return R.error().setMessage(e.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R exception(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String errorFieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMap.put(errorFieldName, errorMessage);
        });
        return R.error().setMessage("参数错误").setData(errorMap);
    }
}
