package com.huidaxuan.ic2cloud.printdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.huidaxuan.ic2cloud.printdemo.netprint.NetPrintActivity;
import com.huidaxuan.ic2cloud.printdemo.receiver.DeviceReceiver;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.TaskCallback;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.BitmapProcess;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterPos58;
import net.posprinter.utils.DataForSendToPrinterPos80;
import net.posprinter.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 连接芯烨打印机 打印测试APP
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_select_blue_tooth_print;
    Button btn_print_text;
    Button btn_print_bitmap_local;
    Button btn_print_bitmap_net;
    Button btn_print_bitmap_sample;
    Button btn_select_blue_tooth_close;
    Button btn_print_net_ip;
    Button btn_print_net_ip_client;
    Button btn_print_net;
    TextView tv_print_device;
    TextView tv_print_device_type;
    EditText et_print_text;
    EditText et_print_net_ip;
    Button btn_picture_photo;

    private BluetoothAdapter blueToothAdapter;

    //配对设备列表弹窗
    private List<String> btList = new ArrayList<>();
    private ArrayList<String> btFoundList = new ArrayList<>();
    private ArrayAdapter<String> btBoundAdapter, btFoundAdapter;
    private View btDialogView;
    private ListView btBoundLv, btFoundLv;
    private LinearLayout ll_bt_found;
    private AlertDialog btDialog;
    private Button btn_scan;
    private DeviceReceiver btReceiver;


    public static IMyBinder myBinder;


    public static boolean ISCONNECT = false;

    ServiceConnection mSerconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (IMyBinder) service;
            Log.e("myBinder", "connect");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("myBinder", "disconnect");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //bind service，get imyBinder
        Intent intent = new Intent(this, PosprinterService.class);
        bindService(intent, mSerconnection, BIND_AUTO_CREATE);

        initViewBind();
        initListener();
    }

    /**
     * 监听初始化
     */
    private void initListener() {
        btn_select_blue_tooth_print.setOnClickListener(this);
        btn_print_text.setOnClickListener(this);
        btn_print_bitmap_local.setOnClickListener(this);
        btn_print_bitmap_net.setOnClickListener(this);
        btn_print_bitmap_sample.setOnClickListener(this);
        btn_select_blue_tooth_close.setOnClickListener(this);
        btn_print_net_ip.setOnClickListener(this);
        btn_print_net_ip_client.setOnClickListener(this);
        btn_print_net.setOnClickListener(this);
        btn_picture_photo.setOnClickListener(this);
    }

    /**
     * 视图控件初始化绑定
     */
    private void initViewBind() {
        btn_select_blue_tooth_print = findViewById(R.id.btn_select_blue_tooth_print);
        tv_print_device = findViewById(R.id.tv_print_device);
        tv_print_device_type = findViewById(R.id.tv_print_device_type);
        btn_print_text = findViewById(R.id.btn_print_text);
        et_print_text = findViewById(R.id.et_print_text);
        btn_print_bitmap_local = findViewById(R.id.btn_print_bitmap_local);
        btn_print_bitmap_net = findViewById(R.id.btn_print_bitmap_net);
        btn_print_bitmap_sample = findViewById(R.id.btn_print_bitmap_sample);
        btn_select_blue_tooth_close = findViewById(R.id.btn_select_blue_tooth_close);
        btn_print_net_ip = findViewById(R.id.btn_select_blue_tooth_close);
        btn_print_net_ip_client = findViewById(R.id.btn_print_net_ip_client);
        et_print_net_ip = findViewById(R.id.et_print_net_ip);
        btn_print_net = findViewById(R.id.btn_print_net);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_blue_tooth_print://选择打印机
                selectBlueTooth();
                break;
            case R.id.btn_print_text://文字打印
                if (!TextUtils.isEmpty(et_print_text.getText().toString())) {
                    printText(et_print_text.getText().toString());
                } else {
                    printText("测试文字");
                }
                break;
            case R.id.btn_print_bitmap_local://打印本地资源图片
                printBitmap(true);
                break;
            case R.id.btn_print_bitmap_net://打印网络图片
                printBitmap(false);
                break;
            case R.id.btn_print_bitmap_sample://打印格式文本
                printSample();
                break;
            case R.id.btn_select_blue_tooth_close://断开链接。。
                disConnect();
                break;
            case R.id.btn_print_net_ip_client://连接网络打印机
                if (!TextUtils.isEmpty(et_print_net_ip.getText().toString())) {
                    connectNet();
                }
                break;
            case R.id.btn_print_net_ip://网络打印机打印
                //先打开系统的打印机
                if (!TextUtils.isEmpty(et_print_net_ip.getText().toString())) {
                    printBitmap(true);
                }
                break;
            case R.id.btn_print_net:
                startActivity(new Intent(MainActivity.this, NetPrintActivity.class));
                break;
            case R.id.btn_picture_photo://截屏
                break;
            default:
                break;
        }
    }


    /**
     * 判断蓝牙是否打开，并展示配对蓝牙列表
     */
    private void selectBlueTooth() {
        blueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        //判断手机蓝牙是否开启
        if (!blueToothAdapter.isEnabled()) {
            //没开启情况请求开启
            Intent blueToothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(blueToothIntent, 1);
        } else {
            //开启情况下，打开配对过的蓝牙设备列表
            showBlueToothList();
        }
    }

    /**
     * 蓝牙配对设备列表
     */
    private void showBlueToothList() {
        //是否正在搜索
        if (!blueToothAdapter.isDiscovering()) {
            Log.e("findViableDeviceList", "---showBlueToothList--蓝牙扫描执行---》");
            blueToothAdapter.startDiscovery();
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        btDialogView = inflater.inflate(R.layout.printer_list, null);
        btBoundAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, btList);
        btBoundLv = btDialogView.findViewById(R.id.listView1);
        btn_scan = btDialogView.findViewById(R.id.btn_scan);
        ll_bt_found = btDialogView.findViewById(R.id.ll1);
        btFoundLv = (ListView) btDialogView.findViewById(R.id.listView2);
        btFoundAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, btFoundList);
        btBoundLv.setAdapter(btBoundAdapter);
        btFoundLv.setAdapter(btFoundAdapter);
        btDialog = new AlertDialog.Builder(this).setTitle("蓝牙设置").setView(btDialogView).create();
        btDialog.show();

        btReceiver = new DeviceReceiver(btFoundList, btFoundAdapter, btFoundLv);

        //注册蓝牙广播接收者
        IntentFilter filterStart = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filterEnd = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(btReceiver, filterStart);
        registerReceiver(btReceiver, filterEnd);

        setListener();
        findViableDevice();
    }

    /**
     * 查找可用设备
     */
    private void findViableDevice() {
        //获取已配对蓝牙设备
        Set<BluetoothDevice> devices = blueToothAdapter.getBondedDevices();
        //列表清空，防止重复添加
        btList.clear();
        //如果不为空并且蓝牙正在搜索中
        if (blueToothAdapter != null && blueToothAdapter.isDiscovering()) {
            //适配器刷新
            btBoundAdapter.notifyDataSetChanged();
        }
        Log.e("findViableDevice", "---------------devices.size()" + devices.size());
        //存在已经配对的蓝牙设备
        if (devices.size() > 0) {
            for (Iterator<BluetoothDevice> iterator = devices.iterator(); iterator.hasNext(); ) {
                BluetoothDevice btd = iterator.next();
                //todo btd.getType();//获取设备的类型，待定要不要过滤,打印机的设备应该是3，
                if (btd.getType() == 3) {
                    //添加到列表中
                    btList.add(btd.getName() + "\n" + btd.getAddress());
                    Log.e("findViableDevice", "---------------devices设备类型:" + btd.getType());
                    //todo 既然这里是已配对的设备是否可以放到循环外边一起刷新，待定
                    btBoundAdapter.notifyDataSetChanged();
                }
            }
        } else {
            btList.add("暂时没有可以使用的蓝牙设备！");
            btBoundAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 可用设备弹窗设置监听
     */
    private void setListener() {
        //todo 扫描点击之后,完善加加载框或者跳转系统蓝牙
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示扫描到新的设备
                ll_bt_found.setVisibility(View.VISIBLE);
                blueToothScanning();

            }
        });

        //已经配对的设备点击---->连接
        btBoundLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogClickPublicMethod(position, btList);
            }
        });

        //未配对的设置点击---->先配对，再连接
        btFoundLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取可配对的列表
