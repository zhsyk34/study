package com.cat.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Archimedes on 2016-07-27.
 */
public class Test {

    private static final String GETURL = "http://www.xmrc.com.cn/net/info/";
    private static final String RESULT = "Resultg.aspx";
    private static final String KEYWORD = "户口,落户,挂靠";
//    private static final String KEYWORD = "湖滨南路,湖滨北路,湖滨西路,湖滨中路,鹭江道,厦禾路,公园东路,思明北路,思明南路,筼筜路";
    //    private static final String KEYWORD = "斗西路,故宫路,角滨路,白鹭洲路,后滨路,后埭溪路";
//    private static final String KEYWORD = "弹性,调休,远程办公";

    private static String keyword = "java";
    private static String searchType = "3";
    private static int pageSize = 10;
    private static int pageMax = 5;

    public static void main(String[] args) throws IOException, InterruptedException {

        ExecutorService service = Executors.newCachedThreadPool();

        List<Future<List<String>>> futures = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(pageMax);

        for (int i = 1; i <= pageMax; i++) {
            final int k = i;
            Future<List<String>> future = service.submit(() -> {
                return search(k, latch);
            });
            futures.add(future);
        }
        latch.await();
        System.out.println("all finished...");
        futures.forEach(f -> {
            try {
                List<String> strings = f.get();
                strings.forEach(s -> System.out.println(s));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        service.shutdown();
    }

    public static List<String> search(int pageNo, CountDownLatch latch) {
        Map<String, String> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("searchtype", searchType);
        map.put("PageSize", Integer.toString(pageSize));
        map.put("PageIndex", Integer.toString(pageNo));

        String url = Util.getUrl(GETURL + RESULT, map);

        Document document;
        List<String> urls = null;
        try {
            document = Jsoup.connect(url).get();
            urls = Util.filter(document, new Filter<String>() {
                List<String> list = new ArrayList<>();

                @Override
                protected List<String> get(Element element) {
                    Element wrap = element.getElementById("ctl00$Body$JobRepeaterPro_main_div");
                    Elements tables = wrap.select("table");
                    Element table = tables.get(1);
                    Elements trs = table.select("tr");

                    for (Element tr : trs) {
                        Elements tds = tr.select("td");
                        if (tds.size() == 0) {
                            continue;
                        }

                        String href = tds.get(1).select("a").attr("href").trim();
                        String link = GETURL + href;

                        try {
                            Document jobDoc = Jsoup.connect(link).get();
                            Elements jobTables = jobDoc.select("#container > table");

                            if (jobTables.size() < 1) {
                                continue;
                            }
                            Element jobTable = jobTables.get(1);
                            Element jobTd = jobTable.select("> tbody > tr > td").last();
                            if (Util.isValid(jobTd, KEYWORD)) {
                                //count++;
                            } else {
                                continue;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String company = tds.get(2).text().trim();

                    /*String job = tds.get(1).text().trim();
                    String salary = tds.get(4).text().trim();
                    String place = tds.get(3).text().trim();*/

                        list.add(company + "\t" + link);
                    }

                    return list;
                }
            });
        } catch (IOException e) {
            latch.countDown();
            e.printStackTrace();
        }

        System.out.println("thread : " + pageNo + "finished...");
        latch.countDown();
        //urls.forEach(link -> System.out.println(link));
        return urls;

    }
}
