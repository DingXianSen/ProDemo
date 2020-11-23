package com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.huidaxuan.ic2cloud.meituanshoppingcart.R;
import com.huidaxuan.ic2cloud.meituanshoppingcart.base.BaseTrDarkActivity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.EventBusShoppingZxypEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ShopCart;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ShopZxxmCart;
import com.huidaxuan.ic2cloud.meituanshoppingcart.pop.CustomPartShadowPopupView;
import com.huidaxuan.ic2cloud.meituanshoppingcart.pop.CustomPartShadowPopupViewZxxm;
import com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.adapter.ComFragmentAdapter;
import com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.fragment.StoreDetailsServiceFragment;
import com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.fragment.StoreDetailsZsypFragment;
import com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.fragment.StoreDetailsZxxmFragment;
import com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.util.ScreenUtil;
import com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.wiget.scrollview.JudgeNestedScrollView;
import com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.wiget.scrollview.cpt.ColorFlipPagerTitleView;
import com.huidaxuan.ic2cloud.meituanshoppingcart.util.StatusBarUtil;
import com.huidaxuan.ic2cloud.meituanshoppingcart.util.Tool;
import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * @packageName:com.huidaxuan.ic2cloud.app2c.view.activity
 * @className: StoreDetailsQmActivityBf
 * @description:门店详情-车主生活,需要和Tab联动动态替换底部购物车
 * @author: dingchao
 * @time: 2020-11-16 11:13
 */
public class StoreDetailsOwnerActivity extends BaseTrDarkActivity implements View.OnClickListener {
    @BindView(R.id.v_include_status_bar_height_dynamic)
    View v_include_status_bar_height_dynamic;

    @BindView(R.id.tv_app_title_text)
    TextView tv_app_title_text;
    @BindView(R.id.iv_app_title_left)
    ImageView iv_app_title_left;
    @BindView(R.id.iv_app_title_right)
    ImageView iv_app_title_right;
    @BindView(R.id.rl_app_title_return)
    RelativeLayout rl_app_title_return;

    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.scrollView)
    JudgeNestedScrollView scrollView;

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.magic_indicator_title)
    MagicIndicator magicIndicatorTitle;
    int toolBarPositionY = 0;
    private int mOffset = 0;
    private int mScrollY = 0;

    //动态设置title
    private String[] mTitles = new String[]{"全部套餐", "自选项目", "装饰用品"};
    private List<String> mDataList = Arrays.asList(mTitles);
    List<Fragment> fragments = new ArrayList<>();
    private String fragmentName = "";

    @BindView(R.id.rl_bottom_shopping_cart_activity)
    RelativeLayout rl_bottom_shopping_cart_activity;
    @BindView(R.id.tv_shopping_cart_count_1)
    TextView tv_shopping_cart_count;
    //购物车无数据时要隐藏处理
    @BindView(R.id.tv_shopping_cart_money_1)
    TextView tv_shopping_cart_money;
    @BindView(R.id.btn_shopping_cart_pay_1)
    Button btn_shopping_cart_pay;
    @BindView(R.id.rl_bottom_shopping_cart_activity2)
    RelativeLayout rl_bottom_shopping_cart_activity2;
    @BindView(R.id.tv_shopping_cart_count_2)
    TextView tv_shopping_cart_count2;
    //购物车无数据时要隐藏处理
    @BindView(R.id.tv_shopping_cart_money_2)
    TextView tv_shopping_cart_money2;
    @BindView(R.id.btn_shopping_cart_pay_2)
    Button btn_shopping_cart_pay2;
    @BindView(R.id.tv_shopping_cart_msg_2)
    TextView tv_shopping_cart_msg_2;

    private ShopCart shopCart;
    private ShopZxxmCart shopZxxmCart;

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_app_title_return:
                finish();
                break;
            case R.id.rl_bottom_shopping_cart_activity://打开装饰用品的购物车
                //获取屏幕的高度，然后拿到百分之70
                int popHeight = (int) (Tool.getWindowHeight(StoreDetailsOwnerActivity.this) * 0.7);
                if (shopCart != null && shopCart.getShoppingAccount() > 0) {
                    new XPopup.Builder(StoreDetailsOwnerActivity.this)
                            .atView(v)
                            .maxHeight(popHeight)
                            .isRequestFocus(false)
                            .asCustom(new CustomPartShadowPopupView(StoreDetailsOwnerActivity.this, shopCart))
                            .show();
                }
                break;
            case R.id.rl_bottom_shopping_cart_activity2://打开装饰用品的购物车
                //获取屏幕的高度，然后拿到百分之70
                int popHeight2 = (int) (Tool.getWindowHeight(StoreDetailsOwnerActivity.this) * 0.7);
                if (shopZxxmCart != null && shopZxxmCart.getShoppingSingle().size() > 0) {
                    new XPopup.Builder(StoreDetailsOwnerActivity.this)
                            .atView(v)
                            .maxHeight(popHeight2)
                            .isRequestFocus(false)
                            .asCustom(new CustomPartShadowPopupViewZxxm(StoreDetailsOwnerActivity.this, shopZxxmCart))
                            .show();
                }
                break;
        }
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_store_details_qm;
    }

    @Override
    protected void initEvent() {
        StatusBarUtil.setStatusBarDarkTheme(StoreDetailsOwnerActivity.this, false);//白色
        Tool.setStatusBarHeight(StoreDetailsOwnerActivity.this, v_include_status_bar_height_dynamic);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//eventbus解绑
    }

    @Override
    protected void noNetWork() {

    }

    @Override
    protected void initListener() {
        rl_app_title_return.setOnClickListener(this);
        rl_bottom_shopping_cart_activity.setOnClickListener(this);
        rl_bottom_shopping_cart_activity2.setOnClickListener(this);
        initView();
    }

    private void initView() {
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;
                ivHeader.setTranslationY(mOffset - mScrollY);
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;
                ivHeader.setTranslationY(mOffset - mScrollY);
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }
        });
        refreshLayout.setEnableRefresh(false);//禁用下拉

        //判断是否是华为手机并且是否有虚拟导航键
