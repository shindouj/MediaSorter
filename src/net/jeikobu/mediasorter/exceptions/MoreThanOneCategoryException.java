package net.jeikobu.mediasorter.exceptions;

import java.io.File;

/**
 * Created by j.strzyzewski.ext on 2017-04-13.
 */
public class MoreThanOneCategoryException extends Exception {
    File f;

    public MoreThanOneCategoryException(File f) {
        super("File: " + f.getAbsolutePath() + " belongs to more than one category! Specify your filters.");
        this.f = f;
    }
}
