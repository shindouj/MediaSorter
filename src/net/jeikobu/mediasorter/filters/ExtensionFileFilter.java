package net.jeikobu.mediasorter.filters;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.File;
import java.io.FileFilter;

/**
 * MediaSorter - Created by shindouj on 2017-04-13
 */
@XStreamAlias("ExtensionFilter")
public class ExtensionFileFilter implements FileFilter {
    @XStreamAsAttribute
    private String extension;

    @Override
    public boolean accept(File f) {
        return f.getName().endsWith(extension);
    }
}
