package com.example.pokemondemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PokemonController {

    @Value("${password}")
    private String password;

    private final PokemonConfig pokemonConfig;

    public PokemonController(PokemonConfig pokemonConfig) {
        this.pokemonConfig = pokemonConfig;
    }

    @GetMapping("")
    public String hello() {
        if (!password.isBlank() && !"mudkipisthebest".equals(password)) {
            return "You shall not pass!";
        }

        if (pokemonConfig.getMessage() != null) {
            return String.format(pokemonConfig.getMessage(), pokemonConfig.getFavourite());
        }

        return "Hello, " + pokemonConfig.getFavourite() + "!";
    }

    @GetMapping("/types")
    public String types() {
        return "Types: Normal, Fire, Water, Electric, Grass, Ice, Fighting, Poison, Ground, Flying, Psychic, Bug, Rock, Ghost, Dragon, Dark, Steel, Fairy";
    }
}
