package com.isagron.accountmi_server.services.utils;

import com.isagron.accountmi_server.domain.common_elements.DateRange;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date startOfMonth(int month, int year) {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, year);
        instance.set(Calendar.MONTH, month);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTime();
    }

    public static Date endOfMonth(int month, int year) {
        Calendar instance = Calendar.getInstance();

        instance.set(Calendar.YEAR, year);
        instance.set(Calendar.MONTH, month);
        instance.set(Calendar.DAY_OF_MONTH, 1);

        instance.add(Calendar.MONTH, 1);
        instance.add(Calendar.DAY_OF_MONTH, -1);
        return instance.getTime();
    }

    public static DateRange yearRange(Integer year) {

        Calendar instance = Calendar.getInstance();

        instance.set(Calendar.YEAR, year);
        instance.set(Calendar.MONTH, Calendar.JANUARY);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayInYear = instance.getTime();
        instance.add(Calendar.DAY_OF_MONTH, -1);
        instance.set(Calendar.YEAR, year);
        Date lastDayInYear = instance.getTime();
        return new DateRange(firstDayInYear, lastDayInYear);
    }

    public static DateRange getMonthDateRange(int month, int year) {
        return new DateRange(startOfMonth(month, year), endOfMonth(month, year));
    }

    public static DateRange getPrevMonthDateRange(int month, int year){

        Calendar instance = Calendar.getInstance();

        instance.set(Calendar.YEAR, month);
        instance.set(Calendar.MONTH, year);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        instance.add(Calendar.DAY_OF_MONTH, -1);

        int prevMonth = instance.get(Calendar.MONTH);
        int prevYear = instance.get(Calendar.YEAR);
        return new DateRange(startOfMonth(prevMonth, prevYear), endOfMonth(prevMonth, prevYear));
    }

    public static DateRange getCurrentMonthDateRange() {
        Calendar instance = Calendar.getInstance();

        int currentMonth = instance.get(Calendar.MONTH);
        int currentYear = instance.get(Calendar.YEAR);
        return new DateRange(startOfMonth(currentMonth, currentYear), endOfMonth(currentMonth, currentYear));
    }

    public static DateRange getPrevMonthDateRange() {

        Calendar instance = Calendar.getInstance();

        int currentMonth = instance.get(Calendar.MONTH);
        int currentYear = instance.get(Calendar.YEAR);
        instance.set(Calendar.YEAR, currentYear);
        instance.set(Calendar.MONTH, currentMonth);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        instance.add(Calendar.DAY_OF_MONTH, -1);

        int prevMonth = instance.get(Calendar.MONTH);
        int prevYear = instance.get(Calendar.YEAR);
        return new DateRange(startOfMonth(prevMonth, prevYear), endOfMonth(prevMonth, prevYear));

    }

    public static DateRange getCurrentYearDateRange() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return yearRange(currentYear);
    }

    public static DateRange beforeXMonthTillNow(int x) {
        Date now = Calendar.getInstance().getTime();
        return new DateRange(beforeXMonth(now, x), now);
    }

    public static Date beforeXMonth(Date d, int x){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.MONTH, -x);
        return calendar.getTime();
    }


}
