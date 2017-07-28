package net.jeikobu.mediasorter.datacontainers;

import net.jeikobu.mediasorter.Category;

import java.io.File;

/**
 * MediaSorter - Created by shindouj on 28/07/2017
 */
public class ParsedVideo implements ParsedFile {

    private Category category;
    private VideoMetadata metadata;
    private File javaFile;

    public ParsedVideo(VideoMetadata metadata, CategorizedFile cf) {
        this.category = cf.getCategory();
        this.metadata = metadata;
        this.javaFile = cf.getFile();
    }


    @Override
    public Category getCategory() {
        return this.category;
    }

    @Override
    public Metadata getMetadata() {
        return this.metadata;
    }

    @Override
    public File getFile() {
        return this.javaFile;
    }

    @Override
    public void renameFileWithMetadataTitle() {
        File renameTo = new File(javaFile.getPath().replace(javaFile.getName(), metadata.getProperFilename()));
    }

    @Override
    public void applyMetadata() {

    }
}
