package com.aman.loans.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;

@ConfigurationProperties(prefix = "loans")
public record LoansContactInfo(HashMap<String, String> contact, String message) {
}
