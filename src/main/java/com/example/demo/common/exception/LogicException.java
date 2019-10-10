package com.example.demo.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 业务逻辑异常类
 *
 * @author wangshulun
 * @date 2019/7/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogicException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 异常信息
     */
    private String errorMsg;
    /**
     * 错误码
     */
    private Integer code;


    /**
     * 抛出逻辑异常
     *
     * @param errorMsg
     * @return
     */
    public static LogicException le(Integer code, String errorMsg) {
        return new LogicException().setCode(code).setErrorMsg(errorMsg);
    }
}

