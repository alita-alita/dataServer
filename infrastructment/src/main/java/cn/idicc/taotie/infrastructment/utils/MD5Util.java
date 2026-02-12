package cn.idicc.taotie.infrastructment.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static String getMd5Id(String input) {
        try {
            // 获取 MessageDigest 实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 更新输入字节数组
            md.update(input.getBytes());

            // 获取最终的哈希值
            byte[] digest = md.digest();

            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                // 将每个字节转为十六进制表示
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString(); // 返回十六进制字符串
        } catch (NoSuchAlgorithmException e) {
            // 如果算法不可用，打印异常并返回空字符串
            e.printStackTrace();
            return "";
        }
    }
}
