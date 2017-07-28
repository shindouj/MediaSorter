package net.jeikobu.mediasorter;

/**
 * MediaSorter - Created by shindouj on 15/04/2017
 */
public enum VideoType {
    MOVIE("movie"),
    SERIES("series"),
    ANIME("anime");

    private String name;

    VideoType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
