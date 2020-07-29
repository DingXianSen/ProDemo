/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */

package com.parkingwang.keyboard.view;

import com.parkingwang.keyboard.engine.KeyboardEntry;

/**
 * 键盘回调接口。
 *
 * @since 2017-09-26 0.1
 */
public interface OnKeyboardChangedListener {

    /**
     * 车牌键按下的回调事件。
     *
     * @param text 所点击的按键的内容
     */
    void onTextKey(String text);

    /**
     * 删除键按下的回调事件。
     */
    void onDeleteKey();

    /**
     * 确定键按下的回调事件。
     */
    void onConfirmKey();

    /**
     * 车牌键盘更新后的回调事件
     *
     * @param keyboard 键盘信息
     */
    void onKeyboardChanged(KeyboardEntry keyboard);

    //////

    class Simple implements OnKeyboardChangedListener {

        @Override
        public void onTextKey(String text) {

        }

        @Override
        public void onDeleteKey() {

        }

        @Override
        public void onConfirmKey() {

        }

        @Override
        public void onKeyboardChanged(KeyboardEntry keyboard) {

        }
    }

}
