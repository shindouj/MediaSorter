package net.jeikobu.mediasorter.filters;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.File;
import java.io.FileFilter;

/**
 * MediaSorter - created by shindouj on 2017-04-13.
 * Licensed under GPLv3.
 */
@XStreamAlias("PrefixFilter")
public class PrefixFileFilter implements FileFilter {

    public PrefixFileFilter(String prefix) {
        this.prefix = prefix;
    }

    @XStreamAsAttribute
    private String prefix;

    @Override
    public boolean accept(File f) {
        return f.getName().startsWith(prefix);
    }

    @Override
    public String toString() {
        return "PrefixFileFilter{" +
                "prefix='" + prefix + '\'' +
                '}';
    }
}
