package net.jeikobu.mediasorter;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by j.strzyzewski.ext on 2017-04-14.
 */
public class PathConverter extends AbstractSingleValueConverter {
    @Override
    public boolean canConvert(Class type) {
        return type.equals(Path.class);
    }

    @Override
    public Object fromString(String str) {
        return Paths.get(str);
    }

    public String toString(Object obj) {
        return ((Path)obj).toString();
    }
}
