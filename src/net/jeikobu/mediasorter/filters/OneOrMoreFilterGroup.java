package net.jeikobu.mediasorter.filters;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.logging.Filter;

/**
 * Created by j.strzyzewski.ext on 2017-04-13.
 */
public class OneOrMoreFilterGroup implements FilterGroup {
    List<FileFilter> filters;

    @Override
    public boolean accept(File f) {
        for (FileFilter filter: filters) {
            if (filter.accept(f)) return true;
        }
        return false;
    }
}
