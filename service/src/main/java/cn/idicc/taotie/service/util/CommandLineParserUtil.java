package cn.idicc.taotie.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandLineParserUtil {

    /**
     * 分词函数：将命令行参数字符串按照空白字符分割，
     * 但如果空白字符位于成对引号（单引号或双引号）之间，则视为一个整体。
     *
     * @param input 命令行参数字符串
     * @return 分割后的 token 列表
     */
    private static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuote = false;
        char quoteChar = 0;  // 记录当前处于哪种引号内

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (inQuote) {
                if (c == quoteChar) {
                    // 结束引号，不把引号字符加入 token
                    inQuote = false;
                } else {
                    sb.append(c);
                }
            } else {
                // 如果遇到引号，开始一个新的引号区间
                if (c == '\'' || c == '"') {
                    inQuote = true;
                    quoteChar = c;
                } else if (Character.isWhitespace(c)) {
                    // 如果遇到空白字符且 sb 中有内容，则作为一个 token 加入列表
                    if (sb.length() > 0) {
                        tokens.add(sb.toString());
                        sb.setLength(0);
                    }
                } else {
                    sb.append(c);
                }
            }
        }
        // 添加最后一个 token（如果有）
        if (sb.length() > 0) {
            tokens.add(sb.toString());
        }
        return tokens;
    }

    /**
     * 解析命令行参数字符串，将其转换为 Map。
     * Map 的 key 为参数名（例如 "-a"），value 为 List<String>，包含该参数所有的值；
     * 如果参数后没有跟随值，则存入 null（也可以根据需求修改为 "true"）。
     *
     * @param argsStr 命令行参数字符串，例如 "-a '2015-02-07 11:12:55' -b -c 718"
     * @return 解析后的 Map
     */
    public static Map<String, List<String>> parseArgs(String argsStr) {
        Map<String, List<String>> result = new HashMap<>();
        if (argsStr == null || argsStr.trim().isEmpty()) {
            return result;
        }

        // 使用自定义的 tokenize 方法处理引号内的空格问题
        List<String> tokens = tokenize(argsStr.trim());

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            // 如果 token 以 "-" 开头，则认为它是参数 key
            if (token.startsWith("-")) {
                String key = token.substring(1);
                result.putIfAbsent(key, new ArrayList<>());
                // 判断下一个 token 是否存在且不以 "-" 开头
                if (i + 1 < tokens.size() && !tokens.get(i + 1).startsWith("-")) {
                    result.get(key).add(tokens.get(i + 1));
                    i++;  // 跳过作为 value 的 token
                } else {
                    // 如果没有跟随的 value，则存入 null 或其他标记值
                    result.get(key).add(null);
                }
            }
        }
        return result;
    }

    // 测试 main 方法
    public static void main(String[] args) {
        String input1 = "-a '2015-02-07 11:12:55' -b -c 718";
        Map<String, List<String>> parsedArgs1 = parseArgs(input1);
        System.out.println("输入: " + input1);
        for (Map.Entry<String, List<String>> entry : parsedArgs1.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        System.out.println("-----");

        String input2 = "-a 1 -a 1 -b -c 718";
        Map<String, List<String>> parsedArgs2 = parseArgs(input2);
        System.out.println("输入: " + input2);
        for (Map.Entry<String, List<String>> entry : parsedArgs2.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
