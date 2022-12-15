package com.uwika.laundrysystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_signup;
    AppCompatButton btn_login;
    TextInputEditText et_username, et_password;

    private String URL = "https://andreditvirs.com/laundry-aje/auth/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signup: {
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            }
            case R.id.btn_login:{
                login(view);
                break;
            }
        }
    }

    public void login(View view){
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        if(!username.equals("") && !password.equals("")){
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
                            System.out.println("LOG DATA USER "+ data.get("user"));

                            // shared preferences
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("username", username);
                    data.put("password", password);
                    return data;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(LoginActivity.this, "Masukkan username dan password terlebih dahulu", Toast.LENGTH_LONG).show();
        }
    }
}