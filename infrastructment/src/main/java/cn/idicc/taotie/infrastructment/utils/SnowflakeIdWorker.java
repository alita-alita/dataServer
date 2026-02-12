package cn.idicc.taotie.infrastructment.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class SnowflakeIdWorker {

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);

    public static Long nextId() {
        return SNOWFLAKE.nextId();
    }
}
