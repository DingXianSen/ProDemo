package com.huidaxuan.ic2cloud.selectlistdata;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sunyuan.calendarlibrary.CalendarView;
import com.sunyuan.calendarlibrary.CalendarViewWrapper;
import com.sunyuan.calendarlibrary.MonthTitleViewCallBack;
import com.sunyuan.calendarlibrary.OnCalendarSelectDayListener;
import com.sunyuan.calendarlibrary.SelectionMode;
import com.sunyuan.calendarlibrary.model.CalendarDay;
import com.sunyuan.calendarlibrary.model.CalendarSelectDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @packageName:com.huidaxuan.ic2cloud.selectlistdata
 * @className: SingleSelectActivity
 * @description:
 * @author: dingchao
 * @time: 2020-09-27 14:25
 */
public class SingleSelectActivity extends AppCompatActivity {

    private TextView tvCurrentSelectDate;
    private CalendarSelectDay<CalendarDay> calendarSelectDay;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_select);
        initSelectCalendar();

    }

    private void initView() {
        calendarView = findViewById(R.id.calendar_view);
        tvCurrentSelectDate = findViewById(R.id.tv_current_select_date);
        String currentSelectDayStr = formatDate("yyyy-MM-dd", calendarSelectDay.getFirstSelectDay().toDate());
        tvCurrentSelectDate.setText(currentSelectDayStr);
        Calendar calendar = Calendar.getInstance();
        Date minDate = calendar.getTime();
//        calendar.add(Calendar.DATE, 90);
        calendar.add(Calendar.MONTH, 3);
        Date maxDate = calendar.getTime();
        CalendarViewWrapper.wrap(calendarView)
                //设置展示的日期范围
                .setDateRange(minDate, maxDate)
                //设置默认选中的日期
                .setCalendarSelectDay(calendarSelectDay)
                //选中模式-单选
                .setSelectionMode(SelectionMode.SINGLE)
                //设置日历选中事件
                .setOnCalendarSelectDayListener(new OnCalendarSelectDayListener<CalendarDay>() {
                    @Override
                    public void onCalendarSelectDay(CalendarSelectDay<CalendarDay> calendarSelectDay) {
                        CalendarDay firstSelectDay = calendarSelectDay.getFirstSelectDay();
                        if (firstSelectDay != null) {
                            String firstSelectDateStr = formatDate("yyyy-MM-dd", firstSelectDay.toDate());
                            tvCurrentSelectDate.setText(firstSelectDateStr);
                        }
                    }
                })
                //月份头是否悬停
                .setStick(true)
                //是否展示月份布局
                .setShowMonthTitleView(true)
                //设置月份布局回调
                .setMonthTitleViewCallBack(new MonthTitleViewCallBack() {
                    @Override
                    public View getMonthTitleView(int position, Date date) {
                        View view = View.inflate(SingleSelectActivity.this, R.layout.layout_calendar_month_title, null);
                        TextView tvMonthTitle = view.findViewById(R.id.tv_month_title);
                        tvMonthTitle.setText(formatDate("yyyy年MM月", date));
                        return view;
                    }
                })
                .display();
        //根据指定日期得到position位置
        int position = calendarView.covertToPosition(calendarSelectDay.getFirstSelectDay());
        if (position != -1) {
            //滚动到指定位置
            calendarView.smoothScrollToPosition(position);
        }
    }

    private void initSelectCalendar() {
        //设置当前日期
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String date = sDateFormat.format(System.currentTimeMillis());
//        String nowYear = date.substring(0, date.indexOf("-"));
//        String nowMonth = date.substring(5, 7);
//        String nowDay = date.substring(8, 10);
//        CalendarDay firstSelectDay = new CalendarDay(Integer.valueOf(nowYear), Integer.valueOf(nowMonth), Integer.valueOf(nowDay));
//        Log.e("dateStr", "-----year---->"+firstSelectDay.getYear());
//        Log.e("dateStr", "-----month---->"+firstSelectDay.getMonth());
//        Log.e("dateStr", "-----day---->"+firstSelectDay.getDay());

        calendarSelectDay = new CalendarSelectDay<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 0);
        CalendarDay firstSelectDay = new CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        calendarSelectDay.setFirstSelectDay(firstSelectDay);
        calendarSelectDay.setFirstSelectDay(firstSelectDay);
        initView();
    }


    public String formatDate(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }
}
