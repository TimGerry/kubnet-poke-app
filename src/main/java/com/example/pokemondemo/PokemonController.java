package com.example.pokemondemo;

import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


@RestController
public class PokemonController {

    @Value("${password}")
    private String password;

    private final PokemonConfig pokemonConfig;
    private WebClient webClient;


    public PokemonController(PokemonConfig pokemonConfig) {
        this.pokemonConfig = pokemonConfig;

        if (!StringUtil.isNullOrEmpty(pokemonConfig.getOpponentUrl())) {
            this.webClient = WebClient.builder()
                    .baseUrl(pokemonConfig.getOpponentUrl())
                    .build();
        }
    }

    @GetMapping("")
    public String hello() {
        System.out.println("new version is running");
        if (!password.isBlank() && !"mudkipisthebest".equals(password)) {
            return "You shall not pass!";
        }

        if (pokemonConfig.getMessage() != null) {
            return String.format(pokemonConfig.getMessage(), pokemonConfig.getFavourite());
        }

        return "Hello, " + pokemonConfig.getFavourite() + "!";
    }

    @PutMapping("/use/{attack}")
    public void use(@PathVariable("attack") String attack) throws IOException {
        if (webClient != null) {
            webClient.put()
                    .uri("/damage/" + attack)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }

        String message = pokemonConfig.getFavourite() + " used " + attack + "!";
        writeMessage(message);
    }

    @PutMapping("/damage/{attack}")
    public void damage(@PathVariable("attack") String attack) throws IOException {
        String message = pokemonConfig.getFavourite() + " was hit by " + attack + "!";
        writeMessage(message);
    }

    @GetMapping("/types")
    public String types() {
        return "Types: Normal, Fire, Water, Electric, Grass, Ice, Fighting, Poison, Ground, Flying, Psychic, Bug, Rock, Ghost, Dragon, Dark, Steel, Fairy";
    }

    private void writeMessage(String message) throws IOException {
        Path file = Path.of("pokemon/logs.txt");
        Files.createDirectories(file.getParent());

        if (!Files.exists(file)) Files.createFile(file);

        Files.writeString(file, message + System.lineSeparator(), StandardOpenOption.APPEND);
    }
}
