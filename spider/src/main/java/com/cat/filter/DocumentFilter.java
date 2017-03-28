package com.cat.filter;

import java.util.Set;

public interface DocumentFilter {

    boolean filter(String document, String... words);

    boolean filter(String document, Set<String> words);
}
