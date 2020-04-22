package com.github.gxhunter.util;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wanggx
 * @date 2020-04-19 10:06
 **/
public class ExpressParser {

    private final Pattern fsPattern;

    public ExpressParser() {
        fsPattern = Pattern.compile("\\{\\w+}");
    }

    public ExpressParser(String pattern) {
        this.fsPattern = Pattern.compile(pattern);
    }

    /**
     * 解析所有字符串带有{xx}的变量，并回调填充
     *
     * @param fragment 支持{#now}、{#topic}、{#currentUser}等
     * @param callback 回调表达式解析器
     * @return 解析完的字符串
     */
    public String parser(String fragment, Function<String, String> callback) {
        Matcher m = fsPattern.matcher(fragment);
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = fragment.length(); i < len; ) {
            if (m.find(i)) {
                if (m.start() != i) {
                    sb.append(fragment, i, m.start());
                }
                String express = fragment.substring(m.start(), m.end());
                express = express.replaceAll("[{}]", "");
                sb.append(callback.apply(express));
                i = m.end();
            } else {
                sb.append(fragment.substring(i));
                break;
            }
        }
        return sb.toString();
    }
}
