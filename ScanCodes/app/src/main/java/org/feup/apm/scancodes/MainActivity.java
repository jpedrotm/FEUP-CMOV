package org.feup.apm.scancodes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.apm.scancodes.Utils.HttpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;

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
        try {
          byte[] array = contents.getBytes("ISO-8859-1");
          contructMessage(array);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void contructMessage(byte[] array) throws JSONException {

    int sign_size = 512/8;
    int mess_size = array.length - sign_size;

    ByteBuffer buffer = ByteBuffer.wrap(array);
    /*byte[] mess = new byte[mess_size];
    byte[] sign = new byte[sign_size];
    buffer.get(mess, 0, mess_size);             // extract the message containing nr. of types + type1 + type2 + ... (nr times)
    buffer.get(sign, 0, sign_size);             // extract the signature

    boolean verified = validate(mess, sign);*/
    JSONObject response = new JSONObject();
    JSONArray items = new JSONArray();

    int customerId = (int) buffer.get(0);
    response.put("customer_id", customerId);
    int nrItems = (int) buffer.get(1);
    if(nrItems > 0) {
      ArrayList<Integer> itemsIds = new ArrayList<>();
      for(int i=0;i < nrItems;i++) {
        items.put((int) buffer.get(i + 2));
        itemsIds.add((int) buffer.get(i + 2));
      }
    }

    response.put("items", items);

    int hasVoucher = buffer.get(nrItems + 2);
    int voucherId = -1;
    if(hasVoucher != 0) {
      voucherId = buffer.get(nrItems + 3);
    }

    response.put("voucher_id", voucherId);

    System.out.println("MESSAGE: " + response.toString());

    CustomerRequest customerRequest = new CustomerRequest(this, response.toString());
    Thread thr = new Thread(customerRequest);
    thr.start();

  }

  /*boolean validate(byte[] message, byte[] signature) {
    boolean verified = false;
    try {
      KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
      ks.load(null);
      KeyStore.Entry entry = ks.getEntry("myIdKey", null);
      PublicKey pub = ((KeyStore.PrivateKeyEntry)entry).getCertificate().getPublicKey();   // the public key is stored as a certificate private key
      Signature sg = Signature.getInstance("SHA1WithRSA");                                 // associated with the private key
      sg.initVerify(pub);
      sg.update(message);
      verified = sg.verify(signature);
    }
    catch (Exception ex) {
      Log.d("", ex.getMessage());
    }
    return verified;
  }*/

  private class CustomerRequest implements Runnable {
    Context context = null;
    String info = null;

    CustomerRequest(Context context, String response) {
      this.context = context;
      this.info = response;
    }

    @Override
    public void run() {
      String response = HttpHandler.customerRequest(info);
      System.out.println("RESPONSE: " + response);
      try {
        final JSONObject info = new JSONObject(response);
        if(response != null) {
          runOnUiThread(new Runnable() {
            public void run()
            {
              try {
                message.setText("Price: " + info.getDouble("price") + "\nRequest ID: " + info.getLong("request_id"));
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
      }
    }
  }
}