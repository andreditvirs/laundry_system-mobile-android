package com.uwika.laundrysystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    RecyclerView rV_last_journals;
    TransactionAdapter last_journal_adapter;
    RecyclerView.LayoutManager layout_manager;
    ArrayList<TransactionModel> last_journal_item = new ArrayList<>();

    private String URL_TRANSACTIONS = "https://andreditvirs.com/laundry-aje/transactions.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        showTransactions();
    }

    public void showTransactions()
    {
        rV_last_journals = findViewById(R.id.rV_last_journals);
        rV_last_journals.setHasFixedSize(true);

        layout_manager = new GridLayoutManager(this, 1);
        rV_last_journals.setLayoutManager(layout_manager);

        SharedPreferences sharedPreferences = getSharedPreferences("uwika-laundry-aje",MODE_PRIVATE);
        String username = sharedPreferences.getString("profile_username", null);
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL_TRANSACTIONS+"?username="+username, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject data = new JSONObject(response);
                    boolean error = Boolean.parseBoolean(data.getString("error"));

                    if(error){
                        String error_msg = data.getString("error_msg");
                        Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                    }else{
                        JSONArray dataTransactions = data.getJSONArray("data");
                        for (int i = 0; i < dataTransactions.length(); i++){
                            JSONObject jsonObject = dataTransactions.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String code = jsonObject.getString("code");
                            String deliver_estimated_time = jsonObject.getString("deliver_estimated_time");
                            String pick_estimated_time = jsonObject.getString("pick_estimated_time");
                            String picked_time = jsonObject.getString("picked_time");
                            String price = jsonObject.getString("price");
                            String weight = jsonObject.getString("weight");
                            String evidence = jsonObject.getString("evidence");
                            String status = jsonObject.getString("status");

                            JSONObject service_shop = dataTransactions.getJSONObject(i).getJSONObject("service_shop");
                            String service_name = service_shop.getString("service_name");

//                            last_journal_item.add(new TransactionModel());
//                            last_journal_adapter = new ShopAdapter(TransactionActivity.this, last_journal_item);
//                            rV_last_journals.setAdapter(last_journal_adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransactionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
//        SharedPreferences sharedPreferences = getSharedPreferences("uwika-laundry-aje",MODE_PRIVATE);
        String name = sharedPreferences.getString("profile_name", null);
    }
}