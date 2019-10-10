package com.example.demo.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean success;
    @JsonProperty(value="ReturnCode")
    private Integer ReturnCode;
    @JsonProperty(value = "Data")
    private Object Data;
    @JsonProperty(value = "ErrMsg")
    private String ErrMsg;
}
