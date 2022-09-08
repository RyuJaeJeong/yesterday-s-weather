package com.ryu.weather.common;

import java.util.Calendar;

public class Util {

    public final static String forecastServiceKey = "2KFAsAFNHzs%2FCIakKfPNyRft%2BsAxNov7i1inpO7f1qxlJ8CXFp5P6KRmBSi%2FuBnpJZdG952jW%2FPnidC76%2FYwbw%3D%3D";

    public final static String weatherServiceKey = "2KFAsAFNHzs%2FCIakKfPNyRft%2BsAxNov7i1inpO7f1qxlJ8CXFp5P6KRmBSi%2FuBnpJZdG952jW%2FPnidC76%2FYwbw%3D%3D";


    //오늘 날짜 반환 YYYYMMDD 형식
    public static String getToday() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return year + "" + ((month<10)?"0"+month:month) + ((day<10)?"0"+day:day);
    }

    //XX일 후의 날짜 반환  YYYYMMDD 형식
    public static String getSomeDayMore(int some) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, some);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return year + "" + ((month<10)?"0"+month:month) + ((day<10)?"0"+day:day);
    }


}
