package com.huidaxuan.ic2cloud.printdemo.netprint;

import android.Manifest;
import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huidaxuan.ic2cloud.printdemo.MainActivity;
import com.huidaxuan.ic2cloud.printdemo.R;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import net.lemonsoft.lemonbubble.LemonBubble;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.functions.Consumer;

/**
 * @packageName:com.huidaxuan.ic2cloud.printdemo
 * @className: NetPrintActivity
 * @description:网络打印机，选择打印机并预览
 * @author: dingchao
 * @time: 2020-10-14 09:17
 */
public class NetPrintActivity extends AppCompatActivity implements View.OnClickListener {
    Button bt_Photo;
    Button bt_AssetsPrintPhoto;
    Button bt_SDPrintPhoto;
    Button bt_PrintUrlHTML;
    Button bt_PrintHTML;
    Button bt_PrintContainImgHTML;
    Button bt_PrintCustom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_print);
        openPermissions();
        initViewBind();
        initListener();
    }

    private void initListener() {
        bt_Photo.setOnClickListener(this);
        bt_AssetsPrintPhoto.setOnClickListener(this);
        bt_SDPrintPhoto.setOnClickListener(this);
        bt_PrintUrlHTML.setOnClickListener(this);
        bt_PrintHTML.setOnClickListener(this);
        bt_PrintContainImgHTML.setOnClickListener(this);
        bt_PrintCustom.setOnClickListener(this);
    }

    private void initViewBind() {
        bt_Photo = findViewById(R.id.bt_Photo);
        bt_AssetsPrintPhoto = findViewById(R.id.bt_AssetsPrintPhoto);
        bt_SDPrintPhoto = findViewById(R.id.bt_SDPrintPhoto);
        bt_PrintUrlHTML = findViewById(R.id.bt_PrintUrlHTML);
        bt_PrintHTML = findViewById(R.id.bt_PrintHTML);
        bt_PrintContainImgHTML = findViewById(R.id.bt_PrintContainImgHTML);
        bt_PrintCustom = findViewById(R.id.bt_PrintCustom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Photo://图片转PDF并打印
                String imgPath = Environment.getExternalStorageDirectory() + "/netPrintTestPhoto.jpg";
                String pdfPath = Environment.getExternalStorageDirectory() + "/netPrintTestPdf.pdf";
                photoTransformationPdfPrint(imgPath, pdfPath);
                break;
            case R.id.bt_AssetsPrintPhoto:
                break;
            case R.id.bt_SDPrintPhoto:
                break;
            case R.id.bt_PrintUrlHTML:
                break;
            case R.id.bt_PrintHTML:
                break;
            case R.id.bt_PrintContainImgHTML:
                break;
            case R.id.bt_PrintCustom:
                break;
            default:
                break;
        }
    }

    /**
     * 图片转PDF打印
     *
     * @param imgPath 图片路径
     */
    private void photoTransformationPdfPrint(String imgPath, String pdfPath) {
        PhotoVerPdf photoVerPdf = new PhotoVerPdf();
        if (photoVerPdf.ready(imgPath, pdfPath)) {
//        if (photoVerPdf.ready()) {
//            doPrint(pdfPath);
        } else {
            LemonBubble.showError(NetPrintActivity.this, "转换失败", 1000);
        }
    }

    /**
     * 自定义打印
     */
    private void doPrint(String filePath) {
        if (!PDFCheck.check(filePath)) {
            LemonBubble.showError(NetPrintActivity.this, "文件下载失败", 2000);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PrintManager printManager = (PrintManager) NetPrintActivity.this.getSystemService(Context.PRINT_SERVICE);
            String jobName = getString(R.string.app_name) + "Document";
            try {
                printManager.print(jobName, new MyPrintDocumentAdapter(NetPrintActivity.this, filePath), null);
            } catch (RuntimeException e) {
            } catch (Exception e) {
            }
        } else {
            Toast.makeText(NetPrintActivity.this, "Android 版本过低不支持自定义打印", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 创建打印适配器
     */
    private class MyPrintDocumentAdapter extends PrintDocumentAdapter {
        private Context mContext;
        private String mFilePath;
        private PrintedPdfDocument mPdfDocument;

        public PdfDocument myPdfDocument;
        public int totalpages = 1;//设置一共打印一张纸

        public MyPrintDocumentAdapter(Context context, String filePath) {
            this.mContext = context;
            this.mFilePath = filePath;
        }

        //当打印进程开始，该方法就将被调用，
        @Override
        public void onStart() {
            super.onStart();
        }

        //当用户改变了打印输出时，比方说页面尺寸，或者页面的方向时，
        // 该函数将被调用。以此会给我们的应用重新计划打印页面的布局，
        // 另外该方法必须返回打印文档包含多少页面。
        @Override
        public void onLayout(PrintAttributes printAttributes,
                             PrintAttributes printAttributes1,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback layoutResultCallback,
                             Bundle bundle) {
//            //使用请求的页属性创建新的pdfdocument
//            mPdfDocument=new PrintedPdfDocument(mContext,printAttributes1);
            // 响应取消请求
            if (cancellationSignal.isCanceled()) {
                layoutResultCallback.onLayoutCancelled();
                return;
            }
            // 将打印信息返回到打印框架
            PrintDocumentInfo info = new PrintDocumentInfo
                    .Builder("name")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build();
            layoutResultCallback.onLayoutFinished(info, true);
        }

        //此函数被调用后，会将打印页面渲染成一个待打印的文件，该函数
        // 可以在onLayout被调用后调用一次或多次
        @Override
        public void onWrite(PageRange[] pageRanges,
                            ParcelFileDescriptor parcelFileDescriptor,
                            CancellationSignal cancellationSignal,
                            WriteResultCallback writeResultCallback) {
            InputStream input = null;
            OutputStream output = null;
            try {
                input = new FileInputStream(mFilePath);
                output = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }
                writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
            } catch (FileNotFoundException e) {
            } catch (Exception e) {
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                }
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
            Toast.makeText(mContext, "已准备好打印，点击右上角蓝色图标开始打印", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开权限
     */
    private void openPermissions() {
        final RxPermissions rxPermissions = new RxPermissions(NetPrintActivity.this); // where this is an Activity or Fragment instance
        rxPermissions.requestEachCombined(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {
                    Log.d("执行", "权限都通过了");
                } else if (permission.shouldShowRequestPermissionRationale) {
                    Log.d("执行", "至少有一个权限被拒绝了");
                    openPermissions();
                } else {
                    Log.d("执行", "转到设置");
                }
            }
        });
    }
}
