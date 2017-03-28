package com.cat.resolver;

import com.cat.Info;
import com.cat.filter.DocumentFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public abstract class AbstractResolver implements Resolver, EnhanceResolver {

    @Override
    public Info getInfo(String url) {
        return this.getInfo(url, null);
    }

    @Override
    public Info getInfo(String url, DocumentFilter filter) {
        Document document = null;
        try {
            document = this.document(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (document == null) {
            return null;
        }
        return parseDocument(document, filter);
    }

    protected abstract Info parseDocument(Document document, DocumentFilter filter, String... words);

//    protected abstract Info parseDocument(Document document, DocumentFilter filter, Set<String> words);

    private Document document(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
