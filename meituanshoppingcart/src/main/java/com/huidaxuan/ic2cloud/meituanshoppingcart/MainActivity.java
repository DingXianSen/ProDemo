package com.huidaxuan.ic2cloud.meituanshoppingcart;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huidaxuan.ic2cloud.meituanshoppingcart.adapter.LeftProductTypeAdapter;
import com.huidaxuan.ic2cloud.meituanshoppingcart.adapter.RightProductAdapter;
import com.huidaxuan.ic2cloud.meituanshoppingcart.base.BaseActivity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.headerManager.RecyclerViewLayoutManager;
import com.huidaxuan.ic2cloud.meituanshoppingcart.util.ToastUtil;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;

public class MainActivity extends BaseActivity {
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
    private boolean rightScroll = true;//右侧滑动了标示
    //存储的key的方式，parentId-childId
    private Map<String, ProductListEntity.ProductEntity> stringchildBeanMap = new HashMap<>();

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

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void noNetWork() {

    }

    @Override
    protected void initListener() {
        leftMenu.setLayoutManager(new LinearLayoutManager(this));
        rightMenu.setLayoutManager(new LinearLayoutManager(this));
        headerLayout.setVisibility(View.GONE);

        StickyHeaderLayoutManager stickyHeaderLayoutManager = new StickyHeaderLayoutManager();
//        RecyclerViewLayoutManager stickyHeaderLayoutManager = new RecyclerViewLayoutManager();
//        LinearLayoutManager stickyHeaderLayoutManager = new LinearLayoutManager(MainActivity.this);
        rightMenu.setLayoutManager(stickyHeaderLayoutManager);
        //右侧列表监听
        rightMenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //手指按下的时候为true
                if (newState == SCROLL_STATE_DRAGGING) {
                    rightScroll = true;
                    leftClickType = false;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //右侧滑动，根据下标选中左侧对应的数据
                View underView = null;
                if (dy > 0) {
                    underView = rightMenu.findChildViewUnder(headerLayout.getX(), headerLayout.getMeasuredHeight() + 1);
                } else {
                    underView = rightMenu.findChildViewUnder(headerLayout.getX(), 0);
                }
                if (underView != null && underView.getContentDescription() != null) {
                    //如果滑动了，但是左侧没点击，不触发
                    if (!leftClickType && rightScroll) {
                        int position = Integer.parseInt(underView.getContentDescription().toString());
                        leftAdapter.setSelectedNum(position);
                        if (leftClickType) leftClickType = false;
                    }
                }

            }
        });
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

        List<ProductListEntity.ProductEntity> productEntities1 = new ArrayList<>();
        productEntities1.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃1-1", "34", 10.0, 0, "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃2-1", "34", 20.0, 0, "2"));
        productEntities1.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃3-1", "34", 30.0, 0, "3"));
        productEntities1.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃4-1", "34", 40.0, 0, "4"));
        productEntities1.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃5-1", "34", 50.0, 0, "5"));
        productEntities1.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃6-1", "34", 50.0, 0, "6"));
        productEntities1.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃7-1", "34", 50.0, 0, "7"));

        List<ProductListEntity.ProductEntity> productEntities2 = new ArrayList<>();
        productEntities2.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃1-2", "34", 10.0, 0, "6"));
        productEntities2.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃2-2", "34", 20.0, 0, "7"));
        productEntities2.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃3-2", "34", 30.0, 0, "8"));
        productEntities2.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃4-2", "34", 40.0, 0, "9"));
        productEntities2.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃5-2", "34", 50.0, 0, "10"));

        List<ProductListEntity.ProductEntity> productEntities3 = new ArrayList<>();
        productEntities3.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃1-3", "34", 10.0, 0, "6"));
        productEntities3.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃2-3", "34", 20.0, 0, "7"));
        productEntities3.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃3-3", "34", 30.0, 0, "8"));
        productEntities3.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃4-3", "34", 40.0, 0, "9"));
        productEntities3.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃5-3", "34", 50.0, 0, "10"));

        List<ProductListEntity.ProductEntity> productEntities4 = new ArrayList<>();
        productEntities4.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃1-4", "34", 10.0, 0, "6"));
        productEntities4.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃2-4", "34", 20.0, 0, "7"));
        productEntities4.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃3-4", "34", 30.0, 0, "8"));
        productEntities4.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃4-4", "34", 40.0, 0, "9"));
        productEntities4.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃5-4", "34", 50.0, 0, "10"));

        List<ProductListEntity.ProductEntity> productEntities5 = new ArrayList<>();
        productEntities5.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃1-5", "34", 10.0, 0, "1"));
        productEntities5.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃2-5", "34", 20.0, 0, "2"));
        productEntities5.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃3-5", "34", 30.0, 0, "3"));
        productEntities5.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃4-5", "34", 40.0, 0, "4"));
        productEntities5.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃5-5", "34", 50.0, 0, "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃6-5", "34", 50.0, 0, "6"));
        productEntities5.add(new ProductListEntity.ProductEntity("", "新上市猕猴桃7-5", "34", 50.0, 0, "7"));

        productListEntities.add(new ProductListEntity("1", "热销水果", productEntities1));
        productListEntities.add(new ProductListEntity("2", "鲜果制作", productEntities2));
        productListEntities.add(new ProductListEntity("3", "特色零食", productEntities3));
        productListEntities.add(new ProductListEntity("4", "水果盒子", productEntities4));
        productListEntities.add(new ProductListEntity("5", "新榨果汁", productEntities5));


        //设置数据源，数据绑定展示
        leftAdapter = new LeftProductTypeAdapter(MainActivity.this, productListEntities);
        rightAdapter = new RightProductAdapter(MainActivity.this, productListEntities);


        /**
         * 加减之后购物车数据展示及存储传输给后台的数据集合
         */
        rightAdapter.setOnItemChrldListner(new RightProductAdapter.OnItemChrldListner() {
            @Override
            public void onCall(View view, int sectionIndex, int itemIndex, String type, ProductListEntity.ProductEntity entity) {
                String mapKey = sectionIndex + "-" + itemIndex;
                //根据type来区分是加还是减操作
                if (type.equals("add")) {
                    //加操作,因为加 购物车的动画效果只有在列表添加，所以把方法抽取出来
                    shoppingCartCountAdd(mapKey, entity);
                    addCart(view);
                } else if (type.equals("reduce")) {
                    //减操作
//                    updateShoppingCartCountMap(mapKey, "reduce");
                } else if (type.equals("zero")) {
                    //归零操作
//                    updateShoppingCartCountMap(mapKey, "zero");
                }
            }
        });

        //左侧列表点击，刷新右侧列表数据
        leftAdapter.setOnItemChrldListner(new LeftProductTypeAdapter.OnItemChrldListner() {
            @Override
            public void onCall(View view, int sectionIndex) {
                leftClickType = true;
//                leftAdapter.setSelectedNum(sectionIndex);
                //当前的父级下标，对应这右侧的父级下标，这里不应该是滑动过去，应该是点击左侧直接定位过去
                rightAdapter.setSelectedNum(sectionIndex);
                //知道是哪一项，是否可以根据列表的条目判断，当前下标之前的数据对应的position就是要滑动过去的区域

//                ToastUtil.showShort(MainActivity.this, "置顶第:" + getPositionByLeftClick(sectionIndex) + "个，内容为：" + productListEntities.get(getPositionByLeftClick(sectionIndex)) + "--》的数据");
                ToastUtil.showShort(MainActivity.this, "置顶第:" + getPositionByLeftClick(sectionIndex) + "个");
//                rightMenu.scrollToPosition(getPositionByLeftClick(sectionIndex));
//                rightMenu.smoothScrollToPosition(getPositionByLeftClick(sectionIndex));

//                ((StickyHeaderLayoutManager) rightMenu.getLayoutManager()).scrollToPositionWithOffset(0, 0);


//                if (getPositionByLeftClick(sectionIndex) != -1) {
//                    smoothMoveToPosition(rightMenu,getPositionByLeftClick(sectionIndex));
//                }else {
//                    smoothMoveToPosition(rightMenu,getPositionByLeftClick(sectionIndex)+1);
//                }
            }
        });


        rightMenu.setAdapter(rightAdapter);
        leftMenu.setAdapter(leftAdapter);
        //左侧列表单项选择
