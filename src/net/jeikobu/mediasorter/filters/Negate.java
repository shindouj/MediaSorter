package net.jeikobu.mediasorter.filters;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * MediaSorter - Created by shindouj on 15/04/2017
 */
@XStreamAlias("Negate")
public class Negate implements FilterGroup {
    @XStreamImplicit
    private List<FileFilter> filterList = new ArrayList<>();

    @Override
    public boolean accept(File f) {
        for (FileFilter filter: filterList) {
            if (filter.accept(f)) return false;
        }
        return true;
    }
}
