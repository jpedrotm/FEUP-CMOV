package org.feup.cmov.acmecoffee;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.UnsupportedEncodingException;

public class QRActivity extends AppCompatActivity {
    ImageView qrCodeImageview;
    TextView errorTv;
    byte [] bContent = {83, 111, 109, 101, 58, 32, -40, -41, -9, -90};  // this is the msg which we will encode in QRcode
    String content = null;
    String errorMsg = "";
    public final static int WIDTH=600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView titleTv;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        qrCodeImageview=(ImageView) findViewById(R.id.img_qr_code_image);
        titleTv = (TextView) findViewById(R.id.title);
        errorTv = (TextView) findViewById(R.id.error);

        bContent = getIntent().getByteArrayExtra("data");

        System.out.println("RECEBI: " + bContent.length);

        for(int i=0;i<bContent.length;i++) {
            System.out.println("R BYTE: " + bContent[i]);
        }

        try {
            content = new String(bContent, "ISO-8859-1");
        }
        catch (UnsupportedEncodingException e) {
            errorMsg = e.getMessage();
            errorTv.setText(errorMsg);
        }
        titleTv.setText("Message: \"" + content + "\"");

        Thread t = new Thread(new Runnable() {  // do the creation in a new thread to avoid ANR Exception
            public void run() {
                final Bitmap bitmap;
                try {
                    bitmap = encodeAsBitmap(content);
                    runOnUiThread(new Runnable() {  // runOnUiThread method used to do UI task in main thread.
                        @Override
                        public void run() {
                            qrCodeImageview.setImageBitmap(bitmap);
                        }
                    });
                }
                catch (WriterException e) {
                    errorMsg += "\n" + e.getMessage();
                }
                if (!errorMsg.isEmpty())
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorTv.setText(errorMsg);
                        }
                    });
            }
        });
        t.start();
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        }
        catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.colorPrimary):getResources().getColor(R.color.colorSmooth);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
