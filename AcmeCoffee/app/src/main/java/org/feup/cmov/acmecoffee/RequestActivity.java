package org.feup.cmov.acmecoffee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONArray;

import java.nio.ByteBuffer;
import java.util.ArrayList;

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



        Button btnSend = (Button) findViewById(R.id.sendRequest);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] message = constructMessage();
                Intent intent = new Intent(getApplicationContext(), QRCodeActivity.class);
                intent.putExtra("data", message);
                startActivity(intent);
            }

        });


        checkAvailableVouchers();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.freeCoffeeRadio){

                    updateTotal(0,0);
                }
                if (checkedId == R.id.fiveDiscountRadio){
                    updateTotal(0,1);
                }
                if ( checkedId == R.id.noVoucherRadio){
                    updateTotal(0,0);
                }
            }
        });

    }
    public void checkAvailableVouchers(){
        vouchers = DatabaseHelper.getInstance(this).getVouchers();

        for(int i = 0; i < vouchers.size(); i++){
            if(vouchers.get(i).getStringFromType(vouchers.get(i).getType()) == "FREE_COFFEE"){
                freeCoffee.setClickable(true);
            }
            else  if(vouchers.get(i).getStringFromType(vouchers.get(i).getType()) == "FIVE_PERCENT_DISCOUNT"){
                fiveDiscount.setClickable(true);
            }
        }

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
        ByteBuffer bb = ByteBuffer.allocate(nr+1); // wrap and allocate a byte[] for message and signature
        bb.put((byte) nr);

        for (int k=0; k<nr; k++)
            bb.put(IDS.get(k).byteValue());                   // put each type (times nr. of items)
        byte[] message = bb.array();                           // get the byte[] for signing

        return message;

        /* int nitems = ad.getCount();
        for (int k = 0; k < nitems; k++)
            if (ad.getItem(k).selected)
                sels.add(ad.getItem(k).type);                  // list of selected items
        int nr = sels.size();                                  // nr. of selected items
        ByteBuffer bb = ByteBuffer.allocate((nr+1)+512/8);     // wrap and allocate a byte[] for message and signature
        bb.put((byte) nr);                                      // put the nr. of elements
        for (int k=0; k<nr; k++)
            bb.put(sels.get(k).byteValue());                   // put each type (times nr. of items)
        byte[] message = bb.array();                           // get the byte[] for signing */
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
        startActivityForResult(intent,STATIC_INT_VALUE );
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
