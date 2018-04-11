package org.feup.cmov.acmecoffee;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;

import org.feup.cmov.acmecoffee.Database.DatabaseHelper;
import org.feup.cmov.acmecoffee.Model.Voucher;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

import static org.feup.cmov.acmecoffee.Database.DatabaseHelper.getInstance;

public class ViewVouchersActivity extends AppCompatActivity {

    public int freeCoffeeAmount = 0;
    public  int discountAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vouchers);

        updateVouchersQuantity();
    }

    private void updateVouchersQuantity(){


        final TextView freeCoffeeQuantity = (TextView) findViewById(R.id.freecoffee_quantity);
        final TextView discountQuantity = (TextView) findViewById(R.id.discount_quantity);
        ArrayList<Voucher> vouchers = DatabaseHelper.getInstance(this).getVouchers();

        for(int i = 0; i < vouchers.size(); i++){
            if(vouchers.get(i).getStringFromType(vouchers.get(i).getType()) == "FREE_COFFEE"){
                freeCoffeeAmount++;
            }
            else  if(vouchers.get(i).getStringFromType(vouchers.get(i).getType()) == "FIVE_PERCENT_DISCOUNT"){
                discountAmount++;
            }
        }
        freeCoffeeQuantity.setText("Quantity: " + Integer.toString(freeCoffeeAmount));
        discountQuantity.setText("Quantity: " + Integer.toString(discountAmount));

    }


}
