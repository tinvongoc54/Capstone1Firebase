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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.philong.banhang.Adapter.Adapter_Spinner_Category;
import com.example.philong.banhang.Objects.Category;
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

public class Update_All_Product_Insert extends AppCompatActivity {


    //khai bao url
    MainActivity mainActivity = new MainActivity();
    String urlCategory = mainActivity.urlIPAddress + "/GraceCoffee/getdataCategory.php";
    String urlInsertProduct = mainActivity.urlIPAddress + "/GraceCoffee/insertProduct.php";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("grace");
    DatabaseReference myRefCatalog = myRef.child("catalog");
    DatabaseReference myRefProduct = myRef.child("product");

    //Khai bao Spinner
    Spinner spn_Category;

    //Khai bao ArrayList
    final ArrayList<Category> arrayList=new ArrayList<>();

    //khai bao Adapter
    Adapter_Spinner_Category adapter_spinner_category;

    String category;

    EditText editTextInsertProductName,editTextInsertProductPrice;

    Button buttonInsertProductConfirm,buttonInsertProductCancel, buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_update__add);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("intent_tendanhmuc"));
        AnhXa();
        XuLyEvent();
        GetDataCategorySpinner();
        SetUpSpinner();
    }



    void AnhXa(){
        editTextInsertProductName=findViewById(R.id.edittext_insert_product_name);
        editTextInsertProductPrice=findViewById(R.id.edittext_insert_product_price);


        spn_Category=findViewById(R.id.spinner_category);

        buttonInsertProductConfirm=findViewById(R.id.button_product_insert_confirm);
        buttonInsertProductCancel=findViewById(R.id.button_product_insert_cancel);
        buttonBack = findViewById(R.id.buttonBack);
    }

    void XuLyEvent(){
        spn_Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                category= String.valueOf(arrayList.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Update_All_Product_Insert.this, "đéo có j", Toast.LENGTH_SHORT).show();
            }
        });

        buttonInsertProductConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editTextInsertProductName.getText().toString().trim();
                String price=editTextInsertProductPrice.getText().toString().trim();
                if(name.isEmpty()||price.isEmpty()){
                    Toast.makeText(Update_All_Product_Insert.this, "Bạn chưa nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    InsertProduct();
                    Toast.makeText(Update_All_Product_Insert.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Update_All_Product_Insert.this, Update_All_Product.class);
                    startActivity(intent);
                }
            }
        });
        buttonInsertProductCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update_All_Product_Insert.this, Update_All_Product.class);
                startActivity(intent);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Update_All_Product_Insert.this, Update_All_Product.class));
            }
        });

    }

    public void GetDataCategorySpinner() {
        myRefCatalog.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String catalog_name = snapshot.child("catalog_name").getValue().toString();
                    String catalogs_des = snapshot.child("catalogs_des").getValue().toString();
                    arrayList.add(new Category(catalog_name, catalogs_des));
                }
                adapter_spinner_category.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //setup for Spinner
    void SetUpSpinner(){

        adapter_spinner_category=new Adapter_Spinner_Category(this,R.layout.item_view_row_spinner,arrayList);
        spn_Category.setAdapter(adapter_spinner_category);

    }
    String category_name = "";
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            category_name = intent.getStringExtra("category_name");
        }
    };

    public void InsertProduct() {
        String name = editTextInsertProductName.getText().toString();
        String price = editTextInsertProductPrice.getText().toString();
        String key = myRefProduct.push().getKey();
        myRefProduct.child(key).setValue(new Product(name, "", category_name, price));
        myRefProduct.child(key).child("product_image").setValue("");
    }
//  private void InsertProduct(String url){
//        RequestQueue requestQueue= Volley.newRequestQueue(this);
//        //POST để đấy lên
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {//khi insert thành công
//                if(response.trim().equals("success")){//success là báo thành công trên php lấy xuống để dùng
//                    Toast.makeText(Update_All_Product_Insert.this, "Them thanh cong", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(Update_All_Product_Insert.this,Update_All_Product.class));
//                }
//                else {
//
//                    Toast.makeText(Update_All_Product_Insert.this, "có lỗi gì rồi", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {//khi insert thất bại
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Update_All_Product_Insert.this, "Loi ", Toast.LENGTH_SHORT).show();
//                Log.d("AAA","Loi!\n"+error.toString());//chi tiết lỗi
//            }
//        }
//        ){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                //tạo map để đẩy lên
//                Map<String,String> params=new HashMap<>();
//                params.put("tenMon",editTextInsertProductName.getText().toString().trim());//đẩy lên Json
//                params.put("gia",editTextInsertProductPrice.getText().toString().trim());//đẩy lên Json
//                params.put("maMuc",category);
//                return params;
//            }
//        };
//
//        requestQueue.add(stringRequest);//add vao
//
//    }


}
