package com.aman.cards.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;

@ConfigurationProperties(prefix = "cards")
public record CardsContactInfo(HashMap<String, String> contact, String message) {
}
