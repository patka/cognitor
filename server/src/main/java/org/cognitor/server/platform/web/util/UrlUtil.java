package org.cognitor.server.platform.web.util;

import org.springframework.util.StringUtils;

/**
 * User: patrick
 * Date: 10.12.12
 */
public final class UrlUtil {
    private UrlUtil() {
        //prevent instance creation
    }

    public static String createQueryString(String originalRequestQuery) {
        if (StringUtils.hasText(originalRequestQuery)) {
            return "?" + originalRequestQuery;
        }
        return "";
    }

    public static String appendQueryToUrl(String url, String queryParameter) {
        if (!StringUtils.hasText(url)) {
            throw new IllegalArgumentException("Need an URL to append to.");
        }
        if (!StringUtils.hasText(queryParameter)) {
            return url;
        }
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append(createQueryString(queryParameter));
        return urlBuilder.toString();
    }
}
