package com.example.philong.banhang.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.philong.banhang.Objects.Bill;
import com.example.philong.banhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

public class ThongKeActivity extends AppCompatActivity {
    ArrayList<String> arrayListChonNgay, arrayListChonThang, arrayListChonNam;
    ArrayList<Integer> arrayListBill = new ArrayList<>();

    Spinner spinnerChonNam, spinnerChonThang, spinnerChonNgay;
    Button buttonXemThongke, buttonBack, buttonManagement, buttonUpdate;
    NotificationBadge buttonNotification;
    TextView textViewDoanhThu, textViewTongHoaDon, textViewHoaDonOnline;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("grace");
    DatabaseReference myRefBill = myRef.child("bill");

    public static int onlineBillNumber = 0;

    ArrayAdapter<String> adapterNam, adapterThang, adapterNgay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        anhXa();
        setSpinnerView();
        xuLySuKien();
    }

    public void anhXa() {
        spinnerChonNam = findViewById(R.id.spinnerChonNam);
        spinnerChonThang = findViewById(R.id.spinnerChonThang);
        spinnerChonNgay = findViewById(R.id.spinnerChonNgay);
        buttonXemThongke = findViewById(R.id.btn_XemThongKe);
        buttonBack = findViewById(R.id.buttonBack);
        buttonManagement = findViewById(R.id.btn_management);
        buttonUpdate = findViewById(R.id.btn_update_menu);
        buttonNotification = findViewById(R.id.btn_alerm);
        textViewDoanhThu = findViewById(R.id.textViewDoanhThu);
        textViewHoaDonOnline = findViewById(R.id.textViewHoaDonOnline);
        textViewTongHoaDon = findViewById(R.id.textViewTongHoaDon);
    }

    public void setSpinnerView() {
        //set Năm
        arrayListChonNam = new ArrayList<>();
        arrayListChonNam.add("Năm");
        for(int i = 2017; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            arrayListChonNam.add(String.valueOf(i));
        }
        adapterNam = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListChonNam);
        adapterNam.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerChonNam.setAdapter(adapterNam);


        //set Tháng
        arrayListChonThang = new ArrayList<>();
        arrayListChonThang.add("Tháng");
        for (int i=1; i<13; i++) {
            if (i > 9) {
                arrayListChonThang.add(String.valueOf(i));
            } else {
                arrayListChonThang.add("0" + String.valueOf(i));
            }
        }
        adapterThang = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListChonThang);
        adapterThang.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerChonThang.setAdapter(adapterThang);


        //set Ngày
        arrayListChonNgay = new ArrayList<>();
        arrayListChonNgay.add("Ngày");

        adapterNgay = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListChonNgay);
        adapterNgay.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerChonNgay.setAdapter(adapterNgay);
    }

    public void xuLySuKien() {
        buttonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(buttonNotification);
            }
        });

        buttonManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThongKeActivity.this, QuanLyBillActivity.class));
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThongKeActivity.this, Update_All.class));
            }
        });

        spinnerChonThang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (spinnerChonThang.getSelectedItem().toString()) {
                    case "01": ThemNgay(31); break;
                    case "02":
                        String nam = spinnerChonNam.getSelectedItem().toString();
                        if (!nam.equalsIgnoreCase("Năm")) {
                            if (Integer.parseInt(nam) % 400 == 0) {
                                ThemNgay(28);
                            } else { ThemNgay(29); }
                        } else {
                            Toast.makeText(ThongKeActivity.this, "Vui lòng chọn năm!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "03": ThemNgay(31); break;
                    case "04": ThemNgay(30); break;
                    case "05": ThemNgay(31); break;
                    case "06": ThemNgay(30); break;
                    case "07": ThemNgay(31); break;
                    case "08": ThemNgay(31); break;
                    case "09": ThemNgay(30); break;
                    case "10": ThemNgay(31); break;
                    case "11": ThemNgay(30); break;
                    case "12": ThemNgay(31); break;
                }
                adapterNgay.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonXemThongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nam = spinnerChonNam.getSelectedItem().toString();
                final String thang = spinnerChonThang.getSelectedItem().toString();
                final String ngay = spinnerChonNgay.getSelectedItem().toString();
                onlineBillNumber = 0;
                arrayListBill.clear();

                if (!nam.equalsIgnoreCase("Năm")) {
                    if (!thang.equalsIgnoreCase("Tháng")) {
                        if (!ngay.equalsIgnoreCase("Ngày")) {
                            getDataStatisticDay(nam, thang, ngay);
                        } else {
                            getDataStatisticMonth(nam, thang);
                        }
                    } else {
                        getDataStatisticYear(nam);
                    }
                } else {
                    Toast.makeText(ThongKeActivity.this, "Bạn chưa chọn dữ liệu cần thống kê!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThongKeActivity.this, MainActivity.class));
            }
        });
    }

    public void getDataStatisticDay(final String nam, final String thang, final String ngay) {
        myRefBill.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String[] time = snapshot.child("date_create").getValue().toString().split(" ");
                    String[] date = time[0].split("-");
                    String thangSnap = date[0];
                    String ngaySnap = date[1];
                    String namSnap = date[2];

                    if (nam.equalsIgnoreCase(namSnap) && thang.equalsIgnoreCase(thangSnap) && ngay.equalsIgnoreCase(ngaySnap)) {
                        int total = Integer.parseInt(snapshot.child("total_price").getValue().toString());
                        arrayListBill.add(total);
                        Log.d("checkSize", String.valueOf(arrayListBill.size()));
                        if (snapshot.child("table").getValue().toString().equalsIgnoreCase("null")) {
                            onlineBillNumber++;
                        }
                    }
                }
                int tongDoanhThu = 0;
                for (int i=0; i<arrayListBill.size(); i++) {
                    tongDoanhThu += arrayListBill.get(i);
                }
                textViewDoanhThu.setText(formatNumber(tongDoanhThu));
                textViewHoaDonOnline.setText(String.valueOf(onlineBillNumber));
                textViewTongHoaDon.setText(String.valueOf(arrayListBill.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getDataStatisticMonth(final String nam, final String thang) {
        myRefBill.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String[] time = snapshot.child("date_create").getValue().toString().split(" ");
                    String[] date = time[0].split("-");
                    String thangSnap = date[0];
                    String namSnap = date[2];

                    if (nam.equalsIgnoreCase(namSnap) && thang.equalsIgnoreCase(thangSnap)) {
                        int total = Integer.parseInt(snapshot.child("total_price").getValue().toString());
                        arrayListBill.add(total);
                        Log.d("checkSize", String.valueOf(arrayListBill.size()));
                        if (snapshot.child("table").getValue().toString().equalsIgnoreCase("null")) {
                            onlineBillNumber++;
                        }
                    }
                }
                setDataStatistic(arrayListBill);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getDataStatisticYear(final String nam) {
        myRefBill.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String[] time = snapshot.child("date_create").getValue().toString().split(" ");
                    String[] date = time[0].split("-");
                    String namSnap = date[2];

                    if (nam.equalsIgnoreCase(namSnap)) {
                        int total = Integer.parseInt(snapshot.child("total_price").getValue().toString());
                        arrayListBill.add(total);
                        Log.d("checkSize", String.valueOf(arrayListBill.size()));
                        if (snapshot.child("table").getValue().toString().equalsIgnoreCase("null")) {
                            onlineBillNumber++;
                        }
                    }
                }
                setDataStatistic(arrayListBill);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setDataStatistic(ArrayList<Integer> arrayList) {
        int tongDoanhThu = 0;
        for (int i=0; i<arrayList.size(); i++) {
            tongDoanhThu += arrayList.get(i);
        }
        textViewDoanhThu.setText(formatNumber(tongDoanhThu));
        textViewHoaDonOnline.setText(String.valueOf(onlineBillNumber));
        textViewTongHoaDon.setText(String.valueOf(arrayList.size()));
    }

    public void showPopupMenu(View v) {
        final PopupMenu popupMenu = new PopupMenu(ThongKeActivity.this, v);
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
                        startActivity(new Intent(ThongKeActivity.this, QuanLyBillActivity.class));
                        return true;
                    case 1:
                        getItemPopupMenu(arrayListKey.get(0));
                        Log.d("checkPopup", arrayListKey.get(0));
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

    public void getItemPopupMenu(String key) {
//        Intent intent = new Intent("intent_key");
//        intent.putExtra("key", key);
//        LocalBroadcastManager.getInstance(ThongKeActivity.this).sendBroadcast(intent);
        Intent intent = new Intent(ThongKeActivity.this, MainActivity.class);
        intent.putExtra("key", key);
        Log.d("checkKeySend", key);
        startActivity(intent);
    }

    public void ThemNgay(int songay) {
        arrayListChonNgay.clear();
        arrayListChonNgay.add("Ngày");
        for (int i = 1; i <= songay; i++) {
            if (i < 10) {
                arrayListChonNgay.add("0" + String.valueOf(i));
            } else {
                arrayListChonNgay.add(String.valueOf(i));
            }

        }
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
