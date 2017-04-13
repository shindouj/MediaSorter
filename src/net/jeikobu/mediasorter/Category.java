package net.jeikobu.mediasorter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import net.jeikobu.mediasorter.exceptions.NotBelongsToAnyCategoryException;
import net.jeikobu.mediasorter.filters.PrefixFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * MediaSorter - Created by shindouj on 2017-04-13
 */
@XStreamAlias("Category")
public class Category {
    @XStreamAlias("Subcategories")
    private List<Category>   subCategories = new ArrayList<>();
    @XStreamAlias("Filters")
    private List<FileFilter> filters       = new ArrayList<>();
    @XStreamAlias("Directory")
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

    public Category(FileFilter f) {
        filters.add(f);
    }
}
