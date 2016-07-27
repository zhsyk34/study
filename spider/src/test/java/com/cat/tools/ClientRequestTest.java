package com.cat.tools;

import com.cat.model.Info;
import com.cat.model.Parse;

import org.junit.Test;

import java.util.List;

/**
 * Created by Archimedes on 2016/7/11.
 */
public class ClientRequestTest {

    @Test
    public void post() throws Exception {

    }

    @Test
    public void get() throws Exception {
        List<Info> list = Parse.parse();
        list.forEach(info -> info.printf());
    }

    @Test
    public void request() throws Exception {
        String url = "http://www.xmrc.com.cn/net/info/showco.aspx?ID=1863213";
        Parse.filter(url, "户口");
    }

}
