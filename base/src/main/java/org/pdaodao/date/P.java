package org.pdaodao.date;


import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class P {

    /**
     * Instant和 Date 一样,它是时区无关的,即是输出的结果始终格林零时区时间
     * ZoneId 表示时区
     */
    @Test
    public void test() {
        Instant now = Instant.now();  // 按照标准的时间，不是当地的时间
        System.out.println(now);
    }


    /**
     * LocalDateTime格式化
     */
    @Test
    public void test1() {
        LocalDateTime now = LocalDateTime.now();
        // 定义格式化格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter1 = DateTimeFormatter.ISO_LOCAL_DATE;
        String format = now.format(formatter);
        String format1 = now.format(formatter1);
        System.out.println(format);
        System.out.println(format1);
    }

    /**
     * LocalDateTime反格式化
     */
    @Test
    public void test2() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse("2020-12-12 23:23:23", dateTimeFormatter);
        System.out.println(parse);
    }


    /**
     * Period：处理两个日期之间的差值
     * Duration：处理两个时间之间的差值
     */
    @Test
    public void test3() {
        LocalDate date = LocalDate.of(2017,7,22);
        LocalDate date1 = LocalDate.now();
        Period period = Period.between(date,date1);
        System.out.println(period.getYears() + "年" + period.getMonths() + "月" + period.getDays() + "天");

        LocalTime time = LocalTime.of(20,30);
        LocalTime time1 = LocalTime.of(23,59);
        Duration duration = Duration.between(time,time1);
        System.out.println(duration.toMinutes() + "分钟");
    }

    /**
     *  Date转LocalDateTime
     *  Date转Instant转ZonedDateTime转LocalDateTime
     */
    @Test
    public void test4() {
        Date date = new Date();
        System.out.println(date);
        Instant instant = date.toInstant();
        System.out.println(instant);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        System.out.println(zonedDateTime);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        System.out.println(localDateTime);
    }

    /**
     *  LocalDateTime转Date
     */
    @Test
    public void test5() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
        System.out.println(zonedDateTime);
        Instant instant = zonedDateTime.toInstant();
        System.out.println(instant);
        Date from = Date.from(instant);
        System.out.println(from);
    }
}
