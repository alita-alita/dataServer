package cn.idicc.taotie.infrastructment.utils;

import com.google.common.hash.Hashing;
import org.apache.tomcat.util.buf.HexUtils;

import java.nio.charset.StandardCharsets;

public class MessageKeyUtils {

    public static String generateKey(String key) {
        byte[] bytes = Hashing.hmacMd5(key.getBytes(StandardCharsets.UTF_8)).newHasher().hash().asBytes();
        return HexUtils.toHexString(bytes);
    }

}
