package net.jeikobu.mediasorter.exceptions;

import net.jeikobu.mediasorter.Category;

import java.io.File;

/**
 * Created by j.strzyzewski.ext on 2017-04-14.
 */
public class DoesNotBelongToThisCategory extends Exception {
    public DoesNotBelongToThisCategory(Category c, File f) {
        super("File " + f.getName() + " does not belong to category " + c.getLabel() + "!");
    }
}
