package org.feup.cmov.acmecoffee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.feup.cmov.acmecoffee.Database.DatabaseHelper;
import org.feup.cmov.acmecoffee.Model.Request;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class RequestsRecordActivity extends AppCompatActivity {

    ArrayList<Long> ids = new ArrayList<>();
    ArrayList<Double> prices = new ArrayList<>();
    public ArrayList<Request> requests = new ArrayList<>();
    public CustomAdapter cursorAdapter = new CustomAdapter();
    public TextView moneySpent;
    public double money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_record);
        requests = DatabaseHelper.getInstance(this).getRequests();
        moneySpent = (TextView) findViewById(R.id.requestsRecord_moneySpent);
        money = 0;

        fillData();

        ListView listView = (ListView) findViewById(R.id.requestsRecordListView);
        listView.setAdapter(cursorAdapter);


    }

    public void fillData(){
        for(int i = 0; i < requests.size(); i++){
            ids.add(requests.get(i).getId());
            prices.add(requests.get(i).getTotalPrice());
            money = money + requests.get(i).getTotalPrice();
        }

        moneySpent.setText("Total money spent: " + String.valueOf(money) + "€");

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ids.size();
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

            view = getLayoutInflater().inflate(R.layout.requests_record_layout,null);


            TextView textView_id = (TextView)view.findViewById(R.id.requestRecord_id);
            TextView textView_price = (TextView)view.findViewById(R.id.requestRecord_price);

            textView_id.setText("Request id: " + String.valueOf(ids.get(i)));
            textView_price.setText("Price: " + String.valueOf(prices.get(i))+ "€");
            return view;
        }
    }
}
