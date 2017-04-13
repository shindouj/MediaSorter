package net.jeikobu.mediasorter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import net.jeikobu.mediasorter.filters.*;

import javax.naming.Name;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * MediaSorter - Created by shindouj on 2017-04-13
 */

public class MediaSorterMainClass {
    public static void main(String[] args) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(AllTrueFilterGroup.class);
        xstream.processAnnotations(ExtensionFileFilter.class);
        xstream.processAnnotations(NameContainsFileFilter.class);
        xstream.processAnnotations(OneOrMoreFilterGroup.class);
        xstream.processAnnotations(PrefixFileFilter.class);
        xstream.processAnnotations(Category.class);
        OneOrMoreFilterGroup filterGroup = new OneOrMoreFilterGroup(new PrefixFileFilter("test"));
        Category cat = new Category(filterGroup);
        try {
            OutputStream os = new FileOutputStream("dupa.xml");
            xstream.toXML(cat, os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
