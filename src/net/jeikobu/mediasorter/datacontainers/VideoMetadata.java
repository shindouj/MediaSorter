package net.jeikobu.mediasorter.datacontainers;

import net.jeikobu.mediasorter.VideoType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MediaSorter - Created by shindouj on 16/04/2017
 */
public class VideoMetadata extends Metadata {
    private String    title;
    private Year      year;
    private int       season;
    private int       episode;
    private String    resolution;
    private boolean   is3D;
    private VideoType type;

    public VideoMetadata(String title, Year year, int season, int episode,
                         String resolution, boolean is3D, VideoType type) {
        this.title = title;
        this.year = year;
        this.season = season;
        this.episode = episode;
        this.resolution = resolution;
        this.is3D = is3D;
        this.type = type;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public Year getYear() {
        return year;
    }

    public VideoType getType() {
        return type;
    }

    public void setType(VideoType type) {
        this.type = type;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public String getResolution() {
        return resolution;
    }

    public boolean isIs3D() {
        return is3D;
    }



    @Override
    public String toString() {
        return "VideoMetadata{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", season=" + season +
                ", episode=" + episode +
                ", resolution='" + resolution + '\'' +
                ", is3D=" + is3D +
                ", type=" + type +
                '}';
    }
}
