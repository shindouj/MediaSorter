package net.jeikobu.mediasorter.filters;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * MediaSorter - Created by shindouj on 2017-04-13
 */
@XStreamAlias("OneOrMoreTrueFilterGroup")
public class OneOrMoreFilterGroup implements FilterGroup {
    public OneOrMoreFilterGroup(FileFilter f) {
        filters.add(f);
    }

    @XStreamImplicit
    private List<FileFilter> filters = new ArrayList<>();

    @Override
    public boolean accept(File f) {
        for (FileFilter filter: filters) {
            if (filter.accept(f)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "OneOrMoreFilterGroup{" +
                "filters=" + filters +
                '}';
    }
}
