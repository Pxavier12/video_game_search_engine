package fr.lernejo.search.api;

public record InfoPojo(String id, String title, String thumbnail, String short_description, String genre,
                       String game_url, String platform, String publisher, String developer, String release_date,
                       String freetogame_profile_url) { }
