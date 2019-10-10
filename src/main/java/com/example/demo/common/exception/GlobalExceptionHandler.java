package com.example.demo.common.exception;

import com.example.demo.common.DecisionResult;
import com.example.demo.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常统一处理
 *
 * @author wangshulun
 * @date 2019/7/16
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object logicExceptionHandler(HttpServletRequest request, Exception e, HttpServletResponse response) {
        //系统级异常，错误码固定为-1，提示语固定为系统繁忙，请稍后再试
        Response resp =new Response().setErrMsg(e.getMessage()).setReturnCode(999);


        //如果是业务逻辑异常，返回具体的错误码与提示信息
        if (e instanceof LogicException) {
            LogicException logicException = (LogicException) e;
            resp.setReturnCode(logicException.getCode());
            resp.setErrMsg(logicException.getErrorMsg());
        } else {
            //对系统级异常进行日志记录
            log.error("系统异常:" + e.getMessage(), e);
        }
        log.error("e.getMessage():"+e.getMessage());
        log.error("系统级异常，错误码code:"+resp.getReturnCode()+" message:"+resp.getErrMsg());

        DecisionResult result = new DecisionResult();
        result.setResponse(resp);
        return result;
    }
}

