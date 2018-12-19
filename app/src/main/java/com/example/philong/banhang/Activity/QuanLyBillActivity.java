package com.example.philong.banhang.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.philong.banhang.Adapter.Adapter_Bill_History;
import com.example.philong.banhang.Adapter.Adapter_Product_Bill;
import com.example.philong.banhang.Objects.Bill;
import com.example.philong.banhang.Objects.Bill_History;
import com.example.philong.banhang.Objects.Product;
import com.example.philong.banhang.Objects.Product_Bill;
import com.example.philong.banhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class QuanLyBillActivity extends AppCompatActivity {
    ListView listViewBillHistory;
    ArrayList<Bill_History> arrayListBillHistory = new ArrayList<>();
    Adapter_Bill_History adapter_bill_history;

    Button buttonBack, buttonXacNhan, buttonThoat, buttonThongKe, buttonUpdate;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("grace");
    DatabaseReference myRefBill = myRef.child("bill");
    DatabaseReference myRefPromotion = myRef.child("promotion");

    public TextView txt_get_time,textViewNumberTable, textViewTotal, textViewThanhTien, textViewStatus;
    public EditText editTextPercent, editTextPromotion, editTextGhiChu;

    final Calendar calendar = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    ArrayList<Product_Bill> arrayListBill = new ArrayList<>();

    Adapter_Product_Bill adapter_product_bill;

    RecyclerView recyclerView_bill;

    static final int MESSAGE_THANH_TIEN = 1;
    static final int MESSAGE_THANH_TIEN_2 = 2;

    Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_bill);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("intent_billhistory"));
        AnhXa();
        anhXaHandler();
        SetUpRecyclerView();
        XuLySuKien();
        GetDataBill();
    }

    public void SetUpRecyclerView() {
        recyclerView_bill.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false);
        recyclerView_bill.setLayoutManager(layoutManager1);
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
                }
            }
        };
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

    private void XuLySuKien() {
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuanLyBillActivity.this, Update_All.class));
            }
        });

        buttonThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuanLyBillActivity.this, ThongKeActivity.class));
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuanLyBillActivity.this, MainActivity.class));
            }
        });
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
    }

    public void XacNhanBillOnline(final String key) {
        final Dialog dialog = new Dialog(QuanLyBillActivity.this);
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
                    Toast.makeText(QuanLyBillActivity.this, "Không được áp dụng các khuyến mãi cùng 1 lúc!", Toast.LENGTH_SHORT).show();
                } else if (textViewStatus.getText().toString().equalsIgnoreCase("Đã xác nhận")) {
                    Toast.makeText(QuanLyBillActivity.this, "Đơn đã được xác nhận!", Toast.LENGTH_SHORT).show();
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

    public void ConfirmCreateOnlineBill(String key) {
        myRefBill.child(key).child("status").setValue("1");
        GetDataBill();
        newBill();
    }

    public void newBill() {
        txt_get_time.setText(df.format(calendar.getTime()));
        textViewNumberTable.setText("Chưa chọn bàn");
        textViewStatus.setText("Đang phục vụ");
        arrayListBill.clear();
        adapter_product_bill.notifyDataSetChanged();
        textViewTotal.setText("0");
        textViewThanhTien.setText("0");
        editTextPercent.setText("0");
        editTextPromotion.setText("");
        editTextGhiChu.setText("");
    }

    public void AnhXa() {
        listViewBillHistory = findViewById(R.id.listViewBill);
        buttonBack = findViewById(R.id.buttonBack);
        recyclerView_bill = findViewById(R.id.recycler_view_bill);

        textViewNumberTable=findViewById(R.id.textview_number_table);
        txt_get_time=findViewById(R.id.text_view_getTime);
        textViewTotal = findViewById(R.id.text_view_toTal);
        textViewThanhTien = findViewById(R.id.text_view_thanhTien);
        textViewStatus = findViewById(R.id.text_view_status);
        editTextPercent = findViewById(R.id.editText_perCent);
        editTextPromotion = findViewById(R.id.editText_promotion);
        editTextGhiChu = findViewById(R.id.editTextGhiChu);


        buttonXacNhan = findViewById(R.id.buttonXacNhan);
        buttonThoat = findViewById(R.id.buttonThoat);
        buttonThongKe = findViewById(R.id.btn_statistical);
        buttonUpdate = findViewById(R.id.btn_update_menu);
    }

    private void GetDataBill() {
//        final DataSnapshot[] snapshot = new DataSnapshot[1];
//        myRefBill.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                snapshot[0] = dataSnapshot;
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
////        Log.d("checkCount", String.valueOf(snapshot[0].getChildrenCount()));
//        int i=1;
//        arrayListBillHistory.clear();
//        while (i <= 8) {
            myRefBill.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String note = snapshot.child("bill_note").getValue().toString();
                        String date = snapshot.child("date_create").getValue().toString();
                        String staff = snapshot.child("staff_create").getValue().toString();
                        String status = snapshot.child("status").getValue().toString();
                        String price = snapshot.child("total_price_after_promotion").getValue().toString();
                        String key = snapshot.getKey();
                        String name = snapshot.child("customer").child("name").getValue().toString();
                        String phone = snapshot.child("customer").child("phone").getValue().toString();

                        arrayListBillHistory.add(new Bill_History(note, date, staff, status, price, key, name, phone));

                    }
                    adapter_bill_history = new Adapter_Bill_History(arrayListBillHistory, getApplicationContext(), R.layout.custom_listview_bill);
                    listViewBillHistory.setAdapter(adapter_bill_history);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//        }
    }

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

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            GetDataBillToDetail(key);
        }
    };

    static String customer_name = "";
    static String customer_phone = "";
    static String customer_email = "";
    static String ghichu = "";

    public void GetDataBillToDetail(final String key) {
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
                textViewThanhTien.setText(formatNumber(Integer.parseInt(dataSnapshot.child("total_price_after_promotion").getValue().toString())));
                editTextGhiChu.setText(dataSnapshot.child("bill_note").getValue().toString());

                customer_name = dataSnapshot.child("customer").child("name").getValue().toString();
                customer_phone = dataSnapshot.child("customer").child("phone").getValue().toString();
                customer_email = dataSnapshot.child("customer").child("email").getValue().toString();
                ghichu = dataSnapshot.child("bill_note").getValue().toString();


                arrayListBill.clear();
                readBillDetail(key);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = textViewStatus.getText().toString();
                if (status.equalsIgnoreCase("Đang phục vụ")) {
                    Toast.makeText(QuanLyBillActivity.this, "Chưa chọn hóa đơn!", Toast.LENGTH_SHORT).show();
                } else if (status.equalsIgnoreCase("Đã xác nhận")) {
                    Toast.makeText(QuanLyBillActivity.this, "Đơn đã được xác nhận!", Toast.LENGTH_SHORT).show();
                } else {
                    if ((!editTextPercent.getText().toString().equals("0")) && (!editTextPromotion.getText().toString().equals(""))) {
                        Toast.makeText(QuanLyBillActivity.this, "Không áp dụng các khuyến mãi cùng 1 lúc!", Toast.LENGTH_SHORT).show();
                    } else if (arrayListBill.size() == 0) {
                        Toast.makeText(QuanLyBillActivity.this, "Chưa có sản phẩm nào trong hóa đơn!", Toast.LENGTH_SHORT).show();
                    } else {
                        XacNhanBillOnline(key);
                    }
                }
            }
        });

        buttonThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBill();
            }
        });
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
                adapter_product_bill = new Adapter_Product_Bill(arrayListBill, getApplicationContext(), textViewTotal);
                recyclerView_bill.setAdapter(adapter_product_bill);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static String formatNumber(int number) {
        if (number < 1000) {
            return String.valueOf(number);
        }
        try {
            NumberFormat formatter = new DecimalFormat("###,###");
            String resp = formatter.format(number);
            resp = resp.replaceAll(",", ".");
            return resp;
        } catch (Exception e) {
            return "";
        }
    }
}
