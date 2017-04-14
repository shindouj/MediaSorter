package net.jeikobu.mediasorter.exceptions;

import java.io.File;

/**
 * Created by j.strzyzewski.ext on 2017-04-13.
 */
public class MoreThanOneSubcategoryException extends Exception {
    File f;

    public MoreThanOneSubcategoryException(File f) {
        super("File: " + f.getAbsolutePath() + " belongs to more than one subcategory! Specify your filters.");
        this.f = f;
    }
}
