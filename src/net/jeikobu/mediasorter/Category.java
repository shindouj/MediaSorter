package net.jeikobu.mediasorter;

import net.jeikobu.mediasorter.exceptions.NotBelongsToAnyCategoryException;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * Created by j.strzyzewski.ext on 2017-04-13.
 */
public class Category {
    List<Category>   subCategories;
    List<FileFilter> filters;

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
}
