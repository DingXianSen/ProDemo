package com.huidaxuan.ic2cloud.prodemo.grouplist;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.huidaxuan.ic2cloud.prodemo.R;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @packageName:com.huidaxuan.ic2cloud.prodemo
 * @className: GroupListActivity
 * @description:分组列表
 * @author: dingchao
 * @time: 2020-08-03 09:17
 */
public class GroupListActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    //存储的key的方式，parentId-childId
    private Map<String, GroupListEntity.childBean> stringchildBeanMap = new HashMap<>();

    //去支付
    private Button btn_shopping_cart_pay;
    private RelativeLayout rl_bottom_shopping_cart;
    private RelativeLayout rl;
    private ImageView iv_shopping_cart_img;
    private PathMeasure mPathMeasure;
    /**
     * 贝塞尔曲线中间过程的点的坐标
     */
    private float[] mCurrentPosition = new float[2];
    private TextView tv_shopping_cart_count;

    //购物车无数据时要隐藏处理
    private TextView tv_shopping_cart_money;
    private TextView tv_shopping_cart_msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_recyclerview);
        initBindView();


    }

    private void initRecyclerViewDataBind() {
        List<GroupListEntity.childBean> childBeans1 = new ArrayList<>();
        childBeans1.add(new GroupListEntity.childBean(1, "更换空气滤芯", "有效净化发动机进气", 0, 10.00));
        childBeans1.add(new GroupListEntity.childBean(2, "更换刹车片", "增强制动力，保障行车安全", 0, 20.00));
        childBeans1.add(new GroupListEntity.childBean(3, "更换刹车油", "保证制动性能", 0, 30.00));
        childBeans1.add(new GroupListEntity.childBean(4, "更换空调滤芯", "保证空气清新", 0, 40.00));

        List<GroupListEntity.childBean> childBeans2 = new ArrayList<>();
        childBeans2.add(new GroupListEntity.childBean(1, "更换空气滤芯1", "有效净化发动机进气1", 0, 10.00));
        childBeans2.add(new GroupListEntity.childBean(2, "更换空气滤芯2", "有效净化发动机进气2", 0, 10.00));

        List<GroupListEntity.childBean> childBeans3 = new ArrayList<>();
        childBeans3.add(new GroupListEntity.childBean(1, "更换空气滤芯3", "有效净化发动机进气", 0, 10.00));
        childBeans3.add(new GroupListEntity.childBean(2, "更换刹车片3", "增强制动力，保障行车安全", 0, 20.00));
        childBeans3.add(new GroupListEntity.childBean(3, "更换刹车油3", "保证制动性能", 0, 30.00));
        childBeans3.add(new GroupListEntity.childBean(4, "更换空调滤芯3", "保证空气清新", 0, 40.00));

        List<GroupListEntity.childBean> childBeans4 = new ArrayList<>();
        childBeans4.add(new GroupListEntity.childBean(1, "更换空气滤芯4", "有效净化发动机进气", 0, 10.00));
        childBeans4.add(new GroupListEntity.childBean(2, "更换刹车片4", "增强制动力，保障行车安全", 0, 20.00));
        childBeans4.add(new GroupListEntity.childBean(3, "更换刹车油4", "保证制动性能", 0, 30.00));
        childBeans4.add(new GroupListEntity.childBean(4, "更换空调滤芯4", "保证空气清新", 0, 40.00));

        List<GroupListEntity.childBean> childBeans5 = new ArrayList<>();
        childBeans5.add(new GroupListEntity.childBean(1, "更换空气滤芯5", "有效净化发动机进气", 0, 10.00));
        childBeans5.add(new GroupListEntity.childBean(2, "更换刹车片5", "增强制动力，保障行车安全", 0, 20.00));
        childBeans5.add(new GroupListEntity.childBean(3, "更换刹车油5", "保证制动性能", 0, 30.00));
        childBeans5.add(new GroupListEntity.childBean(4, "更换空调滤芯5", "保证空气清新", 0, 40.00));

        List<GroupListEntity.childBean> childBeans6 = new ArrayList<>();
        childBeans6.add(new GroupListEntity.childBean(1, "更换空气滤芯6", "有效净化发动机进气", 0, 10.00));
        childBeans6.add(new GroupListEntity.childBean(2, "更换刹车片6", "增强制动力，保障行车安全", 0, 20.00));
        childBeans6.add(new GroupListEntity.childBean(3, "更换刹车油6", "保证制动性能", 0, 30.00));
        childBeans6.add(new GroupListEntity.childBean(4, "更换空调滤芯6", "保证空气清新", 0, 40.00));

        final List<GroupListEntity> groupListEntities = new ArrayList<>();

        groupListEntities.add(new GroupListEntity(1, "常规养护", childBeans1));
        groupListEntities.add(new GroupListEntity(2, "空调养护", childBeans2));
        groupListEntities.add(new GroupListEntity(3, "动力提升", childBeans3));
        groupListEntities.add(new GroupListEntity(4, "动力提升1", childBeans4));
        groupListEntities.add(new GroupListEntity(5, "动力提升2", childBeans5));
        groupListEntities.add(new GroupListEntity(6, "动力提升3", childBeans6));


        StickyHeaderLayoutManager stickyHeaderLayoutManager = new StickyHeaderLayoutManager();
        recyclerView.setLayoutManager(stickyHeaderLayoutManager);

        // set a header position callback to set elevation on sticky headers, because why not
        stickyHeaderLayoutManager.setHeaderPositionChangedCallback(new StickyHeaderLayoutManager.HeaderPositionChangedCallback() {
            @Override
            public void onHeaderPositionChanged(int sectionIndex, View header, StickyHeaderLayoutManager.HeaderPosition oldPosition, StickyHeaderLayoutManager.HeaderPosition newPosition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    boolean elevated = newPosition == StickyHeaderLayoutManager.HeaderPosition.STICKY;
                    header.setElevation(elevated ? 8 : 0);
                }
            }
        });
        //设置适配器
        GroupListAdapter adapter = new GroupListAdapter(GroupListActivity.this, groupListEntities);
        recyclerView.setAdapter(adapter);

        //点击处理，这里主要是处理组装购物车的数据集合
        adapter.setOnItemChrldListner(new GroupListAdapter.OnItemChrldListner() {
            @Override
            public void onCall(View view, int sectionIndex, int itemIndex, String type) {
                //获取操作的key
                String mapkey = groupListEntities.get(sectionIndex).getGroupId() + "-" + groupListEntities.get(sectionIndex).getChildBeans().get(itemIndex);
                //根据type判断是加还是减
                if (type.equals("add")) {//加，方法抽取，因为只有在服务列表才有装入购物车特效
                    shoppingCartCountAdd(stringchildBeanMap, groupListEntities, mapkey, sectionIndex, itemIndex);
                    addCart(view);
                } else if (type.equals("reduce")) {//减
                    shoppingCartReduce(stringchildBeanMap, mapkey);
                } else if (type.equals("zero")) {//到0了，需要把
                    shoppingCartRemoveByMapKey(stringchildBeanMap, mapkey);
                }
                //其实就是把二级的列表单项重新加载到一个新到数组，这个数组传到购物车
                Log.e("groupListEntities", "点击了" + groupListEntities.get(sectionIndex).getGroupTitle()
                        + "组的-->" + groupListEntities.get(sectionIndex).getChildBeans().get(itemIndex).getTitle());
            }
        });
    }

    /**
     * 服务清单数量加操作
     *
     * @param stringchildBeanMap 用来暂存加减的Map集合
     * @param groupListEntities  服务清单列表集合
     * @param mapkey             map集合的Key,groupId-childId
     * @param sectionIndex       组ID
     * @param itemIndex          子项ID
     */
    private void shoppingCartCountAdd(Map<String, GroupListEntity.childBean> stringchildBeanMap, List<GroupListEntity> groupListEntities, String mapkey, int sectionIndex, int itemIndex) {
        //如果集合中存在则增加数量，否则add一条数据之后在进行数量改变
        if (stringchildBeanMap.containsKey(mapkey)) {//存在，改变数量
            //改变数量，根据key获取对象，然后set更改
            GroupListEntity.childBean childBean = stringchildBeanMap.get(mapkey);
            childBean.setCount(childBean.getCount() + 1);
            stringchildBeanMap.put(mapkey, childBean);
        } else {//不存在，添加一条数据
            stringchildBeanMap.put(mapkey, groupListEntities.get(sectionIndex).getChildBeans().get(itemIndex));
        }
    }

    /**
     * 服务清单数量减操作
     *
     * @param stringchildBeanMap 用来暂存加减的Map集合
     * @param mapkey             map集合的Key,groupId-childId
     */
    private void shoppingCartReduce(Map<String, GroupListEntity.childBean> stringchildBeanMap, String mapkey) {
        //判断是否存在key
        if (stringchildBeanMap.containsKey(mapkey)) {//存在判断数量
            //因为在adapter中已经处理数量，所以当0 的时候执行zero,所以在这里直接进行减处理
            GroupListEntity.childBean childBean = stringchildBeanMap.get(mapkey);
            //执行减法操作
            childBean.setCount(childBean.getCount() - 1);
            stringchildBeanMap.put(mapkey, childBean);
            //todo 执行获取map集合数量的方法
            getMapCountAndMoney(stringchildBeanMap);
        } else {//不存在，则不处理这条数据
            return;
        }
    }

    /**
     * 服务清单数量归零
     *
     * @param stringchildBeanMap 用来暂存加减的Map集合
     * @param mapkey             map集合的Key,groupId-childId
     */
    private void shoppingCartRemoveByMapKey(Map<String, GroupListEntity.childBean> stringchildBeanMap, String mapkey) {
        //如果map中存在，删除
        Toast.makeText(GroupListActivity.this, "到0了，map要执行删除", Toast.LENGTH_SHORT).show();
        if (stringchildBeanMap.containsKey(mapkey)) {
            //数据到0，删除对应key下的数据
            stringchildBeanMap.remove(mapkey);
            //todo 执行获取map集合数量的方法
            getMapCountAndMoney(stringchildBeanMap);
        }
    }

    /**
     * 获取Map结合中的count
     *
     * @param stringchildBeanMap
     */
    private void getMapCountAndMoney(Map<String, GroupListEntity.childBean> stringchildBeanMap) {
        int textCount = 0;
        double allMoney = 0.00;
        //遍历Map,然后得到count
        for (GroupListEntity.childBean m : stringchildBeanMap.values()) {
            Log.e("btn_shopping_cart_pay", "map集合中存储的数据---->" + m.getTitle() + "--->X" + m.getCount());
            textCount += m.getCount();
//            m.getCount() * m.getMoney();
            allMoney += (m.getCount() * m.getMoney());
        }
        tv_shopping_cart_count.setText("" + textCount);
        tv_shopping_cart_money.setText("¥" + allMoney);
        tv_shopping_cart_msg.setText("共" + textCount + "项服务");
        if (textCount > 0) {//显示
            tv_shopping_cart_count.setVisibility(View.VISIBLE);
            tv_shopping_cart_money.setVisibility(View.VISIBLE);
            tv_shopping_cart_msg.setVisibility(View.VISIBLE);
        } else {//隐藏处理
            tv_shopping_cart_count.setVisibility(View.INVISIBLE);
            tv_shopping_cart_money.setVisibility(View.INVISIBLE);
            tv_shopping_cart_msg.setVisibility(View.INVISIBLE);
            //归零处理
//            shoppingCartCount = 0;
        }
    }

    private void initBindView() {
        recyclerView = findViewById(R.id.recyclerView);
        btn_shopping_cart_pay = findViewById(R.id.btn_shopping_cart_pay);
        btn_shopping_cart_pay.setOnClickListener(this);
        rl_bottom_shopping_cart = findViewById(R.id.rl_bottom_shopping_cart);
        rl_bottom_shopping_cart.setOnClickListener(this);
        rl = findViewById(R.id.rl);
        iv_shopping_cart_img = findViewById(R.id.iv_shopping_cart_img);
        tv_shopping_cart_count = findViewById(R.id.tv_shopping_cart_count);

        tv_shopping_cart_money = findViewById(R.id.tv_shopping_cart_money);
        tv_shopping_cart_msg = findViewById(R.id.tv_shopping_cart_msg);

        initRecyclerViewDataBind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shopping_cart_pay:
                //暂时获取map集合
                for (GroupListEntity.childBean m : stringchildBeanMap.values()) {
                    Log.e("btn_shopping_cart_pay", "map集合中存储的数据---->" + m.getTitle() + "--->X" + m.getCount());
                }
                break;
            case R.id.rl_bottom_shopping_cart://购物车
                //初始化集合传递给弹窗

                break;
            default:
                break;
        }
    }

    //加入购物车曲线动画
    private void addCart(View view) {
//   一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final ImageView goods = new ImageView(GroupListActivity.this);
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
}
