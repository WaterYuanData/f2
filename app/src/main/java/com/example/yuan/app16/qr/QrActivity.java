package com.example.yuan.app16.qr;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuan.app16.R;
import com.example.yuan.app16.util.Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QrActivity extends AppCompatActivity implements View.OnClickListener {

    private Button open;
    private Button create;
    private EditText input;
    private ImageView imageView;
    private Bitmap qrCodeBit;
    private TextView tv_control_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        // 初始化
        open = (Button) findViewById(R.id.open_button);
        create = (Button) findViewById(R.id.create_button);
        input = (EditText) findViewById(R.id.input_editText);
        imageView = (ImageView) findViewById(R.id.qrCode_imageView);
        tv_control_log = (TextView) findViewById(R.id.tv_control_log);

        open.setOnClickListener(this);
        create.setOnClickListener(this);
        imageView.setOnClickListener(this);

        Utils.requestPermissions(this);

    }

    /**
     * 生成带图标的二维码
     */
    public Bitmap createQR(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        // 配置参数
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 容错级别 最高级H
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 图像数据转换，使用了矩阵转换 二维码尺寸大小
        int widthPix = 300;
        int heightPix = 300;
        try {
            //MultiFormatWriter内部会调用QRCodeWriter
//            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            // 生成QR二维码数据——这里只是得到一个由true和false组成的数组
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;// 黑色
//                        pixels[y * widthPix + x] = 0xffff0000;// 红色 确定二维码的颜色
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;// 白色 -1相当于0xffffffff
                    }
                }
            }
            // 生成二维码图片的格式，使用采用最高的图片效果ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            // 通过像素数组生成bitmap,具体参考api
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
            // 添加logo图标
            Bitmap logoBm = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
            bitmap = addLogo(bitmap, logoBm);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Log.d("size", "addLogo: " + srcWidth + " " + srcHeight + " " + logoWidth + " " + logoHeight + " " + scaleFactor);
        // D/size: addLogo: 300 300 266 266 0.22556391
        // http://www.cnblogs.com/feisky/archive/2010/01/10/1643460.html 详解 6.图片水印的生成方法
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_button:
                startActivity(new Intent(QrActivity.this, CaptureActivity.class));
                tv_control_log.append("\n\n 打开");
                break;
            case R.id.create_button:
                tv_control_log.append("\n\n 创建");
                String content = input.getText().toString().trim();
                qrCodeBit = createQR(content);
                imageView.setImageBitmap(qrCodeBit);
                break;
            case R.id.qrCode_imageView:
                tv_control_log.append("\n\n 保存");
                saveBitmapFile(QrActivity.this, qrCodeBit);
                break;

        }
    }

    /**
     * 二维码保存的文件夹
     */
    public static final String KEY_QR_CODE_DIR = "/sdcard/water_QR_Code/";//二维码保存路径

    /**
     * 保存图片到sdcard
     *
     * @param bitmap
     */
    public void saveBitmapFile(Context mContext, Bitmap bitmap) {
        File temp = new File(KEY_QR_CODE_DIR);//要保存文件先创建文件夹
        if (!temp.exists()) {
            temp.mkdirs();
        }
        try {
            String filePath = KEY_QR_CODE_DIR + "QRCode_" + System.currentTimeMillis() + ".jpg";
            Utils.queryAndRequestPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);//打开文件输出流需要写权限
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Toast.makeText(mContext, "保存成功 " + filePath, Toast.LENGTH_SHORT).show();
            // 新建文件后通知数据库更新,建议发广播而非直接插入
//            nofityImgToGallery(mContext, filePath);
            updateFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 新建文件后通知数据库更新,建议发广播法
     */
    public void updateFile(String filePath) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        Log.d("URI", "fromFile: " + uri);
        // D/fromFile: fromFile: file:///sdcard/water_QR_Code/QRCode_1520495747191.jpg
        intent.setData(uri);
        sendBroadcast(intent);
    }

    /**
     * 通知相册图片增加
     *
     * @param fileName 文件全路径
     */
    public boolean nofityImgToGallery(Context mContext, String fileName) {
        if (fileName == null || fileName.length() <= 0) {
            return false;
        }
        String MimiType = "image/png";
        try {
            ContentValues values = new ContentValues();
            values.put("datetaken", new Date().toString());
            values.put("mime_type", MimiType);
            values.put("_data", fileName);
            ContentResolver cr = mContext.getContentResolver();
            Log.d("URI", "EXTERNAL_CONTENT_URI: " + MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // D/URI: EXTERNAL_CONTENT_URI: content://media/external/images/media
            cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(mContext, new String[]{KEY_QR_CODE_DIR}, null, null);
        return true;
    }
}
