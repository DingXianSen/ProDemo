/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dc.autosize.unit;

import android.util.DisplayMetrics;

import com.dc.autosize.utils.Preconditions;


/**
 * ================================================
 * 管理 AndroidAutoSize 支持的所有单位, AndroidAutoSize 支持五种单位 (dp、sp、pt、in、mm)
 * 其中 dp、sp 这两个是比较常见的单位, 作为 AndroidAutoSize 的主单位, 默认被 AndroidAutoSize 支持
 * pt、in、mm 这三个是比较少见的单位, 只可以选择其中的一个, 作为 AndroidAutoSize 的副单位, 与 dp、sp 一起被 AndroidAutoSize 支持
 * 副单位是用于规避修改 {@link DisplayMetrics#density} 所造成的对于其他使用 dp 布局的系统控件或三方库控件的不良影响
 * 您选择什么单位, 就在 layout 文件中用什么单位布局
 * <p>
 * 两个主单位和一个副单位, 可以随时使用下面的方法关闭和重新开启对它们的支持
 * 如果您想完全规避修改 {@link DisplayMetrics#density} 所造成的对于其他使用 dp 布局的系统控件或三方库控件的不良影响
 * 那请调用 {@link #setSupportDP}、{@link #setSupportSP} 都设置为 {@code false}, 停止对两个主单位的支持 (如果开启 sp, 对其他三方库控件影响不大, 也可以不关闭对 sp 的支持)
 * 并调用 {@link #setSupportSubunits} 从三个冷门单位中选择一个作为副单位 (三个单位的效果都是一样的, 按自己的喜好选择, 比如我就喜欢 mm, 翻译为中文是妹妹的意思)
 * 然后在 layout 文件中只使用这个副单位进行布局, 这样就可以完全规避修改 {@link DisplayMetrics#density} 所造成的问题
 * 因为 dp、sp 这两个单位在其他系统控件或三方库控件中都非常常见, 但三个冷门单位却非常少见
 * <p>
 * ================================================
 */
public class UnitsManager {
    /**
     * 是否支持 dp 单位, 默认支持
     */
    private boolean isSupportDP = true;
    /**
     * 是否支持 sp 单位, 默认支持
     */
    private boolean isSupportSP = true;
    /**
     * 是否支持副单位, 以什么为副单位? 默认不支持
     */
    private Subunits mSupportSubunits = Subunits.NONE;

    /**
     * 是否支持 dp 单位, 默认支持, 详情请看类文件的注释 {@link UnitsManager}
     *
     * @return {@code true} 为支持, {@code false} 为不支持
     */
    public boolean isSupportDP() {
        return isSupportDP;
    }

    /**
     * 是否让 AndroidAutoSize 支持 dp 单位, 默认支持, 详情请看类文件的注释 {@link UnitsManager}
     *
     * @param supportDP {@code true} 为支持, {@code false} 为不支持
     */
    public UnitsManager setSupportDP(boolean supportDP) {
        isSupportDP = supportDP;
        return this;
    }

    /**
     * 是否支持 sp 单位, 默认支持, 详情请看类文件的注释 {@link UnitsManager}
     *
     * @return {@code true} 为支持, {@code false} 为不支持
     */
    public boolean isSupportSP() {
        return isSupportSP;
    }

    /**
     * 是否让 AndroidAutoSize 支持 sp 单位, 默认支持, 详情请看类文件的注释 {@link UnitsManager}
     *
     * @param supportSP {@code true} 为支持, {@code false} 为不支持
     */
    public UnitsManager setSupportSP(boolean supportSP) {
        isSupportSP = supportSP;
        return this;
    }

    /**
     * AndroidAutoSize 以什么单位为副单位, 默认为 {@link Subunits#NONE}, 即不支持副单位, 详情请看类文件的注释 {@link UnitsManager}
     *
     * @return {@link Subunits}
     */
    public Subunits getSupportSubunits() {
        return mSupportSubunits;
    }

    /**
     * 让 AndroidAutoSize 以什么单位为副单位, 在 pt、in、mm 这三个冷门单位中选择一个即可, 三个效果都是一样的
     * 按自己的喜好选择, 比如我就喜欢 mm, 翻译为中文是妹妹的意思
     * 默认为 {@link Subunits#NONE}, 即不支持副单位, 详情请看类文件的注释 {@link UnitsManager}
     *
     * @param supportSubunits {@link Subunits}
     */
    public UnitsManager setSupportSubunits(Subunits supportSubunits) {
        mSupportSubunits = Preconditions.checkNotNull(supportSubunits,
                "The supportSubunits can not be null, use Subunits.NONE instead");
        return this;
    }
}
