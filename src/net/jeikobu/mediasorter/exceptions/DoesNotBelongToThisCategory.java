package net.jeikobu.mediasorter.exceptions;

import net.jeikobu.mediasorter.Category;

import java.io.File;

/**
 * MediaSorter - created by shindouj on 2017-04-14.
 * Licensed under GPLv3.
 */
public class DoesNotBelongToThisCategory extends Exception {
    public DoesNotBelongToThisCategory(Category c, File f) {
        super("File " + f.getName() + " does not belong to category " + c.getLabel() + "!");
    }
}
