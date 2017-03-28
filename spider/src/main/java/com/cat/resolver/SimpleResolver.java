package com.cat.resolver;

import com.cat.Info;
import com.cat.filter.DocumentFilter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SimpleResolver extends AbstractResolver {
    @Override
    protected Info parseDocument(Document document, DocumentFilter filter, String... words) {
        Elements elements = document.select("#container > table");

        if (elements.size() < 1) {
            return null;
        }
        Element table = elements.get(1);
        Element dataElement = table.select("> tbody > tr > td").last();
        String data = dataElement.text();
        if (filter != null && !filter.filter(data, words)) {
            return null;
        }
        Elements target = dataElement.select("table[style=table-layout:fixed;word-wrap:break-word;]");
        if (target == null) {
            return null;
        }
        System.out.println(target);

        String job = target.select("div [id^=subject] > a > h1").text();

        System.err.println(job);
        return new Info().setJob(job);
    }
}
