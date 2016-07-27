package com.cat.tools;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Archimedes on 2016/7/13.
 */
public class BaseRequestTest {

    private static final String url = "http://www.xmrc.com.cn/net/info/showco.aspx";

    @Test
    public void get() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("id", "1804873");
        String result = new BaseRequest().get(url, null, params);
        System.out.println(result);
    }

    @Test
    public void post() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("id", "1804873");
        String result = new BaseRequest().post(url, null, params);
        System.out.println(result);
    }

}
