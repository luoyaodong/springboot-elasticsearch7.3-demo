package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieStar {
    private String agentStarttimezh;
    private String agentStarttime;
    private String orderId;
    private String name;
    private String applicationName;
    private int contrastStatus;
    private String contentbody;
    private String dynamicPriceTemplate;
}
