package net.jeikobu.mediasorter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import net.jeikobu.mediasorter.filters.PrefixFileFilter;

/**
 * Created by j.strzyzewski.ext on 2017-04-13.
 */
public class MediaSorterMainClass {
    public static void main(String[] args) {
        Category cat = new Category(new PrefixFileFilter("test"));
        XStream xstream = new XStream(new StaxDriver());

        xstream.alias("PrefixFileFilter", PrefixFileFilter.class);
        xstream.alias("Category", Category.class);

        System.out.println(xstream.toXML(cat));
    }

}
