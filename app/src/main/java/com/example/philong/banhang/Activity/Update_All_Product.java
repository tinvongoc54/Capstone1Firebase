package com.example.philong.banhang.Activity;
//
//
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.philong.banhang.Adapter.Adapter_Product_Update;
import com.example.philong.banhang.Adapter.Adapter_Product_Update;
import com.example.philong.banhang.Objects.Product;
import com.example.philong.banhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Update_All_Product extends AppCompatActivity {
    //khai báo ArrayList
    ArrayList<Product> menu_updatesArrayList=new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("grace");
    DatabaseReference myRefProduct = myRef.child("product");

    //Khai báo Adapter
    Adapter_Product_Update menu_adapter_update;

    //Khai báo recyclerView
    RecyclerView recyclerViewUpdateMenu;

    //khai báo các nút
    Button btn_UpdateMenu_Add, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_menu);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("intent_tenmoncanxoa"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver1, new IntentFilter("intent_tenmoncansua"));
        anhXa();
        GetDataMenu();
        SetupForRecyclerView();
        xuLyEvent();

    }

    public void GetDataMenu() {
        myRefProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String product_name = snapshot.child("product_name").getValue().toString();
                    String product_price = snapshot.child("product_price").getValue().toString();
                    String category_name = snapshot.child("category_name").getValue().toString();
                    String product_des = snapshot.child("product_description").getValue().toString();
                    menu_updatesArrayList.add(new Product(product_name, product_des, category_name, product_price));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        menu_adapter_update=new Adapter_Product_Update(menu_updatesArrayList,getApplicationContext(),this);
        recyclerViewUpdateMenu.setAdapter(menu_adapter_update);

    }
    public BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String product_name = intent.getStringExtra("tenmon");
            String product_price = intent.getStringExtra("gia");
            Intent intent1 = new Intent(Update_All_Product.this, Update_All_Product_Update.class);
            intent1.putExtra("name", product_name);
            intent1.putExtra("price", product_price);
            startActivity(intent1);
        }
    };

    String product_name = "";
    int vitrixoa = 0;
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            product_name = intent.getStringExtra("tenmon");
            vitrixoa = intent.getIntExtra("vitrixoa", 123);
            DeleteProduct(product_name, vitrixoa);
        }
    };

    public void DeleteProduct(final String name, final int vitrixoa) {
        myRefProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (name.equalsIgnoreCase(snapshot.child("product_name").getValue().toString())) {
                        myRefProduct.child(key).removeValue();
                        Log.d("checkPo", String.valueOf(vitrixoa));
                        menu_updatesArrayList.remove(vitrixoa);
                        menu_adapter_update.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void anhXa(){
        recyclerViewUpdateMenu = findViewById(R.id.recycler_view_menu);
        btn_UpdateMenu_Add=findViewById(R.id.button_UpdateMenu_add);
        buttonBack = findViewById(R.id.buttonBack);
    }

    void xuLyEvent(){
        btn_UpdateMenu_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Update_All_Product.this,Update_All_Product_Insert.class));

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Update_All_Product.this, Update_All.class));
            }
        });
    }

    void SetupForRecyclerView(){
        //setup cho recyclerviewMain
        recyclerViewUpdateMenu.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false);
        recyclerViewUpdateMenu.setLayoutManager(layoutManager);

        //setup gan adapter for recyclerviewMain
        menu_adapter_update=new Adapter_Product_Update(menu_updatesArrayList,getApplicationContext(),this);
        recyclerViewUpdateMenu.setAdapter(menu_adapter_update);
    }

    //setup menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
