package org.feup.cmov.acmecoffee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.feup.cmov.acmecoffee.Database.DatabaseHelper;
import org.feup.cmov.acmecoffee.Model.Request;

import java.util.ArrayList;


public class RequestsRecordActivity extends AppCompatActivity {

    public ArrayList<Request> requests = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_record);

        requests = DatabaseHelper.getInstance(this).getRequests();
        System.out.println("reequest size: " + requests.size());
        for(int i = 0; i < requests.size();i++){
            System.out.println(requests.get(i));
        }
    }
}
