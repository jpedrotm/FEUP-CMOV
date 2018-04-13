package org.feup.cmov.acmecoffee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.security.KeyPairGeneratorSpec;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.cmov.acmecoffee.Database.DatabaseHelper;
import org.feup.cmov.acmecoffee.Model.Voucher;

import org.feup.cmov.acmecoffee.Utils.Constants;
import org.json.JSONArray;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

public class RequestActivity extends AppCompatActivity {

    public  ArrayList<String> NAMES = new ArrayList<>();
    public  ArrayList<Double> PRICES = new ArrayList<>();
    public  ArrayList<String> TYPES = new ArrayList<>();
    public  ArrayList<Voucher> vouchers = new ArrayList<>();
    public  ArrayList<Integer> IDS = new ArrayList<>();
    public  TextView total;
    public  double totalMoney;

    public static final int STATIC_INT_VALUE = 10;
    public static final int RESULT_OK = 11;
    public CustomAdapter cursorAdapter = new CustomAdapter();
    RadioGroup radioGroup;
    RadioButton freeCoffee;
    RadioButton fiveDiscount;
    RadioButton noVoucher;

    public Long voucherToUse;
    public ArrayList<Long> coffeeVouchers = new ArrayList<>();
    public ArrayList<Long> discountVouhcers = new ArrayList<>();

    private SharedPreferences prefs;
    Map<String, ?> sessionContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        totalMoney = 0;
        total = (TextView) findViewById(R.id.total_money);
        radioGroup = (RadioGroup) findViewById(R.id.voucherGroup);
        freeCoffee = (RadioButton) findViewById(R.id.freeCoffeeRadio);
        fiveDiscount = (RadioButton) findViewById(R.id.fiveDiscountRadio);
        noVoucher = (RadioButton) findViewById(R.id.noVoucherRadio);
        ListView listView = (ListView) findViewById(R.id.requestListView);
        listView.setAdapter(cursorAdapter);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sessionContent = prefs.getAll();

