package com.sunyuan.calendarlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.sunyuan.calendarlibrary.utils.Utils;

/**
 * author：six
 * created by:2019-01-20
 * github:https://github.com/sy007
 */
public class MonthLableView extends View {


    private int defaultLableWeekendTextColor = Color.parseColor("#FF6E00");
    private int defaultLableTextColor = Color.parseColor("#000000");
    private int defaultLableTextSize;

    private int weekendTextColor;
    private int textColor;
    private float textSize;
    private Paint.FontMetrics fontMetrics;
    private Paint lablePaint;
    private int columnNum = 7;
    private float lableWidht;
    private static final String[] LABLE_ARR = new String[]{
            "周日", "周一", "周二", "周三", "周四", "周五", "周六"
    };
    private RectF lableRectF;
    private CharSequence[] lableArr;

    public MonthLableView(Context context) {
        this(context, null);
    }

    public MonthLableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthLableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultLableTextSize = Utils.sp2px(context, 13);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MonthLableView);
        weekendTextColor = typedArray.getColor(R.styleable.MonthLableView_cl_lableWeekendTextColor, defaultLableWeekendTextColor);
        textColor = typedArray.getColor(R.styleable.MonthLableView_cl_lableTextColor, defaultLableTextColor);
        textSize = typedArray.getDimension(R.styleable.MonthLableView_cl_lableTextSize, defaultLableTextSize);
        lableArr = typedArray.getTextArray(R.styleable.MonthLableView_cl_lableArr);
        if (lableArr == null) {
            lableArr = LABLE_ARR;
        }
        typedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        lableRectF = new RectF();
        lablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lablePaint.setTextAlign(Paint.Align.CENTER);
        lablePaint.setTextSize(textSize);
        fontMetrics = new Paint.FontMetrics();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        lablePaint.getFontMetrics(fontMetrics);
        setMeasuredDimension(widthMeasureSpec, (int) (fontMetrics.descent - fontMetrics.ascent) + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        lableWidht = (w - getPaddingLeft() - getPaddingRight()) / (columnNum * 1.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        lablePaint.getFontMetrics(fontMetrics);
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int lableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        for (int i = 0; i < columnNum; i++) {
            lableRectF.set(left, top, left + lableWidht, top + lableHeight);
            if (i == 0 || i == columnNum - 1) {
                lablePaint.setColor(weekendTextColor);
            } else {
                lablePaint.setColor(textColor);
            }
            float distance = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
            float baseline = lableRectF.centerY() + distance;
            canvas.drawText(lableArr[i].toString(), lableRectF.centerX(), baseline, lablePaint);
            left += lableWidht;
        }
    }
}
