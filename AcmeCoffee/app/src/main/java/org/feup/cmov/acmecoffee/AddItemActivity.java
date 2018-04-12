package org.feup.cmov.acmecoffee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.cmov.acmecoffee.Database.DatabaseHelper;
import org.feup.cmov.acmecoffee.Model.Item;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<String> itemsNames = new ArrayList<>();
    ArrayList<Item> items = new ArrayList<>();
    private Spinner itemsSpinner;
    TextView priceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        priceInfo = (TextView) findViewById(R.id.princeInfo);
        fillSpinner();
    }

    public void fillSpinner(){
        items = DatabaseHelper.getInstance(this).getItems();

        for(int i = 0; i < items.size(); i++){
            itemsNames.add(items.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemsSpinner = (Spinner) findViewById(R.id.items_spinner);
        itemsSpinner.setAdapter(adapter);
        itemsSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        String selectedItem = adapterView.getItemAtPosition(i).toString();
        String itemPrice= Double.toString(getSelectedItem(selectedItem).getPrice());
        priceInfo.setText(itemPrice + "€");
       // Toast.makeText(adapterView.getContext(),selectedItem,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void addSelectedItem(View view){
        String name = (itemsSpinner.getSelectedItem().toString());
        Item item = getSelectedItem(name);

        if(item != null) {
            Intent intent = new Intent();
            intent.putExtra("newName",name);
            intent.putExtra("newPrice",item.getPrice());
            intent.putExtra("newType",item.getType().name());
            intent.putExtra("id", item.getId().intValue());
            System.out.println("ID: " + item.getId());
            setResult(RequestActivity.RESULT_OK,intent);
            finish();
        }
    }

    public Item getSelectedItem(String name) {
        for (int i = 0; i < items.size(); i++){
            if(name == items.get(i).getName()){
                return items.get(i);
            }
        }
        return null;
    }

}
