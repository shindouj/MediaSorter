package net.jeikobu.mediasorter.filters;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import net.jeikobu.mediasorter.VideoType;

import java.io.File;
import java.io.FileFilter;

/**
 * MediaSorter - Created by shindouj on 15/04/2017
 */
@XStreamAlias("OnlineVideoMetadataFilter")
public class OnlineVideoMetadataFilter implements FileFilter {
    @XStreamAsAttribute
    VideoType type;
    @XStreamAsAttribute
    boolean notAnime;
    @XStreamAsAttribute
    boolean defaultValue = true;

    @Override
    public boolean accept(File f) {
        return false;
    }
}