//        if (DeviceUtil.isHUAWEI() && DeviceUtil.checkDeviceHasNavigationBar(this.getApplicationContext())) {
//            getContentResolver().registerContentObserver(Settings.System.getUriFor
//                    ("navigationbar_is_min"), true, mNavigationStatusObserver);
//        }
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                dealWithViewPager();
            }
        });
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int lastScrollY = 0;
            int h = DensityUtil.dp2px(211);
            int color = ContextCompat.getColor(getApplicationContext(), R.color.color_ffffff) & 0x00ffffff;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] location = new int[2];
                magicIndicator.getLocationOnScreen(location);
                int yPosition = location[1];
                if (yPosition < toolBarPositionY) {
                    magicIndicatorTitle.setVisibility(View.VISIBLE);
                    magicIndicatorTitle.setBackgroundResource(R.color.color_ffffff);
                    scrollView.setNeedScroll(false);
                } else {
                    magicIndicatorTitle.setVisibility(View.GONE);
                    scrollView.setNeedScroll(true);

                }

                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    tv_app_title_text.setAlpha(1f * mScrollY / h);
                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                    ivHeader.setTranslationY(mOffset - mScrollY);
                }
                if (scrollY == 0) {
                    iv_app_title_left.setImageResource(R.mipmap.back_white);
                    iv_app_title_right.setImageResource(R.mipmap.sc_white);
//                    ivMenu.setImageResource(R.drawable.icon_menu_white);
                } else {
                    iv_app_title_left.setImageResource(R.mipmap.iv_app_return);
                    iv_app_title_right.setImageResource(R.mipmap.sc_black);
//                    ivMenu.setImageResource(R.drawable.icon_menu_black);
                }

                lastScrollY = scrollY;
            }
        });
        tv_app_title_text.setAlpha(0);
        toolbar.setBackgroundColor(0);


        viewPager.setAdapter(new ComFragmentAdapter(getSupportFragmentManager(), getFragments()));
        viewPager.setOffscreenPageLimit(10);
        initMagicIndicator();
        initMagicIndicatorTitle();
    }

    /**
     * 如果盖着，自己根据自己的布局调高度
     */
    private void dealWithViewPager() {
        toolBarPositionY = toolbar.getHeight();
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = ScreenUtil.getScreenHeightPx(getApplicationContext())
                - toolBarPositionY
                - magicIndicator.getHeight() + 1;
        viewPager.setLayoutParams(params);
    }

    /**
     * 配置fragment，动态配置
     *
     * @return
     */
    private List<Fragment> getFragments() {
        fragments.clear();
        fragments.add(new StoreDetailsServiceFragment());//全部-无购物车，
        fragments.add(new StoreDetailsZxxmFragment());//自选项目-购物车数量1
        fragments.add(new StoreDetailsZsypFragment());//装饰用品-商品-正常购物车
        return fragments;
    }

    private void initMagicIndicatorTitle() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdjustMode(true);

        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(StoreDetailsOwnerActivity.this, R.color.color_222222));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(StoreDetailsOwnerActivity.this, R.color.color_222222));
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectFragment(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                //方法抽取
                return indicatorSetting(context);
            }
        });
        magicIndicatorTitle.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicatorTitle, viewPager);

    }

    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(StoreDetailsOwnerActivity.this, R.color.color_333333));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(StoreDetailsOwnerActivity.this, R.color.color_333333));
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectFragment(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                //方法抽取
                return indicatorSetting(context);
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    /**
     * fragment 切换，要设置对应的底部购物车显示
     *
     * @param index
     */
    private void selectFragment(int index) {
        viewPager.setCurrentItem(index, false);
        //根据fragment的名字判断
        Log.e("selectFragment", "---------name:" + fragments.get(index).getClass().getName());
        if (fragments.get(index).getClass().getName().equals("com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.fragment.StoreDetailsServiceFragment")) {//全部套餐
            rl_bottom_shopping_cart_activity.setVisibility(View.GONE);
            rl_bottom_shopping_cart_activity2.setVisibility(View.GONE);
            fragmentName = "com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.fragment.StoreDetailsServiceFragment";
        } else if (fragments.get(index).getClass().getName().equals("com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.fragment.StoreDetailsZxxmFragment")) {//自选项目
            //显示2,隐藏另一个
            rl_bottom_shopping_cart_activity2.setVisibility(View.VISIBLE);
            rl_bottom_shopping_cart_activity.setVisibility(View.GONE);
            fragmentName = "com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.fragment.StoreDetailsZxxmFragment";
        } else if (fragments.get(index).getClass().getName().equals("com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.fragment.StoreDetailsZsypFragment")) {//装饰用品
            //装饰用品-商品的：显示商品购物车，同时设置距离底部41dp(2020-11-23:底部高度放在)adapter中设置，在这里就不设置了
            rl_bottom_shopping_cart_activity.setVisibility(View.VISIBLE);
            rl_bottom_shopping_cart_activity2.setVisibility(View.GONE);
            fragmentName = "com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.fragment.StoreDetailsZsypFragment";
        }
    }

    /**
     * TabLayout底部横线绘制抽取
     *
     * @param context
     * @return
     */
    private IPagerIndicator indicatorSetting(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineHeight(UIUtil.dip2px(context, 2));
        indicator.setLineWidth(UIUtil.dip2px(context, 60));
        indicator.setRoundRadius(UIUtil.dip2px(context, 3));
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
        indicator.setColors(ContextCompat.getColor(StoreDetailsOwnerActivity.this, R.color.color_fe3939));
        return indicator;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void savedInstanceState(Bundle savedInstanceState) {

    }


    //定义处理接收的方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusShoppingZxypEntity entity) {
        Log.e("onEvent", "---------------key" + entity.getKey());
        ////选中装饰用品时-更新操作,类名比对说明选中当前
        if (entity.getKey().equals("ZsypUpdateCount") && fragmentName.equals("com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.fragment.StoreDetailsZsypFragment")) {
            shopCart = entity.getShopCart();
            showTotalPrice(entity.getShopCart());
        } else if (entity.getKey().equals("ZxxmUpdateCount") && fragmentName.equals("com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.fragment.StoreDetailsZxxmFragment")) {
            //自选项目
            shopZxxmCart = entity.getShopZxxmCart();
            showTotalPrice2(entity.getShopZxxmCart());
        }
    }

    /**
     * 装饰用品的时候，更新底部购物车操作
     *
     * @param shopCart
     */
    private void showTotalPrice(ShopCart shopCart) {
        if (shopCart != null && shopCart.getShoppingTotalPrice() > 0) {
            tv_shopping_cart_money.setVisibility(View.VISIBLE);
            tv_shopping_cart_money.setText("￥ " + shopCart.getShoppingTotalPrice());
            tv_shopping_cart_count.setVisibility(View.VISIBLE);
            //得到总的数量
            int textCount = 0;
            for (ProductListEntity.ProductEntity m : shopCart.getShoppingSingle().keySet()) {
                Log.e("btn_shopping_cart_pay", "map集合中存储的数据---->" + m.getProductCount());
                textCount += m.getProductCount();
            }
            tv_shopping_cart_count.setText("" + textCount);
        } else {
            tv_shopping_cart_money.setVisibility(View.INVISIBLE);
            tv_shopping_cart_count.setVisibility(View.GONE);
        }
    }

    /**
     * 自选项目的时候，更新底部购物车操作
     *
     * @param shopCart
     */
    private void showTotalPrice2(ShopZxxmCart shopCart) {
        if (shopCart != null && shopCart.getShoppingSingle().size() > 0) {
            tv_shopping_cart_money2.setVisibility(View.VISIBLE);
            tv_shopping_cart_count2.setVisibility(View.VISIBLE);
            //得到总的数量
            int textCount = 0;
            double totalPrict = 0.0;
            for (ProductListEntity.ProductEntity m : shopCart.getShoppingSingle().values()) {
                Log.e("btn_shopping_cart_pay", "map集合中存储的数据---->" + m.getProductCount());
                textCount += m.getProductCount();
                totalPrict += m.getProductMoney();
            }

            tv_shopping_cart_money2.setText("￥ " + totalPrict);
            tv_shopping_cart_count2.setText("" + textCount);
            tv_shopping_cart_msg_2.setText("包含工时费：待定");
        } else {
            tv_shopping_cart_money2.setVisibility(View.INVISIBLE);
            tv_shopping_cart_count2.setVisibility(View.GONE);
        }
    }
}
