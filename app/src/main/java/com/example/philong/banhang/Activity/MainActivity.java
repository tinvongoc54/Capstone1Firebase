package com.example.philong.banhang.Activity;

import android.app.Dialog;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;
import android.support.v7.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.philong.banhang.Adapter.Adapter_Category_Listview;
import com.example.philong.banhang.Adapter.Adapter_Product_Main;
import com.example.philong.banhang.Adapter.Adapter_Table;
import com.example.philong.banhang.Adapter.Adapter_Product_Bill;
import com.example.philong.banhang.Objects.Bill;
import com.example.philong.banhang.Objects.Bill_Detail;
import com.example.philong.banhang.Objects.Category;
import com.example.philong.banhang.Objects.Product;
import com.example.philong.banhang.Objects.Product_Bill;
import com.example.philong.banhang.Objects.Promotion;
import com.example.philong.banhang.Objects.Table;

import com.example.philong.banhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("grace");
        DatabaseReference myRefBill = myRef.child("bill");
        DatabaseReference myRefCategory = myRef.child("catalog");
        DatabaseReference myRefProduct = myRef.child("product");
        DatabaseReference myRefPromotion = myRef.child("promotion");
        DatabaseReference myRefTable = myRef.child("table");

        static int count_notification = 0;
        static int count_stt = 1;
        static int count_category_quantity = 0;
        static int count_product_quantity = 0;
        static int billNumberOfDay = 0;

        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        SimpleDateFormat dfH = new SimpleDateFormat("HH:mm:ss");


        //khai báo thuộc tính con
        public TextView txt_get_time,textViewNumberTable, textViewTotal, textViewThanhTien, textViewStaff, textViewStatus;
        public EditText editTextPercent, editTextPromotion, editTextGhiChu;
        Button btn_update_menu, btn_exit;
        Button buttonMainPrint, buttonManagement, buttonThongKe;
        NotificationBadge notiBadge;
        Button createBill;
        Handler mHandler;

        //khai báo recyclerview
        RecyclerView recyclerView_bill, recyclerView_product, recyclerView_table;
        ListView lvCategory;

        //khai báo arrayList

        ArrayList<Table> tableArrayList = new ArrayList<>();
        ArrayList<Product> ProductArrayList = new ArrayList<>();
        ArrayList<Product_Bill> arrayListBill = new ArrayList<>();
        ArrayList<Category> CategoryArrayList = new ArrayList<>();


        //khai báo adapter
        Adapter_Product_Bill menu_adapter_update_bill;
        Adapter_Product_Main adapter_product;
        Adapter_Table adapter_table;
        Adapter_Category_Listview adapter_category_listview;

        //khai báo 2 thuộc tính để setup grid layout
        RecyclerView.LayoutManager recyclerViewLayoutManager;
        Context context;

        static final int MESSAGE_THANH_TIEN = 1;
        static final int MESSAGE_THANH_TIEN_2 = 2;
        static final int MESSAGE_CHECK_ORDER_ONLINE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anhXa();
        anhXaHandler();

