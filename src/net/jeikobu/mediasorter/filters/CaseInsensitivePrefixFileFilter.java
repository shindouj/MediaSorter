package net.jeikobu.mediasorter.filters;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;

/**
 * MediaSorter - created by shindouj on 2017-04-13.
 * Licensed under GPLv3.
 */
@XStreamAlias("CaseInsensitivePrefixFilter")
public class CaseInsensitivePrefixFileFilter implements FileFilter {

    public CaseInsensitivePrefixFileFilter(String prefix) {
        this.prefix = prefix;
    }

    @XStreamAsAttribute
    private String prefix;

    @Override
    public boolean accept(File f) {
        return StringUtils.startsWithIgnoreCase(f.getName(), prefix);
    }

    @Override
    public String toString() {
        return "CaseInsensitivePrefixFileFilter{" +
                "prefix='" + prefix + '\'' +
                '}';
    }
}
