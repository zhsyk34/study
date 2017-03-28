package com.cat;

import com.cat.tools.ClientRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parse {

    private static final String GETURL = "http://www.xmrc.com.cn/net/info/";
    private static final String RESULT = "Resultg.aspx";
    //	private static final String FILTERWORD = "户口,弹性,调休";
    private static final String FILTERWORD = "户口,挂靠";

    public static List<Info> parse() {
        List<Info> list = new ArrayList<>();

        int index = 0;
        for (int pageIndex = 1; pageIndex <= 50; pageIndex++) {
            Elements trs = getRows(pageIndex);
            if (trs == null || trs.size() == 0) {
                break;
            }

            Info info;
            for (Element tr : trs) {
                Elements tds = tr.select("td");
                if (tds.size() == 0) {
                    continue;
                }

                String link = tds.get(1).select("a").attr("href").trim();
                link = GETURL + link;

                if (!filter(link, FILTERWORD)) {
                    continue;
                }

                String job = tds.get(1).text().trim();
                String company = tds.get(2).text().trim();

                String salary = tds.get(4).text().trim();
                String place = tds.get(3).text().trim();

                info = new Info(index++, company, job, link, place, salary);
                System.out.println(index);
                list.add(info);
            }
        }
        return list;
    }

    public static Elements getRows(int pageIndex) {

        Map<String, String> map = new HashMap<>();
        map.put("keyword", "java");
        map.put("searchtype", "3");
        map.put("PageSize", "100");
        //page
        map.put("PageIndex", Integer.toString(pageIndex));

        String result = ClientRequest.get(GETURL + RESULT, null, map);
        Document document = Jsoup.parse(result);
        Element wrap = document.getElementById("ctl00$Body$JobRepeaterPro_main_div");
        Elements tables = wrap.select("table");
        Element table = tables.get(1);

        return table.select("tr");
    }

    public static boolean filter(String link, String keyword) {
        String html = ClientRequest.get(link, null, null);

        Document document = Jsoup.parse(html);

        Elements wraps = document.select("#container > table");

        if (wraps.size() < 1) {
            return false;
        }
        Element wrap = wraps.get(1);
        Element td = wrap.select("> tbody > tr > td").last();//:eq(2)

        String data = td.text();
        String[] words = keyword.split(",");
        for (int i = 0; i < words.length; i++) {
            if (data.contains(words[i])) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {

    }
}
