package com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.huidaxuan.ic2cloud.meituanshoppingcart.R;
import com.huidaxuan.ic2cloud.meituanshoppingcart.adapter.LeftProductTypeAdapter;
import com.huidaxuan.ic2cloud.meituanshoppingcart.base.BaseFragment;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.adapter.RightServiceNoCartAdapter;
import com.huidaxuan.ic2cloud.meituanshoppingcart.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @packageName:com.huidaxuan.ic2cloud.app2c.view.fragment
 * @className: StoreDetailsServiceFragment
 * @description:门店详情-fragment——全部套餐,这个不需要左侧数字改变什么的所以不需要设置
 * @author: dingchao
 * @time: 2020-11-20 13:23
 */
public class StoreDetailsServiceFragment extends BaseFragment implements LeftProductTypeAdapter.onItemSelectedListener {

    ArrayList<ProductListEntity> productListEntities;
    ProductListEntity headMenu;
    @BindView(R.id.left_menu)//左侧列表
            RecyclerView leftMenu;
    @BindView(R.id.right_menu)//右侧列表
            RecyclerView rightMenu;
    @BindView(R.id.right_menu_item)//右侧标题整体
            LinearLayout headerLayout;
    @BindView(R.id.right_menu_tv)//右侧标题
            TextView headerView;

    private LeftProductTypeAdapter leftAdapter;
    private RightServiceNoCartAdapter rightAdapter;
    private boolean leftClickType = false;//左侧菜单点击引发的右侧联动

    @Override
    protected void fetchData() {
        productListEntities = new ArrayList<>();
        List<ProductListEntity.ProductEntity> productEntities1 = new ArrayList<>();
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "套餐名称1-1", "34", 10.0, 0, "1", "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "套餐名称2-1", "34", 20.0, 0, "2", "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "套餐名称3-1", "34", 30.0, 0, "3", "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "套餐名称4-1", "34", 40.0, 0, "4", "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "套餐名称5-1", "34", 50.0, 0, "5", "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "套餐名称6-1", "34", 50.0, 0, "6", "1"));
        productEntities1.add(new ProductListEntity.ProductEntity("img地址", "套餐名称7-1", "34", 50.0, 0, "7", "1"));

        List<ProductListEntity.ProductEntity> productEntities2 = new ArrayList<>();
        productEntities2.add(new ProductListEntity.ProductEntity("img地址", "套餐名称1-2", "34", 10.0, 0, "6", "2"));
        productEntities2.add(new ProductListEntity.ProductEntity("img地址", "套餐名称2-2", "34", 20.0, 0, "7", "2"));
        productEntities2.add(new ProductListEntity.ProductEntity("img地址", "套餐名称3-2", "34", 30.0, 0, "8", "2"));
        productEntities2.add(new ProductListEntity.ProductEntity("img地址", "套餐名称4-2", "34", 40.0, 0, "9", "2"));
        productEntities2.add(new ProductListEntity.ProductEntity("img地址", "套餐名称5-2", "34", 50.0, 0, "10", "2"));

        List<ProductListEntity.ProductEntity> productEntities3 = new ArrayList<>();
        productEntities3.add(new ProductListEntity.ProductEntity("img地址", "套餐名称1-3", "34", 10.0, 0, "6", "3"));
        productEntities3.add(new ProductListEntity.ProductEntity("img地址", "套餐名称2-3", "34", 20.0, 0, "7", "3"));
        productEntities3.add(new ProductListEntity.ProductEntity("img地址", "套餐名称3-3", "34", 30.0, 0, "8", "3"));
        productEntities3.add(new ProductListEntity.ProductEntity("img地址", "套餐名称4-3", "34", 40.0, 0, "9", "3"));
        productEntities3.add(new ProductListEntity.ProductEntity("img地址", "套餐名称5-3", "34", 50.0, 0, "10", "3"));

        List<ProductListEntity.ProductEntity> productEntities4 = new ArrayList<>();
        productEntities4.add(new ProductListEntity.ProductEntity("img地址", "套餐名称1-4", "34", 10.0, 0, "6", "4"));
        productEntities4.add(new ProductListEntity.ProductEntity("img地址", "套餐名称2-4", "34", 20.0, 0, "7", "4"));
        productEntities4.add(new ProductListEntity.ProductEntity("img地址", "套餐名称3-4", "34", 30.0, 0, "8", "4"));
        productEntities4.add(new ProductListEntity.ProductEntity("img地址", "套餐名称4-4", "34", 40.0, 0, "9", "4"));
        productEntities4.add(new ProductListEntity.ProductEntity("img地址", "套餐名称5-4", "34", 50.0, 0, "10", "4"));

        List<ProductListEntity.ProductEntity> productEntities5 = new ArrayList<>();
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "套餐名称1-5", "34", 10.0, 0, "1", "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "套餐名称2-5", "34", 20.0, 0, "2", "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "套餐名称3-5", "34", 30.0, 0, "3", "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "套餐名称4-5", "34", 40.0, 0, "4", "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "套餐名称5-5", "34", 50.0, 0, "5", "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "套餐名称6-5", "34", 50.0, 0, "6", "5"));
        productEntities5.add(new ProductListEntity.ProductEntity("img地址", "套餐名称7-5", "34", 50.0, 0, "7", "5"));

        productListEntities.add(new ProductListEntity("1", "常规保养", productEntities1));
        productListEntities.add(new ProductListEntity("2", "清洗养护", productEntities2));
        productListEntities.add(new ProductListEntity("3", "预约服务", productEntities3));
        productListEntities.add(new ProductListEntity("4", "事故维修", productEntities4));
        productListEntities.add(new ProductListEntity("5", "贴膜", productEntities5));


        //设置数据源，数据绑定展示
        leftAdapter = new LeftProductTypeAdapter(getActivity(), productListEntities);
        rightAdapter = new RightServiceNoCartAdapter(getActivity(), productListEntities);


        rightMenu.setAdapter(rightAdapter);
        leftMenu.setAdapter(leftAdapter);
        //左侧列表单项选择
        leftAdapter.addItemSelectedListener(this);

        rightAdapter.setOnItemChrldListner(new RightServiceNoCartAdapter.OnItemChrldListner() {
            @Override
            public void onCall(View view, ProductListEntity.ProductEntity entity, String type) {
                ToastUtil.showShort(getActivity(), "点击了名称为：" + entity.getProductName() + "的套餐");
            }
        });
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

    @Override
    protected int getLayout() {
        return R.layout.fragment_store_details_all;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (leftAdapter != null) {
            leftAdapter.removeItemSelectedListener(this);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        leftMenu.setLayoutManager(new LinearLayoutManager(mContext));
        rightMenu.setLayoutManager(new LinearLayoutManager(mContext));
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

    @Override
    public void onLeftItemSelected(int position, ProductListEntity menu) {
        int sum = 0;
        for (int i = 0; i < position; i++) {
            sum += productListEntities.get(i).getProductEntities().size() + 1;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) rightMenu.getLayoutManager();
        rightMenu.scrollToPosition(position);
        layoutManager.scrollToPositionWithOffset(sum, 0);
        leftClickType = true;
    }
}
