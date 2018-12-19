package com.example.philong.banhang.Activity;
//
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.example.philong.banhang.Objects.Product;
import com.example.philong.banhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Update_All_Product_Update extends AppCompatActivity {
    //khai bao cac nut
    EditText editTextName,editTextPrice;
    Button buttonConFirm,buttonCancel;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("grace");
    DatabaseReference myRefProduct = myRef.child("product");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_update__update);

        AnhXa();
        ShowEditProduct();
        XuLyEvent();

    }

    void AnhXa(){
        editTextName=findViewById(R.id.edittext_name);
        editTextPrice=findViewById(R.id.edittext_price);
        buttonConFirm=findViewById(R.id.button_menu_update_update_confirm);
        buttonCancel=findViewById(R.id.button_menu_update_update_cancel);
    }

    String product_name = "";
    String product_price = "";

    private void ShowEditProduct() {
        Intent intent = getIntent();
        product_name = intent.getStringExtra("name");
        product_price = intent.getStringExtra("price");
        editTextName.setText(product_name);
        editTextPrice.setText(product_price);
    }

    private void EditProduct() {
        myRefProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (product_name.equalsIgnoreCase(snapshot.child("product_name").getValue().toString())) {
                        myRefProduct.child(key).child("product_name").setValue(editTextName.getText().toString().trim());
                        myRefProduct.child(key).child("product_price").setValue(editTextPrice.getText().toString().trim());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void XuLyEvent(){
        buttonConFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=editTextName.getText().toString().trim();
                String price=editTextPrice.getText().toString().trim();
                if(name.isEmpty()||price.isEmpty()){
                    Toast.makeText(Update_All_Product_Update.this, "Bạn chưa nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else {
                    EditProduct();
                    Toast.makeText(Update_All_Product_Update.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Update_All_Product_Update.this, Update_All_Product.class));
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Update_All_Product_Update.this, Update_All_Product.class));
            }
        });
    }

}
