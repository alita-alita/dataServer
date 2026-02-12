package cn.idicc.taotie.infrastructment.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.Week;
import cn.idicc.common.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @author wangzi
 */
public class DateUtil {

    /**
     * 一天的秒数
     */
    public static final int DAY_SECONDS = 60 * 60 * 24;

    /**
     * 一天的毫秒数
     */
    public static final long DAY_MILLI_SECONDS = 1000L * DAY_SECONDS;

    /**
     * UTC TIME ZONE
     */
    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     * 获取指定类型日期的开始时间和结束时间字符（字符格式yyyy-MM-dd HH:mm:ss）
     *
     * @param dateType
     */
//    public static Map<String, String> getDateStrByType(Integer dateType) {
//        Map<String, String> map = new LinkedHashMap<>();
//        if (Objects.isNull(RecommendDateTypeEnum.getByCode(dateType))) {
//            throw new BizException("传入推荐日期类型有误");
//        }
//        LocalDate now = LocalDate.now();
//        if (RecommendDateTypeEnum.DAY.getCode().equals(dateType)) {
//            String nowStr = now.toString();
//            map.put("startDate", nowStr);
//            map.put("endDate", nowStr);
//
//        } else if (RecommendDateTypeEnum.WEEK.getCode().equals(dateType)) {
//            map.put("startDate", now.plusWeeks(-1).toString());
//            map.put("endDate", now.toString());
//
//        } else if (RecommendDateTypeEnum.MONTH.getCode().equals(dateType)) {
//            map.put("startDate", now.plusMonths(-1).toString());
//            map.put("endDate", now.toString());
//
//        } else if (RecommendDateTypeEnum.HALF_A_YEAR.getCode().equals(dateType)) {
//            map.put("startDate", now.plusMonths(-6).toString());
//            map.put("endDate", now.toString());
//        }
//        return map;
//    }
//
//    /**
//     * 获取指定类型日期的开始时间戳和结束时间戳
//     *
//     * @param dateType
//     */
//    public static Map<String, Long> getDateStampByType(Integer dateType) {
//        Map<String, Long> map = new LinkedHashMap<>();
//        if (Objects.isNull(RecommendDateTypeEnum.getByCode(dateType))) {
//            throw new BizException("传入推荐日期类型有误");
//        }
//        LocalDate now = LocalDate.now();
//        Long endTimeStamp = getTimestamp(now.plusDays(1).atStartOfDay()) - 1;
//        if (RecommendDateTypeEnum.DAY.getCode().equals(dateType)) {
//            map.put("startDate", getTimestamp(now.atStartOfDay()));
//            map.put("endDate", endTimeStamp);
//
//        } else if (RecommendDateTypeEnum.WEEK.getCode().equals(dateType)) {
//            map.put("startDate", getTimestamp(now.plusWeeks(-1).atStartOfDay()));
//            map.put("endDate", endTimeStamp);
//
//        } else if (RecommendDateTypeEnum.MONTH.getCode().equals(dateType)) {
//            map.put("startDate", getTimestamp(now.plusMonths(-1).atStartOfDay()));
//            map.put("endDate", endTimeStamp);
//
//        } else if (RecommendDateTypeEnum.HALF_A_YEAR.getCode().equals(dateType)) {
//            map.put("startDate", getTimestamp(now.plusMonths(-6).atStartOfDay()));
//            map.put("endDate", endTimeStamp);
//
//        } else if (RecommendDateTypeEnum.YEAR.getCode().equals(dateType)) {
//            map.put("startDate", getTimestamp(now.plusYears(-1).atStartOfDay()));
//            map.put("endDate", endTimeStamp);
//        }
//        return map;
//    }

