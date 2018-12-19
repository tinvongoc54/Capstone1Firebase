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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Update_All_Table_Insert extends AppCompatActivity {
    EditText editTextInsertTable, editTextTableDes;
    Button buttonInsertTableConfirm,buttonInsertTableCancel, buttonBack;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("grace");
    DatabaseReference myRefTable = myRef.child("table");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_table);
        AnhXa();
        XuLySuKien();
    }

    void AnhXa(){
        editTextInsertTable=findViewById(R.id.edittext_number_table);
        editTextTableDes = findViewById(R.id.edittext_table_des);
        buttonInsertTableConfirm=findViewById(R.id.button_table_update_insert_confirm);
        buttonInsertTableCancel=findViewById(R.id.button_table_update_insert_cancel);
        buttonBack = findViewById(R.id.buttonBack);

    }

    void XuLySuKien(){
        buttonInsertTableConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberTable=editTextInsertTable.getText().toString().trim();
                if(numberTable.isEmpty()){
                    Toast.makeText(Update_All_Table_Insert.this, "Bạn chưa nhập tên bàn!", Toast.LENGTH_SHORT).show();
                } else {
                    InsertTable();
                    Toast.makeText(Update_All_Table_Insert.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Update_All_Table_Insert.this, Update_All_Table.class));
                }
            }
        });

        buttonInsertTableCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Update_All_Table_Insert.this, Update_All_Table.class));
            }
        });

//        buttonBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Update_All_Table_Insert.this, Update_All_Table.class));
//            }
//        });
    }
    private void InsertTable(){
        String name = editTextInsertTable.getText().toString();
        String des = editTextTableDes.getText().toString();
        Table table = new Table(name, des);
        myRefTable.push().setValue(table);
    }
}