        Button btnSend = (Button) findViewById(R.id.sendRequest);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] message = constructMessage();
                System.out.println("SIZE: " + message.length);
                Intent intent = new Intent(getApplicationContext(), QRActivity.class);
                intent.putExtra("data", message);
                startActivity(intent);
            }

        });

        checkAvailableVouchers();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.freeCoffeeRadio){
                    voucherToUse = getMinValue(coffeeVouchers);
                    System.out.println(voucherToUse);
                    updateTotal(0,0);
                }
                if (checkedId == R.id.fiveDiscountRadio){
                    voucherToUse = getMinValue(discountVouhcers);
                    System.out.println(voucherToUse);
                    updateTotal(0,1);
                }
                if ( checkedId == R.id.noVoucherRadio){

                    updateTotal(0,0);
                }
            }
        });

        generateAndStoreKeys();
    }

    public void checkAvailableVouchers(){
        vouchers = DatabaseHelper.getInstance(this).getVouchers();

        for(int i = 0; i < vouchers.size(); i++){
            if(vouchers.get(i).getStringFromType(vouchers.get(i).getType()).equals("FREE_COFFEE")){
                coffeeVouchers.add(vouchers.get(i).getId());
                freeCoffee.setClickable(true);
            }
            else  if(vouchers.get(i).getStringFromType(vouchers.get(i).getType()).equals("FIVE_PERCENT_DISCOUNT")){
                discountVouhcers.add(vouchers.get(i).getId());
                fiveDiscount.setClickable(true);
            }
        }

    }

    public static Long getMinValue(ArrayList<Long> array) {
        Long minValue = array.get(0);
        for (int i = 1; i < array.size(); i++) {
            if (array.get(i) < minValue) {
                minValue = array.get(i);
            }
        }
        return minValue;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        double defaultValue = 0;
        int defaultId = -1;
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case(STATIC_INT_VALUE) : {
                if(resultCode == RESULT_OK){
                    String name = data.getStringExtra("newName");
                    double price = data.getDoubleExtra("newPrice",defaultValue);
                    String type = data.getStringExtra("newType");
                    int id = data.getIntExtra("id", defaultId);
                    System.out.println("NAME " +name + "PRICE " + price + "TYPE " + type);
                    addItemToRequest(name, price, type, id);
                }
                break;
            }
        }

    }

    private byte[] constructMessage() {
        int nr = IDS.size();                                  //
        ByteBuffer bb = ByteBuffer.allocate((nr + 3)); // wrap and allocate a byte[] for message and signature
        int customerId = ((Long) sessionContent.get("id")).intValue();
        bb.put((byte) customerId);
        bb.put((byte) nr);

        for (int k=0; k<nr; k++){
            bb.put(IDS.get(k).byteValue());                   // put each type (times nr. of items)
        }

        //VOUCHERS AQUI


        if(voucherToUse != 0){
            bb.put((byte) 1);
            bb.put((byte) voucherToUse.intValue());
        }
        else{
            bb.put((byte) 0);
        }




       /* if(((RadioButton) findViewById( R.id.freeCoffeeRadio)).isChecked()) {
            bb.put((byte) 1); //flag a dizer que um voucher foi escolhido
            bb.put((byte) 1); // id do voucher
        } else if(((RadioButton) findViewById( R.id.fiveDiscountRadio)).isChecked()) {
            bb.put((byte) 1); // flag a dizer que um voucher foi escolhido
            bb.put((byte) 2); // id do voucher
        } else {
            bb.put((byte) 0);
        } */

       

        byte[] message = bb.array();                           // get the byte[] for signing

        /* KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);          // get key entry from the keystore
            PrivateKey pri = ((KeyStore.PrivateKeyEntry)entry).getPrivateKey();   // get the private key
            Signature sg = Signature.getInstance("SHA1WithRSA");                  // build a signing object
            sg.initSign(pri);                                                     // define the signature key
            sg.update(message, 0, nr + 3);                                          // define the message bytes to be signed
            sg.sign(message, nr + 3, Constants.KEY_SIZE/8);                         // sign and append the signature to the message bytes
        } catch (Exception e) {
            e.printStackTrace();
        } */

        return message;
    }

    private void generateAndStoreKeys(){
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.keyname, null);         // verify if the keystore has already the keys
            if (entry == null) {
                Calendar start = new GregorianCalendar();
                Calendar end = new GregorianCalendar();
                end.add(Calendar.YEAR, 20);                                       // start and end validity
                KeyPairGenerator kgen = KeyPairGenerator.getInstance("RSA", Constants.ANDROID_KEYSTORE);    // keys for RSA algorithm
                @SuppressLint({"NewApi", "LocalSuppress"}) AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(this)      // specification for key generation
                        .setKeySize(Constants.KEY_SIZE)                                       // key size in bits
                        .setAlias(Constants.keyname)                                          // name of the entry in keystore
                        .setSubject(new X500Principal("CN=" + Constants.keyname))             // identity of the certificate holding the public key (mandatory)
                        .setSerialNumber(BigInteger.valueOf(12121212))                        // certificate serial number (mandatory)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                kgen.initialize(spec);
                KeyPair kp = kgen.generateKeyPair();                                      // the keypair is automatically stored in the app keystore
            }
        }
        catch (Exception ex) {
            Log.d("ERROR GENERATING KEY: ", ex.getMessage());
        }
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return NAMES.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.customlayout,null);

            ImageView imageView = (ImageView)view.findViewById(R.id.imageView2);
            TextView textView_name = (TextView)view.findViewById(R.id.textView_name);
            TextView textView_amount = (TextView)view.findViewById(R.id.textView_amount);
            switch(TYPES.get(i)){
                case("DRINKS"):
                    imageView.setImageResource(R.drawable.ic_drinks);
                    break;
                case("MEALS"):
                    imageView.setImageResource(R.drawable.ic_meals);
                    break;
                case("SNACKS"):
                    imageView.setImageResource(R.drawable.ic_snacks);
                    break;
                case("CAKES"):
                    imageView.setImageResource(R.drawable.ic_cake);
                    break;
                case("COFFEE"):
                    imageView.setImageResource(R.drawable.ic_coffee);
                    break;
                default:
                    imageView.setImageResource(R.drawable.ic_warning_black_24dp);
                    break;
            }
            textView_name.setText(NAMES.get(i));
            textView_amount.setText(String.valueOf(PRICES.get(i)));
            return view;
        }
    }

    public void goToAddItem(View view){
        Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
        startActivityForResult(intent,STATIC_INT_VALUE);
    }

    public  void addItemToRequest(String name, double price, String type, int id) {
        NAMES.add(name);
        PRICES.add(price);
        TYPES.add(type);
        IDS.add(id);
        updateTotal(price,0);
        cursorAdapter.notifyDataSetChanged();
    }

    public void updateTotal(double price, int discountVoucher){

        if(discountVoucher == 0){
            totalMoney = totalMoney  + price;
            total.setText("Total: " + totalMoney);
        }
        else if ( discountVoucher == 1 ) {
            total.setText("Total: " + (float)Math.round((totalMoney *0.95) * 100) / 100);
        }


    }

}