//        CreateFirebase();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("intent_tenmon"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiverPosition, new IntentFilter("intent_vitrixoabill"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiverCategory, new IntentFilter("intent_category"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiverKey, new IntentFilter("intent_key"));


        GetCategory(myRefCategory);

        SetupRecycerView();
        GetDataTable();
        XuLyEvent();
        timer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String key = "";
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        if (key!= null) {
            getItemPopupMenu(key);
        }
    }

    public void CreateFirebase() {
//        Product product1 = new Product("Coffee đen", "Coffee rang xay", "Coffee", 15000);
//        Product product2 = new Product("Coffee sữa", "Coffee rang xay", "Coffee", 20000);
//        Product product3 = new Product("Coffee đen SG", "Coffee rang xay", "Coffee", 20000);
//        Product product4 = new Product("Coffee sữa SG", "Coffee rang xay", "Coffee", 25000);
//        Product product5 = new Product("Trà hoa hồng", "Trà ngon", "Trà đặc biệt", 30000);
//        myRefProduct.push().setValue(product1);
//        myRefProduct.push().setValue(product2);
//        myRefProduct.push().setValue(product3);
//        myRefProduct.push().setValue(product4);
//        myRefProduct.push().setValue(product5);

//        Category category = new Category("Machiato", "aaaaaaa");
//        myRefCategory.push().setValue(category);
        String key = myRefPromotion.push().getKey();
        myRefPromotion.child(key).child("promotion_name").setValue("Lễ 30/4");
        myRefPromotion.child(key).child("promotion_code").setValue("trasua01");
        myRefPromotion.child(key).child("promotion_description").setValue("Nhân dịp lễ 30/4");
        myRefPromotion.child(key).child("promotion_discount").setValue("10000");
        myRefPromotion.child(key).child("promotion_type").setValue("VNĐ");
    }

    public void tinhThanhTien() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = MESSAGE_THANH_TIEN; //tạo tên msg
                msg.arg1 = Integer.parseInt(textViewTotal.getText().toString().trim()); //gán giá trị cho msg
                msg.arg2 = Integer.parseInt(editTextPercent.getText().toString().trim());
                mHandler.sendMessage(msg); //gửi vào handler
            }
        });
        thread.start();
    }
    public void xuLyThanhTien() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Message msg = new Message();
                msg.what = MESSAGE_THANH_TIEN_2; //tạo tên msg
                final String code = editTextPromotion.getText().toString();
                if (!code.equals("")) {
                    myRefPromotion.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String proCode = snapshot.child("promotion_code").getValue().toString();
                                String proDis = snapshot.child("promotion_discount").getValue().toString();
                                String proType = snapshot.child("promotion_type").getValue().toString();
                                if (editTextPromotion.getText().toString().equalsIgnoreCase(proCode)) {
                                    msg.arg1 = Integer.parseInt(proDis);
                                    mHandler.sendMessage(msg); //gửi vào handler
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    msg.arg1 = 0;
                    mHandler.sendMessage(msg);
                }

            }
        });
        thread.start();
    }

    public void checkOrderOnline() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Message msg1 = new Message();
                msg1.what = MESSAGE_CHECK_ORDER_ONLINE; //tạo tên msg

                myRefBill.limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String status = snapshot.child("status").getValue().toString();
                            if (status.equals("1")) {
                                msg1.arg1 = 1;
                            } else {
                                msg1.arg1 = 2;
                            }
                            mHandler.sendMessage(msg1); //gửi vào handler
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        thread.start();
    }

    public void anhXaHandler() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_THANH_TIEN:
                        textViewThanhTien.setText(String.valueOf(msg.arg1 - msg.arg1*msg.arg2/100));
                    case MESSAGE_THANH_TIEN_2:
                        int total = Integer.parseInt(textViewTotal.getText().toString());
                        int discount = Integer.parseInt(editTextPercent.getText().toString());
                        if (msg.arg1 > 100) {
                            textViewThanhTien.setText(String.valueOf(total - total*discount/100 - msg.arg1));
                        } else {
                            textViewThanhTien.setText(String.valueOf(total - total*discount/100));
                        }
                    case MESSAGE_CHECK_ORDER_ONLINE:
//                        if (msg.arg1 == 2) {
//                            notiBadge.setText(String.valueOf(++count_notification));
//                            Toast.makeText(MainActivity.this, "number: " + count_notification, Toast.LENGTH_SHORT).show();
//                        } else {
////                            Toast.makeText(MainActivity.this, "time: "+df.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
//                        }
                }
            }
        };
    }
//    function decode(id) {
//        id = id.substring(0, 8);
//        var timestamp = 0;
//        for (var i = 0; i < id.length; i++) {
//            var c = id.charAt(i);
//            timestamp = timestamp * 64 + PUSH_CHARS.indexOf(c);
//        }
//        return timestamp;
//    }

    //chuyển push key firebase về datetime
    String PUSH_CHARS = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";
    public long change(String key) {
        key = key.substring(0, 8);
        long timestamp =0;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            timestamp = timestamp * 64 + PUSH_CHARS.indexOf(c);
        }
        return timestamp;
    }


