package com.huidaxuan.ic2cloud.selectlistdata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huidaxuan.ic2cloud.selectlistdata.adapter.GvServiceStationAdapter;
import com.huidaxuan.ic2cloud.selectlistdata.adapter.RvDateListAdapter;
import com.huidaxuan.ic2cloud.selectlistdata.diy.popupwindow.CommonPopupWindow;
import com.huidaxuan.ic2cloud.selectlistdata.diy.popupwindow.gridview.MyGridView;
import com.huidaxuan.ic2cloud.selectlistdata.entity.DateEntity;
import com.huidaxuan.ic2cloud.selectlistdata.entity.ServiceStationEntity;
import com.huidaxuan.ic2cloud.selectlistdata.util.DateUtils;
import com.sunyuan.calendarlibrary.CalendarView;
import com.sunyuan.calendarlibrary.CalendarViewWrapper;
import com.sunyuan.calendarlibrary.MonthTitleViewCallBack;
import com.sunyuan.calendarlibrary.OnCalendarSelectDayListener;
import com.sunyuan.calendarlibrary.SelectionMode;
import com.sunyuan.calendarlibrary.model.CalendarDay;
import com.sunyuan.calendarlibrary.model.CalendarSelectDay;

import org.json.JSONArray;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期列表选择模块
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_date_1;
    Button btn_date_2;
    Button btn_date_3;
    RecyclerView rcv_home_top_there;
    RelativeLayout rl_calendar_sel;

    View activity_popup;
    private CommonPopupWindow win_calendar;
    private PopupWindow popw_calendar;
    TextView tv_cancel;
    private CalendarSelectDay<CalendarDay> calendarSelectDay;
    private CalendarView calendarView;

    //
    MyGridView mgv_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListener();
    }

    private void initListener() {
        btn_date_1 = findViewById(R.id.btn_date_1);
        btn_date_2 = findViewById(R.id.btn_date_2);
        btn_date_3 = findViewById(R.id.btn_date_3);
        btn_date_1.setOnClickListener(this);
        btn_date_2.setOnClickListener(this);
        btn_date_3.setOnClickListener(this);

        rcv_home_top_there = findViewById(R.id.rcv_home_top_there);
        rl_calendar_sel = findViewById(R.id.rl_calendar_sel);
        rl_calendar_sel.setOnClickListener(this);

        activity_popup = findViewById(R.id.activity_popup);
        mgv_list = findViewById(R.id.mgv_list);

        initDateData();
    }

    private void initDateData() {
        //绑定常用三个区域
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
        rcv_home_top_there.setLayoutManager(linearLayoutManager);
        final List<DateEntity> date723 = DateUtils.getDateAndWeekByNum(MainActivity.this, 30);
        final RvDateListAdapter adapter = new RvDateListAdapter(MainActivity.this, date723);
        rcv_home_top_there.setAdapter(adapter);

        adapter.setOnItemChrldListner(new RvDateListAdapter.OnItemChrldListner() {
            @Override
            public void onCall(View view, int position) {
                adapter.setCheckItem(position);
                refreshGridList(date723.get(position).getDateAllName());
//                Toast.makeText(MainActivity.this, "当前选中的日期为：" + date723.get(position).getDateAllName(), Toast.LENGTH_SHORT).show();
            }
        });
        initSelectCalendar();

    }

    /**
     * 初始化弹窗日历
     */
    private void initCalendarData() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;
        win_calendar = new CommonPopupWindow(MainActivity.this, R.layout.pop_calendar,
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (screenHeight * 0.7)) {

            @Override
            protected void initView() {
                View view = getContentView();
                tv_cancel = view.findViewById(R.id.tv_cancel);
                calendarView = view.findViewById(R.id.calendar_view);
            }

            @Override
            protected void initEvent() {
                Calendar calendar = Calendar.getInstance();
                Date minDate = calendar.getTime();
                calendar.add(Calendar.MONTH, 4);
                Date maxDate = calendar.getTime();
                Log.e("dateStr", "----initEvent--->" + calendarSelectDay.getFirstSelectDay());
                Log.e("dateStr", "----initEvent--->" + calendarSelectDay.getLastSelectDay());
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
//                                    Toast.makeText(context, "选中的日期为：" + firstSelectDateStr, Toast.LENGTH_SHORT).show();
                                    refreshGridList(firstSelectDateStr);
                                    dismisss();
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
                                View view = View.inflate(MainActivity.this, R.layout.layout_calendar_month_title, null);
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
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismisss();
                    }
                });
            }


            @Override
            protected void initWindow() {
                super.initWindow();
                PopupWindow instance = getPopupWindow();
                instance.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }
                });
            }
        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_date_1:
