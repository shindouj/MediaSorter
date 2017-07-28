package net.jeikobu.mediasorter.mediadb;

import com.omertron.omdbapi.OmdbApi;
import net.jeikobu.mediasorter.datacontainers.VideoMetadata;

import java.io.File;

/**
 * MediaSorter - Created by shindouj on 16/04/2017
 */
public class MovieDatabaseConnector {
    private OmdbApi omdbAPI;
    public MovieDatabaseConnector() {
        omdbAPI = new OmdbApi();
    }

    public VideoMetadata getMovieInfo(File f) {
        return null;
    }
}
