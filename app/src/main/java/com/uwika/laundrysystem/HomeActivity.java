package com.uwika.laundrysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
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

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static final String[] COUNTRIES = new String[]{"Sidoarjo", "Surabaya", "Jombang", "Blitar"};
    RecyclerView rV_last_journals;
    ShopAdapter last_journal_adapter;
    RecyclerView.LayoutManager layout_manager;
    ArrayList<ShopModel> last_journal_item = new ArrayList<>();
    TextView greeting;

    private String URL_SHOP = "https://andreditvirs.com/laundry-aje/shop.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.home_appbar);
        setSupportActionBar(toolbar);

        AutoCompleteTextView autoCTxtV_journals = findViewById(R.id.autoCTxtV_journals);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_list_item, R.id.txtV_list_item, COUNTRIES);
        autoCTxtV_journals.setAdapter(adapter);

        greeting = findViewById(R.id.greeting);

        showShops();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_notif: {
                startActivity(new Intent(HomeActivity.this, TransactionActivity.class));
                return true;
            }
            case R.id.btn_logout: {
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                this.finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void showShops()
    {
        rV_last_journals = findViewById(R.id.rV_last_journals);
        rV_last_journals.setHasFixedSize(true);

        layout_manager = new GridLayoutManager(this, 1);
        rV_last_journals.setLayoutManager(layout_manager);

        StringRequest stringRequest =  new StringRequest(Request.Method.POST, URL_SHOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject data = new JSONObject(response);
                    boolean error = Boolean.parseBoolean(data.getString("error"));

                    if(error){
                        String error_msg = data.getString("error_msg");
                        Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                    }else{
                        JSONArray dataShops = data.getJSONArray("data");
                        for (int i = 0; i < dataShops.length(); i++){
                            JSONObject jsonObject = dataShops.getJSONObject(i);
                            String uuid = jsonObject.getString("uuid");
                            String name = jsonObject.getString("name");
                            String open_time = jsonObject.getString("open_time");
                            String close_time = jsonObject.getString("close_time");
                            String address = jsonObject.getString("address");
                            String owner_name = jsonObject.getString("owner_name");
                            String image_url = jsonObject.getString("image_url");
                            last_journal_item.add(new ShopModel(uuid, name, open_time, close_time, address, owner_name, image_url));
                            last_journal_adapter = new ShopAdapter(HomeActivity.this, last_journal_item);
                            rV_last_journals.setAdapter(last_journal_adapter);
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
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        SharedPreferences sharedPreferences = getSharedPreferences("uwika-laundry-aje",MODE_PRIVATE);
        String name = sharedPreferences.getString("profile_name", null);
        greeting.setText("Hai, "+ name + "!");
    }
}