package com.example.demo.common;

import com.alibaba.fastjson.annotation.JSONField;
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
public class DecisionResult implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = -2939004287212856142L;
    @JsonProperty(value="Response")
    @JSONField(name="Response")
    private Response Response;
}
