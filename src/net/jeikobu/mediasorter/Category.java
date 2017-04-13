package net.jeikobu.mediasorter;

import net.jeikobu.mediasorter.exceptions.NotBelongsToAnyCategoryException;
import net.jeikobu.mediasorter.filters.PrefixFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by j.strzyzewski.ext on 2017-04-13.
 */
public class Category {
    private List<Category>   subCategories = new ArrayList<>();
    private List<FileFilter> filters       = new ArrayList<>();
    private Path             directory;

    public boolean belongsToCategory(File f) {
        for (FileFilter filter: filters) {
            if (!filter.accept(f)) return false;
        }
        return true;
    }

    public Category belongsToSubcategory(File f) throws NotBelongsToAnyCategoryException {
        external:
        for (Category c: subCategories) {
            for (FileFilter filter: filters) {
                if (!filter.accept(f)) continue external;
            }
            return c;
        }
        throw new NotBelongsToAnyCategoryException(f);
    }

    public Category(PrefixFileFilter f) {
        filters.add(f);
    }
}
