package com.aman.loans.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;

@ConfigurationProperties(prefix = "loans")
@Getter
@Setter
public class LoansContactInfo {
    private HashMap<String, String> contact;
    private String message;
}
