package com.example.demo.elk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "elkconfig")
@Configuration
public class ElkConfig   {
    private String clustername;
    private String index;
    @NestedConfigurationProperty
    private List<Hosts> hosts;
}