//    public void test() {
//        myRefBill.limitToLast(1).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String key = snapshot.getKey();
//                    Timestamp ts = new Timestamp(change(key));
//                    Date date = new Date(ts.getTime());
//                    Log.d("checkTime2", String.valueOf(df.format(date)));
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


    public void anhXa(){
        //ánh xạ recyclerview
        recyclerView_bill = findViewById(R.id.recycler_view_bill);
        recyclerView_product = findViewById(R.id.recycler_view_product);
        recyclerView_table = findViewById(R.id.recycler_view_table);
//        recyclerView_category = findViewById(R.id.recycler_view_category);
        lvCategory = findViewById(R.id.listView_Category);

        textViewNumberTable=findViewById(R.id.textview_number_table);
        txt_get_time=findViewById(R.id.text_view_getTime);
        textViewTotal = findViewById(R.id.text_view_toTal);
        textViewThanhTien = findViewById(R.id.text_view_thanhTien);

        //get tên staff
        textViewStaff = findViewById(R.id.textView_Staff);
        SharedPreferences sharedPreferences = getSharedPreferences("SaveLogin", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("Username", "");
        textViewStaff.setText(name);


        textViewStatus = findViewById(R.id.text_view_status);
        editTextPercent = findViewById(R.id.editText_perCent);
        editTextPromotion = findViewById(R.id.editText_promotion);
        editTextGhiChu = findViewById(R.id.editTextGhiChu);
        txt_get_time.setText(df.format(calendar.getTime()));

        notiBadge = findViewById(R.id.btn_alerm);
        createBill = findViewById(R.id.btn_createBill);

        //ánh xạ button trên tittle
        btn_exit=findViewById(R.id.button_exit);
        btn_update_menu=findViewById(R.id.btn_update_menu);
        buttonMainPrint=findViewById(R.id.button_main_print);
        buttonManagement = findViewById(R.id.btn_management);
        buttonThongKe = findViewById(R.id.btn_statistical);

    }

    public void timer() {
        final String formattedDate = df.format(calendar.getTime());


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                txt_get_time.setText(String.valueOf(formattedDate));
//                GetDataTable(urlGetDataTable);
//                checkOrderOnline();
                mHandler.postDelayed(this,1000);
            }
        },1000);

//        Timer timer=new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                GetDataTable(urlGetDataTable);
//
////                txt_get_time.setText(String.valueOf(formattedDate));
////                checkOrderOnline();
//
//            }
//        },1000,10000);
    }

    public void GetDataTable() {
        myRefTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("table_name").getValue().toString();
                    String des = snapshot.child("table_describe").getValue().toString();
                    tableArrayList.add(new Table(name, des));
                }
                adapter_table = new Adapter_Table(tableArrayList, getApplicationContext(), MainActivity.this, textViewNumberTable);
                recyclerView_table.setAdapter(adapter_table);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void XuLyEvent(){
        buttonManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, QuanLyBillActivity.class));
            }
        });

        buttonThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ThongKeActivity.class));
            }
        });

        //xử lý tổng tiền thay đổi
        textViewTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tinhThanhTien();
                xuLyThanhTien();
            }
        });

        //xử lý giảm giá thay đổi
        editTextPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextPercent.getText().toString().trim().equals("")) {
                    editTextPercent.setText(String.valueOf(0));
                } else if (Integer.parseInt(editTextPercent.getText().toString().trim()) > 100) {
                    editTextPercent.setText(String.valueOf(100));
                }
                tinhThanhTien();
                xuLyThanhTien();
            }
        });


        //xử lý mã giảm giá thay đổi
        editTextPromotion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                editTextPercent.setText("0");
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                xuLyThanhTien();
            }
        });

        //xý lý nhấn enter mã giảm giá
        editTextPromotion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    return true;
                }
                return false;
            }
        });


        //xử lý event thời gian


        //event timer


        //xử lý button exit
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        //xử lý button update
        btn_update_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Update_All.class));
            }
        });

