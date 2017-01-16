package org.valuereporter.utils;

import org.springframework.web.util.HtmlUtils;

/**
 * Basic XSSFiltering. Shuld be replaced by native XSS filtering in Spring, when it arrives...
 * Created by baardl on 2017-01-16.
 */
public class XSSFilter {

    /**
     * Check if input contains HTML, and thus might be an risk.
     * @param input string to be checked.
     * @return true if potential xss risk
     */
    public static boolean hasXssRisk(String input) {
        boolean isHtml = false;
        if (input != null) {
            if (!input.equals(HtmlUtils.htmlEscape(input))) {
                isHtml = true;
            }
        }
        return isHtml;
    }
}
