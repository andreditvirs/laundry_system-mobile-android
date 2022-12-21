package com.uwika.laundrysystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailShopActivity extends AppCompatActivity implements View.OnClickListener {

    String uuid;
    private String URL = "https://andreditvirs.com/laundry-aje/shop.php";
    private String URL_ROOT = "https://andreditvirs.com/laundry-aje/";
    private String URL_TRANSACTIONS = "https://andreditvirs.com/laundry-aje/transactions.php";
    CollapsingToolbarLayout collapsing_toolbar;
    ImageView imgV_shop_detail;
    TextView txtV_detail_address, txtV_detail_note, txtV_detail_open_close_time;
    CardView crdV_cuci_basah,crdV_cuci_kering, crdV_cuci_setrika, crdV_setrika;
    TextView tvTitleCuciBasah, tvTitleCuciKering, tvTitleCuciSetrika, tvTitleSetrika;
    String shop_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_shop);

        uuid = getIntent().getExtras().getString("uuid");
        collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        imgV_shop_detail = findViewById(R.id.imgV_shop_detail);
        txtV_detail_address = findViewById(R.id.txtV_detail_address);
        txtV_detail_open_close_time = findViewById(R.id.txtV_detail_open_close_time);
        txtV_detail_note = findViewById(R.id.txtV_detail_note);

        crdV_cuci_basah = findViewById(R.id.crdV_cuci_basah);
        crdV_cuci_basah.setOnClickListener(this);
        crdV_cuci_kering = findViewById(R.id.crdV_cuci_kering);
        crdV_cuci_kering.setOnClickListener(this);
        crdV_cuci_setrika = findViewById(R.id.crdV_cuci_setrika);
        crdV_cuci_setrika.setOnClickListener(this);
        crdV_setrika = findViewById(R.id.crdV_setrika);
        crdV_setrika.setOnClickListener(this);

        tvTitleCuciBasah = findViewById(R.id.tvTitleCuciBasah);
        tvTitleCuciKering = findViewById(R.id.tvTitleCuciKering);
        tvTitleCuciSetrika = findViewById(R.id.tvTitleCuciSetrika);
        tvTitleSetrika = findViewById(R.id.tvTitleSetrika);

        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL+"?uuid="+uuid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject data = new JSONObject(response);
                    boolean error = Boolean.parseBoolean(data.getString("error"));

                    if(error){
                        String error_msg = data.getString("error_msg");
                        Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                    }else{
                        JSONObject dataDataReal = data.getJSONObject("data");
                        Glide.with(getApplicationContext()).load(URL_ROOT + dataDataReal.getString("image_url")).into(imgV_shop_detail);
                        shop_name = dataDataReal.getString("name");
                        collapsing_toolbar.setTitle(dataDataReal.getString("name"));
                        txtV_detail_address.setText(dataDataReal.getString("address"));
                        txtV_detail_open_close_time.setText("Jam buka: "+ dataDataReal.getString("open_time") + " s/d "+ dataDataReal.getString("close_time"));
                        txtV_detail_note.setText(dataDataReal.getString("note"));

                        JSONArray dataServiceShop = dataDataReal.getJSONArray("service_shop");
                        for(int i = 0; i < dataServiceShop.length(); i++){
                            switch (dataServiceShop.getJSONObject(i).getString("name")){
                                case "Cuci Basah": crdV_cuci_basah.setVisibility(View.VISIBLE); tvTitleCuciBasah.setText("Cuci Basah: Rp. " +dataServiceShop.getJSONObject(i).getString("price")+",00/kg"); break;
                                case "Cuci Kering": crdV_cuci_kering.setVisibility(View.VISIBLE); tvTitleCuciKering.setText("Cuci Kering: Rp. " +dataServiceShop.getJSONObject(i).getString("price")+",00/kg"); break;
                                case "Cuci Setrika": crdV_cuci_setrika.setVisibility(View.VISIBLE); tvTitleCuciSetrika.setText("Cuci Setrika: Rp. " +dataServiceShop.getJSONObject(i).getString("price")+",00/kg"); break;
                                case "Setrika": crdV_setrika.setVisibility(View.VISIBLE); tvTitleSetrika.setText("Setrika: Rp. " +dataServiceShop.getJSONObject(i).getString("price")+",00/kg"); break;
                            }
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
                Toast.makeText(DetailShopActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("uwika-laundry-aje",MODE_PRIVATE);
        String name = sharedPreferences.getString("profile_name", null);
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("shop_name", shop_name);

        Intent intent = new Intent(DetailShopActivity.this, TransactionActivityReq.class);
        switch (view.getId()){
            case R.id.crdV_cuci_basah:{
                reqService("Cuci Basah");
                bundle.putString("type", "Cuci Basah");
                bundle.putString("image_url", "cuci_basah");
                break;
            }
            case R.id.crdV_cuci_kering:{
                reqService("Cuci Kering");
                bundle.putString("type", "Cuci Kering");
                bundle.putString("image_url", "cuci_kering");
                break;
            }
            case R.id.crdV_cuci_setrika:{
                reqService("Cuci Setrika");
                bundle.putString("type", "Cuci & Setrika");
                bundle.putString("image_url", "cuci_dan_setrika");
                break;
            }
            case R.id.crdV_setrika:{
                reqService("Setrika");
                bundle.putString("type", "Setrika");
                bundle.putString("image_url", "setrika");
                break;
            }
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void reqService(String service_name){
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, URL_TRANSACTIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject data = new JSONObject(response);
                    boolean error = Boolean.parseBoolean(data.getString("error"));

                    if(error){
                        String error_msg = data.getString("error_msg");
                        Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                    }else{
                        String msg = data.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailShopActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences sharedPreferences = getSharedPreferences("uwika-laundry-aje",MODE_PRIVATE);
                String username = sharedPreferences.getString("profile_username", null);

                Map<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("uuid", uuid);
                data.put("status", "request");
                data.put("services_name", service_name);
                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}