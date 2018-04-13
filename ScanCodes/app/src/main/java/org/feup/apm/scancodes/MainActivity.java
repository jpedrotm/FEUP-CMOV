package org.feup.apm.scancodes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.apm.scancodes.Utils.Constants;
import org.feup.apm.scancodes.Utils.HttpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

public class MainActivity extends Activity {
  static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
  TextView message;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    message = (TextView) findViewById(R.id.totalToPay);
    Button QRButton = (Button) findViewById(R.id.scan1);
    QRButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        scan(true);
      }
    });
  }

  @Override
  public void onSaveInstanceState(Bundle bundle) {
    super.onSaveInstanceState(bundle);
  }

  public void onRestoreInstanceState(Bundle bundle) {
    super.onRestoreInstanceState(bundle);
  }

  public void scan(boolean qrcode) {
    try {
      Intent intent = new Intent(ACTION_SCAN);
      intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
      startActivityForResult(intent, 0);
    }
    catch (ActivityNotFoundException anfe) {
      showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
    }
  }

  private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
    AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
    downloadDialog.setTitle(title);
    downloadDialog.setMessage(message);
    downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialogInterface, int i) {
        Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        act.startActivity(intent);
      }
    });

    downloadDialog.setNegativeButton(buttonNo, null);
    return downloadDialog.show();
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 0) {
      if (resultCode == RESULT_OK) {
        String contents = data.getStringExtra("SCAN_RESULT");
          CustomerRequest customerRequest = new CustomerRequest(this, contents);
          Thread thr = new Thread(customerRequest);
          thr.start();
      }
    }
  }

  boolean validate(byte[] message, byte[] signature) {
    boolean verified = false;
    try {
      KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
      ks.load(null);
      KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);
      PublicKey pub = ((KeyStore.PrivateKeyEntry)entry).getCertificate().getPublicKey();   // the public key is stored as a certificate private key
      Signature sg = Signature.getInstance("SHA1WithRSA");                                 // associated with the private key
      sg.initVerify(pub);
      sg.update(message);
      verified = sg.verify(signature);
    }
    catch (Exception ex) {
      Log.d("ERROR VALIDATING: ", ex.getMessage());
    }
    return verified;
  }

  private class CustomerRequest implements Runnable {
    Context context = null;
    String info = null;

    CustomerRequest(Context context, String response) {
      this.context = context;
      this.info = response;
    }

    @Override
    public void run() {
      try {
          String response = HttpHandler.customerRequest(info);
        if(response != null) {
          final JSONObject info = new JSONObject(response);
          runOnUiThread(new Runnable() {
            public void run()
            {
              try {
                  String messageText = "Price: " + info.getDouble("price") + "€\nRequest ID: " + info.getLong("request_id");
                if(info.getBoolean("voucher_free_coffee")){
                    messageText += "\nHas a free coffee to receive.";
                }
                message.setText(messageText);
              } catch (JSONException e) {
                e.printStackTrace();
              }
              Toast.makeText(context, "Request made with success.", Toast.LENGTH_LONG).show();
            }
          });
        } else {
          runOnUiThread(new Runnable() {
            public void run()
            {
              Toast.makeText(context, "Something went wrong while doing the request.", Toast.LENGTH_LONG).show();
            }
          });
        }
      } catch (JSONException e) {
        e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }
    }
  }
}
