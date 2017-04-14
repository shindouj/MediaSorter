package net.jeikobu.mediasorter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import net.jeikobu.mediasorter.exceptions.DoesNotBelongToThisCategory;
import net.jeikobu.mediasorter.exceptions.MoreThanOneCategoryException;
import net.jeikobu.mediasorter.exceptions.MoreThanOneSubcategoryException;
import net.jeikobu.mediasorter.filters.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * MediaSorter - Created by shindouj on 2017-04-13
 */

public class MediaSorterMainClass {
    public static void main(String[] args) throws MoreThanOneCategoryException {
        XStream xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(AllTrueFilterGroup.class);
        xstream.processAnnotations(ExtensionFileFilter.class);
        xstream.processAnnotations(NameContainsFileFilter.class);
        xstream.processAnnotations(OneOrMoreFilterGroup.class);
        xstream.processAnnotations(PrefixFileFilter.class);
        xstream.processAnnotations(Category.class);
        xstream.processAnnotations(Categories.class);
        xstream.registerConverter(new PathConverter());

        Categories categories = new Categories();

        try {
            categories = (Categories) xstream.fromXML(new FileInputStream("filter.xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        File directory = new File("C:\\test");
        for (File f: directory.listFiles()) {
            List<Path> pathsToCopy = getListOfPathsToCopy(categories, f);
            if (pathsToCopy.size() != 1) {
                throw new MoreThanOneCategoryException(f);
            } else {
                try {
                    Files.move(f.toPath(), pathsToCopy.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static List<Path> getListOfPathsToCopy(Categories cats, File f) {
        List<Path> returnList = new ArrayList<>();
        for (Category c: cats.getList()) {
            try {
                returnList.add(Paths.get(c.whichCategoryBelongsTo(f).getDirectory().toString()
                        + File.separator + f.getName()));
            } catch (MoreThanOneSubcategoryException e) {
                e.printStackTrace();
                break;
            } catch (DoesNotBelongToThisCategory e) {
                continue;
            }
        }
        return returnList;
    }

}