//                blueToothScanning();
//                dialogClickPublicMethod(position, btFoundList);
            }
        });
    }

    /**
     * 选择设备弹窗，配对设备和未配对设备点击时间方法抽取
     *
     * @param position 列表下标
     * @param btList   数据集合
     */
    private void dialogClickPublicMethod(int position, List<String> btList) {
        try {
            //如果适配器不为空并且处于正在搜索设备阶段，停止搜索
            if (blueToothAdapter != null && blueToothAdapter.isDiscovering()) {
                blueToothAdapter.cancelDiscovery();
            }
            //根据点击的下标获取设别名称等
            String mac = btList.get(position);
            tv_print_device.setText(mac);
            //由于是名称+地址，这里作截取操作
            mac = mac.substring(mac.length() - 17);
            //弹窗关闭
            btDialog.cancel();
            //设置连接

            connectBT(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 蓝牙连接
     */
    private void connectBT(String adrress) {
        String BtAdress = adrress.trim();
        if (BtAdress.equals(null) || BtAdress.equals("")) {
            Toast.makeText(getApplicationContext(), "连接失败请重试", Toast.LENGTH_SHORT).show();
            tv_print_device_type.setText("连接状态：连接失败请重试");

        } else {
            myBinder.ConnectBtPort(BtAdress, new TaskCallback() {
                @Override
                public void OnSucceed() {
                    ISCONNECT = true;
                    Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
                    tv_print_device_type.setText("连接状态：连接成功");
                }

                @Override
                public void OnFailed() {
                    ISCONNECT = false;
                    Toast.makeText(getApplicationContext(), "连接失败请重试", Toast.LENGTH_SHORT).show();
                    tv_print_device_type.setText("连接状态：连接失败请重试");
                }
            });
        }
    }

    /**
     * 关闭弹窗跳转系统蓝牙设置
     */
    private void blueToothScanning() {
        //关闭弹窗跳转系统蓝牙设置
        if (btDialog != null) {
            btDialog.cancel();
        }
        //跳转系统设置
        openBlueSetting();
    }

    /**
     * 打开系统蓝牙设置
     */
    private void openBlueSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 断开连接
     */
    private void disConnect() {
        if (ISCONNECT) {
            myBinder.DisconnectCurrentPort(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    ISCONNECT = false;
                    Toast.makeText(getApplicationContext(), "已断开连接", Toast.LENGTH_SHORT).show();
                    tv_print_device_type.setText("连接状态：已断开连接");
                }

                @Override
                public void OnFailed() {
                    ISCONNECT = true;
                    Toast.makeText(getApplicationContext(), "断开连接失败请重试", Toast.LENGTH_SHORT).show();
                    tv_print_device_type.setText("连接状态：断开连接失败请重试");
                }
            });
        }
    }


    /********************************************蓝牙打印方法********************************************/

    /**
     * 普通文字打印
     *
     * @param printText 要打印的文字
     */
    private void printText(final String printText) {
        //如果状态为连接状态，表示可以传输数据打印
        if (MainActivity.ISCONNECT) {
            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    Toast.makeText(MainActivity.this, "设备准备就绪", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed() {
                    Toast.makeText(MainActivity.this, "打印设备不可用，请重新连接", Toast.LENGTH_SHORT).show();
                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list = new ArrayList<>();
                    list.add(DataForSendToPrinterPos58.initializePrinter());
                    list.add(StringUtils.strTobytes(printText));
                    list.add(DataForSendToPrinterPos58.printAndFeedLine());
                    return list;
                }
            });
        } else {
            Toast.makeText(this, "当前无可使用的打印机设备", Toast.LENGTH_SHORT).show();
        }
    }

