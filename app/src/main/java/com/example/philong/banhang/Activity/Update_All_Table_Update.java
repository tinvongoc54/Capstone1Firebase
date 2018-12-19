package com.example.philong.banhang.Activity;

import android.content.Intent;
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
import com.example.philong.banhang.Objects.Table;
import com.example.philong.banhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Update_All_Table_Update extends AppCompatActivity {
    //khai bao cac nut
    EditText editTextUpdateTableNumber, editTextUpdateTableDes;
    Button buttonUpdateTableConfirm,buttonUpdateTableCancel, buttonBack;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("grace");
    DatabaseReference myRefTable = myRef.child("table");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_table__update);

        AnhXa();
        XuLySuKien();
    }

    void AnhXa(){
        editTextUpdateTableNumber=findViewById(R.id.edittext_update_number_table);
        editTextUpdateTableDes = findViewById(R.id.edittext_update_table_des);
        buttonUpdateTableConfirm=findViewById(R.id.button_table_update_confirm);
        buttonUpdateTableCancel=findViewById(R.id.button_table_update_cancel);
        buttonBack = findViewById(R.id.buttonBack);
    }

    String table_name = "";
    String table_des = "";

    void XuLySuKien(){
        Intent intent = getIntent();
        table_name = intent.getStringExtra("ban");
        table_des = intent.getStringExtra("mota");
        editTextUpdateTableNumber.setText(table_name);
        editTextUpdateTableDes.setText(table_des);

        buttonUpdateTableConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberTable = editTextUpdateTableNumber.getText().toString().trim();
                if(numberTable.isEmpty()){
                    Toast.makeText(Update_All_Table_Update.this, "Bạn chưa nhập tên bàn!", Toast.LENGTH_SHORT).show();
                } else {
                    CapNhapTable();
                    Toast.makeText(Update_All_Table_Update.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Update_All_Table_Update.this, Update_All_Table.class));
                }
            }
        });

        buttonUpdateTableCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Update_All_Table_Update.this, Update_All_Table.class));
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Update_All_Table_Update.this, Update_All_Table.class));
            }
        });
    }


    public void CapNhapTable(){
        myRefTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (table_name.equalsIgnoreCase(snapshot.child("table_name").getValue().toString())) {
                        myRefTable.child(key).child("table_name").setValue(editTextUpdateTableNumber.getText().toString().trim());
                        myRefTable.child(key).child("table_describe").setValue(editTextUpdateTableDes.getText().toString().trim());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
