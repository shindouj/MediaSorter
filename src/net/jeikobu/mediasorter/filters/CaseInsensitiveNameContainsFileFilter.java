package net.jeikobu.mediasorter.filters;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;

/**
 * MediaSorter - Created by shindouj on 2017-04-13
 */
@XStreamAlias("CaseInsensitiveNameContainsFilter")
public class CaseInsensitiveNameContainsFileFilter implements FileFilter {
    @XStreamAsAttribute
    private String contains;

    @Override
    public boolean accept(File f) {
        return StringUtils.containsIgnoreCase(f.getName(), contains);
    }
}
