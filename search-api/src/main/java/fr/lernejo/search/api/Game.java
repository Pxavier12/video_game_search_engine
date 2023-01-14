package fr.lernejo.search.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public record Game(String id, String title, String thumbnail, String short_description, String genre,
                   String game_url, String platform, String publisher, String developer, String release_date,
                   String freetogame_profile_url) { }
