package com.cat.jsoup;

import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Archimedes on 2016-07-27.
 */
public class Util {

    /**
     * get 方法  组装 参数
     */
    public static String getUrl(String url, Map<String, String> paramMap) {
        StringBuilder params = new StringBuilder();
        if (paramMap != null && !paramMap.isEmpty()) {
            paramMap.forEach((k, v) -> params.append("&" + k + "=" + v));
            params.replace(0, 1, "?");
        }

        return url + params;
    }

    public static <T> List<T> filter(Element element, Filter<T> filterClass) throws IOException {
        return filterClass.get(element);
    }

    /**
     * 过滤条件
     */
    public static boolean isValid(Element element, String keyword) {
        if (element == null) {
            return false;
        }

        String data = element.text();
        String[] words = keyword.split(",");
        for (int i = 0; i < words.length; i++) {
            if (data.contains(words[i])) {
                return true;
            }
        }

        return false;
    }
}
