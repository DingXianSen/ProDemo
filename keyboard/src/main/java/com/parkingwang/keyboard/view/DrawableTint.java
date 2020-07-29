package com.parkingwang.keyboard.view;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.TintAwareDrawable;

/**
 * @version 2017-11-03 0.1
 */
public class DrawableTint {

    public static Drawable tint(Drawable drawable, ColorStateList tint) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableTint.setTintMode(drawable, PorterDuff.Mode.MULTIPLY);
        drawable = drawable.mutate();
        DrawableCompat.setTintList(drawable, tint);
        return drawable;
    }

    // 这里使用的是TintAwareDrawable的setTintMode方法
    @SuppressLint("RestrictedApi")
    private static void setTintMode(Drawable drawable, PorterDuff.Mode mode) {
        if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTintMode(mode);
        } else {
            DrawableCompat.setTintMode(drawable, mode);
        }
    }
}