    /**
     * 获取指定天数的日期年份字符集合，字符结构为yyyy
     * 示例：假设传入参数为3，则计算除当前日期外往前推算3年的日期年份[2020,2021,2022]
     *
     * @param num
     * @return
     */
    public static List<String> getSpecifyDateYearStr(Integer num) {
        List<String> result = CollectionUtil.newArrayList();
        Assert.notNull(num, "传入数字不能为空");
        Assert.isTrue(num > 0, "传入数字必须大于0");
        LocalDate now = LocalDate.now();
        for (int i = num; i > 0; i--) {
            LocalDate calculateLocalDate = now.plusYears(-i);
            result.add(String.valueOf(calculateLocalDate.getYear()));
        }
        return result;
    }

    /**
     * 获取指定月数的日期字符集合，字符结构为yyyy-MM
     * 示例：假设传入参数为3，则计算除当前日期外往前推算3月的日期[2023-03,2023-04,2023-05]
     *
     * @param num
     * @return
     */
    public static List<LocalDateTime> getSpecifyDateMonth(Integer num) {
        List<LocalDateTime> result = CollectionUtil.newArrayList();
        Assert.notNull(num, "传入数字不能为空");
        Assert.isTrue(num > 0, "传入数字必须大于0");
        LocalDateTime now = LocalDateTime.now();
        for (int i = num; i > 0; i--) {
            LocalDateTime calculateLocalDateTime = now.plusMonths(-i).toLocalDate().atStartOfDay().with(TemporalAdjusters.firstDayOfMonth());
            result.add(calculateLocalDateTime);
        }
        return result;
    }

