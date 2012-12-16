package org.cognitor.server.platform.web.util;

import org.springframework.util.StringUtils;

/**
 * User: patrick
 * Date: 10.12.12
 */
public class UrlUtil {
    public static String createQueryString(String originalRequestQuery) {
        if (StringUtils.hasText(originalRequestQuery)) {
            return "?" + originalRequestQuery;
        }
        return "";
    }
}
