package net.jeikobu.mediasorter.filters;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by j.strzyzewski.ext on 2017-04-13.
 */
public class NameContainsFileFilter implements FileFilter {
    String contains;

    @Override
    public boolean accept(File f) {
        return f.getName().contains(contains);
    }
}