//        leftAdapter.addItemSelectedListener(this);

        //设置初始头部
//        initHeadView();
    }


    /**
     * 当前选中之前有多少子项，这样就可以得到要滑动多少下标
     *
     * @param position
     * @return
     */
    private int getPositionByLeftClick(int position) {
        int jumpPosition = 0;
        int saveI = 0;
        for (int i = 0; i < position; i++) {
            jumpPosition += productListEntities.get(i).getProductEntities().size() + 1;
            saveI = i;
        }
//        if (jumpPosition != 0) {
//            jumpPosition = jumpPosition - saveI;
//        }

        Log.e("sectionIndexs", "jumpPosition------>" + jumpPosition);
        return jumpPosition;
    }

    /**
     * 抽取增加操作，
     *
     * @param mapKey
     * @param flag
     */
    private void updateShoppingCartCountMap(String mapKey, String flag) {
        if (stringchildBeanMap.containsKey(mapKey)) {
            //存在
            ProductListEntity.ProductEntity childBean = stringchildBeanMap.get(mapKey);
            int count = 0;
            if (flag.equals("add")) {
                childBean.setProductCount(childBean.getProductCount() + 1);
                stringchildBeanMap.put(mapKey, childBean);
                //列表刷新
//                refreshDataSource(mapKey, childBean.getProductCount());
            } else if (flag.equals("reduce")) {

            } else if (flag.equals("zero")) {

            }
        }
    }

    /**
     * 列表UI刷新，为什么抽取出来，当直接点击列表的时候可以通过适配器刷新，但是还有底部弹窗可以操作数量，所以要抽取
     *
     * @param mapKey       父下标，就是真是数据的真是下标，直接刷
     * @param productCount
     */
    private void refreshDataSource(String mapKey, int productCount) {
        String sectionIndex = mapKey.substring(0, mapKey.indexOf("-"));
        String itemIndex = mapKey.substring(mapKey.indexOf("-") + 1, mapKey.indexOf("-") + 2);
        //因为mapKey存储到是组Index-子项Index 所以可以通过拆分得到这两个下标进行刷新
//        productListEntities.get().setCount(count);
//        adapter.notifyDataSetChanged();

        //首先得知道是那一组数据，然后再在组里找是那一条数据，然后替换count进行刷新
        ProductListEntity listEntity = getMenuByPosition(Integer.parseInt(sectionIndex));


        ProductListEntity.ProductEntity entity = getDishByPosition(Integer.parseInt(sectionIndex));
    }

    List<ProductListEntity.ProductEntity> allList = new ArrayList<>();


    /**
     * 借助adapter中的方法来知道刷新那个数据，获取分类对象
     *
     * @param position
     * @return
     */
    public ProductListEntity getMenuByPosition(int position) {
        int sum = 0;
        for (ProductListEntity menu : productListEntities) {
            if (position == sum) {
                return menu;
            }
            sum += menu.getProductEntities().size() + 1;
        }
        return null;
    }

    public int getMenuPositionByPosition(int position) {
        int sum = 0;
        for (ProductListEntity menu : productListEntities) {
            if (position == sum) {
                return sum;
            }
            sum += menu.getProductEntities().size() + 1;
        }
        return sum;
    }


    /**
     * 借助adapter中的方法来知道刷新那个数据，获取对象
     *
     * @param position
     * @return
     */
    public ProductListEntity.ProductEntity getDishByPosition(int position) {
        for (ProductListEntity menu : productListEntities) {
            if (position > 0 && position <= menu.getProductEntities().size()) {
                return menu.getProductEntities().get(position - 1);
            } else {
                position -= menu.getProductEntities().size() + 1;
            }
        }
        return null;
    }

    /**
     * 添加购物车动画
     *
     * @param view
     */
    private void addCart(View view) {
//   一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final ImageView goods = new ImageView(MainActivity.this);
//        goods.setImageDrawable(iv.getDrawable());
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
                getMapCountAndMoney(stringchildBeanMap);
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
     * 更新购物车底部的数据
     *
     * @param stringchildBeanMap
     */
    private void getMapCountAndMoney(Map<String, ProductListEntity.ProductEntity> stringchildBeanMap) {
        int textCount = 0;
        double allMoney = 0.00;
        //遍历Map,然后得到count
        for (ProductListEntity.ProductEntity m : stringchildBeanMap.values()) {
            Log.e("btn_shopping_cart_pay", "map集合中存储的数据---->" + m.getProductName() + "--->X" + m.getProductCount());
            textCount += m.getProductCount();
            allMoney += (m.getProductCount() * m.getProductCount());
        }
        tv_shopping_cart_count.setText("" + textCount);
        tv_shopping_cart_money.setText("¥" + allMoney);
        if (textCount > 0) {//显示
            tv_shopping_cart_count.setVisibility(View.VISIBLE);
            tv_shopping_cart_money.setVisibility(View.VISIBLE);
        } else {//隐藏处理
            tv_shopping_cart_count.setVisibility(View.INVISIBLE);
            tv_shopping_cart_money.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 购物车+效果和数据操作
     *
     * @param mapKey
     * @param entity
     */
    private void shoppingCartCountAdd(String mapKey, ProductListEntity.ProductEntity entity) {
        //如果集合中存在则增加数量，否则add一条数据之后在进行数量改变
        if (stringchildBeanMap.containsKey(mapKey)) {//存在，因为eventbus还需要公用方法，所以不能直接替换对象
            //改变数量，根据key获取对象，然后set更改
            updateShoppingCartCountMap(mapKey, "add");
        } else {//不存在，添加一条数据
            stringchildBeanMap.put(mapKey, entity);
            getMapCountAndMoney(stringchildBeanMap);
        }
    }




    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }
}
