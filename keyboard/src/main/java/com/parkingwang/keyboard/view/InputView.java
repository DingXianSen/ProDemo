package com.parkingwang.keyboard.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.parkingwang.vehiclekeyboard.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 */
public class InputView extends LinearLayout {

    private static final String TAG = InputView.class.getName();

    private static final String KEY_INIT_NUMBER = "pwk.keyboard.key:init.number";

    private final HashMap<String, Object> mKeyMap = new HashMap<>();

    private final Set<OnFieldViewSelectedListener> mOnFieldViewSelectedListeners = new HashSet<>(4);

    private final FieldViewGroup mFieldViewGroup;

    /**
     * 点击选中输入框时，只可以从左到右顺序输入，不可隔位
     */
    private final OnClickListener mOnFieldViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final Button field = (Button) v;
            final ClickMeta clickMeta = getClickMeta(field);
            Log.d(TAG, "当前点击信息: " + clickMeta);

            final int numberLength = mFieldViewGroup.getText().length();
            // 空车牌只能点击第一个
            if (numberLength == 0 && clickMeta.clickIndex != 0) {
                return;
            }
            // 不可大于车牌长度
            if (clickMeta.clickIndex > numberLength) {
                return;
            }

            // 点击位置是否变化
            if (clickMeta.clickIndex != clickMeta.selectedIndex) {
                setFieldViewSelected(field);
            }

