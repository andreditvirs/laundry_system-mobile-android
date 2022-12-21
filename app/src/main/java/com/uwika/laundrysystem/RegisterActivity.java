package com.uwika.laundrysystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_signin, btn_register;
    TextInputEditText et_nik, et_name, et_address, et_username;
    EditText et_password;
    private String URL = "https://andreditvirs.com/laundry-aje/auth/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_signin = findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(this);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        et_nik = findViewById(R.id.et_nik);
        et_name = findViewById(R.id.et_name);
        et_address = findViewById(R.id.et_address);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signin: {
                this.finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            }
            case R.id.btn_register:{
                this.register(view);
            }
        }
    }

    public void register(View view){
        String nik = et_nik.getText().toString();
        String name = et_name.getText().toString();
        String address = et_address.getText().toString();
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        if(!nik.isEmpty() && !name.isEmpty() && !address.isEmpty() && !username.isEmpty() && !password.isEmpty()){
            StringRequest stringRequest =  new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject data = new JSONObject(response);
                        boolean error = Boolean.parseBoolean(data.getString("error"));

                        if(error){
                            String error_msg = data.getString("error_msg");
                            Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                        }else{
                            JSONObject dataUser = data.getJSONObject("user");
                            String username = dataUser.getString("username");
                            Toast.makeText(getApplicationContext(), "Username "+username+" sudah didaftarkan, Silahkan melakukan login", Toast.LENGTH_LONG).show();

                            et_nik.setText("");
                            et_name.setText("");
                            et_address.setText("");
                            et_username.setText("");
                            et_password.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("nik", username);
                    data.put("username", username);
                    data.put("password", password);
                    data.put("name", name);
                    data.put("address", address);
                    data.put("role", "user");
                    return data;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(RegisterActivity.this, "Lengkapi semua form terlebih dahulu!", Toast.LENGTH_LONG).show();
        }
    }
}