//                List<String> date7 = DateUtils.getDaysDate(10);
                List<String> date7 = DateUtils.getDaysDateMd(30);
//                List<String> date7 = DateUtils.getSevendate(10);
                for (int i = 0; i < date7.size(); i++) {
                    Log.e("date7", "----------->" + date7.get(i));
                }
                break;
            case R.id.btn_date_2:
                List<String> date72 = DateUtils.getDaysWeek(MainActivity.this, 10);
                for (int i = 0; i < date72.size(); i++) {
                    Log.e("date72", "----------->" + date72.get(i));
                }
                break;
            case R.id.btn_date_3:
                List<DateEntity> date723 = DateUtils.getDateAndWeekByNum(MainActivity.this, 10);
                for (int i = 0; i < date723.size(); i++) {

                    Log.e("date723", "----------->全日期："
                            + date723.get(i).getDateAllName()
                            + "-->月日：" + date723.get(i).getDateName()
                            + "-->星期：" + date723.get(i).getWeekName());
                }
                break;
            case R.id.rl_calendar_sel://日历选择，底部弹窗选择
//                startActivity(new Intent(MainActivity.this, SingleSelectActivity.class));
                openCalendarPop();
                break;
            default:
                break;
        }
    }

    /**
     * 刷新网格数据
     *
     * @param time
     */
    public void refreshGridList(String time) {
        Toast.makeText(this, "获取" + time + "的数据", Toast.LENGTH_SHORT).show();
        final List<ServiceStationEntity> list = new ArrayList<>();
        list.add(new ServiceStationEntity("9:00", "7", "10", false));
        list.add(new ServiceStationEntity("11:00", "4", "10", true));
        list.add(new ServiceStationEntity("13:00", "3", "10", true));
        list.add(new ServiceStationEntity("14:00", "0", "10", true));
        list.add(new ServiceStationEntity("16:00", "8", "10", true));
        list.add(new ServiceStationEntity("17:00", "10", "10", false));
        final GvServiceStationAdapter adapter = new GvServiceStationAdapter(list, MainActivity.this);
        mgv_list.setAdapter(adapter);

        mgv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //如果当前没有选中设置可以选中，否则取消选中,先判断能不能进行选择处理
                if (list.get(position).isSelFlag()) {//可以选
                    if (!GvServiceStationAdapter.getIsSelected().get(position)) {//判断是选择还是取消选择
                        GvServiceStationAdapter.cancelSelect();//清空所有选中，重新设置选中
                        GvServiceStationAdapter.getIsSelected().put(position, true);
                        adapter.notifyDataSetChanged();
                    } else {//取消选中
                        GvServiceStationAdapter.cancelSelect();
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "当前时间段暂无工位，请重新选择", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * 打开日历弹窗
     */
    private void openCalendarPop() {
        popw_calendar = win_calendar.getPopupWindow();
        //弹出动画
        popw_calendar.setAnimationStyle(R.style.animTranslate);
        //弹出容器
        win_calendar.showAtLocation(activity_popup, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }


    /**
     * 日期格式化
     *
     * @param format
     * @param date
     * @return
     */
    public String formatDate(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }


    /**
     * 设置当前日期
     */
    private void initSelectCalendar() {
        calendarSelectDay = new CalendarSelectDay<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 0);
        CalendarDay firstSelectDay = new CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        calendarSelectDay.setFirstSelectDay(firstSelectDay);
        initCalendarData();
    }
}
