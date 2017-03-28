package com.cat.resolver;

import com.cat.Info;
import com.cat.filter.DocumentFilter;

public interface EnhanceResolver {

    Info getInfo(String url, DocumentFilter filter);
}