//    Bitmap bitmap1;

    /**
     * 打印图片
     *
     * @param flag 本地资源还是网络图
     */
    private void printBitmap(boolean flag) {

        if (flag) {//本地图
            final Bitmap bitmap1 = BitmapProcess.compressBmpByYourWidth
//                    (BitmapFactory.decodeResource(getResources(), R.drawable.uuuufdsfsdfsdf), 400);
        (BitmapFactory.decodeResource(getResources(), R.drawable.hhhhhh99999), 400);
            printImgPublicMethod(bitmap1);
        } else {//网络图片
            final Bitmap[] bitmap2 = {null};
            String netImgUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602563873732&di=41397a8e3ac27cc7627ace48be768778&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F12%2F20160612185804_ujMLx.thumb.700_0.png";
//            String netImgUrl = "https://bpic.588ku.com//back_origin_min_pic/20/10/01/1bc46c61c903a38b68dc1f3c8eb80ee9.jpg";
            Glide.get(MainActivity.this).clearMemory();
            Glide.with(MainActivity.this).load(netImgUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    //Drawable转换为Bitmap
                    BitmapDrawable bd = (BitmapDrawable) resource;
                    bitmap2[0] = bd.getBitmap();
                    Log.e("printImg", "-------------->bitmap2[0]" + bitmap2[0].getConfig().toString());
                    if (bitmap2[0] != null) {
                        final Bitmap bitmap3 = BitmapProcess.compressBmpByYourWidth
                                (bitmap2[0], 400);
                        printImgPublicMethod(bitmap3);
                        Log.e("printImg", "-------!null------->bitmap2[0]" + bitmap3.getConfig().toString());
                    }
                }
            });

        }

    }

    /**
     * 打印图片公共方法抽取
     *
     * @param bitmap
     */
    private void printImgPublicMethod(final Bitmap bitmap) {
        if (MainActivity.ISCONNECT) {
            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    Toast.makeText(MainActivity.this, "设备准备就绪", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed() {
                    Toast.makeText(MainActivity.this, "打印设备不可用，请重新连接", Toast.LENGTH_SHORT).show();
                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list = new ArrayList<>();
                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    List<Bitmap> blist = new ArrayList<>();
                    blist = BitmapProcess.cutBitmap(50, bitmap);
                    for (int i = 0; i < blist.size(); i++) {
                        list.add(DataForSendToPrinterPos80.printRasterBmp(0, blist.get(i), BitmapToByteData.BmpType.Threshold, BitmapToByteData.AlignType.Center, 384));
//                        list.add(DataForSendToPrinterPos80.printRasterBmp(0, blist.get(i), BitmapToByteData.BmpType.Threshold, BitmapToByteData.AlignType.Center, 400));
                    }
//                    list.add(StringUtils.strTobytes("1234567890qwertyuiopakjbdscm nkjdv mcdskjb"));
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());
                    return list;
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "打印设备不可用，请重新连接", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 打印样张
     */
    private void printSample() {
        if (MainActivity.ISCONNECT) {
            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    Toast.makeText(MainActivity.this, "设备准备就绪", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void OnFailed() {
                    Toast.makeText(MainActivity.this, "打印设备不可用，请重新连接", Toast.LENGTH_SHORT).show();
                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list = new ArrayList<>();
                    list.add(DataForSendToPrinterPos58.initializePrinter());
                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(65, 00));//设置初始位置
                    list.add(DataForSendToPrinterPos58.selectCharacterSize(17));//字体放大一倍
                    list.add(StringUtils.strTobytes("永杰汽车"));
                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 00));
                    list.add(StringUtils.strTobytes(""));
                    list.add(DataForSendToPrinterPos58.printAndFeedLine());

                    list.add(DataForSendToPrinterPos58.initializePrinter());
                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 00));
                    list.add(StringUtils.strTobytes("服务项目1"));
                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 00));
                    list.add(StringUtils.strTobytes("500元"));
                    list.add(DataForSendToPrinterPos58.printAndFeedLine());

