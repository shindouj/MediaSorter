package net.jeikobu.mediasorter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * MediaSorter - created by shindouj on 2017-04-14.
 * Licensed under GPLv3.
 */
public class FileCopyTask {
    private Path     copyFrom;
    private Path     copyTo;
    private Category copyCategory;
    private int      copyTrials;

    public FileCopyTask(Category copyCategory, File fileToCopy) {
        this.copyCategory = copyCategory;
        this.copyFrom     = fileToCopy.toPath();
        this.copyTo       = Paths.get(copyCategory.getDirectory().toString() + File.separator + fileToCopy.getName());
        this.copyTrials   = 0;
    }

    public Path getCopyFromPath() {
        return copyFrom;
    }

    public Path getCopyToPath() {
        return copyTo;
    }

    public Category getCopyCategory() {
        return copyCategory;
    }

    public int getCopyTrials() {
        return copyTrials;
    }

    public FileCopyTask incrementCopyTrials() {
        this.copyTrials++;
        return this;
    }

    @Override
    public String toString() {
        return "FileCopyTask{" +
                "copyFrom=" + copyFrom +
                ", copyTo=" + copyTo +
                ", copyCategory=" + copyCategory +
                ", copyTrials=" + copyTrials +
                '}';
    }
}