//      khi có thay đổi trên firebase sẽ gởi thông báo

        //xử lý hiện số thông báo
        createBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!editTextPercent.getText().toString().equals("0")) && (!editTextPromotion.getText().toString().equals(""))) {
                    Toast.makeText(MainActivity.this, "Không áp dụng các khuyến mãi cùng 1 lúc!", Toast.LENGTH_SHORT).show();
                } else if (textViewNumberTable.getText().toString().equalsIgnoreCase("Chưa chọn bàn")) {
                    Toast.makeText(MainActivity.this, "Bạn chưa chọn bàn cần phục vụ!", Toast.LENGTH_SHORT).show();
                } else if (arrayListBill.size() == 0) {
                    Toast.makeText(MainActivity.this, "Chưa có sản phẩm nào trong hóa đơn!", Toast.LENGTH_SHORT).show();
                } else {
                    ConfirmCreateBill();
                }
            }
        });
        notiBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count_notification = 0;
                notiBadge.setNumber(count_notification);
                showPopupMenu(notiBadge);
            }
        });

        editTextPercent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    return true;
                }
                return false;
            }
        });

        buttonMainPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBill();
            }
        });

    }

    void SetupRecycerView(){
        //Setup cấu hình cho recycler bill
        recyclerView_bill.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false);
        recyclerView_bill.setLayoutManager(layoutManager1);

        //Setup cấu hình cho recycler product
        recyclerViewLayoutManager = new GridLayoutManager(context, 1);
        recyclerView_product.setHasFixedSize(true);
        recyclerView_product.setLayoutManager(recyclerViewLayoutManager);

        //Setup cấu hình cho recycler table
        recyclerViewLayoutManager = new GridLayoutManager(context, 5);
        recyclerView_table.setHasFixedSize(true);
        recyclerView_table.setLayoutManager(recyclerViewLayoutManager);

        //Setup gán adapter cho recycler table
        adapter_table=new Adapter_Table(tableArrayList,getApplicationContext(),this, textViewNumberTable);
        recyclerView_table.setAdapter(adapter_table);


        //Setup gán adapter cho recycler product
        adapter_product=new Adapter_Product_Main(ProductArrayList,getApplicationContext());
        recyclerView_product.setAdapter(adapter_product);

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String category_name = intent.getStringExtra("category");
            String product_name = intent.getStringExtra("name");
            String product_description = intent.getStringExtra("description");
            String product_price = intent.getStringExtra("price");

            int viTri = 0, size = 0;
            boolean check = false;
            for (int i = 0; i<arrayListBill.size(); i++) {
                if (product_name.equalsIgnoreCase(arrayListBill.get(i).getProduct_name())) {
                    viTri = i;
                    check = true;
                    size = arrayListBill.get(i).getSize();
                    break;
                }
            }
            if (check) {
                arrayListBill.get(viTri).setSize(++size);
            } else {
                arrayListBill.add(new Product_Bill(product_name, product_description, category_name, product_price, 1));
            }

            //settext cho tổng tiền
            textViewTotal.setText(String.valueOf(tongTien()));

            menu_adapter_update_bill = new Adapter_Product_Bill(arrayListBill,getApplicationContext(),textViewTotal);
            recyclerView_bill.setAdapter(menu_adapter_update_bill);

        }
    };

    public int tongTien() {
        int tong=0;
        for (int i=0; i<arrayListBill.size(); i++) {
            Product_Bill product_bill = arrayListBill.get(i);
            tong += Integer.parseInt(product_bill.getProduct_price())*product_bill.getSize();
        }
        return tong;
    }

    public BroadcastReceiver mMessageReceiverPosition = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("position", 123);
            arrayListBill.remove(position);

            //settext lại cho tổng tiền
            textViewTotal.setText(String.valueOf(tongTien()));

            menu_adapter_update_bill = new Adapter_Product_Bill(arrayListBill, getApplicationContext(), textViewTotal);
            recyclerView_bill.setAdapter(menu_adapter_update_bill);
        }
    };

    public BroadcastReceiver mMessageReceiverCategory = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String category_name = intent.getStringExtra("category_name");
            GetDataProduct(myRefProduct, category_name);
        }
    };

    public BroadcastReceiver mMessageReceiverKey = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            getItemPopupMenu(key);
        }
    };

    //lấy dữ liệu product theo mục category
    public void GetDataProduct(DatabaseReference myRef, final String category) {
        myRef.orderByChild("category_name").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProductArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //lấy từng dữ liệu theo kiểu Product
                    Product product = snapshot.getValue(Product.class);

                    //lấy các thành phần trong product
                    String product_name = product.getProduct_name();
                    String product_description = product.getProduct_description();
                    String category_name = product.getCategory_name();
                    String product_price = product.getProduct_price();

                    //gán vào arraylist
                    ProductArrayList.add(new Product(product_name, product_description, category_name, product_price));

                }
                adapter_product.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //lấy dữ liệu category
    public void GetCategory(DatabaseReference myRef) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CategoryArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    CategoryArrayList.add(new Category(category.getCatalog_name(), category.getCatalogs_des()));
                }
                //setup gans adapter cho recycler category
                adapter_category_listview = new Adapter_Category_Listview(getApplicationContext(), CategoryArrayList, R.layout.item_view_row_category);
                lvCategory.setAdapter(adapter_category_listview);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    String code = "";
    String discount = "";
    String type = "VNĐ";
    //Insert bill lên firebase
    public void CreateBill(final DatabaseReference myRef) {
        final String key = myRef.push().getKey();

        String bill_note = editTextGhiChu.getText().toString();
        //lấy time hiện tại
        Timestamp ts = new Timestamp(change(key));
        Date date = new Date(ts.getTime());
        String formattedDate = df.format(date);

        String price_after_promotion = textViewThanhTien.getText().toString();

        String total = textViewTotal.getText().toString();
        Bill bill = new Bill(bill_note, formattedDate, textViewStaff.getText().toString(), "1", textViewNumberTable.getText().toString(), total, price_after_promotion);

        myRef.child(key).setValue(bill);

        //set giá trị customer
        myRef.child(key).child("customer").child("email").setValue("");
        myRef.child(key).child("customer").child("name").setValue("");
        myRef.child(key).child("customer").child("phone").setValue("");

        //set value promotion
        final String promotionCode = editTextPromotion.getText().toString();
        if (editTextPromotion.getText().toString().equals("")) {
            myRef.child(key).child("promotion").child("promotion_code").setValue("");
            if (discount.equals("0")) {
//                promotion = new Promotion("", "0", "")
                discount = "0";
            } else {
//                promotion = new Promotion("", discount, "%");
                discount = editTextPercent.getText().toString();
                type = "%";
            }
            myRef.child(key).child("promotion").child("promotion_discount").setValue(discount);
            myRef.child(key).child("promotion").child("promotion_type").setValue(type);

        } else {
            myRefPromotion.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean check = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        code = snapshot.child("promotion_code").getValue().toString();
                        discount = snapshot.child("promotion_discount").getValue().toString();
                        type = snapshot.child("promotion_type").getValue().toString();

                        if (promotionCode.trim().equals(code)) {
                            myRef.child(key).child("promotion").child("promotion_code").setValue(code);
                            myRef.child(key).child("promotion").child("promotion_discount").setValue(discount);
                            myRef.child(key).child("promotion").child("promotion_type").setValue(type);
                            check = true;
                        }
                        break;
                    }
                    if (check == false) {
                        myRef.child(key).child("promotion").child("promotion_code").setValue("");
                        myRef.child(key).child("promotion").child("promotion_discount").setValue("0");
                        myRef.child(key).child("promotion").child("promotion_type").setValue("VNĐ");
                    }
//                    promotion = new Promotion(promotion_code, promotion_discount, promotion_type);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //set value bill_detail
        Map<String, Object> myMap = new HashMap<String, Object>();
        for (int i=0; i<arrayListBill.size(); i++) {
            Product_Bill product_bill = arrayListBill.get(i);

            Bill_Detail bill_detail = new Bill_Detail(product_bill.getProduct_price(), String.valueOf(product_bill.getSize()));

            myMap.put(product_bill.getProduct_name(), bill_detail);
        }

        myRef.child(key).child("bill_detail").setValue(myMap);
    }

    //Xác nhận tạo bill
    public void ConfirmCreateBill() {


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm_createbill);
        dialog.setCanceledOnTouchOutside(false);

        final TextView textview_ThanhTien = dialog.findViewById(R.id.textViewThanhTien);
        final EditText editext_TienKhachDua = dialog.findViewById(R.id.editTextTienKhachDua);
        final TextView textview_TienThua = dialog.findViewById(R.id.textViewTienThua);
        Button buttonXacNhan = dialog.findViewById(R.id.buttonXacNhanDialog);
        Button buttonHuy = dialog.findViewById(R.id.buttonHuyDialog);

        textview_ThanhTien.setText(textViewThanhTien.getText().toString());

        editext_TienKhachDua.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textview_TienThua.setText("0");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editext_TienKhachDua.getText().toString().equals("")) {
                    editext_TienKhachDua.setText("0");
                }
                int TKD = Integer.parseInt(editext_TienKhachDua.getText().toString());
                int TT = Integer.parseInt(textview_ThanhTien.getText().toString());
                if (TKD > TT) {
                    textview_TienThua.setText(String.valueOf(TKD - TT));
                }
            }
        });

        editext_TienKhachDua.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    if (editext_TienKhachDua.getText().toString().equals("")) {
                        editext_TienKhachDua.setText("0");
                    }
                    int TKD = Integer.parseInt(editext_TienKhachDua.getText().toString());
                    int TT = Integer.parseInt(textview_ThanhTien.getText().toString());
                    if (TKD < TT) {
                        Toast.makeText(MainActivity.this, "Tiền khách đưa phải lớn hơn tổng tiền!", Toast.LENGTH_SHORT).show();
                    } else {
                        textview_TienThua.setText(String.valueOf(TKD - TT));
                        CreateBill(myRefBill);
                        dialog.dismiss();
                        newBill();
                    }


                    return true;
                }
                return false;
            }
        });

        buttonXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateBill(myRefBill);
                dialog.dismiss();
                newBill();
            }
        });

        buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showPopupMenu(View v) {
        final PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
        popupMenu.inflate(R.menu.popup_menu_list_bill);
        popupMenu.show();
        final ArrayList<String> arrayListKey = new ArrayList<>();

        int i=1;
        while (i<=5) {
            myRefBill.orderByKey().limitToLast(i).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())  {
                        arrayListKey.add(snapshot.getKey());

                        String[] time = snapshot.child("date_create").getValue().toString().split(" ");
                        String key = snapshot.getKey();
                        String billnumber = key.substring(key.length() - 5);
                        popupMenu.getMenu().add(0, arrayListKey.size(), 0, "HĐ: " + billnumber + " - " + time[1]);
                        break;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            i++;
        }


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.itemAll:
                        startActivity(new Intent(MainActivity.this, QuanLyBillActivity.class));
                        return true;
                    case 1:
                        getItemPopupMenu(arrayListKey.get(0));
                        return true;
                    case 2:
                        getItemPopupMenu(arrayListKey.get(1));
                        return true;
                    case 3:
                        getItemPopupMenu(arrayListKey.get(2));
                        return true;
                    case 4:
                        getItemPopupMenu(arrayListKey.get(3));
                        return true;
                    case 5:
                        getItemPopupMenu(arrayListKey.get(4));
                        return true;
                }
                return false;
            }
        });
    }

    static String customer_name = "";
    static String customer_phone = "";
    static String customer_email = "";
    static String ghichu = "";

    public void getItemPopupMenu(final String key) {
        buttonMainPrint.setText("Xác nhận");
        createBill.setText("Thoát");
        Timestamp ts = new Timestamp(change(key));
        final Date date = new Date(ts.getTime());

        //Lấy dữ liệu của bill đc chọn
        myRefBill.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                txt_get_time.setText(df.format(date));
                if (dataSnapshot.child("table").getValue().toString().equalsIgnoreCase("null")) {
                    textViewNumberTable.setText("Order online");
                } else {
                    textViewNumberTable.setText(dataSnapshot.child("table").getValue().toString());
                }

                textViewTotal.setText(dataSnapshot.child("total_price").getValue().toString());
                if (dataSnapshot.child("promotion").child("promotion_type").getValue().toString().equals("%")) {
                    editTextPercent.setText(dataSnapshot.child("promotion").child("promotion_discount").getValue().toString());
                } else {
                    editTextPercent.setText("0");
                }
                if (dataSnapshot.child("status").getValue().toString().equals("0")) {
                    textViewStatus.setText("Đang chờ xác nhận");
                } else {
                    textViewStatus.setText("Đã xác nhận");
                }
                editTextPromotion.setText(dataSnapshot.child("promotion").child("promotion_code").getValue().toString());
                textViewThanhTien.setText(dataSnapshot.child("total_price_after_promotion").getValue().toString());
                editTextGhiChu.setText(dataSnapshot.child("bill_note").getValue().toString());

                customer_name = dataSnapshot.child("customer").child("name").getValue().toString();
                customer_phone = dataSnapshot.child("customer").child("phone").getValue().toString();
                customer_email = dataSnapshot.child("customer").child("email").getValue().toString();



                arrayListBill.clear();
                readBillDetail(key);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Khi bấm nút Xác nhận thì sẽ hiện dialog thông tin KH
        buttonMainPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XacNhanBillOnline(key);
            }
        });

        //Khi bấm button Thoát thì sẽ về như ban đầu
        createBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThoatBillOnline();
            }
        });
    }

    public void XacNhanBillOnline(final String key) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_thong_tin_khach_hang);

        TextView textViewTenKH = dialog.findViewById(R.id.textViewTenKH);
        TextView textViewSDT = dialog.findViewById(R.id.textViewSDT);
        TextView textViewEmail = dialog.findViewById(R.id.textViewEmail);
        TextView textViewGhiChu = dialog.findViewById(R.id.textViewGhiChu);

        Button buttonXacNhan = dialog.findViewById(R.id.buttonXacNhanDialog);
        Button buttonQuayLai = dialog.findViewById(R.id.buttonQuayLaiDialog);

        textViewTenKH.setText(customer_name);
        textViewSDT.setText(customer_phone);
        textViewEmail.setText(customer_email);
        textViewGhiChu.setText(ghichu);


        buttonXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!editTextPercent.getText().toString().equals("0")) && (!editTextPromotion.getText().toString().equals(""))) {
                    Toast.makeText(MainActivity.this, "Không được áp dụng các khuyến mãi cùng 1 lúc!", Toast.LENGTH_SHORT).show();
                } else if (textViewStatus.getText().toString().equalsIgnoreCase("Đã xác nhận")) {
                    Toast.makeText(MainActivity.this, "Đơn đã được xác nhận!", Toast.LENGTH_SHORT).show();
                } else {
                    ConfirmCreateOnlineBill(key);
                    dialog.dismiss();
                }
            }
        });

        buttonQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void ThoatBillOnline() {
        createBill.setText("Thanh toán");
        buttonMainPrint.setText("Tạo mới");
        newBill();
        createBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!editTextPercent.getText().toString().equals("0")) && (!editTextPromotion.getText().toString().equals(""))) {
                    Toast.makeText(MainActivity.this, "Không được áp dụng các khuyến mãi cùng 1 lúc!", Toast.LENGTH_SHORT).show();
                } else if (textViewNumberTable.getText().toString().equalsIgnoreCase("Chưa chọn bàn")) {
                    Toast.makeText(MainActivity.this, "Bạn chưa chọn bàn cần phục vụ!", Toast.LENGTH_SHORT).show();
                } else if (arrayListBill.size() == 0) {
                    Toast.makeText(MainActivity.this, "Chưa có sản phẩm nào trong hóa đơn!", Toast.LENGTH_SHORT).show();
                } else {
                    ConfirmCreateBill();
                }
            }
        });

        buttonMainPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBill();
            }
        });
    }

    public void ConfirmCreateOnlineBill(String key) {
        myRefBill.child(key).child("status").setValue("1");
        newBill();
    }

    public void newBill() {
        txt_get_time.setText(df.format(calendar.getTime()));
        textViewNumberTable.setText("Chưa chọn bàn");
        textViewStatus.setText("Đang phục vụ");
        arrayListBill.clear();
        menu_adapter_update_bill.notifyDataSetChanged();
        textViewTotal.setText("0");
        textViewThanhTien.setText("0");
        editTextPercent.setText("0");
        editTextPromotion.setText("");
        editTextGhiChu.setText("");
    }

    public void readBillDetail(String key) {
        myRefBill.child(key).child("bill_detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.getKey();
                    String price = snapshot.child("product_price").getValue().toString();
                    String size = snapshot.child("product_quantity").getValue().toString();
                    arrayListBill.add(new Product_Bill(name, "", "", price, Integer.parseInt(size)));
                }
                menu_adapter_update_bill = new Adapter_Product_Bill(arrayListBill, getApplicationContext(), textViewTotal);
                recyclerView_bill.setAdapter(menu_adapter_update_bill);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
