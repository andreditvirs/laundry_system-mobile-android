package com.uwika.laundrysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TransactionActivityReq extends AppCompatActivity {

    ImageView imgV_transaction_icon;
    TextView txtV_greeting_transaction, txtV_description;
    Button btn_oke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_req);

        txtV_greeting_transaction = findViewById(R.id.txtV_greeting_transaction);
        txtV_description = findViewById(R.id.txtV_description);
        imgV_transaction_icon = findViewById(R.id.imgV_transaction_icon);
        btn_oke = findViewById(R.id.btn_oke);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String type = bundle.getString("type");
        String shop_name = bundle.getString("shop_name");
        String image_url = bundle.getString("image_url");

        txtV_greeting_transaction.setText("Hai, "+name+"!");
        txtV_description.setText("Permintaan Layanan "+type+"\nsedang dalam konfirmasi\n"+shop_name);

        String uri = "@drawable/"+image_url;
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        imgV_transaction_icon.setImageDrawable(res);

        btn_oke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(TransactionActivityReq.this, HomeActivity.class));
            }
        });
    }
}