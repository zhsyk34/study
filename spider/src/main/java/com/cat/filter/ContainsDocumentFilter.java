package com.cat.filter;

import java.util.Set;

public class ContainsDocumentFilter implements DocumentFilter {
    @Override
    public boolean filter(String document, String... words) {
        for (String word : words) {
            if (document.contains(word)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean filter(String document, Set<String> words) {
        return words == null || words.size() == 0 && this.filter(document, words.toArray(new String[words.size()]));
    }
}
