package com.aman.cards.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;

@ConfigurationProperties(prefix = "cards")
@Getter
@Setter
public class CardsContactInfo {
    private HashMap<String, String> contact;
    private String message;
}
