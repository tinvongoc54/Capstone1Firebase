package com.example.philong.banhang.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.philong.banhang.Adapter.Adapter_Table;
import com.example.philong.banhang.Adapter.Adapter_Update_Table;
import com.example.philong.banhang.Objects.Product;
import com.example.philong.banhang.Objects.Table;
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

public class Update_All_Table extends AppCompatActivity {

    ArrayList<Table> tableArrayList = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("grace");
    DatabaseReference myRefTable = myRef.child("table");

    Adapter_Update_Table adapter_table;

    RecyclerView recyclerviewAddTable;

    Button buttonInsertTable, buttonBack;

    //khai báo 2 thuộc tính để setup grid layout
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_table);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("intent_bancanxoa"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver1, new IntentFilter("intent_bancansua"));
        AnhXa();
        XuLySuKien();
        GetDataTable();
        SetupRecyclerView();
    }
    void AnhXa(){
        buttonInsertTable=findViewById(R.id.button_update_table_add);
        recyclerviewAddTable=findViewById(R.id.recycler_view_add_table);
        buttonBack = findViewById(R.id.buttonBack);
    }

    void XuLySuKien(){
        buttonInsertTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Update_All_Table.this,Update_All_Table_Insert.class));
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Update_All_Table.this, Update_All.class));
            }
        });
    }


    public void GetDataTable(){
        myRefTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String table_name = snapshot.child("table_name").getValue().toString();
                    String table_describe = snapshot.child("table_describe").getValue().toString();
                    tableArrayList.add(new Table(table_name, table_describe));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter_table = new Adapter_Update_Table(tableArrayList,getApplicationContext(),this);
        recyclerviewAddTable.setAdapter(adapter_table);
    }

    String product_name = "";
    int vitrixoa = 0;
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            product_name = intent.getStringExtra("ban");
            vitrixoa = intent.getIntExtra("vitrixoa", 123);
            DeleteTable(product_name, vitrixoa);
        }
    };

    public BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String table_name = intent.getStringExtra("ban");
            String table_des = intent.getStringExtra("mota");
            Intent intent1 = new Intent(Update_All_Table.this, Update_All_Table_Update.class);
            intent1.putExtra("ban", table_name);
            intent1.putExtra("mota", table_des);
            startActivity(intent1);
        }
    };

    public void DeleteTable(final String name, final int vitrixoa){
        myRefTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (name.equalsIgnoreCase(snapshot.child("table_name").getValue().toString())) {
                        myRefTable.child(key).removeValue();
                        tableArrayList.remove(vitrixoa);
                        adapter_table.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void SetupRecyclerView(){

        //Setup cấu hình cho recycler table
        recyclerViewLayoutManager = new GridLayoutManager(context, 5);
        recyclerviewAddTable.setHasFixedSize(true);
        recyclerviewAddTable.setLayoutManager(recyclerViewLayoutManager);

        //Setup gán adapter cho recycler table
        adapter_table=new Adapter_Update_Table(tableArrayList,getApplicationContext(),this);
        recyclerviewAddTable.setAdapter(adapter_table);
    }


}