//                    list.add(DataForSendToPrinterPos58.initializePrinter());
//                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30,00));
//                    list.add(StringUtils.strTobytes("黄焖鸡呀"));
//                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220,00));
//                    list.add(StringUtils.strTobytes("6元"));
//                    list.add(DataForSendToPrinterPos58.printAndFeedLine());
//
//                    list.add(DataForSendToPrinterPos58.initializePrinter());
//                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30,00));
//                    list.add(StringUtils.strTobytes("黄焖鸡"));
//                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220,00));
//                    list.add(StringUtils.strTobytes("7元"));
//                    list.add(DataForSendToPrinterPos58.printAndFeedLine());
//
//                    list.add(DataForSendToPrinterPos58.initializePrinter());
//                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30,00));
//                    list.add(StringUtils.strTobytes("黄焖鸡"));
//                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220,00));
//                    list.add(StringUtils.strTobytes("8元"));
//                    list.add(DataForSendToPrinterPos58.printAndFeedLine());
//
//                    list.add(DataForSendToPrinterPos58.initializePrinter());
//                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30,00));
//                    list.add(StringUtils.strTobytes("黄焖鸡"));
//                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220,00));
//                    list.add(StringUtils.strTobytes("9元"));
//                    list.add(DataForSendToPrinterPos58.printAndFeedLine());
//
//                    list.add(DataForSendToPrinterPos58.initializePrinter());
//                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30,00));
//                    list.add(StringUtils.strTobytes("黄焖鸡"));
//                    list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220,00));
//                    list.add(StringUtils.strTobytes("10元"));
//                    list.add(DataForSendToPrinterPos58.printAndFeedLine());

                    return list;
                }
            });
        } else {
            Toast.makeText(this, "当前无可使用的打印机设备", Toast.LENGTH_SHORT).show();
        }
    }


    /********************************************网络连接相关********************************************/

    /**
     * 网络连接
     */
    private void connectNet() {
        String ip = et_print_net_ip.getText().toString();
        if (ip != null || ISCONNECT == false) {
            myBinder.ConnectNetPort(ip, 9100, new TaskCallback() {
                @Override
                public void OnSucceed() {
                    ISCONNECT = true;
                    Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
                    tv_print_device_type.setText("网络打印机连接状态：连接成功");
                }

                @Override
                public void OnFailed() {
                    ISCONNECT = false;
                    Toast.makeText(getApplicationContext(), "连接失败请重试", Toast.LENGTH_SHORT).show();
                    tv_print_device_type.setText("网络打印机连接状态：连接失败请重试");
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "连接失败请重试", Toast.LENGTH_SHORT).show();
            tv_print_device_type.setText("网络打印机连接状态：连接失败请重试");
        }
    }

}
