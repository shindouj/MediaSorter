package net.jeikobu.mediasorter.parsers;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * MediaSorter - Created by shindouj on 11/09/2017
 */
@XStreamAlias("Settings")
public class ParserSettings {
    @XStreamAlias("FolderNameScheme")
    private String folderNameScheme;
    @XStreamAlias("FileNameScheme")
    private String fileNameScheme;
}
