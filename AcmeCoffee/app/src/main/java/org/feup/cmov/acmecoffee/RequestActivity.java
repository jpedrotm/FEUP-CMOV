package org.feup.cmov.acmecoffee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity {

    public  ArrayList<String> NAMES = new ArrayList<>();
    public  ArrayList<Double> PRICES = new ArrayList<>();
    public  ArrayList<String> TYPES = new ArrayList<>();
    public  TextView total;
    public  double totalMoney;
    public static final int STATIC_INT_VALUE = 10;
    public static final int RESULT_OK = 11;
    public CustomAdapter cursorAdapter = new CustomAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        totalMoney = 0;
        total = (TextView) findViewById(R.id.total_money);


        ListView listView = (ListView) findViewById(R.id.requestListView);
        listView.setAdapter(cursorAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        double defaultValue = 0;
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case(STATIC_INT_VALUE) : {
                if(resultCode == RESULT_OK){
                    String name = data.getStringExtra("newName");
                    double price = data.getDoubleExtra("newPrice",defaultValue);
                    String type = data.getStringExtra("newType");
                    System.out.println("NAME " +name + "PRICE " + price + "TYPE " + type);
                    addItemToRequest(name,price,type);
                }
                break;
            }
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
        startActivityForResult(intent,STATIC_INT_VALUE );
    }



    public  void addItemToRequest(String name, double price, String type) {
        NAMES.add(name);
        PRICES.add(price);
        TYPES.add(type);
        updateTotal(price);
        cursorAdapter.notifyDataSetChanged();

    }

    public void updateTotal(double price){
        totalMoney = totalMoney + 1;
        total.setText("Total: " + totalMoney);
    }

}
