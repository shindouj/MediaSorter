package net.jeikobu.mediasorter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * MediaSorter - created by shindouj on 2017-04-14.
 * Licensed under GPLv3.
 */
@XStreamAlias("Categories")
public class Categories {
    @XStreamImplicit
    private List<Category> list;

    public Categories() {
        list = new ArrayList<>();
    }

    public void add(Category c) {
        list.add(c);
    }

    public List<Category> getList() {
        return list;
    }
}

