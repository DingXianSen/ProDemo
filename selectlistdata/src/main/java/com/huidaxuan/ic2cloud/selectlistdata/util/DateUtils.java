package com.huidaxuan.ic2cloud.selectlistdata.util;

import android.content.Context;

import com.huidaxuan.ic2cloud.selectlistdata.R;
import com.huidaxuan.ic2cloud.selectlistdata.entity.DateEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @packageName:com.huidaxuan.ic2cloud.selectlistdata.util
 * @className: DateUtils
 * @description:包括获取 1 当前年月日 2 当前是周几  3、根据日期获取是周几 4、获取7天的日期 5、获取当天往后的一周
 * @author: dingchao
 * @time: 2020-09-27 10:29
 */
public class DateUtils {

    private static String mYear; // 当前年
    private static String mMonth; // 月
    private static String mDay;
    private static String mWay;


    /**
     * 获取当前日期几月几号
     */
    public static String getDateString() {


        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        if (Integer.parseInt(mDay) > MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth)))) {
            mDay = String.valueOf(MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth))));
        }
        return mMonth + "月" + mDay + "日";
    }


    /**
     * 获取当前年月日
     *
     * @return
     */
    public static String StringData() {


        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        if (Integer.parseInt(mDay) > MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth)))) {
            mDay = String.valueOf(MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth))));
        }
        return mYear + "-" + (mMonth.length() == 1 ? "0" + mMonth : mMonth) + "-" + (mDay.length() == 1 ? "0" + mDay : mDay);
    }


    /**
     * 根据当前日期获得是星期几
     *
     * @return
     */
    public static String getWeek(Context context, String time) {
        String Week = "";


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {


            c.setTime(format.parse(time));


        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
//            Week += "周天";
            Week += context.getResources().getString(R.string.week_7);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
//            Week += "周一";
            Week += context.getResources().getString(R.string.week_1);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
//            Week += "周二";
            Week += context.getResources().getString(R.string.week_2);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
//            Week += "周三";
            Week += context.getResources().getString(R.string.week_3);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
//            Week += "周四";
            Week += context.getResources().getString(R.string.week_4);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
//            Week += "周五";
            Week += context.getResources().getString(R.string.week_5);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
//            Week += "周六";
            Week += context.getResources().getString(R.string.week_6);
        }
        return Week;
    }


    /**
     * 获取今天往后一周的日期（几月几号）
     */
    public static List<String> getSevendate(int days) {
        List<String> dates = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));


        for (int i = 0; i < 7; i++) {
            mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
            mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + i);// 获取当前日份的日期号码
            if (Integer.parseInt(mDay) > MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (i + 1))) {
                mDay = String.valueOf(MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (i + 1)));
            }
//            String date = mMonth + "月" + mDay + "日";
            String date = mMonth + "." + mDay;
            dates.add(date);
        }
        return dates;
    }


    /**
     * 得到当年当月的最大日期
     **/
    public static int MaxDayFromDay_OF_MONTH(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        return day;
    }

    /**
     * 获取今天往后的数据
     */
    public static List<String> getDaysWeek(Context context, int days) {
        String week = "";
        List<String> weeksList = new ArrayList<String>();
        List<String> dateList = getDaysDate(days);
        for (String s : dateList) {
            if (s.equals(StringData())) {
                week = "今天";
            } else {
                week = getWeek(context, s);
            }
            weeksList.add(week);
        }
        return weeksList;
    }

    /**
     * @return
     */
    public static List<String> getDaysDate(int days) {
        List<String> dates = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat(
                "yyyy-MM-dd");
        String date = sim.format(c.getTime());
        dates.add(date);
        for (int i = 0; i < days; i++) {
            c.add(java.util.Calendar.DAY_OF_MONTH, 1);
            date = sim.format(c.getTime());
            dates.add(date);
        }
        return dates;
    }

    /**
     * @return
     */
    public static List<String> getDaysDateMd(int days) {
        List<String> dates = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat(
                "MM.dd");
        String date = sim.format(c.getTime());
        dates.add(date);
        for (int i = 0; i < days; i++) {
            c.add(java.util.Calendar.DAY_OF_MONTH, 1);
            date = sim.format(c.getTime());
            dates.add(date);
        }
        return dates;
    }

    /**
     * @return
     */
    public static List<DateEntity> getDaysDateMdObject(int days) {
        List<DateEntity> dates = new ArrayList<DateEntity>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat(
                "MM.dd");
        java.text.SimpleDateFormat sim2 = new java.text.SimpleDateFormat(
                "yyyy-MM-dd");
        dates.add(new DateEntity(sim.format(c.getTime()), sim2.format(c.getTime())));
        for (int i = 0; i < days; i++) {
            c.add(java.util.Calendar.DAY_OF_MONTH, 1);
            dates.add(new DateEntity(sim.format(c.getTime()), sim2.format(c.getTime())));
        }
        return dates;
    }

    /**
     * 根据传入的天数获取具体后续几天的日期
     *
     * @param days
     * @return
     */
    public static List<DateEntity> getDateAndWeekByNum(Context context, int days) {
        List<DateEntity> dateEntities = new ArrayList<>();
        List<DateEntity> dateList = getDaysDateMdObject(days);
        for (DateEntity s : dateList) {
            if (s.getDateAllName().equals(StringData())) {
                s.setWeekName("今");
            } else {
                s.setWeekName(getWeek(context, s.getDateAllName()));
            }
            dateEntities.add(s);
        }

        return dateEntities;

    }
}
