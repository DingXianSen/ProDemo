package com.huidaxuan.ic2cloud.prodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.huidaxuan.ic2cloud.prodemo.grouplist.ServiceListActivity;
import com.parkingwang.keyboard.OnInputChangedListener;
import com.parkingwang.keyboard.PopupKeyboard;
import com.parkingwang.keyboard.view.InputView;

public class MainActivity extends AppCompatActivity {
    InputView ipv_activity_service_registration_car_num;
    private PopupKeyboard mPopupKeyboard;
    private Button btn_group_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ipv_activity_service_registration_car_num = findViewById(R.id.ipv_activity_service_registration_car_num);
        btn_group_recycler = findViewById(R.id.btn_group_recycler);

        initListener();
    }

    private void initListener() {
        // 创建弹出键盘
        mPopupKeyboard = new PopupKeyboard(this);
        // 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
        mPopupKeyboard.attach(ipv_activity_service_registration_car_num, this);
//            mPopupKeyboard.dismiss(ServiceRegistrationActivity.this);


        mPopupKeyboard.getController().addOnInputChangedListener(new OnInputChangedListener() {
            @Override
            public void onChanged(String number, boolean isCompleted) {
                if (isCompleted) {
                    mPopupKeyboard.dismiss(MainActivity.this);
                }
            }

            @Override
            public void onCompleted(String number, boolean isAutoCompleted) {
                mPopupKeyboard.dismiss(MainActivity.this);
            }
        });

        SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(MainActivity.this);
        //软键盘状态监听
        softKeyBoardListener.setListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //软键盘已经显示，做逻辑
                if (mPopupKeyboard != null && mPopupKeyboard.isShown()) {
                    mPopupKeyboard.dismiss(MainActivity.this);
                }
            }

            @Override
            public void keyBoardHide(int height) {
                //软键盘已经隐藏,做逻辑
            }
        });


        btn_group_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServiceListActivity.class));
            }
        });
    }
}
