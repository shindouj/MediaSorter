package net.jeikobu.mediasorter.parsers;

import com.google.gson.annotations.SerializedName;
import net.jeikobu.mediasorter.datacontainers.CategorizedFile;
import net.jeikobu.mediasorter.datacontainers.ParsedVideo;
import net.jeikobu.mediasorter.datacontainers.VideoMetadata;
import net.jeikobu.mediasorter.VideoType;

import java.time.Year;

/**
 * MediaSorter - Created by shindouj on 01/05/2017
 */
public class MovieParserResponse {
    private String  title;
    private Integer season;
    private Integer episode;
    private Integer year;
    private String  resolution;
    private String  quality;
    private String  codec;
    private String  audio;
    private String  group;
    private String  region;
    private Boolean extended;
    private Boolean hardcoded;
    private Boolean proper;
    private Boolean repack;
    private String  container;
    private Boolean widescreen;
    private String  website;
    private String  language;
    private String  sbs;
    private Boolean unrated;
    private String  size;
    @SerializedName("3d")
    private Boolean is3d;

    @Override
    public String toString() {
        return "MovieParserResponse{" +
                "season=" + season +
                ", episode=" + episode +
                ", year=" + year +
                ", resolution='" + resolution + '\'' +
                ", quality='" + quality + '\'' +
                ", codec='" + codec + '\'' +
                ", audio='" + audio + '\'' +
                ", group='" + group + '\'' +
                ", region='" + region + '\'' +
                ", extended=" + extended +
                ", hardcoded=" + hardcoded +
                ", proper=" + proper +
                ", repack=" + repack +
                ", container='" + container + '\'' +
                ", widescreen=" + widescreen +
                ", website='" + website + '\'' +
                ", language='" + language + '\'' +
                ", sbs='" + sbs + '\'' +
                ", unrated=" + unrated +
                ", size='" + size + '\'' +
                ", is3d=" + is3d +
                '}';
    }

    public ParsedVideo toParsedVideo(CategorizedFile cf) {
        VideoType type;
        if (season != null && episode != null) type = VideoType.SERIES;
        else type = VideoType.MOVIE;

        VideoMetadata vm = new VideoMetadata(title,
                year != null ? Year.of(year) : null,
                season != null ? season : 0,
                episode != null ? episode : 0,
                resolution != null ? resolution : quality,
                is3d != null && is3d,
                type);

        return new ParsedVideo(vm, cf);
    }
}
