package com.huidaxuan.ic2cloud.printdemo.netprint;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;

/**
 * @author dingchao
 * @create 2020/7/2
 * @Describe
 */
public class PDFCheck {
    /**
     * 利用itext打开pdf文档
     *
     * 判断pdf 是否损坏
     *
     */
    public static boolean check(String file) {
        boolean flag1 = false;
        int n = 0;
        try {
            Document document = new Document(new PdfReader(file).getPageSize(1));
            document.open();
            PdfReader reader = new PdfReader(file);
            n = reader.getNumberOfPages();
            if (n != 0)
                flag1 = true;
            document.close();
        } catch (Exception e) {
            Log.d("PDFCheck",e.getMessage());
        }
        return flag1;
    }
}
