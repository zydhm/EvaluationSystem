package com.example.evaluation_system.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {

    private static SimpleDateFormat sSimpleDateFormat;
    /**
     * 将Unix时间戳转换成日月年时分秒格式
     * @param timeStamp
     * @return
     */
    public static String timeStampToDate(long timeStamp) {
        if (sSimpleDateFormat == null) {
            sSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        }
                return sSimpleDateFormat.format(new Date(timeStamp));
    }

}