            // 触发选中事件
            for (OnFieldViewSelectedListener listener : mOnFieldViewSelectedListeners) {
                listener.onSelectedAt(clickMeta.clickIndex);
            }

        }
    };

    @Nullable
    private SelectedDrawable mSelectedDrawable;

    public InputView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.pwkInputStyle);
    }

    public InputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.pwk_input_view, this);
        mFieldViewGroup = new FieldViewGroup() {
            @Override
            protected Button findViewById(int id) {
                return (Button) InputView.this.findViewById(id);
            }
        };
        onInited(context, attrs, defStyleAttr);
    }

    private void onInited(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InputView, defStyleAttr, 0);
        final float textSize = ta.getDimension(R.styleable.InputView_pwkInputTextSize, 0);
        final String drawableClassName = ta.getString(R.styleable.InputView_pwkSelectedDrawable);
        final int itemBorderSelectedColor = ta.getColor(R.styleable.InputView_pwkItemBorderSelectedColor,
                ContextCompat.getColor(context, R.color.pwk_primary_color));
        ta.recycle();

        initSelectedDrawable(drawableClassName, itemBorderSelectedColor);

        mFieldViewGroup.setupAllFieldsTextSize(textSize);
        mFieldViewGroup.setupAllFieldsOnClickListener(mOnFieldViewClickListener);
        mFieldViewGroup.changeTo7Fields();
    }

    private void initSelectedDrawable(String className, int selectedColor) {
        if (TextUtils.isEmpty(className)) {
            return;
        }
        try {
            Class cls = Class.forName(className);
            if (!SelectedDrawable.class.isAssignableFrom(cls)) {
                return;
            }
            mSelectedDrawable = (SelectedDrawable) cls.newInstance();
            mSelectedDrawable.setColor(selectedColor);
            mSelectedDrawable.setWidth(getResources().getDimensionPixelSize(R.dimen.pwk_input_item_highlight_border_width));
            mSelectedDrawable.setRadius(getResources().getDimensionPixelSize(R.dimen.pwk_input_item_radius));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setItemBorderSelectedColor(@ColorInt int itemBorderSelectedColor) {
        if (mSelectedDrawable != null) {
            mSelectedDrawable.setColor(itemBorderSelectedColor);
        }
        invalidate();
    }

    @Nullable
    public SelectedDrawable getSelectedDrawable() {
        return mSelectedDrawable;
    }

    /**
     * 设置文本字符到当前选中的输入框
     *
     * @param text 文本字符
     */
    public void updateSelectedCharAndSelectNext(final String text) {
        final Button selected = mFieldViewGroup.getFirstSelectedFieldOrNull();
        if (selected != null) {
            selected.setText(text);
            performNextFieldViewBy(selected);
        }
    }

    /**
     * 从最后一位开始删除
     */
    public void removeLastCharOfNumber() {
        final Button last = mFieldViewGroup.getLastFilledFieldOrNull();
        if (last != null) {
            last.setText(null);
            performFieldViewSetToSelected(last);
        }
    }

    /**
     * @return 返回当前输入组件是否为完成状态
     */
    public boolean isCompleted() {
        // 所有显示的输入框都被填充了车牌号码，即输入完成状态
        return mFieldViewGroup.isAllFieldsFilled();
    }

    /**
     * 返回当前车牌号码是否被修改过。
     * 与通过 updateNumber 方法设置的车牌号码来对比。
     *
     * @return 是否修改过
     */
    public boolean isNumberChanged() {
        final String current = getNumber();
        return !current.equals(String.valueOf(mKeyMap.get(KEY_INIT_NUMBER)));
    }

    /**
     * 更新/重置车牌号码
     *
     * @param number 车牌号码
     */
    public void updateNumber(String number) {
        // 初始化车牌
        mKeyMap.put(KEY_INIT_NUMBER, number);
        mFieldViewGroup.setTextToFields(number);
    }

    /**
     * 获取当前已输入的车牌号码
     *
     * @return 车牌号码
     */
    public String getNumber() {
        return mFieldViewGroup.getText();
    }

    /**
     * 选中第一个输入框
     */
    public void performFirstFieldView() {
        performFieldViewSetToSelected(mFieldViewGroup.getFieldAt(0));
    }

    /**
     * 选中最后一个可等待输入的输入框。
     * 如果全部为空，则选中第1个输入框。
     */
    public void performLastPendingFieldView() {
        final Button field = mFieldViewGroup.getLastFilledFieldOrNull();
        if (field != null) {
            performNextFieldViewBy(field);
        } else {
            performFieldViewSetToSelected(mFieldViewGroup.getFieldAt(0));
        }
    }

    /**
     * 选中下一个输入框。
     * 如果当前输入框是空字符，则重新触发当前输入框的点击事件。
     */
    public void performNextFieldView() {
        final ClickMeta meta = getClickMeta(null);
        if (meta.selectedIndex >= 0) {
            final Button current = mFieldViewGroup.getFieldAt(meta.selectedIndex);
            if (!TextUtils.isEmpty(current.getText())) {
                performNextFieldViewBy(current);
            } else {
                performFieldViewSetToSelected(current);
            }
        }
    }

    /**
     * 重新触发当前输入框选中状态
     */
    public void rePerformCurrentFieldView() {
        final ClickMeta clickMeta = getClickMeta(null);
        if (clickMeta.selectedIndex >= 0) {
            performFieldViewSetToSelected(mFieldViewGroup.getFieldAt(clickMeta.selectedIndex));
        }
    }

    /**
     * 设置第8位输入框显示状态
     *
     * @param setToShow8thField 是否显示
     */
    public void set8thVisibility(boolean setToShow8thField) {
        final boolean changed;
        if (setToShow8thField) {
            changed = mFieldViewGroup.changeTo8Fields();
        } else {
            changed = mFieldViewGroup.changeTo7Fields();
        }
        if (changed) {
            final Button field = mFieldViewGroup.getFirstEmptyField();
            if (field != null) {
                Log.d(TAG, "[@@ FieldChanged @@] FirstEmpty.tag: " + field.getTag());
                setFieldViewSelected(field);
            }
        }
    }

    /**
     * 是否最后一位被选中状态。
     *
     * @return 是否选中
     */
    public boolean isLastFieldViewSelected() {
        return mFieldViewGroup.getLastField().isSelected();
    }

    public InputView addOnFieldViewSelectedListener(OnFieldViewSelectedListener listener) {
        mOnFieldViewSelectedListeners.add(listener);
        return this;
    }

    private void performFieldViewSetToSelected(Button target) {
        Log.d(TAG, "[== FastPerform ==] Btn.text: " + target.getText());
        // target.performClick();
        // 自动触发的，不要使用Android内部处理，太慢了。
        mOnFieldViewClickListener.onClick(target);
        setFieldViewSelected(target);
    }

    private void performNextFieldViewBy(Button current) {
        final int nextIndex = mFieldViewGroup.getNextIndexOfField(current);
        Log.d(TAG, "[>> NextPerform >>] Next.Btn.idx: " + nextIndex);
        performFieldViewSetToSelected(mFieldViewGroup.getFieldAt(nextIndex));
    }

    private void setFieldViewSelected(Button target) {
        for (Button btn : mFieldViewGroup.getAvailableFields()) {
            btn.setSelected((btn == target));
        }
        invalidate();
    }

    private ClickMeta getClickMeta(Button clicked) {
        int selectedIndex = -1;
        int currentIndex = -1;
        final Button[] fields = mFieldViewGroup.getAvailableFields();
        for (int i = 0; i < fields.length; i++) {
            final Button field = fields[i];
            if (currentIndex < 0 && field == clicked) {
                currentIndex = i;
            }
            if (selectedIndex < 0 && field.isSelected()) {
                selectedIndex = i;
            }
        }
        return new ClickMeta(selectedIndex, currentIndex);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        invalidateSelectedDrawable(canvas);
    }

    private void invalidateSelectedDrawable(Canvas canvas) {
        if (mSelectedDrawable == null) {
            return;
        }
        final int count = getChildCount();
        View lastShown = null;
        View selected;
        for (int i = count - 1; i >= 0; i--) {
            selected = getChildAt(i);
            if (lastShown == null && selected.isShown()) {
                lastShown = selected;
            }
            if (selected.getVisibility() == View.VISIBLE && selected.isSelected()) {
                if (selected == lastShown) {
                    mSelectedDrawable.setPosition(SelectedDrawable.Position.LAST);
                } else if (i == 0) {
                    mSelectedDrawable.setPosition(SelectedDrawable.Position.FIRST);
                } else {
                    mSelectedDrawable.setPosition(SelectedDrawable.Position.MIDDLE);
                }
                final Rect rect = mSelectedDrawable.getRect();
                rect.set(selected.getLeft(), selected.getTop(), selected.getRight(), selected.getBottom());
                mSelectedDrawable.draw(canvas);
                break;
            }
        }
    }

    //////

    public interface OnFieldViewSelectedListener {

        void onSelectedAt(int index);
    }

    //////////

    private static class ClickMeta {

        /**
         * 当前输入框已选中的序号
         */
        final int selectedIndex;

        /**
         * 当前点击的输入框序号
         */
        final int clickIndex;

        private ClickMeta(int selectedIndex, int currentIndex) {
            this.selectedIndex = selectedIndex;
            this.clickIndex = currentIndex;
        }

        @Override
        public String toString() {
            return "ClickMeta{" +
                    "selectedIndex=" + selectedIndex +
                    ", clickIndex=" + clickIndex +
                    '}';
        }
    }

}
