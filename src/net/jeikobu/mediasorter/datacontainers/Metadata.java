package net.jeikobu.mediasorter.datacontainers;

/**
 * MediaSorter - Created by shindouj on 28/07/2017
 */
public abstract class Metadata {
    private String title;
    private String year;
    private String author;

    public String getProperFilename() {
        return this.author + " - " + this.title + " - " + this.year;
    }

    public String getTitle() {
        return this.title;
    }
}
