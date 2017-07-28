package net.jeikobu.mediasorter.datacontainers;

import net.jeikobu.mediasorter.Category;

import java.io.File;

/**
 * MediaSorter - Created by shindouj on 28/07/2017
 */
public class CategorizedFile {
    private File f;
    private Category c;

    public CategorizedFile(File f, Category c) {
        this.f = f;
        this.c = c;
    }

    public File getFile() {
        return f;
    }

    public Category getCategory() {
        return c;
    }
}
