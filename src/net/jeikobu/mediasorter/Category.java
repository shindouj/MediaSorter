package net.jeikobu.mediasorter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import net.jeikobu.mediasorter.exceptions.DoesNotBelongToThisCategory;
import net.jeikobu.mediasorter.exceptions.MoreThanOneSubcategoryException;

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
    @XStreamAsAttribute
    private String           label;


    public Path getDirectory() {
        return directory;
    }

    public String getLabel() {
        return label;
    }

    public boolean allFiltersAccepted(List<FileFilter> filters, File f) {
        for (FileFilter filter: filters) {
            if (!filter.accept(f)) return false;
        }
        return true;
    }

    public Category whichCategoryBelongsTo(File f) throws MoreThanOneSubcategoryException, DoesNotBelongToThisCategory {
        if(!allFiltersAccepted(filters, f))
            throw new DoesNotBelongToThisCategory(this, f);
        List<Category> belongedSubCats = new ArrayList<>();
        if (subCategories != null) {
            for (Category c: subCategories) {
                try {
                    Category belonged = c.whichCategoryBelongsTo(f);
                    belongedSubCats.add(belonged);
                } catch (DoesNotBelongToThisCategory e) {
                    continue;
                }
            }
        }
        if(belongedSubCats.size() > 1)
            throw new MoreThanOneSubcategoryException(f);
        if(belongedSubCats.isEmpty())
            return this;
        return belongedSubCats.get(0);
    }

    public Category(FileFilter f) {
        filters.add(f);
    }

    @Override
    public String toString() {
        return "Category{" +
                "subCategories=" + subCategories +
                ", filters=" + filters +
                ", directory=" + directory +
                ", label='" + label + '\'' +
                '}';
    }
}