    /**
     * 将指定字符转化成localDateTime
     *
     * @param str
     * @param datePattern
     * @return
     */
    public static LocalDateTime Str2LocalDateTime(String str, String datePattern) {
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(datePattern));
    }

    /**
     * 将localDateTime转化成指定格式字符
     *
     * @param localDateTime
     * @param dateTimeFormatter
     * @return
     */
    public static String localDateTime2Str(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 校验日期格式是否正确
     *
     * @param dateStr
     * @param pattern
     * @return true-正确；false-不正确
     */
    public static boolean valid(String dateStr, String pattern) {
        try {
            org.apache.commons.lang3.time.DateUtils.parseDate(dateStr, pattern);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 解析日期字符串
     *
     * @param dateStr
     * @param pattern
     * @return Date
     */
    public static Date parse(String dateStr, String pattern) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(dateStr, pattern);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 格式化日期，日期格式默认：yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return DateFormatUtils.format(date, DatePattern.NORM_DATE_PATTERN);
    }

    /**
     * 格式化日期，日期格式默认：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        return DateFormatUtils.format(date, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 格式化日期
     *
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期字符串转换成特定日期格式的字符串
     * <p>
     * 例如：20200505 转换成 2020-05-05 从 20200505121214 转换成 2020-050-51 21:21:43
     *
     * @param dateStr       日期字符串
     * @param sourcePattern 原始日期格式
     * @param targetPattern 目标日期格式
     * @return
     */
    public static String convert(String dateStr, String sourcePattern, String targetPattern) {
        return format(parse(dateStr, sourcePattern), targetPattern);
    }

    /**
     * 将时间转换成Long型
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return Long型日期
     */
    public static Long convert(Date date, String pattern) {
        return Long.valueOf(format(date, pattern));
    }

    /**
     * LocaleTime 转换为Date
     *
     * @param localTime
     * @return
     */
    public static Date convert(LocalDate nowDate, LocalTime localTime) {
        Date date = null;
        if (localTime != null) {
            LocalDateTime localDateTime = LocalDateTime.of(nowDate, localTime);
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            date = Date.from(instant);
        }
        return date;
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static LocalDateTime nowTime() {
        return LocalDateTime.now();
    }


    /**
     * 获取UTC的时间(单位:毫秒)
     *
     * @return
     */
    public static long utcMilliseconds() {
        return DateTime.now().millisecond();
    }

    /**
     * 获取UTC的时间(单位:秒)
     *
     * @return
     */
    public static int utcSeconds() {
        return (int) (utcMilliseconds() / 1000);
    }

    /**
     * 获取UTC的时间(单位:秒)
     *
     * @return
     */
    public static long utcSeconds(DateTime dateTime) {
        return dateTime.millisecond() / 1000;
    }

    /**
     * 获取当前时间毫秒数
     *
     * @return 当前时间毫秒数
     */
    public static Long currentMilliseconds() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前月
     *
     * @return
     */
    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    /**
     * 获取当前时间秒数
     *
     * @return 当前时间秒数
     */
    public static Integer currentSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * LocalDate To Date
     *
     * @param localDate
     * @return
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime To Date
     *
     * @param localDateTime
     * @return
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date To LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * dateStr To LocalDate
     *
     * @param dateStr 日期字符串，格式为yyyyMMdd
     * @return
     */
    public static LocalDate asLocalDate(String dateStr) {
        return asLocalDate(parse(dateStr, DatePattern.PURE_DATE_PATTERN));
    }

    /**
     * dateStr To LocalDate
     *
     * @param dateStr     日期字符串，
     * @param datePattern 日期字符格式
     * @return
     */
    public static LocalDate asLocalDate(String dateStr, String datePattern) {
        return asLocalDate(parse(dateStr, datePattern));
    }

    /**
     * timestamp To LocalDate
     *
     * @param timestamp 时间戳
     * @return
     */
    public static LocalDate asLocalDate(Long timestamp) {
        return asLocalDateTime(timestamp).toLocalDate();
    }

    /**
     * Date To LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * timestamp To LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime asLocalDateTime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return
     */
    public static Long getTimestamp() {
        return getTimestamp(LocalDateTime.now());
    }


    /**
     * 获取指定日期的时间戳
     *
     * @param date 日期
     * @return
     */
    public static Long getTimestamp(Date date) {
        return getTimestamp(asLocalDateTime(date));
    }

    /**
     * 获取指定时间的时间戳
     *
     * @param localDateTime 时间
     * @return
     */
    public static Long getTimestamp(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    /**
     * 获取指定时间的时间戳
     *
     * @param localDate 时间
     * @return
     */
    public static Long getTimestamp(LocalDate localDate) {
        return getTimestamp(localDate.atStartOfDay());
    }

    /**
     * 获取当前时间截取的时间毫秒数，精确度到DATE
     */
    public static Long getTruncatedTimestamp() {
        return getTruncatedTimestamp(new Date(), Calendar.DATE);
    }

    /**
     * 获取指定日期截取的时间毫秒数
     *
     * @param date 指定日期
     * @return field 精确的粒度，例如：Calendar.DATE
     */
    public static Long getTruncatedTimestamp(Date date, int field) {
        return org.apache.commons.lang3.time.DateUtils.truncate(date, field).getTime();
    }

    /**
     * 获取指定日期的开始时间戳
     *
     * @return
     */
    public static Long getBeginTimestampOfDay(Date date) {
        String dateStr = cn.hutool.core.date.DateUtil.format(date, DatePattern.PURE_DATE_PATTERN);
        if (dateStr == null) {
            return null;
        }
        Date targetDate = cn.hutool.core.date.DateUtil.parse(dateStr, DatePattern.PURE_DATE_PATTERN);
        return targetDate.getTime();
    }

    /**
     * 获取获取指定日期下一天的开始时间戳
     *
     * @return
     */
    public static Long getBeginTimestampOfNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return getBeginTimestampOfDay(calendar.getTime());
    }

    /**
     * 获取指定日期的开始时间
     *
     * @param date
     * @return
     */
    public static Date getBeginOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的结束时间
     *
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取指定日期所在月份的第一天
     *
     * @param date 日期
     * @return 该日期所在月份第一天
     */
    public static Date getFirstOfDayMonth(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int first = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, first);
        // 设置时间为第1秒
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取指定日期所在月份的最后一天
     *
     * @param date 日期
     * @return 该日期所在月份的最后一天
     */
    public static Date getLastOfDayMonth(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        // 设置时间为最后1秒
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取指定时间所在月份的第一天0点
     *
     * @param timestamp 时间戳
     * @return
     */
    public static LocalDateTime getFirstOfDayMonth(Long timestamp) {
        //默认本月
        if (timestamp == null) {
            return getFirstOfDayMonth(currentMilliseconds());
        }
        return asLocalDateTime(getTruncatedTimestamp(new Date(timestamp), Calendar.MONTH));
    }

    /**
     * 获取指定时间下个月份的第一天0点
     *
     * @param timestamp 时间戳
     * @return
     */
    public static LocalDateTime getFirstOfDayNextMonth(Long timestamp) {
        //默认为当前时间
        if (timestamp == null) {
            return getFirstOfDayNextMonth(currentMilliseconds());
        }
        Date nextMonthDay = org.apache.commons.lang3.time.DateUtils.addMonths(new Date(timestamp), 1);
        return asLocalDateTime(getTruncatedTimestamp(nextMonthDay, Calendar.MONTH));
    }

    /**
     * 获取指定日期所在月份的第一天日期
     *
     * @param date 格式：201809
     * @return 格式：2018-09-01
     */
    public static String getFirstDayOfMonth(String date) {
        return asLocalDate(date).toString();
    }

    /**
     * 获取指定日期所在月份的下个月第一天的日期
     *
     * @param date 格式：201809
     * @return 格式：2018-10-01
     */
    public static String getFirstDayOfNextMonth(String date) {
        return asLocalDate(date).with(TemporalAdjusters.lastDayOfMonth()).minusDays(-1).toString();
    }

    /**
     * 获取昨日月份的第一天到昨天的日期集合
     * <p>
     * 例如：
     * 1、今天是20200210，集合返回20200201-20200209
     * 2、今天是20200301，集合返回20200201-20200229
     */
    public static Set<String> getDateSetByLastDay() {
        Set<String> set = new LinkedHashSet<>();
        int day = LocalDate.now().getDayOfMonth();
        int nowDay = 0;
        if (day == 1) {
            nowDay = LocalDate.now().plusDays(-1).lengthOfMonth();
            for (int i = nowDay; i >= 1; i--) {
                int d = Integer.valueOf("-" + i);
                set.add(LocalDate.now().plusDays(d).toString());
            }
        } else {
            nowDay = LocalDate.now().getDayOfMonth();
            for (int i = nowDay - 1; i >= 1; i--) {
                int d = Integer.valueOf("-" + i);
                set.add(LocalDate.now().plusDays(d).toString());
            }
        }

        return set;
    }

    /**
     * 获取指定日期在当前周里的索引
     * 说明：1~7 对应 周一~周日
     *
     * @param date
     * @return
     */
    public static int getWeekDayIndex(Date date) {
        int i = DateTime.of(date).dayOfWeek();
        i--;
        return i == 0 ? 7 : i;
    }

    /**
     * 把英文类型的星期转为中文
     *
     * @param localDate
     * @return
     */
    public static String getChineseWeek(LocalDate localDate) {
        switch (localDate.getDayOfWeek()) {
            case MONDAY:
                return Week.MONDAY.toChinese();
            case TUESDAY:
                return Week.TUESDAY.toChinese();
            case WEDNESDAY:
                return Week.WEDNESDAY.toChinese();
            case THURSDAY:
                return Week.THURSDAY.toChinese();
            case FRIDAY:
                return Week.FRIDAY.toChinese();
            case SATURDAY:
                return Week.SATURDAY.toChinese();
            case SUNDAY:
                return Week.SUNDAY.toChinese();
            default:
                return StringUtils.EMPTY;
        }
    }


    /**
     * 根据生日计算当前周岁数
     *
     * @param birthday 生日的年月日 例如 1992-08-06
     * @param pattern  日期格式
     * @return 参数为空时候, 默认0岁, 否则按照实际计算周岁
     */
    public static Integer getCurrentAge(String birthday, String pattern) {
        if (birthday == null) {
            return null;
        }
        try {
            Date date = parse(birthday, pattern);
            return getCurrentAge(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据生日计算当前周岁数
     *
     * @param birthday 生日的系统毫秒数
     * @return 参数为空时候, 默认0岁, 否则按照实际计算周岁
     */
    public static Integer getCurrentAge(Long birthday) {
        if (birthday == null) {
            return null;
        }
        return getCurrentAge(asLocalDateTime(birthday));
    }

    /**
     * 根据生日计算当前周岁数
     *
     * @param birthday 生日的LocalD阿特Ti么
     * @return 参数为空时候, 默认0岁, 否则按照实际计算周岁
     */
    public static Integer getCurrentAge(LocalDateTime birthday) {
        return getCurrentAge(asDate(birthday), null);
    }

    /**
     * 计算生日与指定日期的年龄差，周岁数
     *
     * @param birthday  生日日期
     * @param referDate 指定日期
     * @return
     */
    public static Integer getCurrentAge(LocalDateTime birthday, LocalDateTime referDate) {
        return getCurrentAge(asDate(birthday), asDate(referDate));
    }

    /**
     * 根据生日计算当前周岁数
     *
     * @param birthday 生日的系统毫秒数
     * @return 参数为空时候, 默认0岁, 否则按照实际计算周岁
     */
    public static Integer getCurrentAge(Date birthday) {
        return getCurrentAge(birthday, null);
    }

    /**
     * 计算生日与指定日期的年龄差，周岁数
     *
     * @param birthday  生日日期
     * @param referDate 指定日期
     * @return
     */
    public static Integer getCurrentAge(Date birthday, Date referDate) {
        if (birthday == null) {
            return null;
        }
        // 参照时间
        Calendar curr = Calendar.getInstance();
        if (referDate != null) {
            curr.setTime(referDate);
        }
        // 生日
        Calendar born = Calendar.getInstance();
        born.setTime(birthday);
        // 年龄 = 当前年 - 出生年
        int age = curr.get(Calendar.YEAR) - born.get(Calendar.YEAR);
        if (age <= 0) {
            return 0;
        }
        // 如果当前月份小于出生月份: age-1
        // 如果当前月份等于出生月份, 且当前日小于出生日: age-1
        int currMonth = curr.get(Calendar.MONTH);
        int currDay = curr.get(Calendar.DAY_OF_MONTH);
        int bornMonth = born.get(Calendar.MONTH);
        int bornDay = born.get(Calendar.DAY_OF_MONTH);
        if ((currMonth < bornMonth) || (currMonth == bornMonth && currDay < bornDay)) {
            age--;
        }
        return age < 0 ? 0 : age;
    }

    /**
     * 获取指定时间到当前时间的天数
     *
     * @param timestamp 起始时间戳
     * @return
     */
    public static Integer getBetweenDays(Long timestamp) {
        return getBetweenDays(timestamp, getTruncatedTimestamp());
    }

    /**
     * 获取两个时间点之间的天数
     *
     * @param startTimestamp 起始时间戳
     * @param endTimestamp   结束时间戳
     * @return
     */
    public static Integer getBetweenDays(Long startTimestamp, Long endTimestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(startTimestamp));
        LocalDate startDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(new Date(endTimestamp));
        LocalDate endDate = LocalDate.of(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH) + 1, endCalendar.get(Calendar.DATE));

        return getBetweenDays(startDate, endDate);
    }

    /**
     * 获取两个时间点之间的天数
     *
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @return
     */
    public static Integer getBetweenDays(LocalDate startDate, LocalDate endDate) {
        Long count = startDate.toEpochDay() - endDate.toEpochDay();
        return count.intValue() + 1;
    }

    /**
     * 是否为上午
     *
     * @param date 日期
     * @return 是否为上午
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public static boolean isAM(Date date) {
        return cn.hutool.core.date.DateUtil.isAM(date);
    }

    /**
     * 是否为下午
     *
     * @param date 日期
     * @return 是否为下午
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public static boolean isPM(Date date) {
        return cn.hutool.core.date.DateUtil.isPM(date);
    }

    /**
     * 获取当前日期以后N天日期
     * <p>
     * 如：2018-08-07，3天后日期为 2018-08-10
     */
    public static Date plusDays(int days) {
        return plusDays(asDate(LocalDate.now()), days);
    }

    /**
     * 获取指定日期以后N天日期
     * <p>
     * 如：2018-08-07，3天后日期为 2018-08-10
     */
    public static Date plusDays(Date date, int days) {
        LocalDate localDate = date
                .toInstant()
                .atZone(ZoneId.systemDefault()).plusDays(days)
                .toLocalDate();
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 获取指定日期以后N天日期,时间不置为0点0分0秒
     * <p>
     * 如：2018-08-07，3天后日期为 2018-08-10
     */
    public static Date plusDays2(int days) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, days);
        return instance.getTime();
    }

    public static Date plus(int field, int amount) {
        Calendar instance = Calendar.getInstance();
        instance.add(field, amount);
        return instance.getTime();
    }

    /**
     * 当前时间经过固定天数后的时间戳
     *
     * @param days
     * @return
     */
    public static Long getPlusDaysEpoch(int days) {
        return LocalDateTime.now().plusDays(days).toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
    }

    /**
     * 当前时间经过固定秒数后的时间戳
     *
     * @param seconds
     * @return
     */
    public static Long getPlusSecondsEpoch(long seconds) {
        return LocalDateTime.now().plusSeconds(seconds).toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
    }

    /**
     * 将秒数转换为日时，
     *
     * @param second
     * @return
     */
    public static String secondToTime(long second) {
        long days = second / 86400;            //转换天数
        second = second % 86400;            //剩余秒数
        long hours = second / 3600;            //转换小时
        if (days > 0) {
            return days + "天" + hours + "时";
        } else {
            return hours + "时";
        }
    }

    /**
     * 获取指定日期所在月份的第一天
     */
    public static Date getFirstDayOfMonth(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int first = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, first);
        // 设置时间为第1秒
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取指定日期所在月份的最后一天
     */
    public static Date getLastDayOfMonth(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        // 设置时间为最后1秒
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取指定日期所在下月份的第一天
     */
    public static Date getFirstDayOfNextMonth(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        final int first = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, first);
        // 设置时间为第1秒
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取指定日期所在下月份的最后一天
     */
    public static Date getLastDayOfNextMonth(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        // 设置时间为最后1秒
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取距今前N年的日期
     *
     * @param year
     * @return
     */
    public static String getFrontYear(Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -year);
        return DateUtil.format(calendar.getTime());
    }

    /**
     * 获取距今前N年的日期
     *
     * @param year
     * @return
     */
    public static Long getFrontYearTimestamp(Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -year);
        return calendar.getTime().getTime();
    }

    /**
     * 获取当前年的时间戳
     * @return
     */
    public static long getCurrentYearTime(){
        String date = DateUtils.getCurrentYear()+"-01-01";
        return DateUtils.parseDate(date,"yyyy-MM-dd").getTime();
    }

    /**
     * 获取前5年的时间戳
     * @return
     */
    public static long getFrontYearTime(Integer frontYear){
        String date = (DateUtils.getCurrentYear() - frontYear)+"-01-01";
        return DateUtils.parseDate(date,"yyyy-MM-dd").getTime();
    }

    public static java.sql.Date toSQLDate(String text) {
        LocalDate localDate = LocalDate.parse(text, dtf);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth(), 0, 0, 0);
        return new java.sql.Date(calendar.getTimeInMillis());
    }

}
