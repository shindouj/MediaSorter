package net.jeikobu.mediasorter.dirfilters;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * MediaSorter - Created by shindouj on 02/05/2017
 */
public class DirectoryContainsFilesFilter implements DirectoryFilter {
    List<FileFilter> filters;

    @Override
    public boolean accept(File pathname) {
        if (!pathname.isDirectory()) return false;
        for (File f: pathname.listFiles()) {
            if (!allFiltersAccepted(f)) continue;
            else return true;
        }
        return false;
    }

    private boolean allFiltersAccepted(File f) {
        for (FileFilter filter: filters) {
            if (!filter.accept(f)) return false;
        }
        return true;
    }
}
