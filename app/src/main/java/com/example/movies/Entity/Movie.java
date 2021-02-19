package com.example.movies.Entity;

public class Movie {
    String name;
    String description;
    String posterPath;

    public Movie(String name, String description, String posterPath) {
        this.name = name;
        this.description = description;
        this.posterPath = posterPath;
    }

    public String getName() { return name; }

    public String getDescription() {
        return description;
    }

    public String getPosterPath() { return posterPath; }
}
