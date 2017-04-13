package net.jeikobu.mediasorter.exceptions;

import java.io.File;

/**
 * Created by j.strzyzewski.ext on 2017-04-13.
 */
public class NotBelongsToAnyCategoryException extends Exception {
    File f;

    public NotBelongsToAnyCategoryException(File f) {
        super("File: " + f.getAbsolutePath() + " does not belong to any category!");
        this.f = f;
    }
}
