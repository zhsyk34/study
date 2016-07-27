package com.cat.tools;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Archimedes on 2016/7/13.
 */
public class BaseRequest extends Util {

    @Override
    public String get(String url, Map<String, String> headerMap, Map<String, String> paramMap) throws Exception {
        String result;
        if (paramMap != null && !paramMap.isEmpty()) {
            url += super.parseParams(Constant.Method.GET, paramMap);
        }

        URL address = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();

        setHeader(connection, headerMap, true);

        int code = connection.getResponseCode();
        if (code != 200) {
            return super.receive(connection.getErrorStream());
        }

        result = super.receive(connection.getInputStream());
        connection.disconnect();

        return result;
    }

    @Override
    public String post(String url, Map<String, String> headerMap, Map<String, String> paramMap) throws Exception {
        String result;
        URL address = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();

        connection.setRequestMethod(Constant.Method.POST.toString());
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);

        setHeader(connection, headerMap, true);

        if (paramMap != null && !paramMap.isEmpty()) {
            PrintWriter out;
            out = new PrintWriter(connection.getOutputStream());
            out.print(super.parseParams(Constant.Method.POST, paramMap));
            out.flush();
            out.close();
        }

        int code = connection.getResponseCode();
        if (code != 200) {
            return super.receive(connection.getErrorStream());
        }

        result = super.receive(connection.getInputStream());
        connection.disconnect();
        return result;
    }

}
