package com.cat.jsoup;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by Archimedes on 2016-07-27.
 */
public abstract class Filter<E> {
    protected abstract List<E> get(Element element);
}
