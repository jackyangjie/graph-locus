package com.trs.locus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Description
 * @DATE 2021.8.11 14:32
 * @Author yangjie
 **/

public class DateUtil {

    public static final  DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/M/d");

    public static final  DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    public static Date stringToDate(String date){
        LocalDate localDate = LocalDate.parse(date,df);
        ZonedDateTime zdt = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }
    public static Date stringToDate2(String date){
        LocalDateTime localDate = LocalDateTime.parse(date,df2);
        ZonedDateTime zdt = localDate.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }
}
