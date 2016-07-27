package com.cat.tools;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cat.tools.Constant.CHARSET;

/**
 * Created by Archimedes on 2016/7/11.
 */
public class ClientRequest {

    private static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String parseParams(Map<String, String> params) {
        StringBuilder result = new StringBuilder("?");
        params.forEach((k, v) -> {
            try {
                result.append("&" + k + "=" + URLEncoder.encode(v, CHARSET));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        result.replace(1, 2, "");
        return result.toString();
    }

    private static String getResponse(CloseableHttpResponse response) throws IOException {
        if (response == null) {
            return null;
        }
        int code = response.getStatusLine().getStatusCode();
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>" + code);

        if (code == HttpStatus.SC_OK) {
//			System.out.println("response success!");

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, CHARSET);
            }
        }
        return null;
    }

    /*send and receive the result*/
    private static String handle(CloseableHttpClient httpClient, HttpRequestBase request) {
        String result = null;
        CloseableHttpResponse response = null;

//		System.out.println("executing method request : " + request.getURI());

        try {
            response = httpClient.execute(request);
            result = getResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(response);
            close(httpClient);
        }
        return result;
    }

    public static String post(String url, Map<String, String> headerMap, Map<String, String> paramMap) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);

		/*set headers*/
        if (headerMap != null) {
            if (!headerMap.containsKey("Content-Type")) {
                headerMap.put("Content-Type", "application/x-www-form-urlencoded;charset=" + CHARSET);
            }
            headerMap.forEach((k, v) -> {
                Header header = new BasicHeader(k, v);
                request.setHeader(header);
            });
        }

		/*set params*/
        List<NameValuePair> params = new ArrayList<>();
        if (paramMap != null) {
            paramMap.forEach((k, v) -> {
                try {
                    params.add(new BasicNameValuePair(k, URLEncoder.encode(v, CHARSET)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            request.setEntity(new UrlEncodedFormEntity(params, CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return handle(httpClient, request);
    }

    public static String get(String url, Map<String, String> headerMap, Map<String, String> paramMap) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

		/*set params into url*/
        if (paramMap != null) {
            url += parseParams(paramMap);
        }
        HttpGet request = new HttpGet(url);

		/*set headers*/
        if (headerMap != null) {
            headerMap.forEach((k, v) -> {
                Header header = new BasicHeader(k, v);
                request.setHeader(header);
            });
        }

        return handle(httpClient, request);
    }

    public static String request(String url, Constant.Method method, Map<String, String> headerMap, Map<String, String> paramMap) {
        switch (method) {
            case GET:
                return get(url, headerMap, paramMap);
            case POST:
                return post(url, headerMap, paramMap);
        }
        return null;
    }
}
