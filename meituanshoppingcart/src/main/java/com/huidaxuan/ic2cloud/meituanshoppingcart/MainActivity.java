package com.huidaxuan.ic2cloud.meituanshoppingcart;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.huidaxuan.ic2cloud.meituanshoppingcart.adapter.LeftProductTypeAdapter;
import com.huidaxuan.ic2cloud.meituanshoppingcart.adapter.RightProductAdapter;
import com.huidaxuan.ic2cloud.meituanshoppingcart.base.BaseActivity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.EventBusShoppingEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ShopCart;
import com.huidaxuan.ic2cloud.meituanshoppingcart.imp.ShopCartImp;
import com.huidaxuan.ic2cloud.meituanshoppingcart.pop.CustomPartShadowPopupView;
import com.huidaxuan.ic2cloud.meituanshoppingcart.util.ToastUtil;
import com.huidaxuan.ic2cloud.meituanshoppingcart.util.Tool;
import com.lxj.xpopup.XPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements LeftProductTypeAdapter.onItemSelectedListener, ShopCartImp, View.OnClickListener {
    @BindView(R.id.left_menu)//左侧列表
            RecyclerView leftMenu;
    @BindView(R.id.right_menu)//右侧列表
            RecyclerView rightMenu;
    @BindView(R.id.right_menu_item)//右侧标题整体
            LinearLayout headerLayout;
    @BindView(R.id.right_menu_tv)//右侧标题
            TextView headerView;

    private LeftProductTypeAdapter leftAdapter;
    private RightProductAdapter rightAdapter;

    ArrayList<ProductListEntity> productListEntities;
    ProductListEntity headMenu;

    private boolean leftClickType = false;//左侧菜单点击引发的右侧联动

    @BindView(R.id.rl)//动画效果二级列表 父容器
            RelativeLayout rl;
    @BindView(R.id.iv_shopping_cart_img)//动画效果底部购物车图标，最终落入的地方
            ImageView iv_shopping_cart_img;
    private PathMeasure mPathMeasure;
    /**
     * 贝塞尔曲线中间过程的点的坐标
     */
    private float[] mCurrentPosition = new float[2];
    @BindView(R.id.tv_shopping_cart_count)
    TextView tv_shopping_cart_count;

    //购物车无数据时要隐藏处理
    @BindView(R.id.tv_shopping_cart_money)
    TextView tv_shopping_cart_money;
    ShopCart shopCart;
    @BindView(R.id.btn_shopping_cart_pay)
    Button btn_shopping_cart_pay;
    @BindView(R.id.rl_bottom_shopping_cart)
    RelativeLayout rl_bottom_shopping_cart;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEvent() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void noNetWork() {

    }

    @Override
    protected void initListener() {
        btn_shopping_cart_pay.setOnClickListener(this);
        rl_bottom_shopping_cart.setOnClickListener(this);
        leftMenu.setLayoutManager(new LinearLayoutManager(this));
        rightMenu.setLayoutManager(new LinearLayoutManager(this));
        headerLayout.setVisibility(View.VISIBLE);

//        StickyHeaderLayoutManager stickyHeaderLayoutManager = new StickyHeaderLayoutManager();
//        rightMenu.setLayoutManager(stickyHeaderLayoutManager);
        //右侧列表监听
        rightMenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.canScrollVertically(1) == false) {//无法下滑
                    showHeadView();
                    return;
                }

                View underView = null;
                if (dy > 0) {
                    underView = rightMenu.findChildViewUnder(headerLayout.getX(), headerLayout.getMeasuredHeight() + 1);
                } else {
                    underView = rightMenu.findChildViewUnder(headerLayout.getX(), 0);
                }

                if (underView != null && underView.getContentDescription() != null) {
                    int position = Integer.parseInt(underView.getContentDescription().toString());
                    ProductListEntity menu = rightAdapter.getMenuOfMenuByPosition(position);

                    if (leftClickType || !menu.getTypeName().equals(headMenu.getTypeName())) {
                        if (dy > 0 && headerLayout.getTranslationY() <= 1 && headerLayout.getTranslationY() >= -1 * headerLayout.getMeasuredHeight() * 4 / 5 && !leftClickType) {// underView.getTop()>9
                            int dealtY = underView.getTop() - headerLayout.getMeasuredHeight();
                            headerLayout.setTranslationY(dealtY);
//                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        } else if (dy < 0 && headerLayout.getTranslationY() <= 0 && !leftClickType) {
                            headerView.setText(menu.getTypeName());
                            int dealtY = underView.getBottom() - headerLayout.getMeasuredHeight();
                            headerLayout.setTranslationY(dealtY);
                        } else {
                            headerLayout.setTranslationY(0);
                            headMenu = menu;
                            headerView.setText(headMenu.getTypeName());
                            for (int i = 0; i < productListEntities.size(); i++) {
                                if (productListEntities.get(i) == headMenu) {
                                    leftAdapter.setSelectedNum(i);
                                    break;
                                }
                            }
                            if (leftClickType) leftClickType = false;
                        }
                    }
                }

            }
        });
    }

    /**
     * 显示标题
     */
    private void showHeadView() {
        headerLayout.setTranslationY(0);
        View underView = rightMenu.findChildViewUnder(headerLayout.getX(), 0);
        if (underView != null && underView.getContentDescription() != null) {
            int position = Integer.parseInt(underView.getContentDescription().toString());
            ProductListEntity entity = rightAdapter.getMenuOfMenuByPosition(position + 1);
            headMenu = entity;
            headerView.setText(headMenu.getTypeName());
            for (int i = 0; i < productListEntities.size(); i++) {
                if (productListEntities.get(i) == headMenu) {
                    leftAdapter.setSelectedNum(i);
                    break;
                }
            }
        }
    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
        //列表数据初始化
        initListData();
    }


    /**
     * 虚拟列表数据
     */
    private void initListData() {
        productListEntities = new ArrayList<>();
        shopCart = new ShopCart();

        List<ProductListEntity.ProductEntity> productEntities1 = new ArrayList<>();
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃1-1", "34", 10.0, 0, "1", "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃2-1", "34", 20.0, 0, "2", "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃3-1", "34", 30.0, 0, "3", "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃4-1", "34", 40.0, 0, "4", "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃5-1", "34", 50.0, 0, "5", "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃6-1", "34", 50.0, 0, "6", "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃7-1", "34", 50.0, 0, "7", "1"));

        List<ProductListEntity.ProductEntity> productEntities2 = new ArrayList<>();
        productEntities2.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃1-2", "34", 10.0, 0, "6", "2"));
        productEntities2.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃2-2", "34", 20.0, 0, "7", "2"));
        productEntities2.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃3-2", "34", 30.0, 0, "8", "2"));
        productEntities2.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃4-2", "34", 40.0, 0, "9", "2"));
        productEntities2.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃5-2", "34", 50.0, 0, "10", "2"));

        List<ProductListEntity.ProductEntity> productEntities3 = new ArrayList<>();
        productEntities3.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃1-3", "34", 10.0, 0, "6", "3"));
        productEntities3.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃2-3", "34", 20.0, 0, "7", "3"));
        productEntities3.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃3-3", "34", 30.0, 0, "8", "3"));
        productEntities3.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃4-3", "34", 40.0, 0, "9", "3"));
        productEntities3.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃5-3", "34", 50.0, 0, "10", "3"));

        List<ProductListEntity.ProductEntity> productEntities4 = new ArrayList<>();
        productEntities4.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃1-4", "34", 10.0, 0, "6", "4"));
        productEntities4.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃2-4", "34", 20.0, 0, "7", "4"));
        productEntities4.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃3-4", "34", 30.0, 0, "8", "4"));
        productEntities4.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃4-4", "34", 40.0, 0, "9", "4"));
        productEntities4.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃5-4", "34", 50.0, 0, "10", "4"));

        List<ProductListEntity.ProductEntity> productEntities5 = new ArrayList<>();
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃1-5", "34", 10.0, 0, "1", "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃2-5", "34", 20.0, 0, "2", "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃3-5", "34", 30.0, 0, "3", "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃4-5", "34", 40.0, 0, "4", "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃5-5", "34", 50.0, 0, "5", "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃6-5", "34", 50.0, 0, "6", "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "新上市猕猴桃7-5", "34", 50.0, 0, "7", "5"));

        productListEntities.add(new ProductListEntity("1", "热销水果", productEntities1));
        productListEntities.add(new ProductListEntity("2", "鲜果制作", productEntities2));
        productListEntities.add(new ProductListEntity("3", "特色零食", productEntities3));
        productListEntities.add(new ProductListEntity("4", "水果盒子", productEntities4));
        productListEntities.add(new ProductListEntity("5", "新榨果汁", productEntities5));
        Log.e("jsonjson", "--------json:" + JSON.toJSONString(productListEntities));


        //设置数据源，数据绑定展示
        leftAdapter = new LeftProductTypeAdapter(MainActivity.this, productListEntities);
        rightAdapter = new RightProductAdapter(MainActivity.this, productListEntities, shopCart);


        rightMenu.setAdapter(rightAdapter);
        leftMenu.setAdapter(leftAdapter);
        //左侧列表单项选择
        leftAdapter.addItemSelectedListener(this);
        rightAdapter.setShopCartImp(this);
        //设置初始头部
        initHeadView();
    }


    /**
     * 初始头部
     */
    private void initHeadView() {
        headMenu = rightAdapter.getMenuOfMenuByPosition(0);
        headerLayout.setContentDescription("0");
        headerView.setText(headMenu.getTypeName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//eventbus解绑
        leftAdapter.removeItemSelectedListener(this);
    }

    /**
     * 左侧列表单项选中
     *
     * @param position
     * @param menu
     */
    @Override
    public void onLeftItemSelected(int position, ProductListEntity menu) {
        int sum = 0;
        for (int i = 0; i < position; i++) {
            sum += productListEntities.get(i).getProductEntities().size() + 1;
        }
//        StickyHeaderLayoutManager layoutManager = (StickyHeaderLayoutManager) rightMenu.getLayoutManager();
        LinearLayoutManager layoutManager = (LinearLayoutManager) rightMenu.getLayoutManager();
        rightMenu.scrollToPosition(position);
        layoutManager.scrollToPositionWithOffset(sum, 0);
        leftClickType = true;
    }

    /**
     * 购物车+
     *
     * @param view
     * @param position
     */
    @Override
    public void add(View view, int position, ProductListEntity.ProductEntity entity) {
        addCart(view, entity);
    }

    //加入购物车曲线动画
    private void addCart(View view, ProductListEntity.ProductEntity entity) {
//   一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final ImageView goods = new ImageView(MainActivity.this);
        goods.setImageDrawable(getResources().getDrawable(R.drawable.shape_shopping_cart_num_bg, null));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        rl.addView(goods, params);

//    二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        rl.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        view.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        iv_shopping_cart_img.getLocationInWindow(endLoc);


//    三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0] + goods.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + goods.getHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + iv_shopping_cart_img.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

//    四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(500);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
//   五、 开始执行动画
        valueAnimator.start();

//   六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                //更新底部数据
                showTotalPrice(entity);
                // 把移动的图片imageview从父布局里移除
                rl.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 底部价格和数量显示
     */
    private void showTotalPrice(ProductListEntity.ProductEntity entity) {
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
        updateLeftCount(entity);
    }

    /**
     * 更新左侧数字角标(暂时不包含清空)，触发更新肯定是在加或者减的时候触发,根据子项中的父ID和左侧ID比对，
     */
    private void updateLeftCount(ProductListEntity.ProductEntity entity) {
        if (shopCart != null) {
            //加和减的时候要知道是那个左侧下边的,知道下标获取父id,然后从map中取count
            if (entity != null) {
                Log.e("updateLeftCount", "-------parentId:" + entity.getParentId() + "---------count:" + shopCart.getParentCountMap().get(entity.getParentId()));
                leftAdapter.setUpdateMenuCount(entity.getParentId(), shopCart.getParentCountMap().get(entity.getParentId()));
            }
            if (rightAdapter != null) rightAdapter.notifyDataSetChanged();//跟新列表
        }
    }

    /**
     * 购物车减
     *
     * @param view
     * @param position
     */
    @Override
    public void remove(View view, int position, ProductListEntity.ProductEntity en) {
        showTotalPrice(en);
    }

    XPopup popup;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_shopping_cart_pay:
                //结算的商品列表
                ToastUtil.showShort(MainActivity.this, "dianjile");
                if (shopCart.getShoppingSingle().size() > 0) {
                    List<ProductListEntity.ProductEntity> commitListData = new ArrayList<>();
                    for (ProductListEntity.ProductEntity m : shopCart.getShoppingSingle().keySet()) {
                        Log.e("btn_cart_pay", "map集合中存储的数据---->" + m.getProductCount());
                        commitListData.add(m);
                    }
                    for (int i = 0; i < commitListData.size(); i++) {
                        Log.e("btn_cart_pay_list", "commitList---->" + commitListData.get(i));
                    }
                    Log.e("btn_cart_pay_list_JSON", "commitList---->" + JSON.toJSONString(commitListData));
                }

                break;
            case R.id.rl_bottom_shopping_cart://打开购物车
                Log.e("getWindowHeight", "---------height:" + Tool.getWindowHeight(MainActivity.this));
                //获取屏幕的高度，然后拿到百分之70
                int popHeight = (int) (Tool.getWindowHeight(MainActivity.this) * 0.7);
                if (shopCart != null && shopCart.getShoppingAccount() > 0) {
                    new XPopup.Builder(MainActivity.this)
                            .atView(view)
                            .maxHeight(popHeight)
                            .isRequestFocus(false)
                            .asCustom(new CustomPartShadowPopupView(MainActivity.this, shopCart))
                            .show();
                }
                break;
            default:
                break;
        }
    }


    /**
     * 清空购物车及左侧列表都角标和商品列表
     */
    private void clearCartDataAndListData() {
        shopCart.clear();
        shopCart.getParentCountMap().clear();
        showTotalPrice(null);
        //左侧清空
        leftAdapter.setClearCount();
    }

    //定义处理接收的方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusShoppingEntity entity) {
        if (entity.getKey().equals("add")) {
            showTotalPrice(entity.getEntity());
        } else if (entity.getKey().equals("reduce")) {
            showTotalPrice(entity.getEntity());
        } else if (entity.getKey().equals("clearAll")) {//清空全部
            clearCartDataAndListData();
        }
    }

}
