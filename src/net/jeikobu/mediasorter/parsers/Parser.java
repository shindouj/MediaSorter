package net.jeikobu.mediasorter.parsers;

import net.jeikobu.mediasorter.datacontainers.CategorizedFile;
import net.jeikobu.mediasorter.datacontainers.ParsedFile;

import java.io.File;

/**
 * MediaSorter - Created by shindouj on 16/04/2017
 */
public interface Parser {
    ParsedFile fromCategorizedFile(CategorizedFile f);
    String getFormat();
    String getName();
}
