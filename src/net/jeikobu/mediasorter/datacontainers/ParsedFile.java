package net.jeikobu.mediasorter.datacontainers;

import net.jeikobu.mediasorter.Category;

import java.io.File;

/**
 * MediaSorter - Created by shindouj on 28/07/2017
 */
public interface ParsedFile {
    Category getCategory();
    Metadata getMetadata();
    File getFile();
    void renameFileWithMetadataTitle();
    void applyMetadata();
}
