package com.example.pokemondemo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pokemon")
public class PokemonConfig {

    private String favourite;
    private String message;

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
