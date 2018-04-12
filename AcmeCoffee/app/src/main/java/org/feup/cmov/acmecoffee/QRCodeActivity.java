package org.feup.cmov.acmecoffee;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.feup.cmov.acmecoffee.R;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Hashtable;

public class QRCodeActivity extends AppCompatActivity {
    private final String TAG = "QR_Code";
    private final String CH_SET = "ISO-8859-1";

    ImageView qrCodeImageview;
    String qr_content = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        byte[] content = getIntent().getByteArrayExtra("data");
        qrCodeImageview = (ImageView) findViewById(R.id.img_qr_code_image);
        //int sign_size = 512/8;
        //int mess_size = content.length;

        try {
            qr_content = new String(content, CH_SET);
            System.out.println("CONTENT: " +qr_content.length());
            //ByteBuffer bb = ByteBuffer.wrap(content);
            //byte[] mess = new byte[mess_size];
            //byte[] sign = new byte[sign_size];
            //bb.get(mess, 0, mess_size);             // extract the message containing nr. of types + type1 + type2 + ... (nr times)
            //bb.get(sign, 0, sign_size);             // extract the signature
            //boolean verified = validate(mess, sign);
            //titleTv.setText("Message: \"" + byteArrayToHex(mess) + "\"\nVerified: " + verified);
        }
        catch (UnsupportedEncodingException e) {
            Log.d(TAG, e.getMessage());
        }

        Thread t = new Thread(new Runnable() {  // do the creation in a new thread to avoid ANR Exception
            public void run() {
                final Bitmap bitmap;
                try {
                    bitmap = encodeAsBitmap(qr_content);
                    System.out.println("BITMAP: " + bitmap.toString());
                    runOnUiThread(new Runnable() {  // runOnUiThread method used to do UI task in main thread.
                        @Override
                        public void run() {
                            qrCodeImageview.setImageBitmap(bitmap);
                        }
                    });
                }
                catch (WriterException e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        });
        t.start();
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        int DIMENSION = 600;
        BitMatrix result;

        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, CH_SET);
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, DIMENSION, DIMENSION, hints);
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
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.colorPrimary) : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    boolean validate(byte[] message, byte[] signature) {
        boolean verified = false;
        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry("myIdKey", null);
            PublicKey pub = ((KeyStore.PrivateKeyEntry)entry).getCertificate().getPublicKey(); // the public key is stored as a certificate private key
            Signature sg = Signature.getInstance("SHA1WithRSA");                                 // associated with the private key
            sg.initVerify(pub);
            sg.update(message);
            verified = sg.verify(signature);
        }
        catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
        return verified;
    }
}
